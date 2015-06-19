package bert.ui.projectList.detailView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import bert.ui.projectList.activity.InstallProjectListActivity;
import bert.utility.ROIExporter;
import bert.ui.R;
import bert.ui.buildingList.activity.AuditBuildingListActivty;
import bert.ui.buildingList.activity.GeneralBuildingListActivity;
import bert.ui.common.BertAlert;
import bert.ui.projectList.ExportChooser;

/**
 * Created by liamcook on 5/29/15.
 */
public class AuditDetailFragment extends GeneralProjectDetailFragment {

    private TextView roomCountTextView;
    private TextView bertCountTextView;

    private Button exportToROIButton;
    private Button beginInstallButton;

    private Button sendToBertButton;

    @Override
    public void onResume() {
        super.onResume();
        Log.d("AUDIT_FRAGMENT", "starting on resume");


        roomCountTextView = (TextView) getView().findViewById(R.id.roomCounterLabel);
        roomCountTextView.setText(Integer.toString(currentProject.getRoomCount()));

        bertCountTextView = (TextView) getView().findViewById(R.id.bertCounterLabel);
        bertCountTextView.setText(Integer.toString(currentProject.getBertCount()));

        exportToROIButton = (Button) getView().findViewById(R.id.exportToROIButton);
        exportToROIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File fileToShare = ROIExporter.generateROI(currentProject);
                    ExportChooser.exportFile(getActivity(), "ROI spreadsheet", fileToShare);
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

        sendToBertButton = (Button) getView().findViewById(R.id.send_to_bert_button);
        sendToBertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ExportChooser.exportFile(getActivity(), "bert@bertbrain.com", projectID, currentProject.getProjectFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }

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
        return inflater.inflate(R.layout.fragment_project_audit_detail, container, false);
    }
}
