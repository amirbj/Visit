package slidingmenu.andc.com.dataaccess;

import com.orm.SugarRecord;

/**
 * Created by Bijarchian on 3/10/2017.
 */

public class SupplyPowerTbl extends SugarRecord<SupplyPowerTbl> {

    public long RequestCode;
    public int VisitTur;
    public int Supplymethod;
    public int ReasonnotSupply;

    public SupplyPowerTbl(){

    }

    public SupplyPowerTbl(long requestCode, int visitTur, int supplymethod, int reasonnotSupply){

        this.RequestCode = requestCode;
        this.VisitTur = visitTur;
        this.Supplymethod = supplymethod;
        this.ReasonnotSupply = reasonnotSupply;


    }
}
