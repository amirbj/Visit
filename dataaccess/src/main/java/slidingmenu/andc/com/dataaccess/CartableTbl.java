package slidingmenu.andc.com.dataaccess;

import com.orm.SugarRecord;

/**
 * Created by SiaJam on 2/24/2017.
 */

public class CartableTbl extends SugarRecord<CartableTbl> {


    public long RequestCode;
    public int VisitDur;
    public String RequestDate;
    public String Name;
    public String FirstName;
    public String LastName;
    public String Address;
    public String MobileNo;
    public String FixedTel;
    public int Status;
    public String IsSend;

 public CartableTbl(){

}

    public CartableTbl(int requestCode, int visitDur , String requestDate, String name, String address, String mobileNo, String fixedTel, int status, String isSend, String firstName, String lastName){

        this.RequestCode = requestCode;
        this.VisitDur = visitDur;
        this.RequestDate = requestDate;
        this.Name = name;
        this.Address =address;
        this.MobileNo = mobileNo;
        this.FixedTel = fixedTel;
        this.Status = status;
        this.IsSend =isSend;
        this.FirstName = firstName;
        this.LastName = lastName;


    }



}
