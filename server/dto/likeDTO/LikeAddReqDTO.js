class LikeAddReqDTO {
    constructor(data) {
      this.user_id = data.user_id;
      this.feed_id = data.feed_id
    }
  }
  
  module.exports = LikeAddReqDTO;