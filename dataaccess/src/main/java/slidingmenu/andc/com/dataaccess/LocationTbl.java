package slidingmenu.andc.com.dataaccess;

import com.orm.SugarRecord;

/**
 * Created by SiaJam on 2/24/2017.
 */

public class LocationTbl extends SugarRecord<LocationTbl> {

    public long RequestCode;
    public int VisitDur;
    public String VisitDateEn;
    public String VisitDate;
    public long TotalArea;
    public long BaseArea;
    public String GeoDir;
    public String FloorCount;
    public String UnitCount;
    public int BuildingStatus;
    public int AreaStatus;
    public int RgnCode;
    public int CityCode;
    public int VillageCode;
    public int FamilyCount;
    public boolean IsVillage;
    public long NextRightFileNo;
    public long NextLeftFileNo;
    public long NextRightShe;
    public long NextLeftShn;
    public long LastX;
    public long LastY;
    public int Industrialcity;
    public int Status;

    public LocationTbl(){

    }
    public LocationTbl(long requestCode, int visitDur, String visitDate, long totalArea, long baseArea,String floorCount, String unitCount, int buildingStatus, int areaStatus
    , int rgnCode, int cityCode, int villageCode, int familyCount, boolean isVillage, long nextRightFileNo, long nextLeftFileNo, long nextRightShe, long nextLeftShn,
                       long lastX, long lastY, int industrialcity, String geoDir, int status){
        this.RequestCode = requestCode;
        this.VisitDur = visitDur;
        this.VisitDate = visitDate;
        this.TotalArea = totalArea;
        this.BaseArea = baseArea;
        this.FamilyCount = familyCount;
        this.FloorCount = floorCount;
        this.UnitCount = unitCount;
        this.BuildingStatus = buildingStatus;
        this.AreaStatus = areaStatus;
        this.RgnCode = rgnCode;
        this.CityCode = cityCode;
        this.VillageCode = villageCode;
        this.IsVillage = isVillage;
        this.NextRightFileNo = nextRightFileNo;
        this.NextLeftFileNo = nextLeftFileNo;
        this.NextRightShe = nextRightShe;
        this.NextLeftShn = nextLeftShn;
        this.LastX = lastX;
        this.LastY = lastY;
        this.Industrialcity = industrialcity;
        this.GeoDir = geoDir;
        this.Status = status;
    }

}
