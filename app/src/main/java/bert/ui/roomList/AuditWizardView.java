package bert.ui.roomList;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
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

import bert.database.Test;
import bert.ui.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AuditWizardView extends Fragment {

    Button cancelButton;
    Button finishedButton;
    GridView gridView;


    AuditTallyBoxGVA tallyGridAdapter;
    TextView locationTextView;

    // TODO: Rename and change types and number of parameters
    public static AuditWizardView newInstance(String param1, String param2) {
        return new AuditWizardView();
    }

    public AuditWizardView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_audit_wizard_view, container, false);
    }


    @Override public void onResume() {
        super.onResume();
        RoomListActivity activity = (RoomListActivity)getActivity();
        tallyGridAdapter = new AuditTallyBoxGVA(this.getActivity(), android.R.layout.simple_gallery_item, activity.getProject().getCategories());

        gridView = (GridView) getView().findViewById(R.id.auditWizardGridView);
        gridView.setAdapter(tallyGridAdapter);

        finishedButton = (Button) getView().findViewById(R.id.finisedAuditWizardButton);
        cancelButton = (Button) getView().findViewById(R.id.canelAuditButton);

        finishedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAuditWizard();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener(){
           @Override
            public void onClick(View view){
               RoomListActivity activity = (RoomListActivity)getActivity();
               activity.openNoSelectionView("Select or Create A Room");
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
<<<<<<< HEAD
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
=======

        public List<Category> getDeviceTypes();
>>>>>>> 0403afcf67863e21fc10be18bb964fec5ab4aaad
        public String getBuilding();
        public void addBerts(ArrayList<BertUnit> berts);
    }

<<<<<<< HEAD
    private void finishAuditWizard(){
        String location = locationTextView.getText().toString();
        HashMap<Category, Integer> counts = tallyGridAdapter.getCounts();

        List<BertUnit> berts = new ArrayList<BertUnit>();
        int categoryCount = 0;
        for (Category category : ((RoomListActivity)getActivity()).getProject().getCategories()) {
            if (counts.get(category) != null) {
                int count = counts.get(category);
                for (int i = 0; i<count; i++) {
                    String name;
                    if (i == 0){
                        name = location + " - " + category.getName();
                    } else {
                        name = location + " - " + category.getName() + " " + (i+1);
                    }
                    BertUnit bert = new BertUnit(name, location, ((RoomListActivity)getActivity()).getBuilding(), categoryCount);
                    berts.add(bert);
                }
            }
            categoryCount++;
        }
        RoomListActivity activity = (RoomListActivity)getActivity();
        activity.addBerts((ArrayList<BertUnit>)berts);
        if (berts.size() > 0){
            activity.openDeviceEditorView(berts.get(0).getLocation());
=======
    private void openNoRoomPopup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getView().getContext());
        builder.setTitle("Room Not Specified");
        builder.setPositiveButton("Set Room", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i){

                InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                locationTextView.setFocusable(true);
                locationTextView.requestFocus();
                locationTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE){
                            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            manager.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
                            finishAuditWizard();
                        }
                        return false;
                    }
                });
            }
        });

        builder.setNeutralButton("Back to Editing", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int i){
                //nothing
            }
        });

        builder.create().show();
    }

    private void finishAuditWizard(){
        String location = locationTextView.getText().toString();
        if (location.length() == 0) {
            openNoRoomPopup();
>>>>>>> 0403afcf67863e21fc10be18bb964fec5ab4aaad
        } else {
            HashMap<Category, Integer> counts = tallyGridAdapter.getCounts();

            List<BertUnit> berts = new ArrayList<BertUnit>();
            int categoryCount = 0;
            for (Category category : ((RoomListActivity)getActivity()).getDeviceTypes()){

                if (counts.get(category) != null){
                    int count = counts.get(category);
                    for (int i = 0; i<count; i++){
                        String name;
                        if (i == 0){
                            name = location + " - " + category.getName();
                        } else {
                            name = location + " - " + category.getName() + " " + (i+1);
                        }

                        BertUnit bert = new BertUnit(name, location, ((RoomListActivity)getActivity()).getBuilding(), categoryCount);
                        berts.add(bert);
                    }
                }

                categoryCount++;
            }
            RoomListActivity activity = (RoomListActivity)getActivity();
            activity.addBerts((ArrayList<BertUnit>)berts);
            if (berts.size() > 0){
                activity.openDeviceEditorView(berts.get(0).getLocation());
            } else {
                System.out.println("done button pressed but no berts to be added");
            }
        }

    }
}