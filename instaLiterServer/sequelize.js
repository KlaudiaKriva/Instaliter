const Sequelize = require('sequelize');
const CommentModel = require('./models/Comment');
const FollowModel = require('./models/Follow');
const ImageModel = require('./models/Image');
const ImageTagModel = require('./models/ImageTag');
const TagModel = require('./models/Tag');
const UserModel = require('./models/User');
const UserInfoModel = require('./models/UserInfo');
const UserLikeModel = require('./models/UserLike');

const sequelizeconn = new Sequelize('insta_database', 'instaserver', 'akademiasovy', {
  host: 'localhost',
  dialect: 'mysql',
  port:3306,
  define: 
  {
    freezeTableName: true, 
    timestamps: false,
    underscored: false
  }
});

const Comment = CommentModel(sequelizeconn, Sequelize); 
const Follow = FollowModel(sequelizeconn, Sequelize); 
const Image = ImageModel(sequelizeconn, Sequelize); 
const ImageTag = ImageTagModel(sequelizeconn, Sequelize); 
const Tag = TagModel(sequelizeconn, Sequelize); 
const User = UserModel(sequelizeconn, Sequelize); 
const UserInfo = UserInfoModel(sequelizeconn, Sequelize);
const UserLike = UserLikeModel(sequelizeconn, Sequelize);  

module.exports = {Comment, Follow, Image, ImageTag, Tag, User, UserInfo, UserLike, sequelizeconn};