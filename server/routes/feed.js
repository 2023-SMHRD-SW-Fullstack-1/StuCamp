const express = require("express");
const { v4: uuidv4 } = require("uuid");
const fs = require("fs");
const fs2 = require("fs").promises;
const FindReqDTO = require("../dto/userDTO/FindReqDto");
const FeedAddReqDto = require("../dto/feedDTO/feedAddReqDTO");
const FeedUpdateReqDto = require("../dto/feedDTO/FeedUpdateReqDTO");
const Feed = require("../models/feed");
const FeedDeleteReqDto = require("../dto/feedDTO/FeedDeleteDTO");
const FeedResDTO = require("../dto/feedDTO/FeedResDTO");
const FeedAllResDTO = require("../dto/feedDTO/FeedAllResDTO");
const { Comment, User } = require("../models");
const { log } = require("console");
const router = express.Router();

//피드 등록
router.post("/add", async (req, res) => {
  console.log("add 통신 확인");
  // //사진 변환 테스트
  // // 강아지 사진 파일 경로
  // const filePath = 'public/img/test/img2.jpg';
  // let base64String = ''

  // // 파일을 읽어와서 Base64로 변환
  // await fs.readFile(filePath, (err, data) => {
  //   if (err) {
  //     console.error('Error reading the file:', err);
  //     return;
  //   }

  // // 데이터를 Base64로 인코딩
  //     base64String = data.toString('base64');
  // //  console.log('Base64 string:', base64String);
  //     console.log("3333")
  //     console.log(base64String)
  //     console.log("4444")
  // });

  const feedAddReqDTO = new FeedAddReqDto(JSON.parse(req.body.feed));
  console.log(feedAddReqDTO);
  try {
    //이미지 처리
    let decode = await Buffer.from(feedAddReqDTO.feed_img, "base64");
    // let decode = await Buffer.from(base64String, 'base64')
    // console.log("34343")
    // console.log(decode)
    const feed_imgpath = uuidv4();
    await fs.writeFileSync("public/img/feed/" + feed_imgpath + ".jpg", decode);

    const userEntity = await User.findOne({
      where: {
        user_email: feedAddReqDTO.user_email,
      },
    });

    if (userEntity) {
      // console.log(userEntity);
      const feedEntity = await Feed.build({
        feed_content: feedAddReqDTO.feed_content,
        feed_imgpath: feed_imgpath,
        user_id: userEntity.user_id,
        feed_like_cnt: 0, //게시물 등록시 좋아요 0개
      });

      await feedEntity
        .save()
        .then((feed) => {
          console.log("feed saved:", feed.toString);
          res.json(1);
        })
        .catch((error) => {
          console.log("error saving feed:", error);
          res.json("feed add fail");
        });
    } else {
      console.log("존재하지 않는 이메일");
    }
  } catch (error) {
    console.log(error);
    res.json("server error");
  }
});

//피드 수정
router.patch("/update", async (req, res) => {
  const feedUpdateReqDTO = new FeedUpdateReqDto(req.body.feed);
  try {
    const feedEntity = await Feed.update(
      {
        feed_content: feedUpdateReqDTO.feed_content,
        feed_imgpath: feedUpdateReqDTO.feed_imgpath,
      },
      {
        where: {
          user_id: feedUpdateReqDTO.user_id,
          feed_id: feedUpdateReqDTO.feed_id,
        },
        individualHooks: true,
      }
    );

    if (feedEntity[0]) {
      console.log(feedEntity[0]);
      res.send("feed update success");
    } else {
      console.log("feed_id 혹은 user_id 불일치");
      res.send("feed_id 혹은 user_id 불일치");
    }
  } catch (error) {
    console.log("error updating feed:", error);
    res.send("feed update fail");
  }
});

//피드 삭제
router.delete("/delete", async (req, res, next) => {
  const feedDeleteDTO = new FeedDeleteReqDto(req.body.deleteFeed);

  try {
    const feedEntity = await Feed.destroy({
      where: {
        feed_id: feedDeleteDTO.feed_id,
        user_id: feedDeleteDTO.user_id,
      },
    });

    if (feedEntity) {
      res.send("feed delete success");
    } else {
      res.send("feed delete fail");
    }
  } catch (error) {
    console.log("error", error);
    next(error);
  }
});

