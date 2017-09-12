package slidingmenu.andc.com.dataaccess;



import com.orm.SugarRecord;

/**
 * Created by SiaJam on 2/20/2017.
 */

public class BaseMaterialTbl extends SugarRecord<BaseMaterialTbl> {

   public int Tablet_Code;
   public int Tablet_MainCode;
    public String Description;
    public int Template_Type;
    public int Data_Type;

  public  BaseMaterialTbl(){

    }

    public BaseMaterialTbl(int Tablet_Code, int Tablet_MainCode,String Description,int Template_Type, int Data_Type ){
        this.Tablet_Code = Tablet_Code;
        this.Tablet_MainCode = Tablet_MainCode;
        this.Description = Description;
        this.Template_Type = Template_Type;
        this.Data_Type = Data_Type;
    }
}

