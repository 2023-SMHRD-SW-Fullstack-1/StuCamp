class CommentAddReqDto {
    constructor(data) {
        this.user_email = data.user_email
        this.feed_id = data.feed_id;
        this.comment_content = data.comment_content;
    }
  }
  
  module.exports = CommentAddReqDto;