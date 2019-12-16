module.exports = (sequelize, type) => {
    return sequelize.define('Follow', {
        id: {type: type.INTEGER, primaryKey: true, autoIncrement: true},
        followerID: {type: type.INTEGER, foreignKey: true},
        followedID: {type: type.INTEGER, foreignKey: true}
    });
}