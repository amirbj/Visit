package slidingmenu.andc.com.dataaccess;

import com.orm.SugarRecord;

/**
 * Created by Bijarchian on 3/3/2017.
 */

public class InstalationTbl extends SugarRecord<InstalationTbl> {

    public long RequestCode;
    public int VisitDur;
    public  int MainObjectcode;
    public int SubObjectcode;
    public int Quantity;
    public int Promiser;
    public int Othermaterials;


    public InstalationTbl(){

    }


    public InstalationTbl(long requestCode, int visitDur, int mainObjectcode, int subObjectcode, int quantity, int promiser, int othermaterials){


        this.RequestCode = requestCode;
        this.VisitDur = visitDur;
        this.MainObjectcode = mainObjectcode;
        this.SubObjectcode = subObjectcode;
        this.Quantity = quantity;
        this.Promiser =promiser;
        this.Othermaterials = othermaterials;
    }


}
