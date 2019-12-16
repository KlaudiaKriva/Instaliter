let database = require('./sequelize');

module.exports = {
    disconnectMatch(array, socketid, found)
    {
        let res = -1;
        for(let i=0; i < array.length; i++)
        {
            if( array[i].socketID == socketid)
            {
                res = i;
                break;
            }
        }
        found(res);
    },

    
    checkUserExistence(array, id, found)
    {
        console.log("USER EX ID " + id);
        let res = -1;
        for(let i=0; i < array.length; i++)
        {
            if(array[i].userID == id)
            {
                res = i;
                break;
            }
        }
        found(res);
    },

    writeUserMessage(obj, data)
    {
        database.sequelizeconn.query("insert into Message(senderID, recieverID, messageText) values(:idd, :idr, :message);",
        { replacements: {idd: obj.id, idr:obj.recieverID, message:obj.messageText}, type: database.sequelizeconn.QueryTypes.INSERT }).then(result => {
          data("Ok");
        }).catch(err =>{console.log(err);data("Error");});
    },

    getChatUsers(obj, data)
    {
        database.sequelizeconn.query("(select DISTINCT User.id, User.name, User.instaName from Message "
        + "inner join User on User.id = Message.recieverID where Message.senderID = :idd) "
        + "UNION "
        + "(select DISTINCT User.id, User.name, User.instaName from Message "
        + "inner join User on User.id = Message.senderID where Message.recieverID  = :idd);",
        { replacements: {idd: obj.id}, type: database.sequelizeconn.QueryTypes.SELECT }).then(result => {
              if(result.length != 0){JSON.stringify(data(result))} else{data("Error");};
        }).catch(err =>{console.log(err); data("Error");});
    },

    getChatHistoryById(obj, data)
    {
        database.sequelizeconn.query("select senderID, recieverID, messageText, cTime from message "
        + " where senderID = :idd and recieverID = :idr or senderID = :idr and recieverID = :idd order by cTime;",
        { replacements: {idd: obj.id, idr:obj.recieverID}, type: database.sequelizeconn.QueryTypes.SELECT }).then(result => {
              if(result.length != 0){JSON.stringify(data(result))} else{data("Error");};
        }).catch(err =>{console.log(err); data("Error");});
    }
    
}

