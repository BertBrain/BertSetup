package bert.ui.projectList.activity;

import java.util.List;

import bert.data.ProjectProvider;
import bert.ui.projectList.addProject.AddProjectFragment;
import bert.ui.projectList.addProject.AuditAddProjectFragment;
import bert.ui.projectList.detailView.AuditDetailFragment;

public class AuditProjectListActivity extends ProjectListActivity {

    @Override
    public List<String> getProjects() {
        return ProjectProvider.getInstance().getAuditProjectNameList();
    }

    @Override
    public void openProjectDetailView(String projectID) {
        loadFragment((new AuditDetailFragment()).newInstance(projectID));
    }

    @Override
    public void openAddProjectView() {
        loadFragment(AuditAddProjectFragment.newInstance());
    }

    @Override
    public String getTitlePrefix() { return "Audit Mode"; }

    @Override
    public String getNewProjectButtonName() { return "CREATE NEW AUDIT"; }

}