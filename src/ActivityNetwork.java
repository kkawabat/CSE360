import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;

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

	public void addPotentialNode(ActivityNode node) {
		this.nodeList.add(node);
		this.isProcessed = false;
		return;
	}

	private ArrayList<ActivityNode> findAdjacentNodes(ActivityNode node, ArrayList<ActivityNode> nodeList) {
		ArrayList<ActivityNode> adjancentNodes = new ArrayList<ActivityNode>();
		for(ActivityNode curr: nodeList) {
			if(Arrays.asList(curr.dependencies).contains(node.name))
				adjancentNodes.add(curr);
		}
		return adjancentNodes;
	}

	private static int indexOfName(ArrayList<ActivityNode> nodeList, String name) {
		int i = 0;
    for(ActivityNode object : nodeList) {
      if (object.name.equals(name))
        return i;
			i++;
    }
    return -1;
	}

	private void topologicalSortUtil(ActivityNode node, boolean visited[], ArrayList<ActivityNode> stack, ArrayList<ActivityNode> activityQueue) {
    visited[indexOfName(activityQueue, node.name)] = true;
    ActivityNode n;
    Iterator<ActivityNode> i = findAdjacentNodes(node, this.nodeList).iterator();
    while (i.hasNext()) {
      n = i.next();
      if (!visited[activityQueue.indexOf(n)])
      	topologicalSortUtil(n, visited, stack, activityQueue);
    }
    stack.add(0, node);
  }

	private ArrayList<ActivityNode> topologicalSort(ArrayList<ActivityNode> activityQueue) {
    ArrayList<ActivityNode> stack = new ArrayList<ActivityNode>();
		ArrayList<ActivityNode> sortedQ = new ArrayList<ActivityNode>();
    boolean visited[] = new boolean[activityQueue.size()];
    for (int i = 0; i < activityQueue.size(); i++)
      if (visited[i] == false)
        topologicalSortUtil(activityQueue.get(i), visited, stack, activityQueue);
    while (!stack.isEmpty()) {
      sortedQ.add(stack.remove(0));
		}
		return sortedQ;
  }

	String[] parsePredecessorFromString(String strPredecessor){
		String[] predecessorList = strPredecessor.split(",", -1);
		return predecessorList;
	}

	public void processQueue(ArrayList<ActivityNode> activityQueue) {
		activityQueue = topologicalSort(activityQueue);
		this.isProcessed = true;
		return;
	}

	public ArrayList<pathAndtotalDuration> getPathLists() {
		ArrayList<pathAndtotalDuration> curPathAndDuration = new ArrayList<pathAndtotalDuration>();
		for(ActivityNode startNode : this.startNodesList) {
			curPathAndDuration.addAll(this.getPathListHelper(startNode));
		}
		return curPathAndDuration;
	}

	private ArrayList<pathAndtotalDuration> getPathListHelper(ActivityNode node) {
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
