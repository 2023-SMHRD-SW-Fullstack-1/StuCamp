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
      db.Like.BelongsTo(db.User, { foreignKey: "user_id", sourceKey: "user_id" });
      db.Like.BelongsTo(db.Feed, { foreignKey: "feed_id", sourceKey: "feed_id" });
    } 
  
};
