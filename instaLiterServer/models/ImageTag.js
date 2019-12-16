module.exports = (sequelize, type) => {
    return sequelize.define('ImageTag', {
        id: {type: type.INTEGER, primaryKey: true, autoIncrement: true},
        idI: {type: type.INTEGER, foreignKey: true},
        idT: {type: type.INTEGER, foreignKey: true}
    });
}