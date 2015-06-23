package bert.ui.buildingList.detailFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import bert.ui.R;
import bert.ui.roomList.roomListActivity.AuditRoomListActivity;

/**
 * Created by liamcook on 5/29/15.
 */
public class AuditBuildingDetailFragment extends BuildingDetailFragment {

    private TextView roomCountTextView;
    private TextView bertCountTextView;

    @Override
    public void onResume() {
        super.onResume();

        roomCountTextView = (TextView) getView().findViewById(R.id.roomCounterLabel);
        roomCountTextView.setText(String.valueOf(project.getRoomCountForBuilding(buildingID)));

        bertCountTextView = (TextView) getView().findViewById(R.id.bertCounterLabel);
        bertCountTextView.setText(String.valueOf(project.getBertCountForBuilding(buildingID)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.building_detail_audit_fragment, container, false);
    }

    @Override
    public void openRoomListActivity() {

        Intent intent = new Intent(getActivity(), AuditRoomListActivity.class);
        intent.putExtra(AuditRoomListActivity.ARG_PROJECT_ID, projectID);
        intent.putExtra(AuditRoomListActivity.ARG_BUILDING_ID, buildingID);
        getActivity().startActivity(intent);
    }
}
