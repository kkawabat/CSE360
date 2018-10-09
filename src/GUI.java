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
	private DefaultListModel<String> listModel = new DefaultListModel<String>();
  private JList<String> list = new JList<String>(listModel);
  private JScrollPane scrollPaneOutput;
	private ArrayList<ActivityNode> activityQueue = new ArrayList<ActivityNode>();
  private ActivityNetwork network = new ActivityNetwork();

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

		//add the option to load an activity list
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

		//options to save current activity list
		JMenuItem mntmSaveActivityList = new JMenuItem("Save Activity List");
		mntmSaveActivityList.setEnabled(false);
		mnFiles.add(mntmSaveActivityList);
		mnFiles.add(mntmOpenInputFile);

		//menu option 'quit' to exit GUI
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

		JLabel lblInput = new JLabel("Inputs (seperate multiple arguments with \",\")");
		lblInput.setBounds(10, 10, 45, 15);
		frame.getContentPane().add(lblInput);

		//button, once pressed begin adding activity to activity list
		JButton btnAddActivity = new JButton("Add Activity");
		btnAddActivity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				queActivity();
			}
		});
		btnAddActivity.setBounds(10, 130, 130, 30);
		frame.getContentPane().add(btnAddActivity);

		//button, once pressed begin processing current activity list
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

		//button, once pressed clear current activity list 
		JButton btnRestart = new JButton("Restart");
		btnRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listModel.removeAllElements();
				network.startNodesList.clear();
				network.nodeList.clear();
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

	//adds new node to queue and the network's nodeList,
	//if there is a syntactic error in the format of the user's imput this is were the Exception will be thrown
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
		if(network.getNodeByName(strActivityName) != null) {JOptionPane.showMessageDialog(null, "Node with same name already exists in network", "Activity Not Generated", JOptionPane.ERROR_MESSAGE);return;}
		int duration;
		//check if duration is valid
		try{duration = Integer.parseInt(strDuration);}
			catch (NumberFormatException ex){JOptionPane.showMessageDialog(null, "Duration is not an integer", "Activity Not Generated", JOptionPane.ERROR_MESSAGE);return;}
		String[] predecessorNameList = network.parsePredecessorFromString(strPredecessor);
		if(Arrays.asList(predecessorNameList).contains(strActivityName)) {JOptionPane.showMessageDialog(null, "node can't depend on itself", "Activity Not Generated", JOptionPane.ERROR_MESSAGE);return;}

		ActivityNode node = new ActivityNode(strActivityName, duration, predecessorNameList);
		network.addPotentialNode(node); //adds to the network's list of all nodes
		activityQueue.add(node);
	}

	//strings together node by setting successors and predecessors
	void addActivity(ActivityNode node) {
		 if(Arrays.asList(node.dependencies).contains("")) {
			network.startNodesList.add(node); //it's a root node so add it to list of root nodes and no need to string it
		 } else {
			 for(String name: node.dependencies) {
				 ActivityNode tmpPredecessor = network.getNodeByName(name);
				 tmpPredecessor.addSuccessor(node);
				 node.addPredecessor(tmpPredecessor);
			 }
		 }
		 return;
	}

	//string on each node in the queue of nodes to be added
	void addActivities() {
		for(ActivityNode node: activityQueue) {
			System.out.println("adding " + node.name);
			addActivity(node);
		}
		activityQueue.clear(); //everything from que has been added so flush it
	}

	void processNetwork() {
		if(network.isEmpty())
			{JOptionPane.showMessageDialog(null, "No Activity Nodes detected", "Could Not Process Network", JOptionPane.ERROR_MESSAGE);return;}

		if(!network.allNodesDefinied(activityQueue))
			{JOptionPane.showMessageDialog(null, "Not all antecedent nodes defined", "Could Not Process Network", JOptionPane.ERROR_MESSAGE);network.removeNodes(activityQueue);activityQueue.clear();return;}

		if(!network.isAllNodesConnected())
			{JOptionPane.showMessageDialog(null, "Not all Nodes are connected", "Could Not Process Network", JOptionPane.ERROR_MESSAGE);network.removeNodes(activityQueue);activityQueue.clear();return;}

		if(network.isThereCycle(activityQueue))
			{JOptionPane.showMessageDialog(null, "Cycle detected in network", "Could Not Process Network", JOptionPane.ERROR_MESSAGE);network.removeNodes(activityQueue);activityQueue.clear();return;}

		network.processQueue(activityQueue); //sort the queue into topological order
		addActivities(); //string together the nodes
		ArrayList<pathAndtotalDuration> pathAndDurationList = network.getPathLists();
		Collections.sort(pathAndDurationList);

		//clear output in gui
		listModel.removeAllElements();
	;
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
