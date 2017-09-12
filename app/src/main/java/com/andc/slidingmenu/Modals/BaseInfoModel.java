package com.andc.slidingmenu.Modals;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SiaJam on 2/21/2017.
 */

public class BaseInfoModel {


    public int getTabletCode() {
        return TabletCode;
    }

    public void setTabletCode(int tabletCode) {
        TabletCode = tabletCode;
    }

    public int getTabletMainCode() {
        return TabletMainCode;
    }

    public void setTabletMainCode(int tabletMainCode) {
        TabletMainCode = tabletMainCode;
    }

    public int getTemplateType() {
        return TemplateType;
    }

    public void setTemplateType(int templateType) {
        TemplateType = templateType;
    }

    public int getDataType() {
        return DataType;
    }

    public void setDataType(int dataType) {
        DataType = dataType;
    }
    @SerializedName("TabletCode")
    public int TabletCode;
    @SerializedName("RegionCode")
    public int TabletMainCode;
 //   public int RegionCode;

 /*   public String getRegionName() {
        return RegionName;
    }

    public void setRegionName(String regionName) {
        RegionName = regionName;
    }

    public int getRegionCode() {
        return RegionCode;
    }

    public void setRegionCode(int regionCode) {
        RegionCode = regionCode;
    }

    public int getCityCode() {
        return CityCode;
    }

    public void setCityCode(int cityCode) {
        CityCode = cityCode;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public int getRgnCode() {
        return RgnCode;
    }

    public void setRgnCode(int rgnCode) {
        RgnCode = rgnCode;
    }

    public int getVillageCode() {
        return VillageCode;
    }

    public void setVillageCode(int villageCode) {
        VillageCode = villageCode;
    }

    public String getVillageName() {
        return VillageName;
    }

    public void setVillageName(String villageName) {
        VillageName = villageName;
    }
*/
  /* public String RegionName;
    public int CityCode;
    public String CityName;
    public int RgnCode;
    public int VillageCode;
    public String VillageName;
*/


    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }


    @SerializedName("Description")
    public String Description;
    @SerializedName("TemplateType")
    public int TemplateType;
    @SerializedName("DataType")
    public int DataType;
}
