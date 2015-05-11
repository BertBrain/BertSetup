package bert.data.utility;

import bert.data.proj.BertUnit;
import bert.data.proj.Project;

/**
 * @author afiol-mahon
 */
public class Test {
    public static Project testProject = new Project("DebugProject");
    static {
        testProject.setContactName("Joe Smith");
        testProject.setContactNumber("555-555-5555");
        testProject.addBert(new BertUnit("Debug1", "1", "MACMAC", 0, 0));
    }

	public static void main(String[] args) {
	}
}
