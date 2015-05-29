package bert.ui.projectList.activity;

import android.app.Fragment;

import java.util.Arrays;
import java.util.List;

import bert.ui.projectList.AddProjectFragment;
import bert.ui.projectList.activity.GeneralProjectListActivity;
import bert.ui.projectList.detailView.InstallDetailFragment;

public class InstallProjectListActivity extends GeneralProjectListActivity {

    public List<String> getProjects() {
        return Arrays.asList("install 1", "install 2");
    }

    public void openProjectDetailView(int projectIndex) {
        loadFragment((new InstallDetailFragment()).newInstance(projectIndex));
    }

    public void openAddProjectView(){

    }
}
