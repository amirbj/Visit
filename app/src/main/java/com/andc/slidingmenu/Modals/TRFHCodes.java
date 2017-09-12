package com.andc.slidingmenu.Modals;

import java.util.HashMap;

/**
 * Created by win on 6/1/2015.
 */
public class TRFHCodes {
    public HashMap<String,String> TRFH;

    public TRFHCodes() {
        this.TRFH = new HashMap<String, String>();
        this.TRFH.put("خانگی", "1");
        this.TRFH.put("عمومی", "2");
        this.TRFH.put("کشاورزی", "3");
        this.TRFH.put("صنعتی", "4");
        this.TRFH.put("سایر", "5");

    }
}
