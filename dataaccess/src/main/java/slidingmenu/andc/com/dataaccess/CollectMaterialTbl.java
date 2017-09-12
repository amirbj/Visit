package slidingmenu.andc.com.dataaccess;

import com.orm.SugarRecord;

/**
 * Created by SiaJam on 2/8/2017.
 */

public class CollectMaterialTbl extends SugarRecord<CollectMaterialTbl> {

    public String branchCode;
    public String requestNumber;
    public String goods;
    public String type;
    public String number;
    public String description;
  public CollectMaterialTbl(){

  }
public CollectMaterialTbl(String branchCode, String requestNumber, String goods, String type, String number, String description){

    this.branchCode = branchCode;
    this.requestNumber = requestNumber;
    this.goods = goods;
    this.type = type;
    this.number = number;
    this.description = description;
}
}
