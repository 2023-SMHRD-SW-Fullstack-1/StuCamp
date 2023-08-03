class WishDeleteReqDTO {
    constructor(user, wish) {
        this.user_email = user.user_email;
        this.feed_id = wish.feed_id;
    }
}

module.exports = WishDeleteReqDTO;
