
public class ActivityNode {

    // Instance Variables 
    String name; 
    int duration; 
    String predecessors;
	
	public ActivityNode(String name, int duration, String predecessors) {
		this.name = name;
		this.duration = duration;
		this.predecessors = predecessors;
	}

}
