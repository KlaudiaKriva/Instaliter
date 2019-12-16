let middleware = require('./middleware');
let handler = require('./handler');
let socketWorker = require('./socketWorker');

const express = require('express');
const app = express();
const server = require('http').createServer(app);
const bodyParser = require("body-parser");
let cors = require('cors');
let multer  = require('multer');
const path = require('path');
const crypto = require('crypto');
const io = require('socket.io')(server);

let upload = multer({
    storage: multer.diskStorage({
        destination: (req, file, cb) => {
            cb(null, "public/images/")
        },
        filename: (req, file, cb) => {
            let customFileName = crypto.randomBytes(18).toString('hex')
            let fileExtension = path.extname(file.originalname).split('.')[1];
            cb(null, customFileName + '.' + fileExtension)
        }
      })
    });

io.set('origins', '*:*');
app.use(cors());
app.use(bodyParser.json());
app.use(express.static("public"));
console.log("Everything is up");

app.use(function(req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    res.header('Access-Control-Allow-Methods', 'POST, GET')
    next();
  });  

  let appClient = [];

io.on('connection', socket =>{

    console.log("Connection");
  socket.on('setIdentifier', data => {

    console.log(data);
    console.log("ID " + data.id);
        socketWorker.checkUserExistence(appClient, data.id, result =>{
            if(result > -1)
            {
                if(io.sockets.connected[appClient[result].socketID])
                {
                    console.log("Duplicate found, disconnecting");
                    io.sockets.connected[appClient[result].socketID].disconnect();
                }
                else{console.log("Client connected"); pushClient();}
            }
            else
            {
                console.log("Client connected");
                pushClient();
            }
        });

        function pushClient()
        {
            let clientInfo = new Object();
            clientInfo.socketID = socket.id;
            clientInfo.userID = data.id;
            appClient.push(clientInfo);
        }
    });

    socket.on('sendMessage', data =>{
        
        socketWorker.writeUserMessage(data, () =>{});
    
        console.log("TRIGGER");
        socketWorker.checkUserExistence(appClient, data.recieverID, result =>{
            console.log("CHECK USER EXISTENCE" + result);
            if(result > -1)
            {
                io.to(appClient[result].socketID).emit('message', data);
                console.log("message sent");
            }
            else
            {
                console.log("Client is offline");
            }
        });
    });


    socket.on('disconnect', ()=>{
        socketWorker.disconnectMatch(appClient, socket.id, result =>{
            if(result > -1)
            {
                appClient.splice(result,1);
                console.log('App Client disconnected');
            }
            console.log(appClient.length);
        });  
    });
});



app.post('/register', handler.register);
app.post('/login', handler.login);

app.post('/userInfo', middleware.checkToken, handler.userInfo);
app.post('/uploadImage', upload.single('recfile'), handler.uploadImage);
app.post('/getUserImages', middleware.checkToken, handler.getUserImages);
app.post('/getImageTag', middleware.checkToken, handler.getImageTag);
app.post('/getImage', middleware.checkToken, handler.getImage);
app.post('/setProfileDescription', middleware.checkToken, handler.setProfileDescription);
app.post('/setUserLike', middleware.checkToken, handler.setUserLike);
app.post('/setUserDislike', middleware.checkToken, handler.setUserDislike);
app.post('/checkUserLike', middleware.checkToken, handler.checkUserLike);
app.post('/getImageLikes', middleware.checkToken, handler.getImageLikes);
app.post('/deleteUserImage', middleware.checkToken, handler.deleteUserImage);
app.post('/setUserProfilePhoto', middleware.checkToken, handler.setUserProfilePhoto);
app.post('/setImageComment', middleware.checkToken, handler.setImageComment);
app.post('/getImageComments', middleware.checkToken, handler.getImageComments);
app.post('/setUserFollow', middleware.checkToken, handler.setUserFollow);
app.post('/setUserUnFollow', middleware.checkToken, handler.setUserUnFollow);
app.post('/getUserFollowers', middleware.checkToken, handler.getUserFollowers);
app.post('/getUsersFollowers', middleware.checkToken, handler.getUsersFollowers);
app.post('/checkIfUserFollowed', middleware.checkToken, handler.checkIfUserFollowed);
app.post('/getFeed', middleware.checkToken, handler.getFeed);
app.post('/getFeedByIntervall', middleware.checkToken, handler.getFeedByIntervall);
app.post('/searchUser', middleware.checkToken, handler.searchUser);
app.post('/getChatUsers', middleware.checkToken, handler.getChatUsers);
app.post('/getChatHistoryById', middleware.checkToken, handler.getChatHistoryById);


server.listen(5005);