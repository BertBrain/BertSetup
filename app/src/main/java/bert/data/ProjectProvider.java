package bert.data;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bert.data.proj.Project;
import bert.data.utility.Cleaner;

/**
 * Created by afiol-mahon on 5/5/15.
 * @author afiol-mahon
 */
public class ProjectProvider {

    private static ProjectProvider instance;
    private List<Project> projectList;
    private ProjectProvider() {}

    public static ProjectProvider getInstance() {
        if (instance == null) {
            log("Creating instance...");
            instance = new ProjectProvider();
            instance.loadProjectListFromFile();
        }
        return instance;
    }

    public Project getProject(int projectID) {
        return projectList.get(projectID);
    }

    public int getTotalProjects() {
        return projectList.size();
    }

    public List<String> getProjectNameList() {
        List<String> nameList = new ArrayList<>();
        for (Project p : projectList) {
            nameList.add(p.getProjectName());
        }
        return nameList;
    }

    private void loadProjectListFromFile() {
        projectList = new ArrayList<>();
        try {
            File projectDir = FileProvider.getProjectDirectory();
            if (projectDir != null && projectDir.listFiles() != null) {
                List<File> files = Arrays.asList(projectDir.listFiles());
                if (files.size() > 0) {
                    for (File f : files) {
                        Project nextProject = FileProvider.loadProject(f);
                        if (nextProject != null) {
                            log("Loading file <" + f.getName() + "> from project directory");
                            projectList.add(nextProject);
                        } else {
                            log("File <" + f.getName() + "> is not a valid project... Skipping");
                        }
                    }
                }
            }
            log("Loaded " + projectList.size() + " projects from storage");
        } catch (IOException e) {
            e.printStackTrace();
            log("Unable to complete loadProjectListFromFile()");
        }
    }

    public void addProject(Project project) {
        projectList.add(project);
        FileProvider.saveProject(project);
        log("Added project <" + project.getProjectName() + "> to projectList");
    }

    public boolean projectNameCheck(String name) {
        boolean canCreate = Cleaner.isValid(name);
        for (String s : getProjectNameList()) {
            if (s.equals(name)) {
                canCreate = false;
            }
        }
        return canCreate;
    }

    public void deleteProject(int projectID) {
        String name = projectList.get(projectID).getProjectName();
        if (FileProvider.deleteProjectFile(projectList.get(projectID))) {
            projectList.remove(projectID);

            List<Project> newList = new ArrayList<>();
            for (Project p : projectList) {
                newList.add(p);
            }
            projectList = newList;
        }
        log("Deleted Project " + name);
    }

    private static void log(String output) {
        Log.d("Project_Provider", output);
    }
}
