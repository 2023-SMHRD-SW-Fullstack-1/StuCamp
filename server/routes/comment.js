const express = require('express')
const CommentAddReqDto = require('../dto/commentDTO/CommentAddReqDTO')
const CommentDelReqDto = require('../dto/commentDTO/CommentDeleteReqDTO')
const { Comment } = require('../models')
CommentAddReqDTO = require('../dto/commentDTO/CommentAddReqDTO')
const router = express.Router()

//댓글작성
router.post("/add", async(req,res) => {
    // console.log("comment add 통신확인")
    const commentAddReqDTO = new CommentAddReqDto(req.body.addComment)
    const commentEntity = await Comment.build({
        user_id : commentAddReqDTO.user_id,
        feed_id : commentAddReqDTO.feed_id,
        comment_content : commentAddReqDTO.comment_content
    })

    await commentEntity
    .save()
    .then((comment)=>{
        console.log("feed saved:", comment.toString);
        res.json(commentEntity)
    })
    .catch((error)=>{
        console.log("error saving feed:", error);
        res.send("comment add fail")
    })
})

//댓글 삭제 
router.delete("/delete", async(req, res, next)=>{
    const commentDelReqDTO = new CommentDelReqDto(req.body.deleteComment)

    try{
        const CommentEntity = await Comment.destroy({
            where: {
                comment_id : commentDelReqDTO.comment_id,
                user_id : commentDelReqDTO.user_id
            }
        })

        if(CommentEntity){
            res.send("feed delete success")
        }else{
            res.send("feed delete fail")
        }

    }catch(error){
        console.log('error', error);
        next(error)
    }

})

module.exports = router