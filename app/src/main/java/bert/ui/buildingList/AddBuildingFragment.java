package bert.ui.buildingList;

import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;

import bert.data.ProjectProvider;
import bert.data.proj.Building;
import bert.data.proj.Category;
import bert.data.proj.CategoryPresets;
import bert.data.proj.Project;
import bert.data.proj.Time;
import bert.data.proj.exceptions.InvalidBuildingNameException;
import bert.ui.buildingList.activity.GeneralBuildingListActivity;
import bert.utility.Cleaner;
import bert.ui.common.BertAlert;
import bert.ui.R;

public class AddBuildingFragment extends Fragment {

    private static final String ARG_PROJECT_ID = "PROJECT_ID";

    private GeneralBuildingListActivity activity;
    private String projectID;
    private Project project;

    private Time defaultStartTime = new Time(8, 0);
    private Time defaultEndTime = new Time(18, 0);
    private ArrayAdapter<String> buildingTypeArrayAdapter;


    private TextView startTimeDisplay;
    private TextView endTimeDisplay;
    private EditText buildingNameEditText;
    private Spinner buildingTypeSpinner;
    private TimeRangeDisplay timeDisplay;
    private Button addBuildingButton;

    public static AddBuildingFragment newInstance(String projectID) {
        AddBuildingFragment fragment = new AddBuildingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PROJECT_ID, projectID);
        fragment.setArguments(args);
        return fragment;
    }

    public AddBuildingFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectID = getArguments().getString(ARG_PROJECT_ID);
            project = ProjectProvider.getInstance().getProject(projectID);
        }
        activity = (GeneralBuildingListActivity) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        startTimeDisplay = (TextView) getView().findViewById(R.id.start_time_textfield);
        endTimeDisplay = (TextView) getView().findViewById(R.id.end_time_text_field);
        timeDisplay = new TimeRangeDisplay(
                getActivity(),
                startTimeDisplay,
                defaultStartTime,
                endTimeDisplay,
                defaultEndTime
        );

        buildingTypeSpinner = (Spinner)getView().findViewById(R.id.building_type_spinner);
        buildingTypeArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, CategoryPresets.getNames());
        buildingTypeSpinner.setAdapter(buildingTypeArrayAdapter);

        buildingNameEditText = (EditText) getView().findViewById(R.id.add_building_name_textfield);
        buildingNameEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addBuildingButton.setEnabled(Cleaner.isValid(buildingNameEditText.getText().toString()));
            }
        });
        buildingNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int i, KeyEvent keyEvent) {
                activity.inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return false;
            }
        });

        addBuildingButton = (Button) getView().findViewById(R.id.add_building_button);
        addBuildingButton.setEnabled(false);
        addBuildingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBuilding();
            }
        });
    }

    private void addBuilding() {
        try {
            String buildingID = buildingNameEditText.getText().toString();
            Time startTime = timeDisplay.getStartTime();
            Time endTime = timeDisplay.getEndTime();
            HashMap<String, Category> presetCategories = CategoryPresets.getPresets().get(buildingTypeSpinner.getSelectedItem());
            project.addBuilding(buildingID, new Building(startTime, endTime, presetCategories));
            project.save();
            GeneralBuildingListActivity activity = (GeneralBuildingListActivity) getActivity();
            activity.loadListView();
            activity.generalOpenBuildingDetailView(buildingID);
        } catch(InvalidBuildingNameException e) {
            BertAlert.show(getActivity(), "This Building Already Exists");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_building_add, container, false);
    }
}
