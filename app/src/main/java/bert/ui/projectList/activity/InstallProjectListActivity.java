package bert.ui.projectList.activity;

import java.util.List;

import bert.data.ProjectProvider;
import bert.ui.projectList.detailView.InstallDetailFragment;

public class InstallProjectListActivity extends GeneralProjectListActivity {

    public List<String> getProjects() {
        return ProjectProvider.getInstance().getProjectNameList(ProjectProvider.INSTALL);
    }

    public void openProjectDetailView(int projectIndex) {
        String projectID = ProjectProvider.getInstance().getProject(ProjectProvider.INSTALL, projectIndex).getProjectName();
        loadFragment((new InstallDetailFragment()).newInstance(projectID));
    }

    public void openAddProjectView() {
        //TODO: create fragment that displays list of audits to turn to installs
    }

    public String getTitlePrefix() {
        return "In Install Mode";
    }

    public String getNewProjectButtonName() {
        return "CREATE NEW INSTALL";
    }
}
