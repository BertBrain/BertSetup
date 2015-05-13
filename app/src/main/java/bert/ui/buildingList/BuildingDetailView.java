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

public class BuildingDetailView extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PROJECT_ID_KEY = "PROJECT_ID_KEY";
    private static final String BUILDING_ID_KEY = "BUILDING_ID_KEY";

    // TODO: Rename and change types of parameters
    private int projectID;
    private int buildingID;
    Building building;

    EditText nameTextField;
    Button openBuildingButton;
    Button openCategoryEditor;

    // TODO: Rename and change types and number of parameters
    /*
    @param projectID
    @param buildingID
    */
    public static BuildingDetailView newInstance(int projectID, int buildingID) {
        BuildingDetailView fragment = new BuildingDetailView();
        Bundle args = new Bundle();
        args.putInt(PROJECT_ID_KEY, projectID);
        args.putInt(BUILDING_ID_KEY, buildingID);
        fragment.setArguments(args);
        return fragment;
    }

    public BuildingDetailView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectID = getArguments().getInt(PROJECT_ID_KEY);
            buildingID = getArguments().getInt(BUILDING_ID_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_building_detail_view, container, false);
    }

    @Override
    public void onResume(){
        super.onResume();
        building = ProjectProvider.getInstance().getProjectList().get(projectID).getBuildings().get(buildingID);
        nameTextField = (EditText) getView().findViewById(R.id.edit_building_name_textfield);
        nameTextField.setText(building.getName());
        nameTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (nameTextField.getText().toString().length() > 0){
                    building.setName(nameTextField.getText().toString());
                    ((BuildingListActivity)getActivity()).reloadListView();
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

    private void openBuilding(){
        Intent intent = new Intent(getActivity(), RoomListActivity.class);
        intent.putExtra(RoomListActivity.ARG_PROJECT_ID, projectID);
        intent.putExtra(RoomListActivity.ARG_BUILDING_ID, buildingID);
        getActivity().startActivity(intent);
    }

    public void onButtonPressed(Uri uri) {
    }

    public void openCategoryEditor() {
        Intent i = new Intent(this.getActivity(), CategoryListActivity.class);
        i.putExtra(CategoryListActivity.ARG_PROJECT_ID, projectID);
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
