package bert.data;

import android.os.Environment;
import android.util.Log;

import org.w3c.dom.Document;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import bert.data.proj.Project;

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

    public List<Project> getProjectList() {
        return projectList;
    }

    public List<String> getProjectNameList() {
        List<String> nameList = new ArrayList<>();
        for (Project p : getProjectList()) {
            nameList.add(p.getProjectName());
        }
        return nameList;
    }

    private void loadProjectListFromFile() {
        File projectDir = FileProvider.getProjectDirectory();
        projectList = new ArrayList<Project>();
        if (projectDir != null && projectDir.listFiles() != null) {
            List<File> files = Arrays.asList(projectDir.listFiles());
            if (files.size() > 0) {
                for (File f : files) {
                    log("Loading file <" + f.getName() + "> from project directory");
                    Project nextProject = FileProvider.loadProject(f);
                    if (nextProject != null) {
                        projectList.add(nextProject);
                    } else {
                        log("File <" + f.getName() + "> is not a valid project... Skipping");
                    }
                }
            }
        }
        log("Loaded " + projectList.size() + " projects from storage");
    }

    public void addProject(Project project) {
        projectList.add(project);
        FileProvider.saveProject(project);
        log("Added project <" + project.getProjectName() + "> to projectList");
    }

    private static void log(String output) {
        Log.d("Project_Provider", output);
    }
}