//단일 피드 조회
router.get("/find/:id", async (req, res, next) => {
  // console.log("find 통신 확인");
  console.log(req.params);
  const findReqFeedId = req.params.id;

  try {
    const feedEntity = await Feed.findOne({
      where: {
        feed_id: findReqFeedId,
      },
    });

    //해당 게시글에 대한 댓글 조회
    const commentEntity = await Comment.findAll({
      where: {
        feed_id: findReqFeedId,
      },
    });

    console.log(commentEntity);

    const feedResDTO = new FeedResDTO(feedEntity, commentEntity);
    const imgPath = feedResDTO.feed_imgpath;
    console.log(imgPath);
    console.log("public/img/feed/" + imgPath + ".jpg");
    console.log(`public/img/feed/${imgPath}.jpg`);
    //비동기 방식으로 파일 읽기)

    await fs.readFile("public/img/feed/" + imgPath + ".jpg", (err, data) => {
      console.log("------------");
      console.log(data);
      console.log("---------------");
      if (err) {
        console.log("error", err);
        next(err);
      } else {
        let encode = Buffer.from(data).toString("base64");
        // console.log(encode);
        // console.log(feedResDTO);
        feedResDTO.feed_imgpath = encode;
        // console.log(encode);
        // console.log(feedResDTO);
        res.json(feedResDTO);
      }
    });
  } catch (error) {
    console.log("error", error);
    next(error);
  }
});

//전체 피드 조회
router.get("/findall", async (req, res, next) => {
  console.log("findall 통신 확인");
  // let feedList = []

  // try{
  //     const feedEntity = await Feed.findAll()
  //     console.log(feedEntity.length);

  //     let cnt = 0;
  //     for(let i = feedEntity.length ; i >= 1 ; i--){
  //         console.log("i : " + i)
  //         try{
  //             const feedOneEntity = await Feed.findOne({
  //                 where: {
  //                     feed_id : i
  //                 }
  //             })

  //             //해당 게시글에 대한 댓글 조회
  //             const commentEntity = await Comment.findAll({
  //                 where : {
  //                     feed_id : i
  //                 }
  //             })

  //             // console.log(commentEntity);

  //             const feedResDTO = new FeedResDTO(feedOneEntity, commentEntity)
  //             const imgPath = feedResDTO.feed_imgpath;
  //             // console.log(imgPath);
  //             // console.log('public/img/feed/' + imgPath + '.jpg')
  //             // console.log(`public/img/feed/${imgPath}.jpg`)
  //             //비동기 방식으로 파일 읽기)

  //             await fs.readFile('public/img/feed/' + imgPath + '.jpg', (err, data) => {
  //                 // console.log("------------")
  //                 // console.log(data)
  //                 // console.log("---------------")
  //                 if (err) {
  //                     console.log('error', err);
  //                 } else {
  //                     let encode = Buffer.from(data).toString('base64');
  //                     // console.log(encode);
  //                     // console.log(feedResDTO);
  //                     feedResDTO.feed_imgpath = encode
  //                     // console.log(encode);
  //                     // console.log(feedResDTO);
  //                     feedList.push(feedResDTO)
  //                     // console.log('---------------------');
  //                     // console.log(feedList);
  //                     console.log(feedList)
  //                 }

  //             });
  //         }catch(error){
  //             console.log('error', error);
  //         }
  //     }
  //     const allfeeds = new FeedAllResDTO(feedList)
  //     res.json(allfeeds)
  // }catch(error){
  //     console.log('error', error);
  //     next(error)
  // }
  try {
    const feedEntity = await Feed.findAll();
    let feedList = [];

    for (let i = feedEntity.length; i >= 1; i--) {
      try {
        const feedOneEntity = await Feed.findOne({
          where: {
            feed_id: i,
          },
        });
        const commentEntity = await Comment.findAll({
          where: {
            feed_id: i,
          },
        });

        //이메일 꺼내기
        const userEntity = await User.findOne({
          where: {
            user_id: feedOneEntity.user_id,
          },
        });

        const feedResDTO = new FeedResDTO(
          feedOneEntity,
          commentEntity,
          userEntity
        );
        const imgPath = feedResDTO.feed_imgpath;

        const data = await fs2.readFile("public/img/feed/" + imgPath + ".jpg");
        const encode = Buffer.from(data).toString("base64");
        feedResDTO.feed_imgpath = encode;

        feedList.push(feedResDTO);
        console.log(feedResDTO);
      } catch (error) {
        console.log("error", error);
      }
    }

    // 모든 파일 읽기 작업이 완료된 후에 응답을 보냅니다.
    const allfeeds = new FeedAllResDTO(feedList);
    res.json(allfeeds);
  } catch (error) {
    console.log("error", error);
    next(error);
  }
});

module.exports = router;
