package parser

/**
 * Created by Tobias on 2014-03-16.
 */
class Util {

    static String today() {
        /* This will give AccessControlException : access denied ("java.lang.RuntimePermission" "accessClassInPackage.sun.util.calendar")
        def cal = Calendar.getInstance()
        String today = cal.get(Calendar.YEAR) + "-" +
                maybeAddZero(cal.get(Calendar.MONTH)+1) + "-" +
                maybeAddZero(cal.get(Calendar.DAY_OF_MONTH))*/

        Date date = new Date()
        String today = date.format("YYYY-MM-dd")
        return today
    }

    static String maybeAddZero(Integer datePart) {
        maybeAddZero(datePart.toString())
    }

    static String maybeAddZero(String datePart) {
        if (datePart?.length() < 2) {
            return "0" + datePart
        }
        return datePart
    }

}
