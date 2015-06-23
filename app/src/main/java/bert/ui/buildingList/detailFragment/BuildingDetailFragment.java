package bert.ui.buildingList.detailFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import bert.data.ProjectProvider;
import bert.data.proj.Building;
import bert.data.proj.exceptions.InvalidBuildingNameException;
import bert.ui.R;
import bert.ui.buildingList.TimeRangeDisplay;
import bert.ui.buildingList.activity.BuildingListActivity;
import bert.ui.categoryList.CategoryListActivity;
import bert.ui.common.ProjectChildEditorFragment;
import bert.ui.common.NoSelectionFragment;

/**
 * Created by liamcook on 5/29/15.
 */
abstract public class BuildingDetailFragment extends ProjectChildEditorFragment {

    private static final String ARG_PROJECT_ID = "PROJECT_ID";
    private static final String ARG_BUILDING_ID = "BUILDING_ID";

    protected String projectID;
    protected String buildingID;
    protected Building building;

    private EditText nameEditText;
    private Button categoryButton;
    private Button deleteButton;
    private Button openRoomListButton;

    private TextView startTimeDisplay;
    private TextView endTimeDisplay;
    private TimeRangeDisplay timeDisplay;

    abstract public void openRoomListActivity();

    public BuildingDetailFragment newInstance(String projectID, String buildingID) {
        Bundle args = new Bundle();
        args.putString(ARG_PROJECT_ID, projectID);
        args.putString(ARG_BUILDING_ID, buildingID);
        this.setArguments(args);
        return this;
    }

    public BuildingDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectID = getArguments().getString(ARG_PROJECT_ID);
            buildingID = getArguments().getString(ARG_BUILDING_ID);
            project = ProjectProvider.getInstance().getProject(projectID);
            building = project.getBuilding(buildingID);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startTimeDisplay = (TextView) getView().findViewById(R.id.openingTimeTextView);

        endTimeDisplay = (TextView) getView().findViewById(R.id.closingTimeTextView);
        timeDisplay = new TimeRangeDisplay(getActivity(), startTimeDisplay, building.getStartTime(), endTimeDisplay, building.getEndTime());

        nameEditText = (EditText) getView().findViewById(R.id.buildingNameEditText);
        nameEditText.setText(buildingID);
        nameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                try {
                    String newBuildingID = nameEditText.getText().toString();
                    project.renameBuilding(buildingID, newBuildingID);
                    buildingID = newBuildingID;
                    project.save();
                    ((BuildingListActivity) getActivity()).loadListView();
                } catch (InvalidBuildingNameException e) {
                    nameEditText.setText(buildingID);
                }
                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return false;
            }
        });

        categoryButton = (Button) getView().findViewById(R.id.categoryListButton);
        categoryButton.setText("Add/Edit/View Categories (" + project.getBuilding(buildingID).getCategoryCount() + ")");
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryListActivity();
            }
        });

        deleteButton = (Button) getView().findViewById(R.id.buildingDeleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Are you sure you want to delete this building?");
                alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        project.deleteBuilding(buildingID);
                        project.save();
                        ((BuildingListActivity)getActivity()).loadFragment(NoSelectionFragment.newInstance("Create a building or select a builing from the list of buildings on the left"));
                        ((BuildingListActivity)getActivity()).loadListView();
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alert.create().show();
            }
        });
        openRoomListButton = (Button) getView().findViewById(R.id.roomListButton);
        openRoomListButton.setText("Add/Edit/View Rooms (" + project.getRoomNamesInBuilding(buildingID).size() + ")");
        openRoomListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRoomListActivity();
            }
        });
    }

    private void openCategoryListActivity() {
        Intent i = new Intent(this.getActivity(), CategoryListActivity.class);
        i.putExtra(CategoryListActivity.ARG_PROJECT_ID, projectID);
        i.putExtra(CategoryListActivity.ARG_BUILDING_ID, buildingID);
        startActivity(i);
    }

    @Override
    public void onPause() {
        super.onPause();
        building.setStartTime(timeDisplay.getStartTime());
        building.setEndTime(timeDisplay.getEndTime());
    }

    public void onButtonPressed(Uri uri) {}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
