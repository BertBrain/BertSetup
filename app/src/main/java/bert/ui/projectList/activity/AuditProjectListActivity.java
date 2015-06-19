package bert.ui.projectList.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

import bert.data.ProjectProvider;
import bert.ui.projectList.AddProjectFragment;
import bert.ui.projectList.detailView.AuditDetailFragment;

public class AuditProjectListActivity extends GeneralProjectListActivity {

     public List<String> getProjects() {
         return ProjectProvider.getInstance().getProjectNameList(ProjectProvider.AUDIT);
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

    public String getNewProjectButtonName() {
        return "CREATE NEW AUDIT";
    }

}