package com.andc.slidingmenu.Modals;

import java.io.Serializable;

/**
 * Created by MBMS on 10/14/13.
 */
public class Observe_List_Item implements Serializable
{
    public int Id;
    public long GroupId;
    public int ObserveType;
    public int ObserveCode;
    public String Title;
    public String Value;
    public String Desc;
    public int DataType;
    public boolean Lock;
}
