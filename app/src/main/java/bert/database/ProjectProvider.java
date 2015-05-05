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
        if (projectDir != null && projectList.size() > 0) {
            List<File> files = Arrays.asList(projectDir.listFiles());
            for (File f : files) {
                projectList.add(new Project(FileHelper.loadDocument(f)));
            }
        }
        if (projectList.size() == 0){
            System.out.println("no projects found");
        }
        Log.d("Project Provider", "Loaded " + projectList.size() + " projects from storage");
    }

    private void saveProject(Project project) {//TODO test this
        Document d = project.exportToXML();
        String fileName = project.getProjectName().replaceAll("\\W|_", "");
        fileName.concat(".xml");
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            Result result = new StreamResult(getProjectDirectory());
            Source source = new DOMSource(d);
        } catch(TransformerConfigurationException e) {
            e.printStackTrace();
        }
    }

    private static boolean externalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public void addProject(Project project){
        projectList.add(project);
    }
}
