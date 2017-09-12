package com.andc.slidingmenu.Modals;

import java.util.HashMap;

/**
 * Created by win on 6/1/2015.
 */
public class VoltageCodes {
    public HashMap<String,String> volt;

    public VoltageCodes() {
        this.volt = new HashMap<String, String>();
        this.volt.put("اولیه", "1");
        this.volt.put("ثانویه", "2");
    }
}
