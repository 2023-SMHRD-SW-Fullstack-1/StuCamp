const express = require("express");
const { sequelize } = require("./models");
const indexRouter = require("./routes/index");
const userRouter = require("./routes/user");
const recordRouter = require("./routes/record");
const feedRouter = require("./routes/feed")
const likeRouter = require('./routes/like')
const commentRouter = require('./routes/comment')
const bodyParser = require('body-parser')
const app = express();

app.use(express.urlencoded({ extended: true }));
app.use(bodyParser.json()) //json형식의 body 파싱
app.use(express.json());

// sequelize 설정
sequelize
  .sync({ force: false })
  .then(() => console.log("DB연결성공!"))
  .catch((err) => {
    console.log(err);
  });
console.log("abc");
//routes 연결
app.use("/", indexRouter);
app.use("/user", userRouter);
app.use("/record", recordRouter);
app.use("/feed", feedRouter);
app.use("/like", likeRouter)
app.use("/comment", commentRouter)

app.set("port", process.env.PORT || 8888);
app.listen(app.get("port"), () => {
  console.log(app.get("port"), "번 포트에서 서버연결 기다리는중...");
});
