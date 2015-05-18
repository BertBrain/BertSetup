package bert.ui.buildingList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import bert.data.ProjectProvider;
import bert.data.proj.Building;
import bert.ui.R;
import bert.ui.categoryList.CategoryListActivity;
import bert.ui.roomList.RoomListActivity;

public class BuildingDetailFragment extends Fragment {
    private static final String ARG_PROJECT_ID = "PROJECT_ID";
    private static final String ARG_BUILDING_ID = "BUILDING_ID";

    private int projectID;
    private int buildingID;
    private Building building;

    private EditText nameTextField;
    private Button openBuildingButton;
    private Button openCategoryEditor;

    private TextView startTimeDisplay;
    private TextView endTimeDisplay;
    private TimeRangeDisplay timeDisplay;

    /*
    @param projectID
    @param buildingID
    */
    public static BuildingDetailFragment newInstance(int projectID, int buildingID) {
        BuildingDetailFragment fragment = new BuildingDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PROJECT_ID, projectID);
        args.putInt(ARG_BUILDING_ID, buildingID);
        fragment.setArguments(args);
        return fragment;
    }

    public BuildingDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectID = getArguments().getInt(ARG_PROJECT_ID);
            buildingID = getArguments().getInt(ARG_BUILDING_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_building_detail_view, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        building = ProjectProvider.getInstance().getProjectList().get(projectID).getBuildings().get(buildingID);

        startTimeDisplay = (TextView) getView().findViewById(R.id.editor_start_time_textfield);
        endTimeDisplay = (TextView) getView().findViewById(R.id.editor_end_time_text_field);
        timeDisplay = new TimeRangeDisplay(getActivity(), startTimeDisplay, building.startTime, endTimeDisplay, building.endTime);

        nameTextField = (EditText) getView().findViewById(R.id.edit_building_name_textfield);
        nameTextField.setText(building.getName());
        nameTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (nameTextField.getText().toString().length() > 0 ) {
                    if (!ProjectProvider.getInstance().getProjectList().get(projectID).getBuildingNames().contains(nameTextField.getText().toString())) {
                        building.setName(nameTextField.getText().toString());
                        ((BuildingListActivity)getActivity()).loadListView();
                    }
                } else {
                    nameTextField.setText(building.getName());
                }
                return false;
            }
        });

        openCategoryEditor = (Button) getView().findViewById(R.id.open_category_list_button);
        openCategoryEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryEditor();
            }
        });

        openBuildingButton = (Button) getView().findViewById(R.id.open_building_button);
        openBuildingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBuilding();
            }
        });
    }

    private void openBuilding() {
        Intent intent = new Intent(getActivity(), RoomListActivity.class);
        intent.putExtra(RoomListActivity.ARG_PROJECT_ID, projectID);
        intent.putExtra(RoomListActivity.ARG_BUILDING_ID, buildingID);
        getActivity().startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        building.setStartTime(timeDisplay.getStartTime());
        building.setEndTime(timeDisplay.getEndTime());
    }

    public void onButtonPressed(Uri uri) {
    }

    public void openCategoryEditor() {
        Intent i = new Intent(this.getActivity(), CategoryListActivity.class);
        i.putExtra(CategoryListActivity.ARG_PROJECT_ID, projectID);
        i.putExtra(CategoryListActivity.ARG_BUILDING_ID, buildingID);
        startActivity(i);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
