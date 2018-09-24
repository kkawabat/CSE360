import java.util.*;

public class ActivityNode {

    // Instance Variables
    String name;
    int duration;
    ArrayList<ActivityNode> predecessors = new ArrayList<ActivityNode>();
    ArrayList<ActivityNode> successors = new ArrayList<ActivityNode>();

	public ActivityNode(String name, int duration, ArrayList<ActivityNode> predecessors) {
		this.name = name;
		this.duration = duration;
		this.predecessors = predecessors;
	}

	public ActivityNode(String name, int duration) {
		this.name = name;
		this.duration = duration;
	}

	public void addSuccessor(ActivityNode successor) {
		this.successors.add(successor);
	}
	
	public ArrayList<ActivityNode> getSuccessors(){
		return this.successors;
	}

	public void addPredecessor(ActivityNode predecessor) {
		this.predecessors.add(predecessor);
	}

}
