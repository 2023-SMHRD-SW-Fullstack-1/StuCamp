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
    db.User.hasMany(db.Record, {
      foreignKey: "user_id", // Record 모델에서 사용할 외래키 컬럼
      sourceKey: "user_id", // User 모델에서 참조하는 기본키 컬럼
      onDelete: "CASCADE", // User 데이터가 삭제될 경우 관련된 Record 데이터도 함께 삭제
      onUpdate: "CASCADE", // User 데이터의 기본키가 업데이트될 경우 관련된 Record 데이터도 함께 업데이트
    });
    db.User.hasMany(db.Feed, {
      foreignKey: "user_id", // Record 모델에서 사용할 외래키 컬럼
      sourceKey: "user_id", // User 모델에서 참조하는 기본키 컬럼
      onDelete: "CASCADE", // User 데이터가 삭제될 경우 관련된 Record 데이터도 함께 삭제
      onUpdate: "CASCADE", // User 데이터의 기본키가 업데이트될 경우 관련된 Record 데이터도 함께 업데이트
    });
    db.User.hasMany(db.Like, {
      foreignKey: "user_id", // Record 모델에서 사용할 외래키 컬럼
      sourceKey: "user_id", // User 모델에서 참조하는 기본키 컬럼
      onDelete: "CASCADE", // User 데이터가 삭제될 경우 관련된 Record 데이터도 함께 삭제
      onUpdate: "CASCADE", // User 데이터의 기본키가 업데이트될 경우 관련된 Record 데이터도 함께 업데이트
    });
    db.User.hasMany(db.Comment, {
      foreignKey: "user_id", // Record 모델에서 사용할 외래키 컬럼
      sourceKey: "user_id", // User 모델에서 참조하는 기본키 컬럼
      onDelete: "CASCADE", // User 데이터가 삭제될 경우 관련된 Record 데이터도 함께 삭제
      onUpdate: "CASCADE", // User 데이터의 기본키가 업데이트될 경우 관련된 Record 데이터도 함께 업데이트
    });
    db.User.hasMany(db.Wish, {
      foreignKey: "user_id", // Record 모델에서 사용할 외래키 컬럼
      sourceKey: "user_id", // User 모델에서 참조하는 기본키 컬럼
      onDelete: "CASCADE", // User 데이터가 삭제될 경우 관련된 Record 데이터도 함께 삭제
      onUpdate: "CASCADE", // User 데이터의 기본키가 업데이트될 경우 관련된 Record 데이터도 함께 업데이트
    });
    // db.User.hasMany(db.Board, { foreignKey: "id", sourceKey: "id" }); // 1:N
    // db.User.hasOne ... 1:1
    // db.User.belongsToMany ... N : M
    // foreignKey는 foreignKey지정만 하는거고
    //데이터 가져올때는 sourceKey로 인해 가져온다.
  }
};
