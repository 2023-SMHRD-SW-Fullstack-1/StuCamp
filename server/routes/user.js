const express = require("express");
const router = express.Router();
const User = require("../models/user");
const UserLoginDTO = require("../dto/userDTO/userLoginDTO");

// 로그인
router.post("/login", async (req, res, next) => {
  console.log("123");
  const userLoginDTO = new UserLoginDTO(req.body.user);
  try {
    const userEntity = await User.findOne({
      where: {
        user_email: userLoginDTO.user_email,
        user_password: userLoginDTO.user_password,
      },
    });

    if (userEntity) {
      console.log(userEntity);
    } else {
      console.log("사용자가 존재하지 않습니다.");
    }
  } catch (error) {
    console.error(error);
    res.status(500).send("내부 서버 오류");
  }
});

module.exports = router;
