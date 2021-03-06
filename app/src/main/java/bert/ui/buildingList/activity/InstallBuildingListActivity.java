package bert.ui.buildingList.activity;

import bert.ui.buildingList.detailFragment.InstallBuildingDetailFragment;

/**
 * Created by liamcook on 5/29/15.
 */
public class InstallBuildingListActivity extends BuildingListActivity {

    @Override
    public void openBuildingDetailView(String buildingID){
        loadFragment((new InstallBuildingDetailFragment()).newInstance(projectID, buildingID));
    }

    @Override
    public String getTitlePrefix() { return "Install Mode"; }
}
