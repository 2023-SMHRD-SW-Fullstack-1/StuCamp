const express = require("express");
const CommentAddReqDto = require("../dto/commentDTO/CommentAddReqDTO");
const CommentDelReqDto = require("../dto/commentDTO/CommentDeleteReqDTO");
const CommentResDTO = require("../dto/commentDTO/CommentResDTO");
const { Comment, User } = require("../models");
CommentAddReqDTO = require("../dto/commentDTO/CommentAddReqDTO");
const router = express.Router();

//댓글작성
router.post("/add", async (req, res) => {
    console.log("comment add 통신확인");
    const commentAddReqDTO = new CommentAddReqDto(req.body.addComment);
    const commentEntity = await Comment.build({
        user_id: commentAddReqDTO.user_id,
        feed_id: commentAddReqDTO.feed_id,
        comment_content: commentAddReqDTO.comment_content,
    });

    await commentEntity
        .save()
        .then((comment) => {
            console.log("feed saved:", comment.toString);
            res.json(commentEntity);
        })
        .catch((error) => {
            console.log("error saving feed:", error);
            res.send("comment add fail");
        });
});

//댓글 삭제
router.delete("/delete", async (req, res, next) => {
    const commentDelReqDTO = new CommentDelReqDto(req.body.deleteComment);

    try {
        const CommentEntity = await Comment.destroy({
            where: {
                comment_id: commentDelReqDTO.comment_id,
                user_id: commentDelReqDTO.user_id,
            },
        });

        if (CommentEntity) {
            res.send("feed delete success");
        } else {
            res.send("feed delete fail");
        }
    } catch (error) {
        console.log("error", error);
        next(error);
    }
});

//해당 게시물에 대한 전체 댓글 조회
router.get("/:feed_id", async (req, res) => {
    console.log("댓글 조회 통신 확인");
    let feed_id = req.params.feed_id;
    console.log("feed id : ", feed_id);
    const commentEntity = await Comment.findAll({
        where: {
            feed_id: feed_id,
        },
    });

    console.log("commnetEntity : ", commentEntity);

    for (const item of commentEntity) {
        console.log(item.feed_id);
        try {
            const commentOneEntity = await Comment.findOne({
                where: {
                    comment_id: item.comment_id,
                },
            });
            console.log(commentOneEntity);

            const userEntity = await User.findOne({
                where: {
                    user_id: commentOneEntity.user_id,
                },
            });

            if (userEntity) {
                const commentResDTO = new CommentResDTO(commentOneEntity.comment_content, userEntity.user_nickname);
                res.json(commentResDTO);
            } else {
                res.json("-1");
            }
        } catch (error) {
            console.log("error---------", error);
        }
    }
});

module.exports = router;
