package bert.ui.roomList;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import bert.data.FileProvider;
import bert.data.ProjectProvider;
import bert.data.proj.BertUnit;
import bert.data.proj.Building;
import bert.data.proj.Project;
import bert.ui.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DeviceDetailEditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DeviceDetailEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceDetailEditFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PROJECT_KEY = "param1";
    private static final String ARG_BUILDING_KEY = "param2";
    private static final String ARG_BERT_KEY = "BERTKEY";
    private static final String ARG_LOCATION_KEY = "LOCATIONKEY";

    // TODO: Rename and change types of parameters
    private int projectID;
    private int buildingID;
    private int bertID;

    private Project project;
    private Building building;
    private String location;
    private BertUnit bert;

    private EditText deviceNameTextField;
    private EditText macAddressTextField;
    private EditText roomTextField;
    private TextView buildingTextField;
    private Spinner categorySelector;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param projectID Parameter 1.
     * @param buildingID Parameter 2.
     * @return A new instance of fragment DeviceDetailEditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeviceDetailEditFragment newInstance(int projectID, int buildingID, String location, int bertID) {
        DeviceDetailEditFragment fragment = new DeviceDetailEditFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PROJECT_KEY, projectID);
        args.putInt(ARG_BUILDING_KEY, buildingID);
        args.putString(ARG_LOCATION_KEY, location);
        args.putInt(ARG_BERT_KEY, bertID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectID = getArguments().getInt(ARG_PROJECT_KEY);
            buildingID = getArguments().getInt(ARG_BUILDING_KEY);
            this.location = getArguments().getString(ARG_LOCATION_KEY);
            bertID = getArguments().getInt(ARG_BERT_KEY);

            project = ProjectProvider.getInstance().getProjectList().get(projectID);
            building = project.getBuildings().get(buildingID);
            bert = project.getBertsByLocation(buildingID, location).get(bertID);
        }
    }

    public DeviceDetailEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume(){
        super.onResume();
        macAddressTextField = (EditText) getView().findViewById(R.id.macAddress);
        deviceNameTextField = (EditText) getView().findViewById(R.id.deviceName);
        roomTextField = (EditText) getView().findViewById(R.id.room);
        buildingTextField = (TextView) getView().findViewById(R.id.currentBuildingDisplay);
        categorySelector = (Spinner) getView().findViewById(R.id.detailViewCategorySelector);

        ArrayAdapter<String> categoryTableAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, building.getCategoryNames());
        categorySelector.setAdapter(categoryTableAdapter);
        //categorySelector.setSelection(bertList.get(0).getCategoryID());

        roomTextField.setText(location);

        macAddressTextField.setText(bert.getMAC());
        macAddressTextField.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {/*->*/ updateMAC(); }
            @Override public void afterTextChanged(Editable s) {}
        });

        deviceNameTextField.setText(bert.getName());
        deviceNameTextField.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {/*->*/ updateName(); }
            @Override public void afterTextChanged(Editable s) {}

        });

        categorySelector.setSelection(bert.getCategoryID());
        categorySelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectCategory(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void updateMAC() {
       bert.setMAC(macAddressTextField.getText().toString());
       FileProvider.saveProject(project);
    }

    private void updateName() {
        bert.setName(deviceNameTextField.getText().toString());
        FileProvider.saveProject(project);
        onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_device_detail_edit, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    void selectCategory(int categoryID){
        bert.setCategoryID(categoryID);
    }
}
