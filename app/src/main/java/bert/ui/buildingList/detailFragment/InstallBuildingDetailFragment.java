package bert.ui.buildingList.detailFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import bert.ui.R;
import bert.ui.roomList.roomListActivity.InstallRoomListActivity;

/**
 * Created by liamcook on 5/29/15.
 */
public class InstallBuildingDetailFragment extends GeneralBuildingDetailFragment{

    private TextView bertCountTextView;


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_building_install_detail, container, false);
    }

    @Override
    public void openRoomListActivity() {

        Intent intent = new Intent(getActivity(), InstallRoomListActivity.class);
        intent.putExtra(InstallRoomListActivity.ARG_PROJECT_ID, projectID);
        intent.putExtra(InstallRoomListActivity.ARG_BUILDING_ID, buildingID);
        getActivity().startActivity(intent);

    }
}
