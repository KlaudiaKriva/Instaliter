module.exports = (sequelize, type) => {
    return sequelize.define('UserInfo', {
        idU: {type: type.INTEGER, foreignKey: true},
        profilePhoto: {type: type.INTEGER, foreignKey: true},
        profileDescription: type.STRING
    });
}