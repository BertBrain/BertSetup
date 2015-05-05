package bert.database;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by afiol-mahon on 5/5/15.
 * @author afiol-mahon
 */
public class ProjectProvider {
    private static ProjectProvider instance;
    private List<Project> projectList;

    private ProjectProvider() {
    }

    public static ProjectProvider getInstance() {
        if (instance == null) {
            instance = new ProjectProvider();
            instance.loadProjectList();
        }
        return instance;
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    private File getProjectDirectory() {
        if (externalStorageAvailable()) {
            return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "BertProjects");
        } else {
            Log.e("Storage Error", "Unable to load Project Folder");
            return null;
        }
    }

    private void loadProjectList() {
        File projectDir = getProjectDirectory();
        projectList = new ArrayList<Project>();
        if (projectDir != null) {
            List<File> files = Arrays.asList(projectDir.listFiles());
            for (File f : files) {
                projectList.add(new Project(FileHelper.loadDocument(f)));
            }
        }
        Log.d("Project Provider", "Loaded " + projectList.size() + " projects from storage");
    }

    private static boolean externalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
