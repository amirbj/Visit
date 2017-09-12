package slidingmenu.andc.com.dataaccess;

import com.orm.SugarRecord;

/**
 * Created by Bijarchian on 3/8/2017.
 */

public class ObservationTbl extends SugarRecord<ObservationTbl> {

    public long RequestCode;
    public int VisitDur;
    public int ObserveCode;
    public String Desc;
    public long BranchCode;
    public long BranchSrl;
    public String FabrikNumber;
    public String Value;
    public int Status;

    public ObservationTbl(){


    }

    public ObservationTbl(long requestCode, int visitDur, int observeCode, String desc, long branchCode, long branchSrl, String fabrikNumber, String value, int status){

        this.RequestCode = requestCode;
        this.VisitDur = visitDur;
        this.ObserveCode = observeCode;
        this.Desc = desc;
        this.BranchCode = branchCode;
        this.BranchSrl = branchSrl;
        this.FabrikNumber = fabrikNumber;
        this.Value= value;
        this.Status = status;
    }
}
