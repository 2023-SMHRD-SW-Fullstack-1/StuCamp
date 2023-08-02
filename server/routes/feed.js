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

    const feedAddReqDTO = new FeedAddReqDto(JSON.parse(req.body.feed));
    // console.log(feedAddReqDTO);
    try {
        //이미지 처리
        let decode = await Buffer.from(feedAddReqDTO.feed_img, "base64");
        // let decode = await Buffer.from(base64String, 'base64')
        // console.log("34343")
        // console.log(decode)
        // console.log(feedAddReqDTO.feed_img);
        const feed_imgpath = uuidv4();
        await fs.writeFileSync("public/img/feed/" + feed_imgpath + ".jpg", decode);

        const userEntity = await User.findOne({
            where: {
                user_email: feedAddReqDTO.user_email,
            },
        });

        if (userEntity) {
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
                    res.json(0);
                });
        } else {
            console.log("존재하지 않는 이메일");
        }
    } catch (error) {
        console.log(error);
        res.json(-1);
    }
});

//피드 수정
router.post("/update", async (req, res) => {
    console.log("feed update 통신 확인");
    const feedUpdateReqDTO = new FeedUpdateReqDto(JSON.parse(req.body.updateFeed));
    // console.log("feed update DTO : ", feedUpdateReqDTO);

    //이미지 처리
    let decode = await Buffer.from(feedUpdateReqDTO.feed_img, "base64");
    const feed_imgpath = uuidv4();
    await fs.writeFileSync("public/img/feed/" + feed_imgpath + ".jpg", decode);

    const userEntity = await User.findOne({
        where: {
            user_email: feedUpdateReqDTO.user_email,
        },
    });

    if (userEntity) {
        try {
            const feedEntity = await Feed.update(
                {
                    feed_content: feedUpdateReqDTO.feed_content,
                    feed_imgpath: feed_imgpath,
                },
                {
                    where: {
                        user_id: userEntity.user_id,
                        feed_id: feedUpdateReqDTO.feed_id,
                    },
                    individualHooks: true,
                }
            );

            if (feedEntity[0]) {
                console.log(feedEntity[0]);
                res.json(1);
            } else {
                console.log("feed_id 혹은 user_id 불일치");
                res.json(0);
            }
        } catch (error) {
            console.log("error updating feed:", error);
            res.json(-1);
        }
    }
});

//피드 삭제
router.post("/delete", async (req, res, next) => {
    const feedDeleteDTO = new FeedDeleteReqDto(JSON.parse(req.body.deleteFeed));

    //이메일 조회
    const userEntity = await User.findOne({
        where: {
            user_email: feedDeleteDTO.user_email,
        },
    });

    if (userEntity) {
        try {
            const feedEntity = await Feed.destroy({
                where: {
                    feed_id: feedDeleteDTO.feed_id,
                    user_id: userEntity.user_id,
                },
            });

            if (feedEntity) {
                res.json(1);
            } else {
                res.json(0);
            }
        } catch (error) {
            console.log("error", error);
            next(error);
        }
    }
});

//전체 피드 조회
router.get("/findall", async (req, res, next) => {
    console.log("findall 통신 확인");

    try {
        const feedEntity = await Feed.findAll();
        let feedList = [];

        for (let item of feedEntity) {
            // console.log(feedEntity.length);
            try {
                const feedOneEntity = await Feed.findOne({
                    where: {
                        feed_id: item.feed_id,
                    },
                });
                // console.log("--------------------", feedOneEntity);
                const commentEntity = await Comment.findAll({
                    where: {
                        feed_id: item.feed_id,
                    },
                });

                //이메일 꺼내기
                const userEntity = await User.findOne({
                    where: {
                        user_id: feedOneEntity.user_id,
                    },
                });

                const feedResDTO = new FeedResDTO(feedOneEntity, commentEntity, userEntity);
                const imgPath = feedResDTO.feed_imgpath;

                const data = await fs2.readFile("public/img/feed/" + imgPath + ".jpg");
                const encode = Buffer.from(data).toString("base64");
                feedResDTO.feed_imgpath = encode;

                feedList.push(feedResDTO);
                // console.log(feedResDTO);
            } catch (error) {
                console.log("피드 조회 error", error);
            }
        }

        // for (let i = feedEntity.length; i >= 1; i--) {
        //     console.log(feedEntity.length);
        //     try {
        //         const feedOneEntity = await Feed.findOne({
        //             where: {
        //                 feed_id: i,
        //             },
        //         });
        //         console.log("--------------------", i, feedOneEntity);
        //         const commentEntity = await Comment.findAll({
        //             where: {
        //                 feed_id: i,
        //             },
        //         });

        //         //이메일 꺼내기
        //         const userEntity = await User.findOne({
        //             where: {
        //                 user_id: feedOneEntity.user_id,
        //             },
        //         });

        //         const feedResDTO = new FeedResDTO(feedOneEntity, commentEntity, userEntity);
        //         const imgPath = feedResDTO.feed_imgpath;

        //         const data = await fs2.readFile("public/img/feed/" + imgPath + ".jpg");
        //         const encode = Buffer.from(data).toString("base64");
        //         feedResDTO.feed_imgpath = encode;

        //         feedList.push(feedResDTO);
        //         console.log(feedResDTO);
        //     } catch (error) {
        //         console.log("피드 조회 error", error);
        //     }
        // }

        // 모든 파일 읽기 작업이 완료된 후에 응답을 보냅니다.
        const allfeeds = new FeedAllResDTO(feedList);
        // console.log(allfeeds);
        res.json(allfeeds);
    } catch (error) {
        console.log("error : ", error);
        next(error);
    }
});

