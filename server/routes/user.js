const express = require("express");
const { User } = require("../models");
const router = express.Router();

//회원가입 
router.get('/aa', (req,res) => {
    console.log("abc")
})

router.post('/join', (req, res, next)=>{
    console.log("first")
    let result = req.body
    console.log(result);
    // let result = req.body.joinVO
    // const joinDto = new joinDto(result);
    // const userEntity = User.build({
    //     user_email: joinDto.user_email,
    //     user_password: joinDto.user_password,
    //     user_nickname: joinDto.user_nickname
    // })

    // userEntity.save()
    // .then((user)=>{
    //     console.log('User saved:', user.toJSON);
    // })
    // .catch((error)=>{
    //     console.log('Error saving user:', error);
    // })

    res.json("result")
})

module.exports = router;
