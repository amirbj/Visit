package slidingmenu.andc.com.dataaccess;

import com.orm.SugarRecord;

/**
 * Created by Bijarchian on 3/13/2017.
 */

public class NotSucessVisitTbl extends SugarRecord<NotSucessVisitTbl> {

    public long RequestCode;
    public String LastvisitDate;
    public String Desc;
    public int Reason;
    public int Type;

    public NotSucessVisitTbl(){

    }

    public NotSucessVisitTbl(long requestCode, String lastvisitDate, String desc, int reason, int type){

        this.RequestCode = requestCode;
        this.LastvisitDate = lastvisitDate;
        this.Desc = desc;
        this.Reason = reason;
        this.Type = type;
    }
}
