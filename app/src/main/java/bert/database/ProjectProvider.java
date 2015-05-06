package bert.database;

import android.os.Environment;
import android.util.Log;

import org.w3c.dom.Document;

import java.io.File;
import java.io.FileOutputStream;
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
            instance.loadProjectList();
        }
        return instance;
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    private File getProjectDirectory() {
        if (isExternalStorageAvailable()) {
            return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "BertProjects");
        } else {
            Log.e("Project_Provider", "Unable to load Project Folder");
            return null;
        }
    }

    private void loadProjectList() {
        File projectDir = getProjectDirectory();
        projectList = new ArrayList<Project>();
        if (projectDir != null) {
            List<File> files = Arrays.asList(projectDir.listFiles());
            if (files.size() > 0) {
                for (File f : files) {
                    log("Loading file <" + f.getName() + "> from project directory");
                    projectList.add(new Project(FileHelper.loadDocument(f)));
                }
            }
        }
        log("Loaded " + projectList.size() + " projects from storage");
    }

    public void saveProject(Project project) {//TODO test this
        log("Saving Project: " + project.getProjectName() + " to XML file");
        Document d = project.exportToXML();
        String fileName = project.getProjectName().replaceAll("\\W|_", "");
        fileName.concat(".xml");
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            Result result = new StreamResult(getProjectDirectory());
            Source source = new DOMSource(d);
            transformer.transform(source, result);
        } catch(TransformerConfigurationException e) {
            e.printStackTrace();
        } catch(TransformerException e) {
            e.printStackTrace();
        }
    }

    public void addProject(Project project) {
        projectList.add(project);
        saveProject(project);
        log("Added project <" + project.getProjectName() + "> to projectList");
    }

    private static boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        log("External storage state: " + state);
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private static void log(String output) {
        Log.d("Project_Provider", output);
    }
}
