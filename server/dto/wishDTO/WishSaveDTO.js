class WishSaveDTO {
  constructor(User, Feed) {
    this.user_id = User.user_id;
    this.feed_id = Feed.feed_id;
  }
}

module.exports = WishSaveDTO;
