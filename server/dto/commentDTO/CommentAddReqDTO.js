class CommentAddReqDto {
    constructor(data) {
        this.user_id = data.user_id
        this.feed_id = data.feed_id;
        this.comment_content = data.comment_content;
    }
  }
  
  module.exports = CommentAddReqDto;