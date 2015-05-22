package bert.data;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

/**
 * @author afiol-mahon
 */
public class FileProvider {
    public static final String EXPORT_DIR_NAME = "BertExports";
    public static final String PROJECT_DIR_NAME = "BertProjects";

	/**
	 * Will print the xml file to the console output. Useful for debugging quickly.
	 * @param document
	 */
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

	private static boolean isExternalStorageAvailable() {
		String state = Environment.getExternalStorageState();
		log("External storage state: " + state);
		return Environment.MEDIA_MOUNTED.equals(state);
	}

	public static File getProjectDirectory() throws IOException {
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