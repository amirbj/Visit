package slidingmenu.andc.com.dataaccess;

import android.support.annotation.NonNull;

import com.orm.SugarRecord;

/**
 * Created by SiaJam on 2/23/2017.
 */

public class CityTbl extends SugarRecord<CityTbl> {
    @NonNull
    public int RgnCode;
    @NonNull
    public int CityCode;
    public String CityName;


    public CityTbl(){

    }

    public CityTbl(int RgnCode, int CityCode, String CityName){

        this.RgnCode = RgnCode;
        this.CityCode = CityCode;
        this.CityName = CityName;
    }
}
