package bert.ui.buildingList.detailFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import bert.ui.R;
import bert.ui.roomList.roomListActivity.AuditRoomListActivity;

/**
 * Created by liamcook on 5/29/15.
 */
public class AuditBuildingDetailFragment extends GeneralBuildingDetailFragment{

    private TextView roomCountTextView;
    private TextView bertCountTextView;

    private Button openRoomListButton;

    @Override
    public void onResume() {
        super.onResume();

        openRoomListButton = (Button) getView().findViewById(R.id.roomListButton);
        openRoomListButton.setText("View Rooms (" + project.getRoomNamesInBuilding(buildingID).size() + ")");
        openRoomListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRoomListActivity();
            }
        });

        roomCountTextView = (TextView) getView().findViewById(R.id.roomCounterLabel);
        roomCountTextView.setText(Integer.toString(project.getRoomCountForBuilding(buildingID)));

        bertCountTextView = (TextView) getView().findViewById(R.id.bertCounterLabel);
        bertCountTextView.setText(Integer.toString(project.getBertCountForBuilding(buildingID)));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_building_detail, container, false);
    }

    @Override
    public void openRoomListActivity() {

        Intent intent = new Intent(getActivity(), AuditRoomListActivity.class);
        intent.putExtra(AuditRoomListActivity.ARG_PROJECT_ID, projectID);
        intent.putExtra(AuditRoomListActivity.ARG_BUILDING_ID, buildingID);
        getActivity().startActivity(intent);
    }
}
