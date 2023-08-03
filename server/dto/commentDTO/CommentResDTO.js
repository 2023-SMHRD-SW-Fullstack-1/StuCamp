class CommentAddReqDto {
    constructor(commentOneEntity, userEntity) {
        this.user_nickname = userEntity.user_nickname;
        this.comment_content = commentOneEntity.comment_content;
    }
}

module.exports = CommentAddReqDto;
