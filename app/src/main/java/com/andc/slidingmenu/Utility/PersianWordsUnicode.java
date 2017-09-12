package com.andc.slidingmenu.Utility;

import android.support.v4.app.FragmentActivity;

import com.andc.slidingmenu.R;

import java.util.HashMap;

public class PersianWordsUnicode {

    public HashMap<String, Integer> words;

    /**
     *
     * @param context
     * hash map contains persian alphabets as a key and order number as value
     */
    public PersianWordsUnicode(FragmentActivity context) {
        this.words = new HashMap<String, Integer>();
        String [] temp = context.getResources().getStringArray(R.array.alphabet);
        int i = 1;
        for(String s:temp) {
            words.put(s, i);
            i++;
        }
    }

    public int getNumberOfCharacter(String c){
        char[] d = c.toCharArray();
        if(d[0]<='z' && d[0]>='a')
            return 100;
        if(d[0]<='9' && d[0]>='0')
            return 100;
        int temp = this.words.get(c);
            return temp;
    }

}

