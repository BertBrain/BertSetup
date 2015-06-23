package bert.data;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import bert.data.proj.Project;
import bert.data.proj.exceptions.InvalidProjectNameException;
import bert.utility.Cleaner;

/**
 * Created by afiol-mahon on 5/5/15.
 * @author afiol-mahon
 */
public class ProjectProvider {

    private static ProjectProvider INSTANCE;
    private HashMap<String, Project> projectMap;
    private ProjectProvider() {}

    public static ProjectProvider getInstance() {
        if (INSTANCE == null) {
            log("Initializing...");
            INSTANCE = new ProjectProvider();
            INSTANCE.loadProjectListFromFile();
        }
        return INSTANCE;
    }

    public Project getProject(String projectID) {
        return projectMap.get(projectID);
    }

    public List<Project> getAuditProjects() {
        List<Project> auditProjects = new ArrayList<>();
        for (Project project : projectMap.values()) {
            if (project.isAudit() == true) {
                auditProjects.add(project);
            }
        }
        return auditProjects;
    }

    public List<Project> getInstallProjects() {
        List<Project> installProjects = new ArrayList<>();
        for (Project project : projectMap.values()) {
            if (project.isAudit() == false) {
                installProjects.add(project);
            }
        }
        return installProjects;
    }

    public int getProjectCount() {
        return projectMap.size();
    }

    public List<String> getProjectNameList() {
        List<String> nameList = new ArrayList<>();
        for (Project project : projectMap.values()) {
            nameList.add(project.getProjectName());
        }
        return nameList;
    }

    public List<String> getAuditProjectNameList() {
        List<String> nameList = new ArrayList<>();
        for (Project project : getAuditProjects()) {
            nameList.add(project.getProjectName());
        }
        return nameList;
    }

    public List<String> getInstallProjectNameList() {
        List<String> nameList = new ArrayList<>();
        for (Project project : getInstallProjects()) {
            nameList.add(project.getProjectName());
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
                        try {
                            Project nextProject = Project.loadProject(f);
                            if (nextProject != null) {
                                log("Loading file <" + f.getName() + "> from project directory");
                                projectMap.put(nextProject.getProjectName(), nextProject);
                            } else {
                                log("File <" + f.getName() + "> is not a valid project... Skipping");
                            }
                        } catch (InvalidProjectNameException e) {
                            //data has been corrupted, project name checked before save
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

    public void deleteProject(String projectID) {
        Project project = projectMap.get(projectID);
        try {
            File projectFile = project.getProjectFile();
            Log.d("DELETING", "path: " + projectFile.getAbsolutePath());
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
        for (String s : getProjectNameList()) {
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
