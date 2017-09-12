package slidingmenu.andc.com.dataaccess;

import android.support.annotation.NonNull;

import com.orm.SugarRecord;

/**
 * Created by SiaJam on 2/23/2017.
 */

public class VillageTbl extends SugarRecord<VillageTbl> {

    public int RgnCode;
    @NonNull
    public int CityCode;
    @NonNull
    public int VillageCode;
    @NonNull
    public String VillageName;


    public VillageTbl(){

    }

    public VillageTbl(int RgnCode, int CityCode, int VillageCode, String VillageName){
        this.RgnCode = RgnCode;
        this.CityCode = CityCode;
        this.VillageCode = VillageCode;
        this.VillageName = VillageName;
    }
}


