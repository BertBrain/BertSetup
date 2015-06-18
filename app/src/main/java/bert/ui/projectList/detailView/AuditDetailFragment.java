package bert.ui.projectList.detailView;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
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
import bert.ui.common.BertEditTextAlert;
import bert.ui.projectList.activity.InstallProjectListActivity;
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

    private TextView roomCountTextView;
    private TextView bertCountTextView;

    private Button exportToROIButton;
    private Button beginInstallButton;

    @Override
    public void onResume() {
        super.onResume();
        Log.d("AUDIT_FRAGMENT", "starting on resume");


        roomCountTextView = (TextView) getView().findViewById(R.id.roomCounterLabel);
        roomCountTextView.setText(String.valueOf(currentProject.getRoomCount()));
        roomCountTextView.setText(Integer.toString(currentProject.getRoomCount()));

        bertCountTextView = (TextView) getView().findViewById(R.id.bertCounterLabel);
        bertCountTextView.setText(String.valueOf(currentProject.getBuildingCount()));
        bertCountTextView.setText(Integer.toString(currentProject.getBertCount()));

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

        beginInstallButton = (Button) getView().findViewById(R.id.begin_install_button);
        beginInstallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BertAlert.show(getActivity(), "This cannot be undone. Are you Sure?",
                        "Turn to Install",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                currentProject.convertToInstall();
                                currentProject.save();
                                Intent intent = new Intent(getActivity(), InstallProjectListActivity.class);
                                startActivity(intent);
                            }
                        }, "Cancel", null);
            }
        });
    }

    @Override
    public void openBuildingList() {
        Intent i = new Intent(this.getActivity(), AuditBuildingListActivty.class);
        i.putExtra(GeneralBuildingListActivity.ARG_PROJECT_ID, projectID);
        startActivity(i);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_audit_detail, container, false);
    }
}
