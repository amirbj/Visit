package com.andc.slidingmenu.Modals;

import android.graphics.Bitmap;

/**
 * Created by win on 3/16/2015.
 */
public class ReportsModel {
    public Bitmap icon ;
    public String Description;

    public ReportsModel(String description) {
        Description = description;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

}
