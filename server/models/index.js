const Sequelize = require("sequelize");
const User = require("./user");
const Comment = require("./comment");
const Feed = require("./feed");
const Like = require("./like");
const RecordDetail = require("./record_detail");
const Record = require("./record");
const Wish = require("./wish");

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

// model init
User.init(sequelize);
Comment.init(sequelize);
Feed.init(sequelize);
Like.init(sequelize);
RecordDetail.init(sequelize);
Record.init(sequelize);
Wish.init(sequelize);

// 관계설정
User.associate(db);
Comment.associate(db);
Feed.associate(db);
Like.associate(db);
RecordDetail.associate(db);
Record.associate(db);
Wish.associate(db);

module.exports = db;
