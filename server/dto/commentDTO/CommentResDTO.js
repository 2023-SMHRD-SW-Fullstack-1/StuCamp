class CommentAddReqDto {
    constructor(data) {
        this.user_nick = data.user_nick;
        this.comment_content = data.comment_content;
    }
}

module.exports = CommentAddReqDto;
