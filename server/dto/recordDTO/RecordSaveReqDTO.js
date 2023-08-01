class RecordSaveReqDTO {
  constructor(data) {
    this.record_date = data.record_date;
    this.user_email = data.user_email;
    this.record_detail = data.record_detail;
  }
}

module.exports = RecordSaveReqDTO;
