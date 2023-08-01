const express = require("express");
const router = express.Router();
const User = require("../models/user");
const LoginReqDTO = require("../dto/userDTO/LoginReqDTO");
const UpdateReqDTO = require("../dto/userDTO/UpdateReqDto");
const FindReqDTO = require("../dto/userDTO/FindReqDto");

// 로그인
router.post("/login", async (req, res, next) => {
  const loginReqDTO = new LoginReqDTO(req.body.user);
  try {
    const userEntity = await User.findOne({
      where: {
        user_email: loginReqDTO.user_email,
        user_password: loginReqDTO.user_password,
      },
    });

    if (userEntity) {
      console.log(userEntity);
    } else {
      console.log("user login ... 사용자가 존재하지 않습니다.");
    }
  } catch (error) {
    console.error(error);
    res.status(500).send("user login ... 내부 서버 오류");
  }
});

// 단일 회원 정보 확인
router.get("/find/:user_email", async (req, res, next) => {
  const findReqDTO = new FindReqDTO(req.params.user_email);
  try {
    const userEntity = await User.findOne({
      where: {
        user_email: findReqDTO.user_email,
      },
    });

    if (userEntity) {
      console.log(userEntity);
    } else {
      console.log("user find ... 사용자가 존재하지 않습니다.");
    }
  } catch (error) {
    console.error(error);
    res.status(500).send("user find ... 내부 서버 오류");
  }
});

// 회원 수정
router.put("/update", async (req, res, next) => {
  const updateReqDTO = new UpdateReqDTO(req.body.user);
  try {
    const userEntity = await User.update(
      {
        user_password: updateReqDTO.user_password,
        user_nickname: updateReqDTO.user_nickname,
      },
      {
        where: {
          user_email: updateReqDTO.user_email,
        },
      }
    );

    if (userEntity[0]) {
      console.log(userEntity[0]);
      // res.status(200).json()
    } else {
      console.log("user update ... 사용자가 존재하지 않습니다.");
    }
  } catch (error) {
    console.error(error);
    res.status(500).send("user update ... 내부 서버 오류");
  }
});

router.post("/join", (req, res, next) => {
  console.log("first");
  let result = req.body;
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

  res.json("result");
});

module.exports = router;
