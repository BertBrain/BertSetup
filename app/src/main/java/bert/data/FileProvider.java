package bert.data;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import bert.data.proj.Project;

/**
 * @author afiol-mahon
 */
public class FileProvider {
    public static final String EXPORT_DIR_NAME = "BertExports";
    public static final String PROJECT_DIR_NAME = "BertProjects";

	public static void printDocument(Document document) {
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        DOMSource source = new DOMSource(document);
	        StreamResult console = new StreamResult(System.out);
	        transformer.transform(source, console);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	
	public static Document createDocument() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			return builder.newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Project loadProject(File file) {
        log("Loading document: " + file.getName());
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document document = null;
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.parse(file);
            return ProjectSerializer.getProjectFromDocument(document);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
            return null;
		} catch (SAXException e) {
			e.printStackTrace();
            return null;
		} catch (IOException e) {
			e.printStackTrace();
            return null;
		}
	}

	public static void saveProject(Project project) {
		log("Saving Project: " + project.getProjectName() + " to XML file");

		Document d = ProjectSerializer.exportToXML(project);
		String fileName = project.getProjectName() + ".xml";
		try {
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            File outputFile = new File(getProjectDirectory(), fileName);
			Result result = new StreamResult(outputFile);
			Source source = new DOMSource(d);
			transformer.transform(source, result);
		} catch(TransformerConfigurationException e) {
			e.printStackTrace();
		} catch(TransformerException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean isExternalStorageAvailable() {
		String state = Environment.getExternalStorageState();
		log("External storage state: " + state);
		return Environment.MEDIA_MOUNTED.equals(state);
	}

	protected static File getProjectDirectory() throws IOException {
		if (isExternalStorageAvailable()) {
			File docs = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            docs.mkdir();

			File bertProjects = new File(docs, PROJECT_DIR_NAME);
			bertProjects.mkdir();
            if (bertProjects.isDirectory()) {
                return bertProjects;
            } else {
                throw new IOException();
            }
		} else {
			log("Unable to load Project Folder");
			throw new IOException();
		}
	}

	public static File getExportsDirectory() throws IOException {
		if (isExternalStorageAvailable()) {
            File docs = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            docs.mkdir();
			File exportDir = new File(docs, EXPORT_DIR_NAME);
            exportDir.mkdir();
            if (exportDir.isDirectory()) {
                return exportDir;
            } else {
                throw new IOException();
            }
        } else {
            log("Unable to load Exports Folder");
			throw new IOException();
		}
	}

	private static void log(String output) {
		Log.d("File_Provider", output);
	}

}