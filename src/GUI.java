import java.util.*;

import java.awt.EventQueue;
import java.awt.ScrollPane;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;

import java.io.File;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.DefaultListModel;

import javax.swing.JOptionPane;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class GUI {

	private JFrame frame;
	final JFileChooser fc = new JFileChooser();
	private JTextField textFieldActivityName;
	private JTextField textFieldDuration;
	private JTextField textFieldPredecessor;
	private ArrayList<ActivityNode> startNodes = new ArrayList<ActivityNode>();
	private ArrayList<ActivityNode> allNodes = new ArrayList<ActivityNode>();
	private ArrayList<ActivityNode> activityQueue = new ArrayList<ActivityNode>();

	private DefaultListModel<String> listModel = new DefaultListModel<String>();
    private JList<String> list = new JList<String>(listModel);

    private JScrollPane scrollPaneOutput;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 442, 432);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnFiles = new JMenu("Files");
		menuBar.add(mnFiles);

		JMenuItem mntmOpenInputFile = new JMenuItem("Load Activity List");
		mntmOpenInputFile.setEnabled(false);
		mntmOpenInputFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			  int returnVal = fc.showOpenDialog(null);

		    if (returnVal == JFileChooser.APPROVE_OPTION) {
		    	File file = fc.getSelectedFile();
		    } else {
		      System.out.print("Open command cancelled by user.\n");
		    }
		  }
		});

		JMenuItem mntmSaveActivityList = new JMenuItem("Save Activity List");
		mntmSaveActivityList.setEnabled(false);
		mnFiles.add(mntmSaveActivityList);
		mnFiles.add(mntmOpenInputFile);

		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 System.exit(1);
			}
		});

		mnFiles.add(mntmQuit);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);

		JMenuItem mntmHelp = new JMenuItem("Help");
		mnHelp.add(mntmHelp);
		frame.getContentPane().setLayout(null);

		textFieldActivityName = new JTextField();
		textFieldActivityName.setBounds(100, 35, 320, 20);
		frame.getContentPane().add(textFieldActivityName);
		textFieldActivityName.setColumns(10);

		textFieldDuration = new JTextField();
		textFieldDuration.setBounds(100, 65, 320, 20);
		frame.getContentPane().add(textFieldDuration);
		textFieldDuration.setColumns(10);

		textFieldPredecessor = new JTextField();
		textFieldPredecessor.setBounds(100, 95, 320, 20);
		frame.getContentPane().add(textFieldPredecessor);
		textFieldPredecessor.setColumns(10);

		JLabel lblInput = new JLabel("Input");
		lblInput.setBounds(10, 10, 45, 15);
		frame.getContentPane().add(lblInput);

		JButton btnAddActivity = new JButton("Add Activity");
		btnAddActivity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				queActivity();
			}
		});
		btnAddActivity.setBounds(10, 130, 130, 30);
		frame.getContentPane().add(btnAddActivity);

		JButton btnProcess = new JButton("Process");
		btnProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processNetwork();
			}
		});
		btnProcess.setBounds(150, 130, 130, 30);
		frame.getContentPane().add(btnProcess);

		JLabel lblOutput = new JLabel("Output");
		lblOutput.setBounds(10, 170, 45, 15);
		frame.getContentPane().add(lblOutput);

		JButton btnRestart = new JButton("Restart");
		btnRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startNodes.clear();
				listModel.removeAllElements();
				allNodes.clear();
				activityQueue.clear();
			}
		});
		btnRestart.setBounds(290, 130, 130, 30);
		frame.getContentPane().add(btnRestart);

		JLabel lblNewLabel = new JLabel("Activity Name");
		lblNewLabel.setBounds(10, 35, 80, 15);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Duration");
		lblNewLabel_1.setBounds(10, 65, 80, 15);
		frame.getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Predecessor");
		lblNewLabel_2.setBounds(10, 100, 80, 15);
		frame.getContentPane().add(lblNewLabel_2);

		scrollPaneOutput = new JScrollPane();
		scrollPaneOutput.setBounds(10, 185, 410, 180);
		scrollPaneOutput.setViewportView(list);
		frame.getContentPane().add(scrollPaneOutput);
	}

	public static boolean containsName(ArrayList<ActivityNode> list, String name) {
    for(ActivityNode object : list) {
      if (object.name.equals(name))
        return true;
    }
    return false;
	}

	public static int indexOfName(ArrayList<ActivityNode> list, String name) {
		int i = 0;
    for(ActivityNode object : list) {
      if (object.name.equals(name))
        return i;
			i++;
    }
    return -1;
	}

	void queActivity() {
		String strActivityName = textFieldActivityName.getText();
		String strDuration = textFieldDuration.getText();
		String strPredecessor = textFieldPredecessor.getText();

		textFieldActivityName.setText("");
		textFieldDuration.setText("");
		textFieldPredecessor.setText("");

	 //check if name is valid
		if(strActivityName.equals("")) {JOptionPane.showMessageDialog(null, "Node must have a name", "Activity Not Generated", JOptionPane.ERROR_MESSAGE);return;}
		//check if node with same name exists
		if(containsName(allNodes, strActivityName)) {JOptionPane.showMessageDialog(null, "Node with same name already exists in network", "Activity Not Generated", JOptionPane.ERROR_MESSAGE);return;}
		int duration;
		//check if duration is valid
		try{duration = Integer.parseInt(strDuration);}
			catch (NumberFormatException ex){JOptionPane.showMessageDialog(null, "Duration is not an integer", "Activity Not Generated", JOptionPane.ERROR_MESSAGE);return;}

		String[] predecessorNameList = parsePredecessorFromString(strPredecessor);
		ActivityNode node = new ActivityNode(strActivityName, duration, predecessorNameList);
		allNodes.add(node);
		if(strPredecessor.equals("")) {
			activityQueue.add(0, node);
		}
		else
			activityQueue.add(node);
	}

  void topologicalSortUtil(ActivityNode node, boolean visited[], ArrayList<ActivityNode> stack) {
    visited[indexOfName(activityQueue, node.name)] = true;
    ActivityNode n;
    Iterator<ActivityNode> i = findAdjacentNodes(node).iterator();
    while (i.hasNext()) {
      n = i.next();
      if (!visited[activityQueue.indexOf(n)])
      	topologicalSortUtil(n, visited, stack);
    }
    stack.add(0, node);
  }

	ArrayList<ActivityNode> topologicalSort() {
    ArrayList<ActivityNode> stack = new ArrayList<ActivityNode>();
		ArrayList<ActivityNode> sortedQ = new ArrayList<ActivityNode>();
    boolean visited[] = new boolean[activityQueue.size()];
    for (int i = 0; i < activityQueue.size(); i++)
      visited[i] = false;
    for (int i = 0; i < activityQueue.size(); i++)
      if (visited[i] == false)
        topologicalSortUtil(activityQueue.get(i), visited, stack);
    while (!stack.isEmpty()) {
      sortedQ.add(stack.get(0));
			stack.remove(0);
		}
		return sortedQ;
  }

	ArrayList<ActivityNode> findAdjacentNodes(ActivityNode node) {
		ArrayList<ActivityNode> adjancentNodes = new ArrayList<ActivityNode>();
		for(ActivityNode curr: allNodes) {
			if(Arrays.asList(curr.dependencies).contains(node.name))
				adjancentNodes.add(curr);
		}
		return adjancentNodes;
	}

	void addActivities() {
		for(ActivityNode node: activityQueue)
			addActivity(node);
		activityQueue.clear();
	}

	void addActivity(ActivityNode node) {
		System.out.println("adding " + node.name);
		 if(Arrays.asList(node.dependencies).contains("")) {
			 this.startNodes.add(node);
		 } else {
			 for(String name: node.dependencies) {
				 ActivityNode tmpPredecessor = getNodeByName(name, this.startNodes);
				 tmpPredecessor.addSuccessor(node);
				 node.addPredecessor(tmpPredecessor);
			 }
		 }
		 return;
	}

	String[] parsePredecessorFromString(String strPredecessor){
		String[] predecessorList = strPredecessor.split(",", -1);
		return predecessorList;
	}

	ActivityNode getNodeByName(String name, ArrayList<ActivityNode> rootNodes) {
		if(rootNodes.isEmpty()) {
			return null;
		}
		ActivityNode tmpNode = null;
		for(ActivityNode node: rootNodes) {
				tmpNode = nodeSearch(name, node);
				if(tmpNode != null)
					return tmpNode;
		}
		return tmpNode;
	}

	ActivityNode nodeSearch(String name, ActivityNode curNode) {
		if(curNode == null) {
			return null;
		}

		if(curNode.name.equals(name)) {
			return curNode;
		}

		ActivityNode tmpNode;
		for(ActivityNode node: curNode.getSuccessors()) {
			tmpNode = nodeSearch(name, node);
			if(tmpNode != null) {
				return tmpNode;
			}
		}
		return null;
	}

	ArrayList<pathAndtotalDuration> getPathLists(ArrayList<ActivityNode> rootNodes) {
		ArrayList<pathAndtotalDuration> allPaths = new ArrayList<pathAndtotalDuration>();
		for(ActivityNode node: rootNodes) {
			allPaths.addAll(getPartPath(node));
		}
		return allPaths;
	}

	ArrayList<pathAndtotalDuration> getPartPath(ActivityNode node) {
		ArrayList<pathAndtotalDuration> curPathAndDuration = new ArrayList<pathAndtotalDuration>();
		ArrayList<ActivityNode> successors = node.getSuccessors();
		if(successors == null || successors.isEmpty()) {
			curPathAndDuration.add(new pathAndtotalDuration(node.name, node.duration));
		} else {
			for(ActivityNode tmpNode: successors) {
				curPathAndDuration.addAll(getPartPath(tmpNode));
			}
			for(pathAndtotalDuration tmpPathAndDuration: curPathAndDuration ) {
				tmpPathAndDuration.path = node.name + "->" + tmpPathAndDuration.path;
				tmpPathAndDuration.duration += node.duration;
			}
		}
		return curPathAndDuration;
	}

	void processNetwork() {
		activityQueue = topologicalSort();
		addActivities();
		if(startNodes.isEmpty()) {
			{JOptionPane.showMessageDialog(null, "No Activity Nodes detected", "Could Not Process Network", JOptionPane.ERROR_MESSAGE);return;}
		}
		ArrayList<pathAndtotalDuration> pathAndDurationList = getPathLists(this.startNodes);
		listModel.removeAllElements();
		Collections.sort(pathAndDurationList);
		for(pathAndtotalDuration tmpPath: pathAndDurationList) {
			listModel.addElement(tmpPath.toString());
			list.setModel(listModel);
			scrollPaneOutput.revalidate();
			scrollPaneOutput.repaint();
			System.out.println(tmpPath.toString());
		}
		return;
	}
}
