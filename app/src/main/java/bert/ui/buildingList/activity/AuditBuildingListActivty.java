package bert.ui.buildingList.activity;

import bert.ui.buildingList.detailFragment.AuditBuildingDetailFragment;
import bert.ui.buildingList.detailFragment.InstallBuildingDetailFragment;

/**
 * Created by liamcook on 5/29/15.
 */
public class AuditBuildingListActivty extends GeneralBuildingListActivity {

    public void openBuildingDetailView(String buildingID){
        loadFragment((new AuditBuildingDetailFragment()).newInstance(projectID, buildingID));
    }

}
