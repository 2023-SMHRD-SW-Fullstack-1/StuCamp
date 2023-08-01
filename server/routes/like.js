const express = require('express')
const LikeAddReqDTO = require('../dto/likeDTO/LikeAddReqDTO')
const { Like, Feed } = require('../models')
const router = express.Router()

//좋아요 클릭했을 때
router.post("/add", async(req,res) => {
    // console.log("like add 통신 확인")
    const likeAddDTO = new LikeAddReqDTO(req.body.addLike)
    const likeEntity = await Like.build({
        user_id : likeAddDTO.user_id,
        feed_id : likeAddDTO.feed_id
    })

    //좋아요 등록
    await likeEntity
    .save()
    .then(async (like)=>{

        //좋아요 개수 추가 (feedEntity)
        //해당 게시글에 대한 전체 좋아요 조회
        const allLike = await Like.findAll({
            where : {
                feed_id : likeAddDTO.feed_id
            }
        })

        console.log("like list :" , allLike.length);

        //Feed 엔터티 좋아요 개수 update
        Feed.update({
            feed_like_cnt: allLike.length
        },{
            where : {
                feed_id : likeAddDTO.feed_id
            }
        })

        console.log("like saved: ", like.toJSON);
        res.send("like add success")
    })
    .catch((error)=>{
        console.log("Error saving like : ", error);
        res.send("like add fail")
    })
})

//좋아요 취소
router.delete('/cancel', async(req, res, next)=>{
    const likeCancelDTO = new LikeAddReqDTO(req.body.cancelLike)
    // console.log(likeCancelDTO);

    try{
        const likeEntity = await Like.destroy({
            where: {
                feed_id : likeCancelDTO.feed_id,
                user_id : likeCancelDTO.user_id 
            }
        })
        if(likeEntity){
            res.send("like cancel success")
        }else{
            res.send("like cancel fail")
        }
    }catch(error){
        console.log('error', error);
        next(error)
    }

})

module.exports = router