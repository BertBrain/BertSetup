package bert.ui.projectList.detailView;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import bert.data.ProjectProvider;
import bert.data.proj.Project;
import bert.data.proj.exceptions.InvalidProjectNameException;
import bert.utility.CSVExporter;
import bert.utility.Cleaner;
import bert.utility.ROIExporter;
import bert.ui.R;
import bert.ui.buildingList.activity.AuditBuildingListActivty;
import bert.ui.buildingList.activity.GeneralBuildingListActivity;
import bert.ui.common.BertAlert;
import bert.ui.projectList.ExportChooser;
import bert.ui.projectList.activity.GeneralProjectListActivity;

/**
 * Created by liamcook on 5/29/15.
 */
public class AuditDetailFragment extends GeneralProjectDetailFragment {

    @Override
    public Project getProjectForID(int projectID) {
        //TODO: make different for audit vs install
        return ProjectProvider.getInstance().getProject(projectID);
    }

    private TextView roomCountTextView;
    private TextView bertCountTextView;

    private Button exportToROIButton;

    @Override
    public void onResume() {
        super.onResume();
        Log.d("AUDIT_FRAGMENT", "starting on resume");

        //TODO: make different for audit vs install
        //roomCountTextView = (TextView) getView().findViewById(R.id.roomCountTextView);
        //roomCountTextView.setText("audit");
        //roomCountTextView.setText(Integer.toString(currentProject.getRoomCount()));

        //TODO: make different for audit vs install
        //bertCountTextView = (TextView) getView().findViewById(R.id.bertCountTextView);
        //bertCountTextView.setText(Integer.toString(currentProject.getBerts().size()));

        exportToROIButton = (Button) getView().findViewById(R.id.exportToROIButton);
        exportToROIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File fileToShare = ROIExporter.generateROI(currentProject);
                    new ExportChooser(getActivity()).exportFile("ROI spreadsheet", fileToShare);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void openBuildingList() {
        //TODO: make different for audit vs install
        Intent i = new Intent(this.getActivity(), AuditBuildingListActivty.class);
        i.putExtra(GeneralBuildingListActivity.ARG_PROJECT_ID, projectID);
        startActivity(i);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_audit_detail, container, false);
    }
}
