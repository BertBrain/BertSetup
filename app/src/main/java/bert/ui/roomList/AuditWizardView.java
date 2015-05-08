package bert.ui.roomList;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import bert.database.BertUnit;

import bert.database.Category;

import bert.ui.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AuditWizardView extends Fragment {
    private static final String ARG_BUILDING = "building";
    private String building;

    private Button cancelButton;
    private Button finishedButton;
    private GridView gridView;
    private TextView totalBertsCounter;

    private AuditTallyBoxGVA tallyGridAdapter;
    private TextView locationTextView;

    private RoomListActivity activity;

    public static AuditWizardView newInstance(String building) {
        AuditWizardView fragment = new AuditWizardView();
        Bundle args = new Bundle();
        args.putString(ARG_BUILDING, building);
        fragment.setArguments(args);
        return fragment;
    }

    public AuditWizardView() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            building = getArguments().getString(ARG_BUILDING);
        }
        activity = (RoomListActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_audit_wizard_view, container, false);
    }

    @Override public void onResume() {
        super.onResume();

        tallyGridAdapter = new AuditTallyBoxGVA(this, this.getActivity(), android.R.layout.simple_gallery_item, activity.getProject().getCategories());
        gridView = (GridView) getView().findViewById(R.id.auditWizardGridView);
        gridView.setAdapter(tallyGridAdapter);

        totalBertsCounter = (TextView) getView().findViewById(R.id.totalCounterTextField);
        finishedButton = (Button) getView().findViewById(R.id.finisedAuditWizardButton);
        cancelButton = (Button) getView().findViewById(R.id.canelAuditButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view) {
               activity.openNoSelectionView("Select or Create A Room");
           }
        });

        finishedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAuditWizard();
            }
        });


        locationTextView = (TextView)getView().findViewById(R.id.locationNameTextField);
        locationTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    public void setBertTotalCounter(int count) {
        totalBertsCounter.setText("Total: " + count);
    }

    private void openNoRoomPopup() {
        AlertDialog.Builder noRoomNameSetAlert = new AlertDialog.Builder(getView().getContext());
        noRoomNameSetAlert.setTitle("Room Name Not Specified");
        noRoomNameSetAlert.setPositiveButton("Set Room Name", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                locationTextView.setFocusable(true);
                locationTextView.requestFocus();
                locationTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            manager.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
                            finishAuditWizard();
                        }
                        return false;
                    }
                });
            }
        });

        noRoomNameSetAlert.setNeutralButton("Back to Editing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {}
        });
        noRoomNameSetAlert.create().show();
    }

    private void finishAuditWizard() {
        String location = locationTextView.getText().toString();
        if (location.length() == 0) {
            openNoRoomPopup();
        } else {
            HashMap<Category, Integer> categoryCounts = tallyGridAdapter.getCounts();
            List<BertUnit> berts = new ArrayList<BertUnit>();
            int categoryCount = 0;
            for (Category category : activity.getProject().getCategories()) {
                if (categoryCounts.get(category) != null) {
                    for (int i = 0; i < categoryCounts.get(category); i++) {
                        String name;
                        if (i == 0) {
                            name = location + " - " + category.getName();
                        } else {
                            name = location + " - " + category.getName() + " " + (i + 1);
                        }
                        BertUnit bert = new BertUnit(name, location, building, categoryCount);
                        berts.add(bert);
                    }
                }
                categoryCount++;
            }

            activity.getProject().addBerts(berts);
            activity.createLocationlistView(); //Refresh view

            if (activity.getProject().getBertsByLocation(building, location).size() > 0) {
                activity.openDeviceEditorView(berts.get(0).getLocation());
            } else {
                System.out.println("done button pressed but no berts to be added");
            }
        }
    }
}