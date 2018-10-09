import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;

public class ActivityNetwork {

	 // Instance Variables
    ArrayList<ActivityNode> nodeList;
    ArrayList<ActivityNode> startNodesList;
    boolean isProcessed = false;

	//constructor to initialize nodeList and startNodeList
	public ActivityNetwork() {
		this.nodeList = new ArrayList<ActivityNode>();
		this.startNodesList = new ArrayList<ActivityNode>();
	}

	
	public ActivityNetwork(ArrayList<ActivityNode> nodeList) {
		this.nodeList = nodeList;
		this.startNodesList = getStartNodes();
	}

	//create a new ArrayList of Activity Nodes that contain the starting nodes
	public ArrayList<ActivityNode> getStartNodes(){
		ArrayList<ActivityNode> startNodes = new ArrayList<ActivityNode>();
		for(ActivityNode node : this.nodeList) {
			if(node.predecessors.isEmpty())
				startNodes.add(node);
		}
		return startNodes;
	}

	//used to add a Node passed from the user into the current nodeList
	public void addPotentialNode(ActivityNode node) {
		this.nodeList.add(node);
		this.isProcessed = false;
		return;
	}

	//method to remove a node from current nodeList
  	public void removeNodes(ArrayList<ActivityNode> list) {
    		for(ActivityNode node : list) {
      			this.nodeList.remove(node);
      			this.startNodesList.remove(node);
    		}
  	}

  	//returns list of successors of node
	private ArrayList<ActivityNode> findAdjacentNodes(ActivityNode node, ArrayList<ActivityNode> list) {
		ArrayList<ActivityNode> adjancentNodes = new ArrayList<ActivityNode>();
		for(ActivityNode curr: list) {
			if(Arrays.asList(curr.dependencies).contains(node.name))
				adjancentNodes.add(curr);
		}
		return adjancentNodes;
	}

	//returns list of successors of node
  	private ArrayList<ActivityNode> findDoubleyAdjacentNodes(ActivityNode node, ArrayList<ActivityNode> list) {
		ArrayList<ActivityNode> adjancentNodes = new ArrayList<ActivityNode>();
		for(ActivityNode curr: list) {
			if(Arrays.asList(curr.dependencies).contains(node.name))
				adjancentNodes.add(curr);
		}
    		for(String predecessorName: node.dependencies) {
      			if(!predecessorName.equals(""))
        			adjancentNodes.add(getNodeByName(predecessorName));
    		}
		return adjancentNodes;
	}

	//method used to find the index of a node of a particular list
	private static int indexOfName(ArrayList<ActivityNode> list, String name) {
		int i = 0;
    		for(ActivityNode object : list) {
      			if (object.name.equals(name))
        			return i;
			i++;
    		}
    		return -1; //if index is not found return 'out of bounds'
	}

	//recursive sorting method to represent the graph
	private void topologicalSortUtil(ActivityNode node, boolean visited[], ArrayList<ActivityNode> stack, ArrayList<ActivityNode> activityQueue) {
    		visited[indexOfName(activityQueue, node.name)] = true; //set current node to visited
    		ActivityNode n;
    		Iterator<ActivityNode> i = findAdjacentNodes(node, this.nodeList).iterator();

    		while (i.hasNext()) {
      			n = i.next();
      			if (!visited[activityQueue.indexOf(n)]) //for each successor recurse that hasn't been visited recurse
      				topologicalSortUtil(n, visited, stack, activityQueue);
    		}
    		stack.add(0, node); //as recursion unwinds nodes get added to the stack
  	}
	
	//method to do topological sort
	//uses recursive topologicalSortUtil   
	private ArrayList<ActivityNode> topologicalSort(ArrayList<ActivityNode> activityQueue) {
    		ArrayList<ActivityNode> stack = new ArrayList<ActivityNode>();
		ArrayList<ActivityNode> sortedQ = new ArrayList<ActivityNode>();

		
    		boolean visited[] = new boolean[activityQueue.size()]; //mark all vertices as not visited

    		for (int i = 0; i < activityQueue.size(); i++)
      			if (!visited[i]) //for each node that needs to be added that hasn't been visited call topologicalSortUtil on that node
        			topologicalSortUtil(activityQueue.get(i), visited, stack, activityQueue);
    		while (!stack.isEmpty()) { //pop everything of the stack into a queue for the desired order
      			sortedQ.add(stack.remove(0));
		}
		return sortedQ; //return a beautifully sorted queue of nodes
  	}

	//method used to parse the 'predecessor' input from the user using ',' as the split
	String[] parsePredecessorFromString(String strPredecessor){
		String[] predecessorList = strPredecessor.split(",", -1);
		return predecessorList;
	}

	//begin sorting the graph
	public void processQueue(ArrayList<ActivityNode> activityQueue) {
		activityQueue = topologicalSort(activityQueue);
		this.isProcessed = true;
		return;
	}

