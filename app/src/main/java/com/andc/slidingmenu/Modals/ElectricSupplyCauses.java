package com.andc.slidingmenu.Modals;

import java.util.HashMap;

/**
 * Created by win on 6/1/2015.
 */
public class ElectricSupplyCauses {

    public HashMap<String,String> ESC;

    public ElectricSupplyCauses() {
        this.ESC = new HashMap<String, String>();
        this.ESC.put("قرار داشتن در حریم", "1");
        this.ESC.put("وجود موانع غیر قابل رفع", "2");
        this.ESC.put("وجود انشعاب دیگر در محل", "3");
        this.ESC.put("عدم وجود امکانات", "4");
        this.ESC.put("بدهکار میباشد", "5");
        this.ESC.put("نیاز به تهیه طرح", "6");
        this.ESC.put("سایر موارد", "7");
    }
}
