package slidingmenu.andc.com.dataaccess;

import com.orm.SugarRecord;

/**
 * Created by SiaJam on 2/26/2017.
 */

public class ChangeBranchTbl extends SugarRecord<ChangeBranchTbl> {

    public long RequestCode;
    public long BranchCode;
    public int VisitDur;
    public long BranchSrl;
    public String FabrikNumber;
    public int OldPhs;
    public int OldAmp;
    public int OldPwrcnt;
    public int OldtrfHcode;
    public int OldVoltcode;
    public int OldBranchTypeCode;
    public int OldTrfType;
    public int Phs;
    public int Amp;
    public int Pwrcnt;
    public int TrfHcode;
    public int VoltCode;
    public int BranchTypeCode;
    public int TrfType;
    public boolean HaveChangeMeter;
    public boolean HaveChangePlace;
    public int PwrIcn;
    public int FamilyCode;
    public int ActionType;
    public int RequestActionType;
    public int OldPwrIcn;
    public int Status;
    public ChangeBranchTbl(){

    }

    public ChangeBranchTbl(long requestCode, long branchCode, int visitDur, long branchSrl, String fabrikNumber
    , int oldPhs, int oldAmp, int oldPwrcnt, int oldtrfHcode, int oldVoltcode, int oldBranchTypeCode, int oldTrfType, int phs
    , int amp, int pwrcnt, int trfHcode, int voltCode, int branchTypeCode, int trfType, boolean haveChangeMeter, boolean haveChangePlace
    , int pwrIcn, int familyCode, int actionType, int requestActionType, int oldPwrIcn, int status){

        this.RequestCode = requestCode;
        this.BranchCode = branchCode;
        this.VisitDur = visitDur;
        this.BranchSrl = branchSrl;
        this.FabrikNumber = fabrikNumber;
        this.OldPhs =oldPhs;
        this.OldAmp = oldAmp;
        this.OldPwrcnt = oldPwrcnt;
        this.OldtrfHcode = oldtrfHcode;
        this.OldVoltcode = oldVoltcode;
        this.OldBranchTypeCode = oldBranchTypeCode;
        this.OldTrfType = oldTrfType;
        this.OldPhs = oldPhs;
        this.Phs = phs;
        this.Amp = amp;
        this.Pwrcnt = pwrcnt;
        this.TrfHcode = trfHcode;
        this.VoltCode =voltCode;
        this.BranchTypeCode = branchTypeCode;
        this.TrfType = trfType;
        this.HaveChangeMeter = haveChangeMeter;
        this.HaveChangePlace = haveChangePlace;
        this.PwrIcn =pwrIcn;
        this.FamilyCode = familyCode;
        this.ActionType = actionType;
        this.RequestActionType = requestActionType;
        this.OldPwrIcn = oldPwrIcn;
        this.Status = status;


    }

}
