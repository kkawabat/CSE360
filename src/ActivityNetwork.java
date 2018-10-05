import java.util.ArrayList;

public class ActivityNetwork {
	
	 // Instance Variables
    ArrayList<ActivityNode> nodeList;
    ArrayList<ActivityNode> startNodesList;
    boolean isProcessed = false;

	public ActivityNetwork() {
		this.nodeList = new ArrayList<ActivityNode>();
		this.startNodesList = new ArrayList<ActivityNode>();
	}
	
	public ActivityNetwork(ArrayList<ActivityNode> nodeList) {
		this.nodeList = nodeList;
		this.startNodesList = getStartNodes();
	}
	
	public ArrayList<ActivityNode> getStartNodes(){
		ArrayList<ActivityNode> startNodes = new ArrayList<ActivityNode>();
		for(ActivityNode node : this.nodeList) {
			if(node.predecessors.isEmpty())
				startNodes.add(node);
		}
		return startNodes; 
	}
	
	public void addPotentialNode(String name, int duration, String strPredecessors) {
		ActivityNode node = new ActivityNode(name, duration);
		String[] predecessorNames = parsePredecessorFromString(strPredecessors);
		this.nodeList.add(node);
		this.isProcessed = false;
		return;
	}
	
	String[] parsePredecessorFromString(String strPredecessor){
		String[] predecessorList = strPredecessor.split(",", -1);
		return predecessorList;
	}
	
	private void processNodeList() {
		//to do
		this.isProcessed = true;
		return;
	} 
	
	public ArrayList<pathAndtotalDuration> getPathLists() {
		this.processNodeList();
		ArrayList<pathAndtotalDuration> curPathAndDuration = new ArrayList<pathAndtotalDuration>();
		for(ActivityNode startNode : this.startNodesList) {
			curPathAndDuration.addAll(this.getPathListHelper(startNode));
		}
		return curPathAndDuration;
	}
	
	private ArrayList<pathAndtotalDuration> getPathListHelper(ActivityNode node){
		ArrayList<pathAndtotalDuration> curPathAndDuration = new ArrayList<pathAndtotalDuration>();
		ArrayList<ActivityNode> successors = node.getSuccessors();
		if(successors == null || successors.isEmpty()) {
			curPathAndDuration.add(new pathAndtotalDuration(node.name, node.duration));
		}else {
			for(ActivityNode tmpNode: successors) {
				curPathAndDuration.addAll(this.getPathListHelper(tmpNode));
			}
			for(pathAndtotalDuration tmpPathAndDuration: curPathAndDuration ) {
				tmpPathAndDuration.path = node.name + "->" + tmpPathAndDuration.path;
				tmpPathAndDuration.duration += node.duration;
			}
		}
		return curPathAndDuration;
		
	}
	
	public ActivityNode getNodeByName(String nodeName) {
		for(ActivityNode node: this.nodeList) {
			if(node.name.equals(nodeName)) {
				return node;
			}
		}
		return null;
	}
	
	public boolean isEmpty() {
		return nodeList.isEmpty(); 
	}
	
	public boolean isAllNodesConnected() {
		//to do
		return false;
	}
	
	public boolean isThereCycle() {
		//to do
		return false;
	}
}

	


////..if no predecessors add node to startNodes
//if(strPredecessor.equals("")) {
//	 this.startNodes.add(node);
//} else {
//	 //parse user input of predecessors and set predecessors and successors for the activity nodes involved
//	 String[] predecessorNameList = parsePredecessorFromString(strPredecessor);
//	 for(String name: predecessorNameList) {
//		 ActivityNode tmpPredecessor = getNodeByName(name, this.startNodes);
//		 tmpPredecessor.addSuccessor(node);
//		 node.addPredecessor(tmpPredecessor);
//	 }
//}