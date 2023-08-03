const express = require("express");
const router = express.Router();
const Wish = require("../models/wish");
const User = require("../models/user");
const Feed = require("../models/feed");
const fs2 = require("fs").promises;
const WishSaveDTO = require("../dto/wishDTO/WishSaveDTO");
const WishDeleteReqDTO = require("../dto/wishDTO/WishDeleteReqDTO");
const WishFindDTO = require("../dto/wishDTO/WishFindDTO");

// 찜 등록
router.post("/add", async (req, res, next) => {
    let result = JSON.parse(req.body.wish);

    const user = await User.findOne({
        where: {
            user_email: result.user_email,
        },
    });
    const feed = await Feed.findOne({
        where: {
            feed_id: result.feed_id,
        },
    });
    const wishSaveDTO = new WishSaveDTO(user, feed);

    // 중복 찜 확인
    let wishEntity = await Wish.findOne({
        where: {
            user_id: user.user_id,
            feed_id: feed.feed_id,
        },
    });

    if (wishEntity) {
        console.log("이미 찜목록에 있습니다");
        res.json(-1);
    } else {
        wishEntity = await Wish.build({
            user_id: wishSaveDTO.user_id,
            feed_id: wishSaveDTO.feed_id,
        });

        await wishEntity
            .save()
            .then((wish) => {
                console.log("wish saved:", wish.toJSON);
                res.json(1);
            })
            .catch((error) => {
                console.log("Error saving user:", error);
                res.json(0);
            });
    }
});

// 찜 삭제
router.post("/delete", async (req, res, next) => {
    let result = JSON.parse(req.body.wish);
    // const wishDeleteReqDTO = new WishDeleteReqDTO(JSON.parse(req.body.wish));

    try {
        const userEntitiy = await User.findOne({
            where: {
                user_email: result.user_email,
            },
        });

        const wishEntity = await Wish.destroy({
            where: {
                feed_id: result.feed_id,
                user_id: userEntitiy.user_id,
            },
        });

        if (wishEntity) {
            res.json(1);
        } else {
            res.json(0);
        }
    } catch (error) {
        console.log("error", error);
        next(error);
    }
});

// 단일 유저 찜 조회
router.get("/:user_email", async (req, res, next) => {
    // console.log('findall 통신 확인');
    const wishFindDTO = new WishFindDTO(req.params.user_email);

    try {
        const userEntity = await User.findOne({
            where: {
                user_email: wishFindDTO.user_email,
            },
        });
        const wishEntity = await Wish.findAll({
            where: {
                user_id: userEntity.user_id,
            },
            include: [Feed],
        });
        // console.log(wishEntity);
        if (wishEntity.length > 0) {
            //이미지 변환
            for (let item of wishEntity) {
                // console.log(item);
                const imgPath = item.dataValues.Feed.feed_imgpath;
                // console.log("imgPath : ", imgPath);

                const data = await fs2.readFile("public/img/feed/" + imgPath + ".jpg");
                // console.log("data : ", data);

                const encode = Buffer.from(data).toString("base64");
                // console.log("encode : ", encode);
                item.Feed.set("feed_imgpath", encode);
            }
            // console.log(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", wishEntity);
            res.json(wishEntity);
        } else {
            res.json(0);
        }
    } catch (error) {
        console.log("error", error);
        next(error);
    }
});

module.exports = router;
