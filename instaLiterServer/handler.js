let worker = require('./worker');
let socketWorker = require('./socketWorker');
let jwt = require('jsonwebtoken');
let config = require('./config');

module.exports = {
    register(req, res)
    {
        worker.registerUser(req.body, success =>{
            console.log(success);
            if(success == "Error")
            {
                res.status(403).send("Server Error");
            }
            else if(success == "User is already registered")
            {
                res.status(403).send(success);
            }
            else if(success == "Email is already in use")
            {
                res.status(403).send(success);
            }
            else if(success == "Insta name is already in use")
            {
                res.status(403).send(success);
            }
            else
            {
                res.status(200).send("User registered");
            }
            console.log(success);
        });
    },

    login(req,res)
    {
        worker.getUser(req.body, result =>{
            console.log(result);
            if(result != "Error")
            {
                let token = jwt.sign({email:result[0].email},
                    config.secret,{ expiresIn: '30d'}
                );
                console.log(token);
                let respObj = {id: result[0].id, name: result[0].name, email: result[0].email, token:token};
                res.status(200).send(JSON.stringify(respObj));
            }
            else
            {
                console.log("Error Login");
                res.status(403).send(JSON.stringify({respone:"Error"}));
            }
        });
    },

    userInfo(req, res)
    {
        worker.getUserInfo(req.body, result =>{
            if(result != "Error")
            {
                res.status(200).send(result);
            }
            else
            {
                res.status(400).send(JSON.stringify({respone:"Error"}));
            }
        });
    },

    uploadImage(req, res)
    {
        try 
        {
            let detail =  JSON.parse(JSON.stringify(req.body));//outer
            let detail_ = JSON.parse(detail.details);//inner
            let fileDetail = JSON.parse(JSON.stringify(req.file));//filedetail

            worker.imageWorker(fileDetail, detail_, result =>{
                if(result == "Ok")
                {
                    res.status(200).send(JSON.stringify({respone:"Image uploaded successfully"}));
                }
                else
                {
                    res.status(400).send(JSON.stringify({respone:"Error"}));
                }
            });
        }
        catch(err) 
        {
            console.log(err);
            res.sendStatus(400);
        }
    },

    getUserImages(req, res)
    {
        worker.getUserImages(req.body, result =>{
            if(result != "Error")
            {
                res.status(200).send(result);
            }
            else
            {
                res.status(400).send(JSON.stringify({respone:"Error"}));
            }
        });
    },

    getImageTag(req, res)
    {
        worker.getImageTag(req.body, result =>{
            if(result != "Error")
            {
                res.status(200).send(result);
            }
            else
            {
                res.status(400).send(JSON.stringify({respone:"Error"}));
            }
        });
    },

    getImage(req, res)
    {
        worker.getImage(req.body, result =>{
            if(result != "Error")
            {
                res.status(200).send(result);
            }
            else
            {
                res.status(400).send(JSON.stringify({respone:"Error"}));
            }
        });
    },

    setProfileDescription(req, res)
    {
        worker.setProfileDescription(req.body, result=>{
            if(result != "Error")
            {
                res.status(200).send(JSON.stringify({respone:"Ok"}));
            }
            else
            {
                res.status(400).send(JSON.stringify({respone:"Error"}));
            }
        });
    },

    setUserLike(req, res)
    {
        worker.setUserLike(req.body, result =>{
            if(result != "Error")
            {
                res.status(200).send(JSON.stringify({respone:"Ok"}));
            }
            else
            {
                res.status(400).send(JSON.stringify({respone:"Error"}));
            }
        });
    },

    setUserDislike(req, res)
    {
        worker.setUserDislike(req.body, result =>{
            if(result != "Error")
            {
                res.status(200).send(JSON.stringify({respone:"Ok"}));
            }
            else
            {
                res.status(400).send(JSON.stringify({respone:"Error"}));
            }
        });
    },

    checkUserLike(req, res)
    {
        worker.checkUserLike(req.body, result =>{
            if(result != "Error")
            {
                res.status(200).send(result);
            }
            else
            {
                res.status(400).send(JSON.stringify({respone:"Error"}));
            }
        });
    },

    getImageLikes(req, res)
    {
        worker.getImageLikes(req.body, result =>{
            if(result != "Error")
            {
                res.status(200).send(result);
            }
            else
            {
                res.status(400).send(JSON.stringify({respone:"Error"}));
            }
        });
    },

    deleteUserImage(req, res)
    {
        worker.deleteUserImage(req.body, result =>{
            if(result != "Error")
            {
                res.status(200).send(JSON.stringify({respone:"Ok"}));
            }
            else
            {
                res.status(400).send(JSON.stringify({respone:"Error"}));
            }
        });
    },

    setUserProfilePhoto(req, res)
    {
        worker.setUserProfilePhoto(req.body, result =>{
            if(result != "Error")
            {
                res.status(200).send(JSON.stringify({respone:"Ok"}));
            }
            else
            {
                res.status(400).send(JSON.stringify({respone:"Error"}));
            }
        });
    },

    setImageComment(req, res)
    {
        worker.setImageComment(req.body, result =>{
            if(result != "Error")
            {
                res.status(200).send(JSON.stringify({respone:"ok"}));
            }
            else
            {
                res.status(400).send(JSON.stringify({respone:"Error"}));
            }
        });
    },

    getImageComments(req, res)
    {
        worker.getImageComments(req.body, result =>{
            if(result != "Error")
            {
                res.status(200).send(result);
            }
            else
            {
                res.status(400).send(JSON.stringify({respone:"Error"}));
            }
        });
    },

    setUserFollow(req, res)
    {
        worker.setUserFollow(req.body, result =>{
            if(result != "Error")
            {
                res.status(200).send(JSON.stringify({respone:"Ok"}));
            }
            else
            {
                res.status(400).send(JSON.stringify({respone:"Error"}));
            }
        });
    },

    setUserUnFollow(req, res)
    {
        worker.setUserUnFollow(req.body, result =>{
            if(result != "Error")
            {
                res.status(200).send(JSON.stringify({respone:"Ok"}));
            }
            else
            {
                res.status(400).send(JSON.stringify({respone:"Error"}));
            }
        });
    },

    getUserFollowers(req, res)
    {
        worker.getUserFollowers(req.body, result =>{
            if(result != "Error")
            {
                res.status(200).send(result);
            }
            else
            {
                res.status(400).send(JSON.stringify({respone:"Error"}));
            }
        });
    },

    getUsersFollowers(req, res)
    {
        worker.getUsersFollowers(req.body, result =>{
            if(result != "Error")
            {
                res.status(200).send(result);
            }
            else
            {
                res.status(400).send(JSON.stringify({respone:"Error"}));
            }
        });
    },

    checkIfUserFollowed(req, res)
    {
        worker.checkIfUserFollowed(req.body, result =>{
            if(result == 1)
            {
                res.status(200).send(JSON.stringify({followed:true}));
            }
            else if(result == 0)
            {
                res.status(200).send(JSON.stringify({followed:false}));
            }
            else
            {
                res.status(400).send(JSON.stringify({respone:"Error"}));
            }
        });
    },

    getFeed(req, res)
    {
        worker.getFeed(req.body, result =>{
            if(result != "Error")
            {
                res.status(200).send(result);
            }
            else
            {
                res.status(400).send(JSON.stringify({respone:"Error"}));
            }
        });
    },

    getFeedByIntervall(req, res)
    {
        worker.getFeedByIntervall(req.body, result =>{
            if(result != "Error")
            {
                res.status(200).send(result);
            }
            else
            {
                res.status(400).send(JSON.stringify({respone:"Error"}));
            }
        });
    },

    searchUser(req, res)
    {
        worker.searchUser(req.body, result =>{
            if(result != "Error")
            {
                res.status(200).send(result);
            }
            else
            {
                res.status(400).send(JSON.stringify({respone:"Error"}));
            }
        });
    },

    getChatUsers(req, res)
    {
       socketWorker.getChatUsers(req.body, result=>{
        if(result != "Error")
        {
            res.status(200).send(result);
        }
        else
        {
            res.status(400).send(JSON.stringify({respone:"Error"}));
        }
       });
    },

    getChatHistoryById(req, res)
    {
       socketWorker.getChatHistoryById(req.body, result =>{
            if(result != "Error")
            {
                res.status(200).send(result);
            }
            else
            {
                res.status(400).send(JSON.stringify({respone:"Error"}));
            }
       });
    }
};