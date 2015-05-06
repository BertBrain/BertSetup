package bert.ui.roomList;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AuditWizardView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AuditWizardView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AuditWizardView extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    AuditTallyBoxGVA tallyGridAdapter;
    TextView locationTextView;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AuditWizardView.
     */
    // TODO: Rename and change types and number of parameters
    public static AuditWizardView newInstance(String param1, String param2) {
        AuditWizardView fragment = new AuditWizardView();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AuditWizardView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_audit_wizard_view, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override public void onResume() {
        super.onResume();
        RoomListActivity activity = (RoomListActivity)getActivity();
        tallyGridAdapter = new AuditTallyBoxGVA(this.getActivity(), android.R.layout.simple_gallery_item, activity.getDeviceTypes());

        GridView gridView = (GridView) getView().findViewById(R.id.auditWizardGridView);
        gridView.setAdapter(tallyGridAdapter);

        Button finishedButton = (Button) getView().findViewById(R.id.finisedAuditWizardButton);
        finishedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAuditWizard();
            }
        });

        locationTextView = (TextView)getView().findViewById(R.id.locationNameTextField);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onFragmentInteraction. in auditwizard");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);

        public List<Category> getDeviceTypes();
        public String getBuilding();
        public void addBerts(ArrayList<BertUnit> berts);
    }

    private void finishAuditWizard(){
        String location = locationTextView.getText().toString();
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