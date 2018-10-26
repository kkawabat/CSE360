import java.util.*;

public class ActivityNode implements Comparable<ActivityNode>{

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

    //setter function to add successor
    public void addSuccessor(ActivityNode successor) {
        this.successors.add(successor);
    }

    //getter function to get the successor list
    public ArrayList<ActivityNode> getSuccessors(){
        return this.successors;
    }

    //setter function to add a predecessor to the predecessor list
    public void addPredecessor(ActivityNode predecessor) {
        this.predecessors.add(predecessor);
    }

    @Override
    public String toString() {
    	return this.name + " : " + this.duration + " : " + String.join(",", this.dependencies);
    }

    public String toString2() {
    	return this.name + " : " + this.duration;
    }

	@Override
	public int compareTo(ActivityNode B) {
		return this.name.compareTo(B.name);
	}

}
