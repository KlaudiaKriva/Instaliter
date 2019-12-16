const { Comment, Follow, Image, ImageTag, Tag, User, UserInfo, UserLike } = require('./sequelize')
let database = require('./sequelize');
let thumb = require('node-thumbnail').thumb;

module.exports = {
    registerUser(obj, isSuccess)
    {
        database.sequelizeconn.query("select instaName, email from User where email = :email or instaName = :instaname;",
      { replacements: {instaname: obj.instaName, email: obj.email}, type: database.sequelizeconn.QueryTypes.SELECT }).then(result => {
            if(result.length != 0)
            {
              if((result[0].instaName == obj.instaName) && (result[0].email == obj.email))
              {
                isSuccess("User is already registered");
              }
              else if(result[0].email == obj.email)
              {
                isSuccess("Email is already in use");
              }
              else
              {
                isSuccess("Insta name is already in use");
              }
            }
             else
             {
               database.sequelizeconn.query("insert into User(name, instaName, email, password) values(:name, :instaname, :mail, sha1(:pass));",
                { replacements: {name: obj.name, instaname:obj.instaName, mail: obj.email, pass: obj.password}, type: database.sequelizeconn.QueryTypes.INSERT }).then(result => {
                  console.log(result);
                  isSuccess("Ok");
                }).catch(err =>{console.log(err);isSuccess("Error");});
              };
      }).catch(err =>{console.log(err); isSuccess("Error");});
    },

    getUser(obj, data)
    {
      database.sequelizeconn.query("select id, name, email from User where password = sha1(:pass) AND email = :mail;",
      { replacements: {pass: obj.password, mail: obj.email}, type: database.sequelizeconn.QueryTypes.SELECT }).then(result => {
            if(result.length != 0){data(result)} else{data("Error");};
      }).catch(err =>{console.log(err); data("Error");});
    },

    getUserInfo(obj, data)
    {
      database.sequelizeconn.query("select User.id, user.name, user.instaName, user.email, userInfo.profileDescription,Image.id as idI, Image.path as imagePath,Image.cTime as imageDate, Image.thumbnailPath, Image.description from User "
      + "left join Userinfo on UserInfo.idU=User.ID "
      + "left join Image on Image.id=UserInfo.profilePhoto "
      + "where User.ID like :userid;",
      { replacements: {userid:obj.id}, type: database.sequelizeconn.QueryTypes.SELECT }).then(result => {
            if(result.length != 0){data(JSON.stringify(result))} else{data("Error");};
      }).catch(err =>{console.log(err); data("Error");});
    },

    imageWorker(obj, obj2, data)
    {
      thumb({
        source: obj.path,
        destination: 'public/thumbnails',
        suffix: '',
      }, function(files, err, stdout, stderr) 
      {
        console.log('All done!');
        executeInserts();
      });

     
      function executeInserts()
      {
        if(obj2.type == 1)
        {
          checkProfilePhoto(obj2, cppresult =>{
            if(cppresult.length == 0)
            {
              insertImage(obj, obj2, result =>{
                if(result != "Error")
                {
                  insertProfilePhoto(result, ippresult =>{
                    if(ippresult == "error"){data("Error")};
                  });
                  console.log("FIRST");
                  if(obj2.tag.length != 0 && obj2.tag != undefined)
                  {
                    insertTag(obj2, tresult=>{
                      if(tresult == "Ok")
                      {
                        insertImageTag(obj2, result, iitresult =>{
                          if(iitresult == "Error"){data("Error");}
                          else{data("Ok");}
                        });
                      }
                      else
                      {
                        data("Tag insertion failed");
                      }
                    });
                  }
                  else{data("Ok");}
                }
              });
            }
            else if(cppresult != "Error")
            {
              updateOldProfile(cppresult, obj2, uopresult =>{
                if(uopresult != "Error")
                {
                  insertImage(obj, obj2, result =>{
                    if(result != "Error")
                    {
                      insertProfilePhoto(result, ippresult =>{
                        if(ippresult == "error"){data("Error")};
                      });
                      console.log("SECOND");
                      if(obj2.tag.length != 0 && obj2.tag != undefined)
                      {
                        insertTag(obj2, tresult=>{
                          if(tresult == "Ok")
                          {
                            insertImageTag(obj2, result, iitresult =>{
                              if(iitresult == "Error"){data("Error");}
                              else{data("Ok");}
                            });
                          }
                          else
                          {
                            data("Tag insertion failed");
                          }
                        });
                      }
                      else{data("Ok");}
                    }
                  });
                }
              });
            }
            else{data("Error");}
          });
        }
        else
        {
          insertImage(obj, obj2, result =>{
            if(result != "Error")
            {
              console.log("THIRD");
              if(obj2.tag.length != 0 && obj2.tag != undefined)
              {
                insertTag(obj2, tresult=>{
                  if(tresult == "Ok")
                  {
                    insertImageTag(obj2, result, iitresult =>{
                      if(iitresult == "Error"){data("Error");}
                      else{data("Ok");}
                    });
                  }
                  else
                  {
                    data("Tag insertion failed");
                  }
                });
              }
              else{data("Ok");}
            }
          });
        }
      }

      function updateOldProfile(path, obj2, uopresult)
      {
        database.sequelizeconn.query("update image set type = 0 where idU = :idd and path = :ppath;",
        { replacements: {idd: obj2.id, ppath:path[0].path}, type: database.sequelizeconn.QueryTypes.UPDATE}).then(result => {
          uopresult(result);
        }).catch(err =>{console.log(err); uopresult("Error");});
      }

      function checkProfilePhoto(obj2, cppresult)
      {
        database.sequelizeconn.query("select path from image where idU = :idd and type = 1;",
        { replacements: {idd: obj2.id}, type: database.sequelizeconn.QueryTypes.SELECT }).then(result => {
          cppresult(result);
        }).catch(err =>{console.log(err); cppresult("Error");});
      }

      function insertImage(obj, obj2, iresult)
      {
        database.sequelizeconn.query("insert into Image(idU, description, type, path, thumbnailPath) "
        +"values(:idu, :imagedescription, :ttype, :ppath, :thumbnailpath);",
        { replacements: {idu: obj2.id, imagedescription: obj2.imageDescription, ttype: obj2.type, ppath:"images//"+obj.filename, thumbnailpath:"thumbnails//"+obj.filename}, type: database.sequelizeconn.QueryTypes.INSERT })
        .then(result => {
            if(result.length != 0){iresult(result)} else{iresult("Error");};

        }).catch(err =>{console.log(err); data("Error");});
      }

      function insertProfilePhoto(imgid, ppresult)
      {
        database.sequelizeconn.query("insert into UserInfo(idU, profilePhoto) values(:idu, :imgk) on duplicate key update profilePhoto = :imgk;",
        { replacements: {idu:obj2.id, imgk:imgid[0]}, type: database.sequelizeconn.QueryTypes.INSERT }).then(result => {
          ppresult("Ok");
        }).catch(err =>{console.log(err);ppresult("Error");});
      }

      function insertTag(obj2, tresult)
      {
        createTagQuery(obj2, query =>{
          database.sequelizeconn.query(query,{ type: database.sequelizeconn.QueryTypes.INSERT }).then(result => {
            tresult("Ok");
          }).catch(err =>{console.log(err);ppresult("Error");});
        });
        function createTagQuery(obj2, ctresult)
        {
          let template = '("rep")';
          let result = "";
          let query = "insert ignore into Tag(tagText) values replacedat;";

          for(let i =0; i<obj2.tag.length; i++)
          {
            if(i==0){template = template.replace("rep", obj2.tag[i]);}
            else{template = template.replace(obj2.tag[i-1], obj2.tag[i]);}
            
            if(i==1){template = ","+template;}
            result += template;
          }
          query = query.replace("replacedat", result);
          ctresult(query);
        }
      }

      function insertImageTag(obj2, imgid, iitresult)
      {

        queryImageTags(obj2, ids =>{
          if(ids != "Error")
          {
            createInsertTagQuery(imgid, ids, query =>{
              database.sequelizeconn.query(query,{ type: database.sequelizeconn.QueryTypes.INSERT }).then(result => {
                iitresult("Ok");
              }).catch(err =>{console.log(err);iitresult("Error");});
            });
          }
          else{iitresult("Error");}
        });

        function createInsertTagQuery(iid, ids, cintresult)
        {
          console.log("CREATE INSERT TAG QUERY " + iid[0]);
          let template = '(rep)';
          let result = "";
          let query = " insert into imageTag(idI, idT) values replacedat;";

          for(let i =0; i<ids.length; i++)
          {
          if(i==0){template = template.replace("rep", iid[0] + "," + ids[i].id);}
          else{template = template.replace(iid[0] + "," + ids[i-1].id, iid[0] + "," + ids[i].id);}
                      
          if(i==1){template = " , "+template;}
          result += template;
          }
          query = query.replace("replacedat", result);
          cintresult(query);
        }

        function queryImageTags(obj2, itresult)
        {
          createImageTagQuery(obj2, query =>{
            database.sequelizeconn.query(query,{ type: database.sequelizeconn.QueryTypes.SELECT }).then(result => {
              console.log(result);
              itresult(result);
            }).catch(err =>{console.log(err);itresult("Error");});
          });
        }

        function createImageTagQuery(obj2, citresult)
        {
          var template = 'tagText = "rep"';
          let result = "";
          let query = "select id from Tag where replacedat;";

          for(let i =0; i<obj2.tag.length; i++)
          {
            if(i==0){template = template.replace("rep", obj2.tag[i]);}
            else{template = template.replace(obj2.tag[i-1], obj2.tag[i]);}
                        
            if(i==1){template = " or "+template;}
            result += template;
          }
          query = query.replace("replacedat", result);
          citresult(query);
        }
      }
    },

    getUserImages(obj, data)
    {
      database.sequelizeconn.query("select idU as id, id as idI, path, thumbnailPath, description, cTime as imageDate, type from Image where idU = :idd and deleted = 0;",
      { replacements: {idd: obj.id}, type: database.sequelizeconn.QueryTypes.SELECT }).then(result => {
            if(result.length != 0){JSON.stringify(data(result))} else{data("Error");};
      }).catch(err =>{console.log(err); data("Error");});
    },

    getImageTag(obj, data)
    {
      database.sequelizeconn.query("select imagetag.idI, tag.tagText from imagetag inner join Tag on imagetag.idT=Tag.id where imagetag.idI = :idi;",
      { replacements: {idi: obj.idI}, type: database.sequelizeconn.QueryTypes.SELECT }).then(result => {
            if(result.length != 0){JSON.stringify(data(result))} else{data("Error");};
      }).catch(err =>{console.log(err); data("Error");});
    },

    getImage(obj, data)
    {
      database.sequelizeconn.query("select image.idU as id, image.id as idI, image.path, image.thumbnailPath, image.description, type from Image where image.id = :idi and deleted = 0;",
      { replacements: {idi: obj.idI}, type: database.sequelizeconn.QueryTypes.SELECT }).then(result => {
            if(result.length != 0){JSON.stringify(data(result))} else{data("Error");};
      }).catch(err =>{console.log(err); data("Error");});
    },

    deleteUserImage(obj, data)
    {
      database.sequelizeconn.query("update Image set deleted = 1 where id = :idi and idU = :idd;",
      { replacements: {idi: obj.idI, idd:obj.id}, type: database.sequelizeconn.QueryTypes.UPDATE }).then(result => {
            updateUserProfilePhoto(obj, uuppresult =>{
              if(uuppresult == "Ok"){data("Ok");}else{data("Error");}
            });
      }).catch(err =>{console.log(err); data("Error");});

      function updateUserProfilePhoto(obj, uuppresult)
      {
        database.sequelizeconn.query("update userinfo set profilePhoto = NULL where idU = :idd;",
        { replacements: {idd:obj.id}, type: database.sequelizeconn.QueryTypes.UPDATE }).then(result => {
          uuppresult("Ok");
        }).catch(err =>{console.log(err); uuppresult("Error");});
      }
    },

    setUserProfilePhoto(obj, data)
    {
      checkProfilePhoto(obj, cppres =>{
        if(cppres.length > 0)
        {
          updateOldProfile(cppres, obj, uopres =>{
            if(uopres == "Ok")
            {
              updateNewProfile(obj, unpres =>{

              });
              insertProfilePhoto(obj, ippres =>{
                if(ippres == "Ok"){data("Ok");}
                else{console.log("ERROR IN INSERT PROFILE PHOTO");data("Error");}
              });
            }
            else{console.log("ERROR IN UPDATE PROFILE PHOTO");data("Error");}
          });
        }
        else
        {
          updateNewProfile(obj, unpres =>{
                
              });
          insertProfilePhoto(obj, ippres =>{
            if(ippres == "Ok"){data("Ok");}
            else{console.log("ERROR IN INSERT PROFILE PHOTO 2");data("Error");}
          });
        }
      });

      function insertProfilePhoto(obj, ippresult)
      {
        database.sequelizeconn.query("insert into UserInfo(idU, profilePhoto) values(:idu, :imgk) on duplicate key update profilePhoto = :imgk;",
        { replacements: {idu:obj.id, imgk:obj.idI}, type: database.sequelizeconn.QueryTypes.INSERT }).then(result => {
          ippresult("Ok");
        }).catch(err =>{console.log(err); ippresult("Error");});
      }

      function updateOldProfile(path, obj, uopresult)
      {
        database.sequelizeconn.query("update image set type = 0 where idU = :idd and path = :ppath;",
        { replacements: {idd: obj.id, ppath:path[0].path}, type: database.sequelizeconn.QueryTypes.UPDATE}).then(result => {
          updateNewProfile(obj, unpresult =>{
            uopresult("Ok");
          });
        }).catch(err =>{console.log(err); uopresult("Error");});
      }

      function updateNewProfile(obj, unpresult)
      {
        database.sequelizeconn.query("update image set type = 1 where idU = :idd and id = :idi;",
        { replacements: {idd: obj.id, idi:obj.idI}, type: database.sequelizeconn.QueryTypes.UPDATE}).then(result => {
          unpresult("Ok");
        }).catch(err =>{console.log(err); unpresult("Error");});
      }

      function checkProfilePhoto(obj, cppresult)
      {
        database.sequelizeconn.query("select path from image where idU = :idd and type = 1;",
        { replacements: {idd: obj.id}, type: database.sequelizeconn.QueryTypes.SELECT }).then(result => {
          cppresult(result);
        }).catch(err =>{console.log(err); cppresult("Error");});
      }
    },

    setProfileDescription(obj, data)
    {
      database.sequelizeconn.query("insert into UserInfo(idU, profileDescription) values(:idu, :pd) on duplicate key update profileDescription = :pd;",
                { replacements: {idu: obj.id, pd:obj.profileDescription}, type: database.sequelizeconn.QueryTypes.INSERT }).then(result => {
                  data("Ok");
                }).catch(err =>{console.log(err);data("Error");});
    },

    setUserLike(obj, data)
    {
      database.sequelizeconn.query("insert into UserLike(idU, idI) values(:idu, :idi)  on duplicate key update active =1;",
                { replacements: {idu: obj.id, idi:obj.idI}, type: database.sequelizeconn.QueryTypes.INSERT }).then(result => {
                  data("Ok");
                }).catch(err =>{console.log(err);data("Error");});
    },

    setUserDislike(obj, data)
    {
      database.sequelizeconn.query("update UserLike set active = 0 where idU = :idu and idI = :idi;",
                { replacements: {idu: obj.id, idi:obj.idI}, type: database.sequelizeconn.QueryTypes.UPDATE }).then(result => {
                  data("Ok");
                }).catch(err =>{console.log(err);data("Error");});
    },

    checkUserLike(obj, data)
    {
      database.sequelizeconn.query("select idU as id, idI, active from UserLike where idU=:idd and idI = :idi;",
      { replacements: {idd: obj.id, idi: obj.idI}, type: database.sequelizeconn.QueryTypes.SELECT }).then(result => {
            if(result.length != 0){JSON.stringify(data(result))} else{data("Error");};
      }).catch(err =>{console.log(err); data("Error");});
    },

    getImageLikes(obj, data)
    {
      database.sequelizeconn.query("select User.id, User.name, User.instaname from UserLike "
      + "inner join User on UserLike.idU = User.id "
      + "where UserLike.idI = :idi and active = 1;",
      { replacements: {idi: obj.idI}, type: database.sequelizeconn.QueryTypes.SELECT }).then(result => {
            if(result.length != 0){JSON.stringify(data(result))} else{data("Error");};
      }).catch(err =>{console.log(err); data("Error");});
    },

    setImageComment(obj, data)
    {
      console.log(obj);
      database.sequelizeconn.query("insert into Comment(idU, idI, commentText) values(:idd, :idi, :cmnt);",
      { replacements: {idd: obj.id, idi:obj.idI, cmnt:obj.commentText}, type: database.sequelizeconn.QueryTypes.INSERT }).then(result => {
        data("Ok");
      }).catch(err =>{console.log(err);data("Error");});
    },

    getImageComments(obj, data)
    {
      console.log(obj);
      database.sequelizeconn.query("select user.id, user.name, Comment.idI, Comment.cTime, Comment.commentText from Comment "
      + "inner join User on User.id=Comment.idU "
      + "where comment.idI like :idi order by Comment.cTime;",
      { replacements: {idi: obj.idI,}, type: database.sequelizeconn.QueryTypes.SELECT }).then(result => {
            if(result.length != 0){JSON.stringify(data(result))} else{data("Error");};
      }).catch(err =>{console.log(err); data("Error");});
    },

    setUserFollow(obj, data)
    {
      database.sequelizeconn.query("insert into Follow(followerID, followedID) values(:idu, :idu2) on duplicate key update active = 1;",
                { replacements: {idu: obj.id, idu2:obj.idF}, type: database.sequelizeconn.QueryTypes.INSERT }).then(result => {
                  data("Ok");
                }).catch(err =>{console.log(err);data("Error");});
    },

    setUserUnFollow(obj, data)
    {
      database.sequelizeconn.query("update Follow set active = 0 where followerID = :idu and followedID = :idu2;",
                { replacements: {idu: obj.id, idu2:obj.idF}, type: database.sequelizeconn.QueryTypes.UPDATE }).then(result => {
                  data("Ok");
                }).catch(err =>{console.log(err);data("Error");});
    },

    getUserFollowers(obj, data)
    {
      database.sequelizeconn.query("select User.id, User.name, User.instaname from Follow "
      + "inner join User on Follow.followedID = User.id "
      + "where Follow.followerID = :idd and active = 1;",
      { replacements: {idd: obj.id}, type: database.sequelizeconn.QueryTypes.SELECT }).then(result => {
            if(result.length != 0){JSON.stringify(data(result))} else{data("Error");};
      }).catch(err =>{console.log(err); data("Error");});
    },

    getUsersFollowers(obj, data)
    {
      database.sequelizeconn.query("select User.id, User.name, User.instaname from Follow "
      + "inner join User on Follow.followerID = User.id "
      + "where Follow.followedID = :idd and active = 1;",
      { replacements: {idd: obj.id}, type: database.sequelizeconn.QueryTypes.SELECT }).then(result => {
            if(result.length != 0){JSON.stringify(data(result))} else{data("Error");};
      }).catch(err =>{console.log(err); data("Error");});
    },

    checkIfUserFollowed(obj, data)
    {
      database.sequelizeconn.query("select active from follow where followerID = :idd and followedID = :idf;",
      { replacements: {idd: obj.id, idf: obj.idF}, type: database.sequelizeconn.QueryTypes.SELECT }).then(result => {
            if(result.length != 0)
            {
              if(result[0].active == 1){data(1);}
              else{data(0);}
            } 
            else{data(0);};
      }).catch(err =>{console.log(err); data("Error");});
    },

    getFeed(obj, data)
    {
      database.sequelizeconn.query("select image.id as idI, image.idu as id, image.path, image.thumbnailpath, image.type, image.ctime, image.description from image "
      + "inner join follow on follow.followedid=image.idu "
      + "where follow.followerid = :idd and follow.active = 1;",
      { replacements: {idd: obj.id}, type: database.sequelizeconn.QueryTypes.SELECT }).then(result => {
            if(result.length != 0){JSON.stringify(data(result))} else{data("Error");};
      }).catch(err =>{console.log(err); data("Error");});
    },

    getFeedByIntervall(obj, data)
    {
      database.sequelizeconn.query("select image.id as idI, image.idu as id, image.path, image.thumbnailpath, image.type, image.ctime, image.description from image "
      + "inner join follow on follow.followedid=image.idu "
      + "where image.ctime <= :time1 and image.ctime >= :time2 and follow.followerid like :idd and follow.active = 1;",
      { replacements: {idd: obj.id, time1:obj.till, time2:obj.from}, type: database.sequelizeconn.QueryTypes.SELECT }).then(result => {
            if(result.length != 0){JSON.stringify(data(result))} else{data("Error");};
      }).catch(err =>{console.log(err); data("Error");});
    },

    searchUser(obj, data)
    {
      let template = "%replace%";
      template = template.replace("replace", obj.word);
      database.sequelizeconn.query("select User.id, User.name, User.instaName, Image.id as idI, Image.path as imagePath,  Image.thumbnailPath from user "
      + "left join Userinfo on UserInfo.idU=User.ID "
      + "left join Image on Image.id=UserInfo.profilePhoto "
      + " where User.name like :word or User.instaname like :word;",
      { replacements: {word: template}, type: database.sequelizeconn.QueryTypes.SELECT }).then(result => {
            if(result.length != 0){JSON.stringify(data(result))} else{data("Error");};
      }).catch(err =>{console.log(err); data("Error");});
    }
};