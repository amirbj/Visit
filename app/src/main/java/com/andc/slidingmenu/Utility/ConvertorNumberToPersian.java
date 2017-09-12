package com.andc.slidingmenu.Utility;

/**
 * Created by win on 6/1/2015.
 */
public class ConvertorNumberToPersian {

    /**
     *
     * @param input
     * this method is writted to convert english format of date to persian format to show to user
     * input characters replace with persian numbers then return
     *
     * @return input
     *
     */
    public String toPersianNumber(String input){
        String[] persian = { "۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹" };

        if(input!=null)
            if(input.length()>0)
                for (int j=0; j<persian.length; j++)
                    input = input.replace(String.valueOf(j), persian[j]);

        return input;
    }

}
