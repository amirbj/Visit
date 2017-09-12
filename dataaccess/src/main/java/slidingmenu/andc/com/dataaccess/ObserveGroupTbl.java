package slidingmenu.andc.com.dataaccess;

import com.orm.SugarRecord;

/**
 * Created by Bijarchian on 3/8/2017.
 */

public class ObserveGroupTbl extends SugarRecord<ObserveGroupTbl> {



    public int GroupId;
    public String Title;

    public ObserveGroupTbl(){

    }

    public ObserveGroupTbl(int groupId, String title){
       this.GroupId = groupId;
        this.Title = title;

    }
}