	//recursive function to check the connectivity of a node
	private void connectivityUtil(ActivityNode node, boolean visited[], ArrayList<ActivityNode> stack, ArrayList<ActivityNode> list) {
  		visited[list.indexOf(node)] = true; //set current node to visited
  		ActivityNode n;
  		Iterator<ActivityNode> i = findDoubleyAdjacentNodes(node, this.nodeList).iterator();
  		while (i.hasNext()) {
    			n = i.next();
    			if(!visited[list.indexOf(n)]) //for each successor recurse that hasn't been visitied recurse
      				connectivityUtil(n, visited, stack, list);
  		}
  		stack.add(0, node);
	}

	//method to represent the possible paths of the graph and to calculate the duration of each path taken
	public ArrayList<pathAndtotalDuration> getPathLists() {
		ArrayList<pathAndtotalDuration> curPathAndDuration = new ArrayList<pathAndtotalDuration>();
		for(ActivityNode startNode : this.startNodesList) {
			curPathAndDuration.addAll(this.getPathListHelper(startNode)); //traverse through each starting node and call the recursive function getPathListHelper
		}
		return curPathAndDuration; //return list that contain the paths with durations for the graph
	}

	//recursive function to find the paths with the total durations
	private ArrayList<pathAndtotalDuration> getPathListHelper(ActivityNode node) {
		ArrayList<pathAndtotalDuration> curPathAndDuration = new ArrayList<pathAndtotalDuration>();
		ArrayList<ActivityNode> successors = node.getSuccessors();
		if(successors == null || successors.isEmpty()) { //base case has reached the end of a path
			curPathAndDuration.add(new pathAndtotalDuration(node.name, node.duration));
		}else {
			//otherwise go through that nodes successors recursively to find the end of the path
			for(ActivityNode tmpNode: successors) {
				curPathAndDuration.addAll(this.getPathListHelper(tmpNode));
			}
			//add the node to the path
			for(pathAndtotalDuration tmpPathAndDuration: curPathAndDuration ) {
				tmpPathAndDuration.path = node.name + "->" + tmpPathAndDuration.path;
				tmpPathAndDuration.duration += node.duration;
			}
		}
		return curPathAndDuration;
	}


	//method used to find the node in the nodeList if just given the node name
	public ActivityNode getNodeByName(String nodeName) {
		for(ActivityNode node: this.nodeList) {
			if(node.name.equals(nodeName)) {
				return node;
			}
		}
		return null;
	}

	//method to check if the nodeList is empty meaning nothing has been entered by the user resulting in an error
	public boolean isEmpty() {
		return nodeList.isEmpty();
	}

	//method to check if all nodes in the current list are defined (true) otherwise an undefined node was detected (false)
  	public boolean allNodesDefinied(ArrayList<ActivityNode> list) {
    		for(ActivityNode node: list) {
      			for(String name: node.dependencies) {
        			if(indexOfName(this.nodeList, name) == -1 && !name.equals(""))
          				return false;
      			}
    		}
    		return true;
  	}

	//method used once user pressed 'Process' on the GUI to determine if all nodes are connected (true) otherwise a break was detected (false)
	public boolean isAllNodesConnected() {
    		ArrayList<ActivityNode> stack = new ArrayList<ActivityNode>();

    		boolean visited[] = new boolean[this.nodeList.size()];

    		connectivityUtil(this.nodeList.get(0), visited, stack, this.nodeList);
    		for(ActivityNode node: this.nodeList) {
      			if(stack.indexOf(node) == -1)
        			return false;
    		}
    		return true;
	}

	//cycles in the graph are not permitted
	//method used to check if graph contains a cycle
	public boolean isThereCycle(ArrayList<ActivityNode> activityQueue) {
    		int size = activityQueue.size();
    		char[] state = new char[size];
    		for(int i = 0; i < size; i++)
      			state[i] = 's';
    		for(int i = 0; i < size; i++) {
      			if(state[i] == 's') {
        			if(cycleUtil(activityQueue.get(i), state, activityQueue) == true)
          				return true;
      			}
    		}
    		return false;
 	}
	
	//recursive method to check if the graph contains a cycle
	boolean cycleUtil(ActivityNode node, char[] state, ArrayList<ActivityNode> activityQueue) {
  		state[activityQueue.indexOf(node)] = 'p';
  		ActivityNode n;
  		Iterator<ActivityNode> i = findAdjacentNodes(node, this.nodeList).iterator();
  		while (i.hasNext()) {
    			n = i.next();
    			if(state[activityQueue.indexOf(n)] == 'p')
      				return true;
    			if (state[activityQueue.indexOf(n)] == 's' && cycleUtil(n, state, activityQueue))
      				return true;
  		}
  		state[activityQueue.indexOf(node)] = 'f';
  		return false;
	}
}
