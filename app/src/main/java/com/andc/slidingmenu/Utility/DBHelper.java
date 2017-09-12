package com.andc.slidingmenu.Utility;

import com.andc.slidingmenu.Modals.Observe_Activity;
import com.andc.slidingmenu.Modals.Observe_Group;
import com.andc.slidingmenu.Modals.Observe_List;

import java.util.List;

/**
 * Created by win on 6/7/2015.
 */
public class DBHelper {
    public static String InitObservesFromServer() {
        String rv = "لیستی دریافت نشد.";
    /*    try {
            String serverAddress = "217.219.37.50:8050"; //Server Address
            List<Observe_List> observes_list = WSHelper.GetObserveList(serverAddress);
            List<Observe_Activity> branch_activity = WSHelper.GetBranchActivities(serverAddress);

            if (observes_list != null && observes_list.size() > 0) {
                //Delete Previews Data From ObserveGroups
                ObserveGroupTbl.deleteAll(ObserveGroupTbl.class);
                //Delete Previews Data From ObserveList
                ObserveItemTbl.deleteAll(ObserveItemTbl.class);
                //Delete Previews Data From ObserveActivity
                ObserveActivityTbl.deleteAll(ObserveActivityTbl.class);

                long group_count = 0, item_count = 0;
                for (int i = 0; i < observes_list.size(); i++) {
                    Observe_List ol = observes_list.get(i);
                    if (observes_list.get(i).itemId != -1) {
                        //SaveObserveListItem(ol);
                        ObserveItemTbl obsItem = new ObserveItemTbl(ol.itemId,
                        ol.systemCode,
                        ol.groupId,
                        ol.observeType,
                        Integer.parseInt(ol.dataType),
                        ol.title);
                        obsItem.save();

                        item_count++;
                    } else {
                        Observe_Group og = new Observe_Group();
                        og.groupId = ol.groupId;
                        og.title = ol.title;
                        //SaveObserveGroupItem(og);
                        ObserveGroupTbl obsGroup = new ObserveGroupTbl();
                        obsGroup.groupId = og.groupId;
                        obsGroup.title = og.title;
                        obsGroup.save();
                        group_count++;
                    }
                }
                for (int i = 0; i < branch_activity.size(); i++) {
                    Observe_Activity ba = branch_activity.get(i);
                    //SaveActivityItem(ba);
                    ObserveActivityTbl obsActivity = new ObserveActivityTbl();
                    obsActivity.Id = ba.Id;
                    obsActivity.Title = ba.Title;
                    obsActivity.save();
                }
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("%d %s %d %s", item_count, "مورد در", group_count, "گروه دریافت شد."));
                sb.append("\n");
                sb.append(String.format("%d %s", branch_activity.size(), "کد فعالیت دریافت شد."));
                rv = sb.toString();
            }
        } catch (Exception e) {
            rv = "خطا در برقراری ارتباط با سرور";
        }*/
        return rv;

    }
}
