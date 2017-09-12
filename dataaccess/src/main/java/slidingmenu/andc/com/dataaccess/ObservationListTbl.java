package slidingmenu.andc.com.dataaccess;

import com.orm.SugarRecord;

/**
 * Created by Bijarchian on 3/8/2017.
 */

public class ObservationListTbl extends SugarRecord<ObserveGroupTbl> {

    public int Id;
    public int GroupId;
    public int Observecode;
    public String Title;
    public int TemplateType;
    public int DataType;

    public ObservationListTbl(){


    }

    public ObservationListTbl(int id, int groupId, String title, int templateType, int dataType, int observecode){

        this.Id =id;
        this.GroupId = groupId;
        this.Title = title;
        this.TemplateType = templateType;
        this.DataType = dataType;
        this.Observecode = observecode;

    }
}
