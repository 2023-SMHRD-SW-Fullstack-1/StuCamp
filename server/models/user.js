const Sequelize = require("sequelize");

module.exports = class User extends Sequelize.Model {
  // init : 테이블 컬럼, 자료형 .. -> 테이블 자체 설정
  // associate : 테이블과 테이블의 관계 설정
  static init(sequelize) {
    return super.init(
      {
        // 컬럼 정보(id, pw, age)
        user_id: {
          type: Sequelize.BIGINT, // 자료형, 크기
          allowNull: false, // NULL 값 허용 여부
          primaryKey: true, // 기본키 지정
          unique: true, // UNIQUE 설정
          autoIncrement: true,
        },
        user_email: {
          type: Sequelize.STRING(50), // 자료형, 크기
        },
        user_password: {
          type: Sequelize.STRING(50),
        },
        user_nickname: {
          type: Sequelize.STRING(50),
        },
      },
      {
        // 테이블에 대한 설정
        sequelize, // models/index -> user 연결
        timestamps: true, // true -> createdAt, updateAt
        modelName: "User", // 프로젝트(node) 에서 사용하는 모델 이름
        tableName: "user", // 실제 DB 테이블 이름
        charset: "utf8",
        collate: "utf8_general_ci",
      }
    );
  }
  static associate(db) {
    // db.User.hasMany(db.Board, { foreignKey: "id", sourceKey: "id" }); // 1:N
    // db.User.hasOne ... 1:1
    // db.User.belongsToMany ... N : M
    // foreignKey는 foreignKey지정만 하는거고
    //데이터 가져올때는 sourceKey로 인해 가져온다.
  }
};
