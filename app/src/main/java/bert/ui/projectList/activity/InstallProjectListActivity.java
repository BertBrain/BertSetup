package bert.ui.projectList.activity;

import android.content.DialogInterface;
import android.content.Intent;

import java.util.List;

import bert.data.ProjectProvider;
import bert.ui.common.BertAlert;
import bert.ui.projectList.detailView.InstallDetailFragment;

public class InstallProjectListActivity extends ProjectListActivity {

    @Override
    public List<String> getProjects() {
        return ProjectProvider.getInstance().getInstallProjectNameList();
    }

    @Override
    public void openProjectDetailView(String projectID) {
        loadFragment((new InstallDetailFragment()).newInstance(projectID));
    }

    @Override
    public void openAddProjectView() {
        BertAlert.show(this, "Convert an audit project into an installation to add it to this list.",
                "Take me to the Audit List",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openProjectListActivity();
                    }
                },
                "Cancel",
                null
        );
    }

    @Override
    public String getTitlePrefix() { return "Install Mode"; }

    @Override
    public String getNewProjectButtonName() { return "CREATE NEW INSTALL"; }

    private void openProjectListActivity() {
        startActivity(new Intent(this, AuditProjectListActivity.class));
    }
}
