package slidingmenu.andc.com.dataaccess;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SiaJam on 2/26/2017.
 */

public class NewBranchTbl extends SugarRecord<NewBranchTbl> {

    public long Request_Code;
    public long Branch_Code;
    public int VisitDur;
    public int Phs;
    public int Amp;
    public int Pwrcnt;
    public int TrfHcode;
    public int VoltCode;
    public int Count;
    public int ActionType;
    public int RequestActionType;
    public int BranchTypeCode;
    public int TrfType;
    public int PwrIcn;



    public NewBranchTbl(){

    }




    public NewBranchTbl(long request_Code, long branch_Code, int visitDur, int phs, int amp, int pwrcnt, int trfHcode, int voltCode, int count,
                        int actionType, int requestActionType,int branchTypeCode, int trfType, int pwricn){

        this.Request_Code = request_Code;
        this.Branch_Code = branch_Code;
        this.VisitDur = visitDur;
        this.Phs = phs;
        this.Amp = amp;
        this.Pwrcnt = pwrcnt;
        this.TrfHcode = trfHcode;
        this.VoltCode =voltCode;
        this.Count =count;
        this.ActionType =actionType;
        this.RequestActionType = requestActionType;
        this.BranchTypeCode =branchTypeCode;
        this.TrfType = trfType;
        this.PwrIcn = pwricn;


    }



}




