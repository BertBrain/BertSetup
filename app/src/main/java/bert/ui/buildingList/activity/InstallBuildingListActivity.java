package bert.ui.buildingList.activity;

import android.widget.TextView;

import bert.ui.buildingList.detailFragment.InstallBuildingDetailFragment;

/**
 * Created by liamcook on 5/29/15.
 */
public class InstallBuildingListActivity extends GeneralBuildingListActivity {

    protected void openBuildingDetailView(String buildingID){
        loadFragment((new InstallBuildingDetailFragment()).newInstance(projectID, buildingID));
    }

    public String getTitlePrefix() {
        return "In Install Mode";
    }
}
