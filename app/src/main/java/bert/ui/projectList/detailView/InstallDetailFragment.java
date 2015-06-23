package bert.ui.projectList.detailView;

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

import bert.utility.CSVExporter;
import bert.ui.R;
import bert.ui.buildingList.activity.BuildingListActivity;
import bert.ui.buildingList.activity.InstallBuildingListActivity;
import bert.ui.projectList.ExportChooser;

/**
 * Created by liamcook on 5/29/15.
 */
public class InstallDetailFragment extends ProjectDetailFragment {

    private TextView roomCountTextView;
    private TextView bertCountTextView;
    private TextView roomCountDoneTextView;
    private TextView bertCountDoneTextView;

    private Button exportToBertConfigButton;

    @Override
    public void onResume() {
        super.onResume();
        Log.d("INSTALL_FRAGMENT", "starting on resume");


        roomCountTextView = (TextView) getView().findViewById(R.id.total_rooms_label);
        roomCountTextView.setText(String.valueOf(project.getRoomCount()));

        bertCountTextView = (TextView) getView().findViewById(R.id.total_berts_label);
        bertCountTextView.setText(String.valueOf(project.getBertCount()));

        roomCountDoneTextView = (TextView) getView().findViewById(R.id.rooms_completed_label);
        roomCountDoneTextView.setText(String.valueOf(project.getRoomCompletedCount()));

        bertCountDoneTextView = (TextView) getView().findViewById(R.id.berts_installed_label);
        bertCountDoneTextView.setText(String.valueOf(project.getBertCompletedCount()));

        exportToBertConfigButton = (Button) getView().findViewById(R.id.exportToBertConfiguratorButton);
        exportToBertConfigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File fileToShare;
                try {
                    fileToShare = CSVExporter.generateCSV(project);
                    ExportChooser.exportFile(getActivity(), "CSV for bert configurtor", fileToShare);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("Project Detail Fragment", "Unable to generate Configurator CSV File");
                    fileToShare = new File("test file");
                }
            }
        });
    }

    @Override
    public void openBuildingList() {
        //TODO: make different for audit vs install
        Intent i = new Intent(this.getActivity(), InstallBuildingListActivity.class);
        i.putExtra(BuildingListActivity.ARG_PROJECT_ID, projectID);
        startActivity(i);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.project_detail_fragment_install, container, false);
    }

}
