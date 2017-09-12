package com.andc.slidingmenu.Modals;

import java.util.HashMap;

/**
 * Created by win on 6/1/2015.
 */
public class MeasurementMaterialStatus {
    public HashMap<String,String> MMS;

    public MeasurementMaterialStatus() {
        this.MMS = new HashMap<String, String>();
        this.MMS.put("نصب", "1");
        this.MMS.put("جمع اوری", "2");
    }
}
