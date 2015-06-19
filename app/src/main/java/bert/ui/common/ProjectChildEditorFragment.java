package bert.ui.common;

import android.app.Fragment;
import android.util.Log;

import bert.data.ProjectProvider;
import bert.data.proj.Project;

/**
 * Created by liamcook on 6/19/15.
 */
public class ProjectChildEditorFragment extends Fragment {
    public Project project;

    @Override
    public void onPause() {
        super.onPause();
        if (project != null) {
            project.save();
        } else{
            Log.d("ProjectSaver", "no project to save");
        }

    }
}
