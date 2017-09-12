package slidingmenu.andc.com.dataaccess;

import com.orm.SugarRecord;

/**
 * Created by Bijarchian on 3/3/2017.
 */

public class CollectMeterTbl extends SugarRecord<CollectMeterTbl> {

    public long Request_Code;
    public long BranchCode;
    public int VisitDur;
    public long BranchSrl;
    public String FabrikNumber;
    public  int MainObjectcode;
    public int SubObjectcode;
    public String Desc;



    public CollectMeterTbl(){

    }

    public CollectMeterTbl(long request_Code, long branchCode, int visitDur, long branchSrl, String fabrikNumber, int mainObjectcode, int subObjectcode, String desc){


        this.Request_Code = request_Code;
        this.BranchCode = branchCode;
        this.VisitDur = visitDur;
        this.BranchSrl = branchSrl;
        this.FabrikNumber = fabrikNumber;
        this.MainObjectcode = mainObjectcode;
        this.SubObjectcode = subObjectcode;
        this.Desc = desc;



    }
}


