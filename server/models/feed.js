const Sequelize = require("sequelize");
const User = require('./user');

module.exports = class Feed extends Sequelize.Model {

  static init(sequelize) {
    return super.init(
      {
       feed_id: {
          type: Sequelize.BIGINT, 
          allowNull: false, 
          primaryKey: true, 
          unique: true, 
          autoIncrement: true
        },
        feed_content: {
            type: Sequelize.STRING(5000)
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
    db.Feed.hasMany(db.User, { foreignKey: "user_id", sourceKey: "user_id" }); // 1:N
    // db.User.hasOne ... 1:1
    // db.User.belongsToMany ... N : M
    // foreignKey는 foreignKey지정만 하는거고
    //데이터 가져올때는 sourceKey로 인해 가져온다.
  }
};
