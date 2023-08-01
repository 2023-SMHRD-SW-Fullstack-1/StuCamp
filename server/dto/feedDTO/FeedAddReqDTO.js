class FeedAddReqDto {
  constructor(data) {
    this.feed_content = data.feed_content;
    this.feed_img = data.feed_img;
    this.user_id = data.user_id
  }
}

module.exports = FeedAddReqDto;