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

	private static int indexOfName(ArrayList<ActivityNode> list, String name) {
		int i = 0;
    for(ActivityNode object : list) {
      if (object.name.equals(name))
        return i;
			i++;
    }
    return -1;
	}

	private void topologicalSortUtil(ActivityNode node, boolean visited[], ArrayList<ActivityNode> stack, ArrayList<ActivityNode> activityQueue) {
    visited[indexOfName(activityQueue, node.name)] = true; //set current node to visited
    ActivityNode n;
    Iterator<ActivityNode> i = findAdjacentNodes(node, this.nodeList).iterator();
    while (i.hasNext()) {
      n = i.next();
      if (!visited[activityQueue.indexOf(n)]) //for each successor recurse that hasn't been visitied recurse
      	topologicalSortUtil(n, visited, stack, activityQueue);
    }
    stack.add(0, node); //as recursion unwinds nodes get added to the stack
  }

	private ArrayList<ActivityNode> topologicalSort(ArrayList<ActivityNode> activityQueue) {
    ArrayList<ActivityNode> stack = new ArrayList<ActivityNode>();
		ArrayList<ActivityNode> sortedQ = new ArrayList<ActivityNode>();
    boolean visited[] = new boolean[activityQueue.size()];
    for (int i = 0; i < activityQueue.size(); i++)
      if (!visited[i]) //for each node that needs to be added that hasn't been visited call topologicalSortUtil on that node
        topologicalSortUtil(activityQueue.get(i), visited, stack, activityQueue);
    while (!stack.isEmpty()) { //pop everythin of the stack into a queue for the desired order
      sortedQ.add(stack.remove(0));
		}
		return sortedQ; //return a beautifully sorted queue of nodes
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

  public boolean allNodesDefinied(ArrayList<ActivityNode> list) {
    for(ActivityNode node: list) {
      for(String name: node.dependencies) {
        if(indexOfName(this.nodeList, name) == -1 && !name.equals(""))
          return false;
      }
    }
    return true;
  }

	public boolean isAllNodesConnected() {
		//to do
		return false;
	}

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
}
