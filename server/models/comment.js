const Sequelize = require("sequelize");

module.exports = class Comment extends Sequelize.Model {

  static init(sequelize) {
    return super.init(
      {
       comment_id: {
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
        comment_content: {
          type: Sequelize.STRING(1000),
        },
      },
      {
        sequelize, 
        timestamps: true,  
        modelName: "Comment", 
        tableName: "comment", 
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
