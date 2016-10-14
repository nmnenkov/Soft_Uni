package com.nnenkov.mvh_sugarorm;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by nik on 11.10.16.
 */

public class GeneralFunctions {

    public GeneralFunctions() {
    }

    public Integer convertStringToInt(String str) throws NumberFormatException {
        Integer res = 0;
        try {
            res = Integer.valueOf(str.toString());
        } catch (NumberFormatException nfe) {
            Log.d("Convert Error", "Could not convert " + str + " to Integer!");
            throw new NumberFormatException("Invalid integer value" + str);
        }
        return res;
    }

    public int convertBooleanToInt(Boolean bul) {
        if (bul) return 1;
        else return 0;
    }

    public Date convertStringToDate(String dateStr, String dateFormat) throws ParseException {
        Date date;
        SimpleDateFormat dateFormatter;

        if (dateFormat.length() == 0) {
            dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        } else {
            dateFormatter = new SimpleDateFormat(dateFormat);
        }

        if (dateStr.length() == 0) {
            return Calendar.getInstance().getTime();
        }

        try {
            date = dateFormatter.parse(dateStr);
//        } catch (DataFormatException e) {
//            throw new DataFormatException();
        } catch (ParseException e) {
            throw new ParseException(dateStr, 1);
        }


        return date;
    }

    public String convertDateToString(Date date, String dateFormat) {

        SimpleDateFormat dateFormatter;
        String dateStr;

        if (dateFormat.length() == 0) {
            dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        } else {
            dateFormatter = new SimpleDateFormat(dateFormat);
        }
        if (date == null) date = Calendar.getInstance().getTime();

        dateStr = dateFormatter.format(date);

        return dateStr;
    }
}
