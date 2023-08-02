const express = require("express");
const router = express.Router();
const User = require("../models/user");
const Record = require("../models/record");
const RecordDetail = require("../models/record_detail");
const RecordSaveReqDTO = require("../dto/recordDTO/RecordSaveReqDTO");
const RecordDetailSaveReqDTO = require("../dto/recordDetailDTO/RecordDetailSaveReqDTO");
const FindReqDTO = require("../dto/userDTO/FindReqDto");
const FindOneResDTO = require("../dto/recordDTO/findOneResDTO");
const FindAllResDTO = require("../dto/recordDTO/findAllResDTO");

// 사용자별 기록 전체 조회
router.get("/:user_email", async (req, res, next) => {
  const findReqDTO = new FindReqDTO(req.params.user_email);
  const date = req.query?.record_date;
  try {
    const userEntity = await User.findOne({
      where: {
        user_email: findReqDTO.user_email,
      },
    });

    if (userEntity) {
      let recordDetails = [];
      if (date) {
        // date가 같이 실어져 왔을시 => 특정일자
        const recordEntity = await Record.findOne({
          where: {
            user_id: userEntity.user_id,
            record_date: date,
          },
        });
        recordDetails = [
          ...recordDetails,
          await RecordDetail.findAll({
            where: {
              record_id: recordEntity.record_id,
            },
          }),
        ];
        const findOneResDTO = new FindOneResDTO(recordEntity, recordDetails);
        res.json(findOneResDTO);
      } else {
        // 전체 조회
        const recordEntity = await Record.findAll({
          where: {
            user_id: userEntity.user_id,
          },
        });

        let recordDetails = [];
        for (const item of recordEntity) {
          const details = await RecordDetail.findAll({
            where: {
              record_id: item.record_id,
            },
          });
          recordDetails.push({ record_date: item.record_date, details });
        }
        const findAllResDTO = new FindAllResDTO(recordDetails);

        res.json(findAllResDTO);
      }
    } else {
      console.log("user find ... 사용자가 존재하지 않습니다.");
      res.send("userNotExisted");
    }
  } catch (error) {
    console.error(error);
    res.status(500).send("user find ... 내부 서버 오류");
  }
});

// 사용자별 해당 일자 조회
router.get("/:user_email?date={date}");

// 기록 저장
router.post("/add", async (req, res, next) => {
  console.log(req.body)
  // 사용자로부터 받은 데이터(더미)
  // const requestData = {
  //   record: {
  //     record_date: "2023-08-05",
  //     user_email: 222222,
  //     record_detail: {
  //       record_start_date: "2023-08-05T10:20:40",
  //       record_end_date: "2023-08-05T10:20:59",
  //       record_elapsed_time: 20,
  //       record_subject: "국어",
  //     },
  //   },
  // };
  // 실제
  const requestData = req.body;

  const recordSaveReqDTO = new RecordSaveReqDTO(requestData.record);
  try {
    // 이메일을 기준으로 사용자 데이터 조회
    const userEntity = await User.findOne({
      where: { user_email: recordSaveReqDTO.user_email },
    });
    if (userEntity) {
      // 해당날짜 기록 존재유무
      let existDate = await Record.findOne({
        where: {
          user_id: userEntity.user_id,
          record_date: recordSaveReqDTO.record_date,
        },
      });
      if (!existDate) {
        // 기록 저장
        existDate = await Record.create({
          user_id: userEntity.user_id,
          record_date: recordSaveReqDTO.record_date,
        });
      }
      const { record_elapsed_time, record_subject } =
        recordSaveReqDTO.record_detail;
      const record_start_date = new Date(
        requestData.record.record_detail.record_start_date
      );
      const record_end_date = new Date(
        requestData.record.record_detail.record_end_date
      );
      // record detail 저장
      await RecordDetail.create({
        record_start_date,
        record_end_date,
        record_elapsed_time,
        record_subject,
        record_id: existDate.record_id,
      }).then(() => {
        res.send("RecordSaveSuccess");
      });
    } else {
      console.log("사용자를 찾을 수 없습니다.");
      res.send("RecordSaveFail");
    }
  } catch (error) {
    console.error(error);
  }
});

module.exports = router;
