class CommentDeleteReqDto {
    constructor(data) {
      this.comment_id = data.comment_id;
      this.user_id = data.user_id
    }
  }
  
  module.exports = CommentDeleteReqDto;