//마이 피드 조회
router.get("/:user_email", async (req, res, next) => {
    let user_email = req.params.user_email;
    console.log("user email : ", user_email);
    const userEntity = await User.findOne({
        where: {
            user_email: user_email,
        },
    });

    console.log("userEntitiy:", userEntity);

    if (userEntity) {
        try {
            const feedEntity = await Feed.findAll({
                where: {
                    user_id: userEntity.user_id,
                },
            });
            let feedList = [];

            for (const item of feedEntity) {
                console.log(item.feed_id);
                try {
                    const feedOneEntity = await Feed.findOne({
                        where: {
                            feed_id: item.feed_id,
                        },
                    });
                    console.log(feedOneEntity);
                    const commentEntity = await Comment.findAll({
                        where: {
                            feed_id: item.feed_id,
                        },
                    });

                    const feedResDTO = new FeedResDTO(feedOneEntity, commentEntity, userEntity);
                    const imgPath = feedResDTO.feed_imgpath;

                    const data = await fs2.readFile("public/img/feed/" + imgPath + ".jpg");
                    const encode = Buffer.from(data).toString("base64");
                    feedResDTO.feed_imgpath = encode;

                    feedList.push(feedResDTO);
                    // console.log(feedResDTO);
                } catch (error) {
                    console.log("error---------", error);
                }
            }

            // for (let i = feedEntity.length; i >= 1; i--) {
            // console.log(feedEntity.length);
            // try {
            //     const feedOneEntity = await Feed.findOne({
            //         where: {
            //             feed_id: i,
            //         },
            //     });
            //     console.log(feedOneEntity);
            //     const commentEntity = await Comment.findAll({
            //         where: {
            //             feed_id: i,
            //         },
            //     });
            //     const feedResDTO = new FeedResDTO(feedOneEntity, commentEntity, userEntity);
            //     const imgPath = feedResDTO.feed_imgpath;
            //     const data = await fs2.readFile("public/img/feed/" + imgPath + ".jpg");
            //     const encode = Buffer.from(data).toString("base64");
            //     feedResDTO.feed_imgpath = encode;
            //     feedList.push(feedResDTO);
            //     // console.log(feedResDTO);
            // } catch (error) {
            //     console.log("error---------", error);
            // }
            // }

            // 모든 파일 읽기 작업이 완료된 후에 응답을 보냅니다.
            const allfeeds = new FeedAllResDTO(feedList);
            res.json(allfeeds);
        } catch (error) {
            console.log("error", error);
            next(error);
        }
    }
});

//단일 피드 조회
// router.get("/find/:id", async (req, res, next) => {
//     // console.log("find 통신 확인");
//     console.log(req.params);
//     const findReqFeedId = req.params.id;

//     try {
//         const feedEntity = await Feed.findOne({
//             where: {
//                 feed_id: findReqFeedId,
//             },
//         });

//         //해당 게시글에 대한 댓글 조회
//         const commentEntity = await Comment.findAll({
//             where: {
//                 feed_id: findReqFeedId,
//             },
//         });

//         console.log(commentEntity);

//         const feedResDTO = new FeedResDTO(feedEntity, commentEntity);
//         const imgPath = feedResDTO.feed_imgpath;
//         console.log(imgPath);
//         console.log("public/img/feed/" + imgPath + ".jpg");
//         console.log(`public/img/feed/${imgPath}.jpg`);
//         //비동기 방식으로 파일 읽기)

//         await fs.readFile("public/img/feed/" + imgPath + ".jpg", (err, data) => {
//             console.log("------------");
//             console.log(data);
//             console.log("---------------");
//             if (err) {
//                 console.log("error", err);
//                 next(err);
//             } else {
//                 let encode = Buffer.from(data).toString("base64");
//                 // console.log(encode);
//                 // console.log(feedResDTO);
//                 feedResDTO.feed_imgpath = encode;
//                 // console.log(encode);
//                 // console.log(feedResDTO);
//                 res.json(feedResDTO);
//             }
//         });
//     } catch (error) {
//         console.log("error", error);
//         next(error);
//     }
// });

module.exports = router;
