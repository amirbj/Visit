package slidingmenu.andc.com.dataaccess;

import android.support.v4.app.FragmentActivity;

import com.orm.SugarRecord;

import java.util.HashMap;

/**
 * Created by win on 5/5/2015.
 */
public class UnitDB extends SugarRecord<UnitDB> {

    private HashMap<Integer, String> map;

    public UnitDB(FragmentActivity context){
        map = new HashMap<Integer, String>();
        map.put(1, context.getResources().getString(R.string.material_unit_device));
        map.put(2, context.getResources().getString(R.string.material_unit_meter));
        map.put(3, context.getResources().getString(R.string.material_unit_number));
        map.put(4, context.getResources().getString(R.string.material_unit_item));
    }

    public void putMap(String s){
        map.put(map.size(), s);
    }

    public String getItem(int i){
        return map.get(i);
    }

    public int getSize(){
        return map.size();
    }

    public void cleanMap(){
        map.clear();
    }

    public HashMap<Integer, String> getMap() {
        return map;
    }
}
