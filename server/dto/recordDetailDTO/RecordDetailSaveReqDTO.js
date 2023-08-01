class RecordDetailSaveReqDTO {
  constructor(data) {
    this.record_start_date = data.record_start_date;
    this.record_end_date = data.record_end_date;
    this.record_elapsed_time = data.record_elapsed_time;
    this.record_subject = data.record_subject;
  }
}

module.exports = RecordDetailSaveReqDTO;
