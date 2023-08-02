class FeedDeleteReqDto {
    constructor(data) {
        this.feed_id = data.feed_id;
        this.user_email = data.user_email;
    }
}

module.exports = FeedDeleteReqDto;
