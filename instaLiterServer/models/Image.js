module.exports = (sequelize, type) => {
    return sequelize.define('Image', {
        id: {type: type.INTEGER, primaryKey: true, autoIncrement: true},
        idU: {type: type.INTEGER, foreignKey: true},
        path: type.STRING,
        cTime: type.DATE,
        description: type.STRING
    });
}