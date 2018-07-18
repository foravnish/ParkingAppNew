package com.newwebinfotech.rishabh.parkingapp.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtility {


    public static String convertTime24to12Hours(String inputTime) {
        DateFormat inputFormat = new SimpleDateFormat("HH:mm:ss");
        DateFormat outputFormat = new SimpleDateFormat("hh:mm a");
        Date date = null;
        String outputDateStr = "";
        try {
            date = inputFormat.parse(inputTime);
            outputDateStr = outputFormat.format(date);
        } catch (ParseException | NullPointerException e) {
            L.m(e.toString());
        }
        return outputDateStr;
    }

    public static String convertTime12to24Hours(String inputTime) {
        DateFormat inputFormat = new SimpleDateFormat("hh:mm a");
        DateFormat outputFormat = new SimpleDateFormat("HH:mm");
        Date date = null;
        String outputDateStr = "";
        try {
            date = inputFormat.parse(inputTime);
            outputDateStr = outputFormat.format(date);
        } catch (ParseException e) {
            L.m(e.toString());
        }
        return outputDateStr;
    }

    public static String convertDateMMM(String inputDate) {
        DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat outputFormat = new SimpleDateFormat("dd-MMM");
        Date date = null;
        String outputDateStr = "";
        try {
            date = inputFormat.parse(inputDate);
            outputDateStr = outputFormat.format(date);
        } catch (ParseException e) {
            L.m(e.toString());
        }
        return outputDateStr;
    }

    public static String convertDate(String inputDate) {
        DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = null;
        String outputDateStr = "";
        try {
            date = inputFormat.parse(inputDate);
            outputDateStr = outputFormat.format(date);
        } catch (ParseException e) {
            L.m(e.toString());
        }
        return outputDateStr;
    }

    public static String convertDateMMM2(String inputDate) {
        DateFormat inputFormat = new SimpleDateFormat("yyyyMMdd");
        DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = null;
        String outputDateStr = "";
        try {
            date = inputFormat.parse(inputDate);
            outputDateStr = outputFormat.format(date);
        } catch (ParseException e) {
            L.m(e.toString());
        }
        return outputDateStr;
    }

    public static String convertDateYMD(String inputDate) {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = null;
        String outputDateStr = "";
        try {
            date = inputFormat.parse(inputDate);
            outputDateStr = outputFormat.format(date);
        } catch (ParseException e) {
            L.m(e.toString());
        }
        return outputDateStr;
    }

    public static String convertDateYMD2(String inputDate) {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yy");
        Date date = null;
        String outputDateStr = "";
        try {
            date = inputFormat.parse(inputDate);
            outputDateStr = outputFormat.format(date);
        } catch (ParseException e) {
            L.m(e.toString());
        }
        return outputDateStr;
    }

    public static String serverFormat(String inputDate) {
        DateFormat inputFormat = new SimpleDateFormat("dd-MMM-yy");
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        String outputDateStr = "";
        try {
            date = inputFormat.parse(inputDate);
            outputDateStr = outputFormat.format(date);
        } catch (ParseException e) {
            L.m(e.toString());
        }
        return outputDateStr;
    }

    public static String convertDateYMD3(String inputDate) {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yy");
        Date date = null;
        String outputDateStr = "";
        try {
            date = inputFormat.parse(inputDate);
            outputDateStr = outputFormat.format(date);
        } catch (ParseException e) {
            L.m(e.toString());
        }
        return outputDateStr;
    }

    public static String convertDateSplit(String inputDate) {
        DateFormat inputFormat = new SimpleDateFormat("dd-MMM-yy");
        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        String outputDateStr = "";
        try {
            date = inputFormat.parse(inputDate);
            outputDateStr = outputFormat.format(date);
        } catch (ParseException e) {
            L.m(e.toString());
        }
        return outputDateStr;
    }

    public static String convertDate(int day, int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month);
//		int numDays = cal.getActualMaximum(Calendar.DATE);
        String format = new SimpleDateFormat("d MMMM yyyy, EEEE").format(cal.getTime());

        return format;
    }

    public static String cDateDDMMMYY(int day, int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month);
        String format = new SimpleDateFormat("dd-MMM-yy").format(cal.getTime());

        return format;
    }

    public static String cDateMDY(int day, int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month);
        String format = new SimpleDateFormat("MM/dd/yyyy").format(cal.getTime());

        return format;
    }

    public static String cViewDateMDY(int day, int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month);
        String format = new SimpleDateFormat("d MMMM yyyy, EEEE").format(cal.getTime());

        return format;
    }

    public static String convertDateStamp(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month);
        String format = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());

        return format;
    }

    public static String convertDateTimeStamp(int year, int month, int day, int hour, int min) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, min);

        String format = new SimpleDateFormat("yyyyMMddHHmm").format(cal.getTime());

        return format;
    }

    public static String convertTimeTo24Hours(int hour, int min, String AMPM) {
        String startTime = String.valueOf(hour) + ":" + String.valueOf(min) + ":" + AMPM.toLowerCase();
        DateFormat inputFormat = new SimpleDateFormat("hh:mm:a");
        DateFormat outputFormat = new SimpleDateFormat("HH:mm");
        Date date = null;
        String outputDateStr = "";
        try {
            date = inputFormat.parse(startTime);
            outputDateStr = outputFormat.format(date);
        } catch (ParseException e) {
            L.m(e.toString());
        }
        return outputDateStr;
    }

    // enter fix Date eg.type 1- dd-MM-yyyy ,type 2- yyyy-MM-dd
    // enter dateFormat eg. dd-MM-yyyy,yyyy-MM-dd,
    public static String convertFormateDate(String Date, int type, String dateFormat) {
        String Day, middle, Month, Year;
        String finalDate = Date;
        if (type == 1) {
            Day = Date.substring(0, 2);
            middle = Date.substring(2, 3);
            Month = Date.substring(3, 5);
            Year = Date.substring(6, 10);

        } else {
            Day = Date.substring(0, 4);
            middle = Date.substring(4, 5);
            Month = Date.substring(5, 7);
            Year = Date.substring(8, 10);
        }

        switch (dateFormat) {
            case "dd-MM-yyyy":
                finalDate = Day + middle + Month + middle + Year;
                break;
            case "yyyy-MM-dd":
                finalDate = Year + middle + Month + middle + Day;
                break;
            case "MM-dd-yyyy":
                finalDate = Month + middle + Day + middle + Year;
                break;
            default:
                finalDate = "Date Format Incorrest";
        }
        return finalDate;
    }

    public static String convertTime(String time) {
        // String s = "12:18:00";
        String[] res = time.split(":");
        int hr = Integer.parseInt(res[0]);
        String min = res[1];

        if (hr == 12) {
            return (12 + ":" + min + " " + ((hr >= 12) ? "PM" : "AM"));
        }

        return (hr % 12 + ":" + min + " " + ((hr >= 12) ? "PM" : "AM"));
    }

    public void getDateTime() {
        String date;
        final Calendar cal = Calendar.getInstance();
//		syear = cal.get(Calendar.YEAR);
//		smonth = cal.get(Calendar.MONTH);
//		sday = cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int generateTimeStamp(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month);
        String format = new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime());
        String[] tempDate = format.split("-");
        String timeStamp = String.valueOf(tempDate[2]) + String.valueOf(tempDate[1]) + String.valueOf(tempDate[0]);
        return Integer.parseInt(timeStamp);
    }

    public static int generateTimeStamp(String date) {
        String[] tempDate = convertDateSplit(date).split("-");
        String timeStamp = String.valueOf(tempDate[2]) + String.valueOf(tempDate[1]) + String.valueOf(tempDate[0]);
        return Integer.parseInt(timeStamp);
    }

    public static String getTimeStamp() {
        return new SimpleDateFormat("_yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    }

    public static String getDateStampFormatted() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    public static int[] getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        return new int[]{day, month, year};
    }

    public static String getDateTimeStampFormatted() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    public static String[] getMonthStampFormatted() {
        String[] temp = new String[2];
        String month = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
        temp[0] = month;
        Calendar cal = Calendar.getInstance();
        int numDays = cal.getActualMaximum(Calendar.DATE);
        temp[1] = String.valueOf(numDays);
        return temp;
    }

    public static String getTimeStampFormatted() {
        return new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    public static String convertSerevrDate(String inputDate) {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yy");
        Date date = null;
        String outputDateStr = "";
        try {
            date = inputFormat.parse(inputDate);
            outputDateStr = outputFormat.format(date);
        } catch (ParseException | NullPointerException e) {
            L.m(e.toString());
        }
        return outputDateStr;
    }

    public static String convertSerevrDateTime(String inputDate) {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yy hh:mm:a");
        Date date = null;
        String outputDateStr = "";
        try {
            date = inputFormat.parse(inputDate);
            outputDateStr = outputFormat.format(date);
        } catch (ParseException | NullPointerException e) {
            L.m(e.toString());
        }
        return outputDateStr;
    }


    public static String[] splitTime(String startTime) {
        DateFormat inputFormat = new SimpleDateFormat("HH:mm");
        DateFormat outputFormat = new SimpleDateFormat("hh:mm:a");
        Date date = null;
        String outputDateStr = "";
        try {
            date = inputFormat.parse(startTime);
            outputDateStr = outputFormat.format(date);
        } catch (ParseException e) {
            L.m(e.toString());
        }
        return outputDateStr.split(":");
    }

    public static String getYearMonth() {
        return new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new Date());
    }

    public static String getYearMonth(String inputDate) {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM");
        Date date = null;
        String outputDateStr = "";
        try {
            date = inputFormat.parse(inputDate);
            outputDateStr = outputFormat.format(date);
        } catch (ParseException | NullPointerException e) {
            L.m(e.toString());
        }
        return outputDateStr;
    }
//	public static int generateTimeStamp2(String date) {
//		String[] tempDate = convertDateSplit(date).split("-");
//		String timeStamp=String.valueOf(tempDate[2])+String.valueOf(tempDate[1])+String.valueOf(tempDate[0]);
//		return Integer.parseInt(timeStamp);
//	}
}
