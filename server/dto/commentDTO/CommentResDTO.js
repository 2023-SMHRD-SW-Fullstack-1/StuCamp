class CommentAddReqDto {
    constructor(commentOneEntity, userEntity) {
        this.user_nickname = userEntity.user_nickname;
        this.comment_content = commentOneEntity.comment_content;
        this.comment_id = commentOneEntity.comment_id;
        this.user_email = userEntity.user_email;
    }
}

module.exports = CommentAddReqDto;
