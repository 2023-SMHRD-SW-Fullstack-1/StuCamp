class FeedResDTO {
    constructor(data) {
        this.feed_content = data.feed_content;
        this.feed_imgpath = data.feed_imgpath;
        this.user_id = data.user_id;
        this.feed_id = data.feed_id;
        this.feed_like_cnt = data.feed_like_cnt;
    }
  }
  
  module.exports = FeedResDTO;