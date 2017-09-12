package slidingmenu.andc.com.dataaccess;

import com.orm.SugarRecord;

/**
 * Created by SiaJam on 2/27/2017.
 */

public class CollectBranchTbl extends SugarRecord<CollectBranchTbl> {
    public long RequestCode;
    public long BranchCode;
    public int VisitDur;
    public long BranchSrl;
    public String FabrikNumber;
    public int Phs;
    public int Amp;
    public int Pwrcnt;
    public int TrfHcode;
    public int VoltCode;
    public int BranchTypeCode;
    public int TrfType;
    public int ActionType;
    public int RequestActionType;
    public int Status;


   public CollectBranchTbl(){

    }

   public CollectBranchTbl(long requestCode, long branchCode, int visitDur, int branchSrl,String fabrikNumber, int phs, int amp, int pwrcnt, int trfHcode
    , int voltCode, int branchTypeCode, int trfType, int actionType, int requestActionType, int status){
        this.RequestCode = requestCode;
        this.BranchCode = branchCode;
        this.VisitDur = visitDur;
        this.BranchSrl = branchSrl;
        this.FabrikNumber = fabrikNumber;
        this.Phs = phs;
        this.Amp = amp;
        this.Pwrcnt = pwrcnt;
        this.TrfHcode = trfHcode;
        this.VoltCode = voltCode;
        this.BranchTypeCode = branchTypeCode;
        this.TrfType = trfType;
        this.ActionType = actionType;
        this.RequestActionType = requestActionType;
        this.Status = status;








    }


}
