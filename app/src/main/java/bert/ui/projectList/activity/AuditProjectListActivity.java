package bert.ui.projectList.activity;

import java.util.Arrays;
import java.util.List;

import bert.data.ProjectProvider;
import bert.ui.projectList.AddProjectFragment;
import bert.ui.projectList.detailView.AuditDetailFragment;

public class AuditProjectListActivity extends GeneralProjectListActivity {

     public List<String> getProjects() {
         //TODO: make this seperate for install vs audit
         return ProjectProvider.getInstance().getProjectNameList();
         //return Arrays.asList("audit 1", "audit 2");
     }

     public void openProjectDetailView(int projectIndex) {
        loadFragment((new AuditDetailFragment()).newInstance(projectIndex));
     }

     public void openAddProjectView(){
        loadFragment(AddProjectFragment.newInstance());
     }

}