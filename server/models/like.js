const Sequelize = require("sequelize");

module.exports = class Like extends Sequelize.Model {

  static init(sequelize) {
    return super.init(
      {
       like_id: {
          type: Sequelize.BIGINT, 
          allowNull: false, 
          primaryKey: true, 
          unique: true, 
          autoIncrement: true
        },
        user_id: {
            type: Sequelize.BIGINT,
        },
        feed_id: {
            type: Sequelize.BIGINT,
        },
      },
      {
        sequelize, 
        timestamps: true,  
        modelName: "Like", 
        tableName: "like", 
        charset: "utf8",
        collate: "utf8_general_ci",
      }
    );
  }
  static associate(db) {
    // db.User.hasMany(db.Board, { foreignKey: "id", sourceKey: "id" }); // 1:N
    // db.User.hasOne ... 1:1
    // db.User.belongsToMany ... N : M
    // foreignKey는 foreignKey지정만 하는거고
    //데이터 가져올때는 sourceKey로 인해 가져온다.
  }
};
