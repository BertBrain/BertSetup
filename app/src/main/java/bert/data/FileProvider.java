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
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ProjectSerializer.getProjectFromDocument(document);
	}

	public static void saveProject(Project project) {
		log("Saving Project: " + project.getProjectName() + " to XML file");
		Document d = ProjectSerializer.exportToXML(project);
		String fileName = project.getProjectName() + ".xml";
		try {
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			Result result = new StreamResult(new File(getProjectDirectory(), fileName));
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

	public static boolean isExternalStorageAvailable() {
		String state = Environment.getExternalStorageState();
		log("External storage state: " + state);
		return Environment.MEDIA_MOUNTED.equals(state);
	}

	public static File getProjectDirectory() throws IOException {
		if (isExternalStorageAvailable()) {
			File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "BertProjects");
			f.mkdir();
			return f;
		} else {
			log("Unable to load Project Folder");
			throw new IOException();
		}
	}

	public static File getExportsDirectory() throws IOException {
		if (isExternalStorageAvailable()) {
			File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "BertExports");
			f.mkdir();
			return f;
		} else {
			log("Unable to load Exports Folder");
			throw new IOException();
		}
	}

	private static void log(String output) {
		Log.d("File_Provider", output);
	}

}