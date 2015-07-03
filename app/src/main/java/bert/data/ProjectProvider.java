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
import bert.data.proj.exceptions.NoSuchProjectException;
import bert.utility.Cleaner;

/**
 * Created by afiol-mahon on 5/5/15.
 * @author afiol-mahon
 */
public class ProjectProvider {

    private static ProjectProvider INSTANCE;
    private List<Project> projectList;
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
        for (Project project : projectList) {
            if (projectID.equals(project.getProjectName())) {
                return project;
            }
        }
        return null;
    }

    public List<Project> getAuditProjects() {
        log("project list: " + projectList.toString());
        List<Project> auditProjects = new ArrayList<>();
        for (Project project : projectList) {
            if (project.isAudit() == true) {
                auditProjects.add(project);
            }
        }
        return auditProjects;
    }

    public List<Project> getInstallProjects() {
        List<Project> installProjects = new ArrayList<>();
        for (Project project : projectList) {
            if (project.isAudit() == false) {
                installProjects.add(project);
            }
        }
        return installProjects;
    }

    public int getProjectCount() {
        return projectList.size();
    }

    public List<String> getProjectNameList() {
        List<String> nameList = new ArrayList<>();
        for (Project project : projectList) {
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
        projectList = new ArrayList<>();
        try {
            File projectDir = FileProvider.getProjectDirectory();
            if (projectDir != null && projectDir.listFiles() != null) {
                List<File> files = Arrays.asList(projectDir.listFiles());
                if (files.size() > 0) {
                    for (File f : files) {
                        try {
                            Project project = Project.loadProject(f);
                            if (project != null) {
                                log("Loading file <" + f.getName() + "> from project directory");
                                log("adding project <" + project.getProjectName() + "> from project directory");
                                projectList.add(project);
                            } else {
                                log("File <" + f.getName() + "> is not a valid project... Skipping");
                            }
                        } catch (InvalidProjectNameException e) {
                            //data has been corrupted, project name checked before save
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

    public boolean deleteProject(String projectID) {

        try {
            Log.d("ProjectSaver", "deleting path: " + Project.getProjectFileNoSpaces(projectID).getAbsolutePath());

            File deleteFile = Project.getProjectFileNoSpaces(projectID);
            if (deleteFile.exists()){
                if (deleteFile.delete()) {
                    Project project = getProject(projectID);
                    if (project != null) {
                        projectList.remove(project);
                    }
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        } catch (IOException e){
            e.printStackTrace();
            return false;
        }

    }

    public void addProject(Project project) {
        projectList.add(project);
        project.save();
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

    private static void log(String output) {
        Log.d("Project_Provider", output);
    }
}
