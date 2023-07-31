const Sequelize = require("sequelize");
const User = require("./user");

// 어떤 db, 계정
const env = process.env.NODE_ENV || "development";
const config = require("../config/config")[env];

// sequelize 객체 생성(객체 내부에는 DB 연결 정보 가지고 있음)
const sequelize = new Sequelize(
  config.database,
  config.username,
  config.password,
  config
);

// sequelize, model
const db = {};

db.sequelize = sequelize;
db.User = User;
db.Comment = Comment;
db.Feed = Feed;
db.Like = Like;
db.RecordDetail = RecordDetail;
db.Record = Record;
db.Wish = Wish;

User.init(sequelize);
User.associate(db); //관계설정했을시

module.exports = db;
