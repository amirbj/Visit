package com.andc.slidingmenu.Modals;

import com.andc.slidingmenu.Fragments.NavigationFragments.BaseInfo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by SiaJam on 2/20/2017.
 */

public class AndroidParameterNewFinalList {
    public List<BaseInfoModel> getAndroidParameterNewFinalList() {
        return AndroidParameterNewFinalList;
    }

    public void setAndroidParameterNewFinalList(List<BaseInfoModel> androidParameterNewFinalList) {
        AndroidParameterNewFinalList = androidParameterNewFinalList;
    }

    @SerializedName("AndroidParameterNewFinalList ")
   List<BaseInfoModel> AndroidParameterNewFinalList= new ArrayList<>();

    @SerializedName("CityList")
    List<BaseInfoModel> CityList= new ArrayList<>();

    @SerializedName("VillageList")
    List<BaseInfoModel> VillageList = new ArrayList<>();

    @SerializedName("RegionList")
    List<BaseInfoModel> RegionList = new ArrayList<>();




    public List<BaseInfoModel> getCityList() {
        return CityList;
    }

    public void setCityList(List<BaseInfoModel> cityList) {
        CityList = cityList;
    }

    public List<BaseInfoModel> getRegionList() {
        return RegionList;
    }

    public void setRegionList(List<BaseInfoModel> regionList) {
        RegionList = regionList;
    }

    public List<BaseInfoModel> getVillageList() {
        return VillageList;
    }

    public void setVillageList(List<BaseInfoModel> villageList) {
        VillageList = villageList;
    }







}
