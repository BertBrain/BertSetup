package bert.ui.projectList.activity;

import java.util.List;

import bert.data.ProjectProvider;
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
        //TODO: create fragment that displays list of audits to turn to installs
    }

    @Override
    public String getTitlePrefix() { return "Install Mode"; }

    @Override
    public String getNewProjectButtonName() { return "CREATE NEW INSTALL"; }
}
