package bert.data;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import bert.data.proj.Project;
import bert.utility.Cleaner;

/**
 * Created by afiol-mahon on 5/5/15.
 * @author afiol-mahon
 */
public class ProjectProvider {

    static final public boolean AUDIT = true;
    static final public boolean INSTALL = false;

    private static ProjectProvider instance;
    private HashMap<String, Project> projectMap;
    private ProjectProvider() {}

    public static ProjectProvider getInstance() {
        if (instance == null) {
            log("Creating instance...");
            instance = new ProjectProvider();
            instance.loadProjectListFromFile();
        }
        return instance;
    }

    public Project getProject(String projectID) {
        return projectMap.get(projectID);
    }


    public Project getProject(boolean isRequestForAudit, int projectID) {
        return getProjects(isRequestForAudit).get(projectID);
    }

    //TODO: find more descriptive and clear name
    public List<Project> getProjects(boolean isRequestForAudit) {
        List<Project> auditProjects = new ArrayList<>();
        for (Project project : projectMap.values()) {
            if (project.isAudit() == isRequestForAudit) {
                auditProjects.add(project);
            }
        }
        return  auditProjects;
    }

    public HashMap<String, Project> getProjects() {
        return projectMap;
    }


    public int getTotalProjects() {
        return projectMap.size();
    }

    //TODO: find more descriptive and clear name
    public List<String> getProjectNameList(boolean isRequestForAudit) {
        List<String> nameList = new ArrayList<>();
        for (Project p : getProjects(isRequestForAudit)) {
            nameList.add(p.getProjectName());
        }
        return nameList;
    }

    private void loadProjectListFromFile() {
        projectMap = new HashMap<>();
        try {
            File projectDir = FileProvider.getProjectDirectory();
            if (projectDir != null && projectDir.listFiles() != null) {
                List<File> files = Arrays.asList(projectDir.listFiles());
                if (files.size() > 0) {
                    for (File f : files) {
                        Project nextProject = Project.loadProject(f);
                        if (nextProject != null) {
                            log("Loading file <" + f.getName() + "> from project directory");
                            projectMap.put(nextProject.getProjectName(), nextProject);
                        } else {
                            log("File <" + f.getName() + "> is not a valid project... Skipping");
                        }
                    }
                }
            }
            log("Loaded " + projectMap.size() + " projects from storage");
        } catch (IOException e) {
            e.printStackTrace();
            log("Unable to complete loadProjectListFromFile()");
        }
    }

    public void deleteProject(String projectID){
        Project project = projectMap.get(projectID);
        try {
            File projectFile = project.getProjectFile();
            Log.d("DELTING", "path: " + projectFile.getAbsolutePath());
            File deleteFile = new File(projectFile.getAbsolutePath().replace(" ", "%20"));
            deleteFile.delete();
            projectMap.remove(projectID);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void addProject(Project project) {
        projectMap.put(project.getProjectName(), project);
        project.save();
        log("Added project <" + project.getProjectName() + "> to projectMap");
    }

    public boolean projectNameCheck(String name) {
        boolean canCreate = Cleaner.isValid(name);
        List<String> allProjectNames =  getProjectNameList(AUDIT);
        getProjectNameList(AUDIT).addAll(getProjectNameList(INSTALL));
        for (String s : allProjectNames) {
            if (s.equals(name)) {
                canCreate = false;
            }
        }
        return canCreate;
    }

    private static void log(String output) {
        Log.d("Project_Provider", output);
    }
}
