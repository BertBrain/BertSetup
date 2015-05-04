package bert.database;

/**
 * @author afiol-mahon
 */
public class Test {
	public static void main(String[] args) {		
		if (true) {
			Project p = new Project("Client", "Joe Smith", "555-555-5555");
			p.addBert(new BertUnit("Projector1", "102", "Science", 0));
			p.addBert(new BertUnit("vendingMachine", "245", "Math", 1));
			p.exportToXML();
		} else {
			Project p = new Project();
			p.exportToXML();
		}
	}
}
