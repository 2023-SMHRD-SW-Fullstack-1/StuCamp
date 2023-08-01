const express = require("express");
const router = express.Router();
const User = require("../models/user");
const LoginReqDTO = require("../dto/userDTO/LoginReqDTO");
const UpdateReqDTO = require("../dto/userDTO/UpdateReqDto");
const FindReqDTO = require("../dto/userDTO/FindReqDto");
const UserDeleteDTO = require("../dto/userDTO/userDeleteDTO");
const JoinDTO = require("../dto/userDTO/JoinDTO");

// 로그인
router.post("/login", async (req, res, next) => {
  const loginReqDTO = new LoginReqDTO(req.body.loginUser);
  try {
    const userEntity = await User.findOne({
      where: {
        user_email: loginReqDTO.user_email,
        user_password: loginReqDTO.user_password,
      },
    });

    if (userEntity) {
      console.log(userEntity);
      res.send("Success");
    } else {
      console.log("user login ... 사용자가 존재하지 않습니다.");
      res.send("Fail");
    }
  } catch (error) {
    console.error(error);
    res.status(500).send("user login ... 내부 서버 오류");
  }
});

// 단일 회원 정보 확인
router.get("/find/:user_email", async (req, res, next) => {
  const findReqDTO = new FindReqDTO(req.params.user_email);
  console.log(findReqDTO.user_email);
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
  const updateReqDTO = new UpdateReqDTO(req.body.updateUser);
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
        individualHooks: true, // updatedAt 칼럼을 자동으로 업데이트하도록 설정
      }
    );

    if (userEntity[0]) {
      console.log(userEntity[0]);
      res.status(200).send("Success");
    } else {
      console.log("user update ... 사용자가 존재하지 않습니다.");
    }
  } catch (error) {
    console.error(error);
    res.status(500).send("Fail");
  }
});

//회원가입
router.post("/join", async (req, res, next) => {
  let result = req.body.joinUser;

  const joinDto = new JoinDTO(result);
  // console.log(joinDto.user_email);
  // console.log(joinDto.user_nickname);
  // console.log(joinDto.user_password);

  //중복 이메일 확인
  let userEntity = await User.findOne({
    where: {
      user_email: joinDto.user_email,
    },
  });

  if (userEntity) {
    console.log("이미 존재하는 사용자");
    res.send("join fail : existed email");
  } else {
    userEntity = await User.build({
      user_email: joinDto.user_email,
      user_password: joinDto.user_password,
      user_nickname: joinDto.user_nickname,
    });

    await userEntity
      .save()
      .then((user) => {
        console.log("User saved:", user.toJSON);
        res.send("join success");
      })
      .catch((error) => {
        console.log("Error saving user:", error);
        res.send("join fail");
      });
  }
});

//회원탈퇴
router.delete("/delete", async (req, res, next) => {
  const userDeleteDTO = new UserDeleteDTO(req.body.deleteUser);
  try {
    //해당 아이디 패스워드 확인
    let userEntity = await User.findOne({
      where: {
        user_email: userDeleteDTO.user_email,
        user_password: userDeleteDTO.user_password,
      },
    });

    //계정 삭제
    if (userEntity) {
      userEntity = await User.destroy({
        where: {
          user_email: userDeleteDTO.user_email,
          user_password: userDeleteDTO.user_password,
        },
      });
      res.send("success delete");
    } else {
      res.send("패스워드가 일치하지 않습니다.");
    }
  } catch (error) {
    console.error("에러발생 : ", error);
    next(error);
  }
});

module.exports = router;
