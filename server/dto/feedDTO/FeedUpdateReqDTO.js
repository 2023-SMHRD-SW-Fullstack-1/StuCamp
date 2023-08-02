class FeedUpdateReqDto {
    constructor(data) {
        this.feed_content = data.feed_content;
        this.feed_img = data.feed_imgpath;
        this.user_email = data.user_email;
        this.feed_id = data.feed_id;
    }
}

module.exports = FeedUpdateReqDto;
