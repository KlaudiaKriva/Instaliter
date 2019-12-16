module.exports = (sequelize, type) => {
    return sequelize.define('Comment', {
        id: {type: type.INTEGER, primaryKey: true, autoIncrement: true},
        idI: {type: type.INTEGER, foreignKey: true},
        idU: {type: type.INTEGER, foreignKey: true},
        commentText: type.STRING,
        cTime: type.DATE
    });
}