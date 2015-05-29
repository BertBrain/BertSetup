package bert.ui.projectList.detailView;

import android.app.Fragment;
import android.content.Intent;
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
import bert.data.utility.CSVExporter;
import bert.data.utility.Cleaner;
import bert.data.utility.ROIExporter;
import bert.ui.R;
import bert.ui.buildingList.BuildingListActivity;
import bert.ui.common.BertAlert;
import bert.ui.projectList.ExportChooser;
import bert.ui.projectList.activity.GeneralProjectListActivity;

/**
 * Created by liamcook on 5/29/15.
 */
public class InstallDetailFragment extends GeneralProjectDetailFragment {

    private TextView roomCountTextView;
    private TextView bertCountTextView;
    private TextView roomCountDoneTextView;
    private TextView bertCountDoneTextView;

    private Button exportToBertConfigButton;

    @Override
    public void onResume() {
        super.onResume();
        Log.d("INSTALL_FRAGMENT", "starting on resume");

        //TODO: make different for audit vs install
        //roomCountTextView = (TextView) getView().findViewById(R.id.roomCountTextView);
        //roomCountTextView.setText("install");
        //roomCountTextView.setText(Integer.toString(currentProject.getRoomCount()));

        //TODO: make different for audit vs install
        //bertCountTextView = (TextView) getView().findViewById(R.id.bertCountTextView);
        //bertCountTextView.setText(Integer.toString(currentProject.getBerts().size()));

        exportToBertConfigButton = (Button) getView().findViewById(R.id.exportToBertConfiguratorButton);
        exportToBertConfigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File fileToShare;
                try {
                    fileToShare = CSVExporter.generateCSV(currentProject);
                    new ExportChooser(getActivity()).exportFile("CSV for bert configurator", fileToShare);
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
        Intent i = new Intent(this.getActivity(), BuildingListActivity.class);
        i.putExtra(BuildingListActivity.ARG_PROJECT_ID, projectID);
        startActivity(i);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_install_detail, container, false);
    }

    @Override
    public Project getProjectForID(int projectID){
        //TODO: make different for audit vs install
        return ProjectProvider.getInstance().getProject(projectID);

    }
}
