package slidingmenu.andc.com.dataaccess;

import com.orm.SugarRecord;

/**
 * Created by Bijarchian on 3/3/2017.
 */

public class CollectionTbl extends SugarRecord<CollectionTbl> {

    public long RequestCode;
    public int VisitDur;
    public  int MainObjectcode;
    public int SubObjectcode;
    public int Quantity;
    public String Desc;
    public int Othermaterials;


    public CollectionTbl(){

    }


    public CollectionTbl(long requestCode, int visitDur, int mainObjectcode, int subObjectcode, int quantity,String desc, int othermaterials){


        this.RequestCode = requestCode;
        this.VisitDur = visitDur;
        this.MainObjectcode = mainObjectcode;
        this.SubObjectcode = subObjectcode;
        this.Quantity = quantity;
        this.Desc =desc;
        this.Othermaterials = othermaterials;
    }

}
