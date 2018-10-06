import java.util.*;

public class ActivityNode {

    // Instance Variables
    String name;
    int duration;
    ArrayList<ActivityNode> predecessors = new ArrayList<ActivityNode>();
    ArrayList<ActivityNode> successors = new ArrayList<ActivityNode>();
    String[] dependencies;

  public ActivityNode(String name, int duration, String[] dependencies) {
		this.name = name;
		this.duration = duration;
		this.dependencies = dependencies;
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
