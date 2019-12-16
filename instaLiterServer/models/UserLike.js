module.exports = (sequelize, type) => {
    return sequelize.define('Follow', {
        id: {type: type.INTEGER, primaryKey: true, autoIncrement: true},
        idU: {type: type.INTEGER, foreignKey: true},
        idI: {type: type.INTEGER, foreignKey: true}
    });
}