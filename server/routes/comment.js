const express = require("express");
const CommentAddReqDto = require("../dto/commentDTO/CommentAddReqDTO");
const CommentDelReqDto = require("../dto/commentDTO/CommentDeleteReqDTO");
const CommentResDTO = require("../dto/commentDTO/CommentResDTO");
const { Comment, User } = require("../models");
const CommentAllResDTO = require("../dto/commentDTO/CommentAllDTO");
const router = express.Router();

//댓글작성
router.post("/add", async (req, res) => {
  console.log("comment add 통신확인");
  console.log(req.body)
  const commentAddReqDTO = new CommentAddReqDto(JSON.parse(req.body.addComment));
   console.log("------------------------", commentAddReqDTO);

      //이메일 조회
      const userEntity = await User.findOne({
        where: {
            user_email: commentAddReqDTO.user_email,
        },
    });

    if(userEntity){

        const commentEntity = await Comment.build({
            user_id: userEntity.user_id,
            feed_id: commentAddReqDTO.feed_id,
            comment_content: commentAddReqDTO.comment_content,
          });
        
          await commentEntity
            .save()
            .then((comment) => {
              console.log("feed saved:", comment.toString);
              res.json(commentEntity);
              console.log("commentEntity 확인 ->" , commentEntity)
            })
            .catch((error) => {
              console.log("error saving feed:", error);
              res.send("comment add fail");
            });
    }

});

//댓글 삭제
router.delete("/delete", async (req, res, next) => {
  const commentDelReqDTO = new CommentDelReqDto(req.body.deleteComment);

        //이메일 조회
        const userEntity = await User.findOne({
            where: {
                user_email: commentDelReqDTO.user_email,
            },
        });

        if(userEntity){
            try {
                const CommentEntity = await Comment.destroy({
                  where: {
                    comment_id: commentDelReqDTO.comment_id,
                    user_id: userEntity.user_id,
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
        }
     });
        

        
  

//해당 게시물에 대한 전체 댓글 조회
router.get("/:feed_id", async (req, res) => {
  console.log("댓글 조회 통신 확인");
  let feed_id = req.params.feed_id;
  console.log("req.params", req.params)
  console.log("feed id : ", feed_id);

  const commentEntity = await Comment.findAll({
    where: {
      feed_id: feed_id,
    },
  });

  console.log("commnetEntity : ", commentEntity);
  let commentList = []

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
        console.log(commentOneEntity.comment_content);
        console.log(userEntity.user_nickname);
        const commentResDTO = new CommentResDTO(commentOneEntity, userEntity)
        console.log("---------------------코멘트 결과 : ", commentResDTO);
        commentList.push(commentResDTO)
      } else {
        res.json("-1");
      }
    } catch (error) {
      console.log("error---------", error);
    }
  }

  const allComments = new CommentAllResDTO(commentList)
  res.json(allComments)

});

module.exports = router;
