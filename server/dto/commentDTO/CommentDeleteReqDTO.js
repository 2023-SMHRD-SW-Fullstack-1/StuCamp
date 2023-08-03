class CommentDeleteReqDto {
    constructor(data) {
      this.comment_id = data.comment_id;
      this.user_email = data.user_email
    }
  }
  
  module.exports = CommentDeleteReqDto;