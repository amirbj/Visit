package com.andc.slidingmenu.Modals;

import java.util.HashMap;

/**
 * Created by win on 6/1/2015.
 */
public class BranchTypeCodes {
    public HashMap<String,String> BTC;

    public BranchTypeCodes() {
        this.BTC = new HashMap<String, String>();
        this.BTC.put("عادی", "2");
        this.BTC.put("دیماندی", "1");
    }
}
