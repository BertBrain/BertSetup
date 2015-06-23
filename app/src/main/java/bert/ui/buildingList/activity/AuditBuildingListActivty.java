package bert.ui.buildingList.activity;

import bert.ui.buildingList.detailFragment.AuditBuildingDetailFragment;

/**
 * Created by liamcook on 5/29/15.
 */
public class AuditBuildingListActivty extends BuildingListActivity {

    @Override
    protected void openBuildingDetailView(String buildingID) {
        loadFragment((new AuditBuildingDetailFragment()).newInstance(projectID, buildingID));
    }

    @Override
    public String getTitlePrefix() { return "Audit Mode"; }

}

