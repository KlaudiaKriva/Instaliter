module.exports = (sequelize, type) => {
    return sequelize.define('User', {
        id: {type: type.INTEGER, primaryKey: true, autoIncrement: true},
        name: type.STRING,
        instaName: type.STRING,
        email: type.STRING,
        password: type.STRING,
        cTime: type.DATE
        
    });
}