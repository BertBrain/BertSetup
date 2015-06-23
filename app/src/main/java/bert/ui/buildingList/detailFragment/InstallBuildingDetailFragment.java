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
public class InstallBuildingDetailFragment extends BuildingDetailFragment {

    private TextView roomCountTextView;
    private TextView bertCountTextView;
    private TextView roomCountDoneTextView;
    private TextView bertCountDoneTextView;


    @Override
    public void onResume() {
        super.onResume();

        roomCountTextView = (TextView) getView().findViewById(R.id.total_rooms_label);
        roomCountTextView.setText(String.valueOf(project.getRoomCountForBuilding(buildingID)));

        bertCountTextView = (TextView) getView().findViewById(R.id.total_berts_label);
        bertCountTextView.setText(String.valueOf(project.getBertCountForBuilding(buildingID)));

        roomCountDoneTextView = (TextView) getView().findViewById(R.id.rooms_completed_label);
        roomCountDoneTextView.setText(String.valueOf(project.getRoomCompletedCountForBuilding(buildingID)));

        bertCountDoneTextView = (TextView) getView().findViewById(R.id.berts_installed_label);
        bertCountDoneTextView.setText(String.valueOf(project.getBertCompletedCountForBuilding(buildingID)));

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
