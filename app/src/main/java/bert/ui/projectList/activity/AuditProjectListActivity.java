package bert.ui.projectList.activity;

import java.util.Arrays;
import java.util.List;

import bert.data.ProjectProvider;
import bert.ui.projectList.AddProjectFragment;
import bert.ui.projectList.detailView.AuditDetailFragment;

public class AuditProjectListActivity extends GeneralProjectListActivity {

     public List<String> getProjects() {
         //TODO: make this seperate for install vs audit
         return ProjectProvider.getInstance().getProjectNameList(ProjectProvider.AUDIT);
         //return Arrays.asList("audit 1", "audit 2");
     }

     public void openProjectDetailView(int projectIndex) {
         String projectID = ProjectProvider.getInstance().getProject(ProjectProvider.AUDIT, projectIndex).getProjectName();
        loadFragment((new AuditDetailFragment()).newInstance(projectID));
     }

     public void openAddProjectView(){
        loadFragment(AddProjectFragment.newInstance());
     }

     public String getTitlePrefix() {
         return "In Audit Mode";
     }

}