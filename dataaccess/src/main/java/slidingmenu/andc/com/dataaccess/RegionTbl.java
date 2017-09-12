package slidingmenu.andc.com.dataaccess;

import android.graphics.Region;
import android.support.annotation.NonNull;

import com.orm.SugarRecord;

/**
 * Created by SiaJam on 2/23/2017.
 */

public class RegionTbl extends SugarRecord<RegionTbl> {
    @NonNull
    public int RgnCode;
    public String RgnName;

    public RegionTbl(){

    }

    public RegionTbl(int RgnCode,String RgnName){

        this.RgnCode = RgnCode;
        this.RgnName = RgnName;
    }
}
