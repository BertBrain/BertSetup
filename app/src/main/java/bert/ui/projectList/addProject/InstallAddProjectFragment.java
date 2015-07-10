package bert.ui.projectList.addProject;

import android.content.Intent;

import bert.ui.projectList.activity.AuditProjectListActivity;
import bert.ui.projectList.activity.InstallProjectListActivity;
import bert.ui.projectList.activity.ProjectListActivity;

/**
 * Created by liamcook on 7/9/15.
 */
public class InstallAddProjectFragment extends AddProjectFragment {

    public static AddProjectFragment newInstance() {
        return new InstallAddProjectFragment();
    }

    protected void returnToPreviousActivity() {
        InstallProjectListActivity activity = (InstallProjectListActivity) getActivity();
        Intent intent = new Intent(activity, InstallProjectListActivity.class);
        intent.putExtra(ProjectListActivity.ARG_PROJECT_ID, newProjectName);

        activity.openNoSelectionView();
        startActivity(intent);
    }

    protected boolean isAudit() { return false ; }

}
