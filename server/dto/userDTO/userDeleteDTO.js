class UserDeleteDTO {
    constructor(data) {
      this.user_email = data.user_email;
      this.user_password = data.user_password;
    }
  }
  
  module.exports = UserDeleteDTO;