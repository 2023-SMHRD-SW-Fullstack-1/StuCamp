const express = require("express");
const LikeAddReqDTO = require("../dto/likeDTO/LikeAddReqDTO");
const { Like, Feed, User } = require("../models");
const router = express.Router();

//좋아요 클릭했을 때
router.post("/add", async (req, res) => {
    console.log("like add 통신 확인");
    const likeAddDTO = new LikeAddReqDTO(JSON.parse(req.body.addLike));

    //이메일 조회
    const userEntity = await User.findOne({
        where: {
            user_email: likeAddDTO.user_email,
        },
    });

    // console.log("==================", userEntity);

    // //중복 좋아요 제외 처리
    // let likeEntity = await Like.findAll({
    //     where: {
    //         user_id: userEntity.user_id,
    //         feed_id: likeAddDTO.feed_id,
    //     },
    // });

    // console.log("======================", likeEntity);

    if (userEntity) {
        likeEntity = await Like.build({
            user_id: userEntity.user_id,
            feed_id: likeAddDTO.feed_id,
        });

        //좋아요 등록
        await likeEntity
            .save()
            .then(async (like) => {
                //좋아요 개수 추가 (feedEntity)
                //해당 게시글에 대한 전체 좋아요 조회
                const allLike = await Like.findAll({
                    where: {
                        feed_id: likeAddDTO.feed_id,
                    },
                });

                console.log("like list :", allLike.length);

                //Feed 엔터티 좋아요 개수 update
                await Feed.update(
                    {
                        feed_like_cnt: allLike.length,
                    },
                    {
                        where: {
                            feed_id: likeAddDTO.feed_id,
                        },
                    }
                );

                console.log("like saved: ", like.toJSON);

                res.json(1);
            })
            .catch((error) => {
                console.log("Error saving like : ", error);
                res.json(0);
            });
    } else {
        console.log("존재하지 않는 이메일");
    }
});

//좋아요 취소
router.post("/cancel", async (req, res, next) => {
    const likeCancelDTO = new LikeAddReqDTO(JSON.parse(req.body.cancelLike));
    // console.log(likeCancelDTO);
    // 이메일 조회
    const userEntity = await User.findOne({
        where: {
            user_email: likeCancelDTO.user_email,
        },
    });

    if (userEntity) {
        try {
            const likeEntity = await Like.destroy({
                where: {
                    feed_id: likeCancelDTO.feed_id,
                    user_id: userEntity.user_id,
                },
            });

            const allLike = await Like.findAll({
                where: {
                    feed_id: likeCancelDTO.feed_id,
                },
            });

            await Feed.update(
                {
                    feed_like_cnt: allLike.length,
                },
                {
                    where: {
                        feed_id: likeCancelDTO.feed_id,
                    },
                }
            );

            if (likeEntity) {
                res.json(1);
            } else {
                res.json(0);
            }
        } catch (error) {
            console.log("error", error);
            next(error);
        }
    } else {
        console.log("error");
    }
});

//해당 게시물에 대한 좋아요 조회
router.get("/:feed_id", async (req, res, next) => {
    let result = req.params.feed_id;

    try {
        const likeEntity = await Like.findAll({
            where: {
                feed_id: result,
            },
            include: [User],
        });

        if (likeEntity) {
            // console.log("=======================================", likeEntity);
            res.json(likeEntity);
        } else {
            res.json(0);
        }
    } catch {
        console.log("error", error);
        next(error);
    }
});

module.exports = router;
