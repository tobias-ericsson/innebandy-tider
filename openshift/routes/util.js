exports.getCurrentDateString = function () {
    var d = new Date();
    var curr_date = d.getDate();
    var curr_month = d.getMonth() + 1; //Months are zero based
    if (curr_month < 10) {
        curr_month = "0" + curr_month;
    }
    if (curr_date < 10) {
        curr_date = "0" + curr_date;
    }
    var curr_year = d.getFullYear();
    var dateString = curr_year + "-" + curr_month + "-" + curr_date;
    return dateString;
};