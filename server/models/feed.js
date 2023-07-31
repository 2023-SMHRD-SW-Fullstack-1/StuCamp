const Sequelize = require("sequelize");

module.exports = class Feed extends Sequelize.Model {
  static init(sequelize) {
    return super.init(
      {
        feed_id: {
          type: Sequelize.BIGINT,
          allowNull: false,
          primaryKey: true,
          unique: true,
          autoIncrement: true,
        },
        feed_content: {
          type: Sequelize.STRING(5000),
        },
        feed_imgpath: {
          type: Sequelize.STRING(1000),
        },
        user_id: {
          type: Sequelize.BIGINT,
        },
        feed_like_cnt: {
          type: Sequelize.INTEGER(100),
        },
      },
      {
        sequelize,
        timestamps: true,
        modelName: "Feed",
        tableName: "feed",
        charset: "utf8",
        collate: "utf8_general_ci",
      }
    );
  }
  static associate(db) {
    db.Feed.belongsTo(db.User, { foreignKey: "user_id", sourceKey: "user_id" });
    db.Feed.hasMany(db.Like, { foreignKey: "feed_id", sourceKey: "feed_id" });
    // db.Feed.hasMany(db.Comment, {
    //   foreignKey: "comment_id",
    //   sourceKey: "comment_id",
    // });
  }
};
