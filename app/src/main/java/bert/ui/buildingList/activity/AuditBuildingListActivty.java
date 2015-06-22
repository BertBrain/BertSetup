package bert.ui.buildingList.activity;

import bert.ui.buildingList.detailFragment.AuditBuildingDetailFragment;

/**
 * Created by liamcook on 5/29/15.
 */
public class AuditBuildingListActivty extends GeneralBuildingListActivity {

    protected void openBuildingDetailView(String buildingID) {
        loadFragment((new AuditBuildingDetailFragment()).newInstance(projectID, buildingID));
    }

    public String getTitlePrefix() {
        return "Audit Mode";
    }

}

