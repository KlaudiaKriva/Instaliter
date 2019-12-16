module.exports = (sequelize, type) => {
    return sequelize.define('Tag', {
        id: {type: type.INTEGER, primaryKey: true, autoIncrement: true},
        tagText: type.STRING
    });
}