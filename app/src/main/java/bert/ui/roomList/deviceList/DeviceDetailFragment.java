package bert.ui.roomList.deviceList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import java.text.DecimalFormat;
import java.text.MessageFormat;

import bert.data.ProjectProvider;
import bert.data.proj.BertUnit;
import bert.data.proj.Building;
import bert.data.proj.Project;
import bert.data.proj.exceptions.InvalidBertNameException;
import bert.ui.common.BertAlert;
import bert.ui.R;
import bert.ui.roomList.RoomListActivity;

public class DeviceDetailFragment extends Fragment {
    private static final String ARG_PROJECT_ID = "PROJECT_ID";
    private static final String ARG_BUILDING_ID = "BUILDING_ID";
    private static final String ARG_BERT_ID = "BERT_ID";
    private static final String ARG_LOCATION_ID = "LOCATION";

    private int projectID;
    private String buildingID;
    private int bertID;
    private String location;

    private RoomListActivity activity;
    private Project project;
    private Building building;
    private BertUnit bert;

    private ArrayAdapter<String> categoryAdapter;

    private EditText deviceNameTextField;
    private EditText macAddressTextField;
    private TextView roomTextField;
    private TextView buildingTextField;
    private Spinner categorySelector;
    private Button deleteButton;
    private Button saveButton;

    public static DeviceDetailFragment newInstance(int projectID, String buildingID, String location, int bertID) {
        DeviceDetailFragment fragment = new DeviceDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PROJECT_ID, projectID);
        args.putString(ARG_BUILDING_ID, buildingID);
        args.putString(ARG_LOCATION_ID, location);
        args.putInt(ARG_BERT_ID, bertID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectID = getArguments().getInt(ARG_PROJECT_ID);
            buildingID = getArguments().getString(ARG_BUILDING_ID);
            location = getArguments().getString(ARG_LOCATION_ID);
            bertID = getArguments().getInt(ARG_BERT_ID);

            project = ProjectProvider.getInstance().getProject(projectID);
            building = project.getBuilding(buildingID);
            bert = project.getBertsByRoom(buildingID, location).get(bertID);
            activity = (RoomListActivity)getActivity();
        }
    }

    public DeviceDetailFragment() {}

    @Override
    public void onResume() {
        super.onResume();
        macAddressTextField = (EditText) getView().findViewById(R.id.macAddress);
        deviceNameTextField = (EditText) getView().findViewById(R.id.deviceName);
        roomTextField = (TextView) getView().findViewById(R.id.room);
        buildingTextField = (TextView) getView().findViewById(R.id.currentBuildingDisplay);
        categorySelector = (Spinner) getView().findViewById(R.id.detailViewCategorySelector);

        saveButton = (Button) getView().findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
            }
        });

        deleteButton = (Button) getView().findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Are you sure you want to delete bert?");
                alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        project.deleteBert(bert);
                        activity.onResume();
                        activity.openDeviceListFragment(location);
                        project.save();
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

        categoryAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, building.getCategoryNames());
        categorySelector.setAdapter(categoryAdapter);

        roomTextField.setText(location);

        buildingTextField.setText(buildingID);

        macAddressTextField.setText(bert.getMAC());
        if (bert.getMAC().length() == 0){
            macAddressTextField.requestFocus();
        }
        macAddressTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(textView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return false;
            }
        });
        macAddressTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            private int lastCursorPosition = 0;
            String oldtext = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.d("MACFORMATTER", "on text changed start: " + start + "last start: " + lastCursorPosition + "before: " + before + "count: " + count);
                if (before != count) {
                    String text = s.toString().replace(":", "");
                    macAddressTextField.setText(text.replaceAll("([0-9A-Fa-f]{2})", "$1:"));
                    if (macAddressTextField.getText().length() > 17) {
                        macAddressTextField.setText(oldtext);
                    }
                    if (start != 0 || (start == 0 && lastCursorPosition == 0)) {
                        int cursorPosition = start + (count - before);
                        if (macAddressTextField.getText().toString().substring(macAddressTextField.getText().length() - 1).contains(":")) {
                            if (count > before) {
                                cursorPosition++;
                            } else {
                                cursorPosition--;
                            }
                        }
                        macAddressTextField.setSelection(cursorPosition, cursorPosition);
                        lastCursorPosition = cursorPosition;
                    }

                    Log.d("MACFORMATTER", text.replaceAll("([0-9A-Fa-f]{2})", "$1:"));
                    oldtext = macAddressTextField.getText().toString();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        deviceNameTextField.setText(bert.getName());
        deviceNameTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(textView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return false;
            }
        });

        categorySelector.setSelection(categoryAdapter.getPosition(bert.getCategoryID()));
    }

    private void saveChanges() {
        try {
            bert.setName(deviceNameTextField.getText().toString());
            bert.setMAC(macAddressTextField.getText().toString());
            bert.setCategoryID(categoryAdapter.getItem(categorySelector.getSelectedItemPosition()));
            project.save();
            activity.deviceListFragment.onResume();
            onResume();
        } catch (InvalidBertNameException e) {
            BertAlert.show(this.getActivity(), "Invalid Bert Name");
            deviceNameTextField.setText(bert.getName());
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_device_detail, container, false);
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
