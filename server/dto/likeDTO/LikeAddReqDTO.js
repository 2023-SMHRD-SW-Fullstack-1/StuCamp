class LikeAddReqDTO {
    constructor(data) {
      this.user_email = data.user_email;
      this.feed_id = data.feed_id
    }
  }
  
  module.exports = LikeAddReqDTO;