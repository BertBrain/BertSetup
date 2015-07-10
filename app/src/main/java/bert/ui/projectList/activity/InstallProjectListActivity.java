package bert.ui.projectList.activity;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;

import java.util.List;

import bert.data.ProjectProvider;
import bert.ui.common.BertAlert;
import bert.ui.projectList.addProject.InstallAddProjectFragment;
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
        Builder builder = new Builder(this);
        builder.setTitle("Convert an audit project into an installation to add it to this list.");
        builder.setMessage("You can turn an existing audit into an install or you can create a new install from scratch");

        builder.setPositiveButton("Open Audit List", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                openProjectListActivity();
            }
        });
       builder.setNegativeButton("Create Install From Scratch", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialogInterface, int i) {
               loadFragment(InstallAddProjectFragment.newInstance());
           }
       });
        builder.setNeutralButton("Cancel", null);
        builder.create().show();
    }

    @Override
    public String getTitlePrefix() { return "Install Mode"; }

    @Override
    public String getNewProjectButtonName() { return "CREATE NEW INSTALL"; }

    private void openProjectListActivity() {
        startActivity(new Intent(this, AuditProjectListActivity.class));
    }
}
