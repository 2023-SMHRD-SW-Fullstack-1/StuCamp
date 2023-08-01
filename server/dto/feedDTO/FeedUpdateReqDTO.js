class FeedUpdateReqDto {
    constructor(data) {
      this.feed_content = data.feed_content;
      this.feed_imgpath = data.feed_imgpath;
      this.user_id = data.user_id;
      this.feed_id = data.feed_id;
    }
  }
  
  module.exports = FeedUpdateReqDto;