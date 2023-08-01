class FeedDeleteReqDto {
    constructor(data) {
      this.feed_id = data.feed_id;
      this.user_id = data.user_id
    }
  }
  
  module.exports = FeedDeleteReqDto;