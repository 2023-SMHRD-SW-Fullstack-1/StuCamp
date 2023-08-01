const Sequelize = require("sequelize");

module.exports = class RecordDetail extends Sequelize.Model {
  static init(sequelize) {
    return super.init(
      {
        record_detail_id: {
          type: Sequelize.BIGINT,
          allowNull: false,
          primaryKey: true,
          unique: true,
          autoIncrement: true,
        },
        record_start_date: {
          type: Sequelize.DataTypes.DATE,
        },
        record_end_date: {
          type: Sequelize.DataTypes.DATE,
        },
        record_elapsed_time: {
          type: Sequelize.BIGINT,
        },
        record_subject: {
          type: Sequelize.STRING,
        },
        record_id: {
          type: Sequelize.BIGINT,
        },
      },
      {
        // 테이블에 대한 설정
        sequelize, // models/index -> user 연결
        timestamps: true, // true -> createdAt, updateAt
        modelName: "RecordDetail", // 프로젝트(node) 에서 사용하는 모델 이름
        tableName: "recorddetail", // 실제 DB 테이블 이름
        charset: "utf8",
        collate: "utf8_general_ci",
      }
    );
  }
  static associate(db) {
    db.RecordDetail.belongsTo(db.Record, {
      foreignKey: "record_id",
      targetKey: "record_id",
    });
    // db.User.hasMany(db.Board, { foreignKey: "id", sourceKey: "id" }); // 1:N
    // db.User.hasOne ... 1:1
    // db.User.belongsToMany ... N : M
    // foreignKey는 foreignKey지정만 하는거고
    //데이터 가져올때는 sourceKey로 인해 가져온다.
  }
};
