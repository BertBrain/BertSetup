package bert.ui.projectList.addProject;

import android.content.Intent;

import bert.ui.projectList.activity.AuditProjectListActivity;
import bert.ui.projectList.activity.ProjectListActivity;

/**
 * Created by liamcook on 7/9/15.
 */
public class AuditAddProjectFragment extends AddProjectFragment {

    public static AddProjectFragment newInstance() {
        return new AuditAddProjectFragment();
    }

    protected void returnToPreviousActivity() {
        AuditProjectListActivity activity = (AuditProjectListActivity) getActivity();
        Intent intent = new Intent(activity, AuditProjectListActivity.class);
        intent.putExtra(ProjectListActivity.ARG_PROJECT_ID, newProjectName);

        activity.openNoSelectionView();
        startActivity(intent);
    }

    protected boolean isAudit() { return true; }
}
