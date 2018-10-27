import java.util.*;
import java.nio.charset.*;
import java.time.LocalDateTime;

import java.awt.EventQueue;
import java.awt.ScrollPane;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;

import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.DefaultListModel;

import javax.swing.JOptionPane;
import javax.swing.JList;
import javax.swing.JScrollPane;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.awt.Font;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.DefaultListSelectionModel;

public class GUI {

	//GUI elements
	private JFrame frame;
	final JFileChooser fc = new JFileChooser();
	private JTextField textFieldActivityName;
	private JTextField textFieldDuration;
	private JTextField textFieldPredecessor;
	private DefaultListModel<String> pathListModel = new DefaultListModel<String>();
	private JList<String> path_list = new JList<String>(pathListModel);
	private JScrollPane scrollPanePaths;

	private DefaultListModel<ActivityNode> nodeListModel = new DefaultListModel<ActivityNode>();
	private JList<ActivityNode> node_list = new JList<ActivityNode>(nodeListModel);
	private JScrollPane scrollPaneNodes;

	//activityNetowrk elements
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
		frame.setBounds(100, 100, 750, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnFiles = new JMenu("Files");
		menuBar.add(mnFiles);

		//menu option 'quit' to exit GUI
		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}
		});

		JMenuItem mntmCreateReport = new JMenuItem("Create Report");
		mntmCreateReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					createReport();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		mnFiles.add(mntmCreateReport);

		mnFiles.add(mntmQuit);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				displayTextFromFile("src\\about.txt");
				return;
			}
		});
		mnHelp.add(mntmAbout);

		JMenuItem mntmHelp = new JMenuItem("Help");
		mntmHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayTextFromFile("src\\help.txt");
				return;
			}
		});
		mnHelp.add(mntmHelp);
		frame.getContentPane().setLayout(null);

		textFieldActivityName = new JTextField();
		textFieldActivityName.setBounds(100, 35, 404, 20);
		frame.getContentPane().add(textFieldActivityName);
		textFieldActivityName.setColumns(10);

		textFieldDuration = new JTextField();
		textFieldDuration.setBounds(100, 65, 404, 20);
		frame.getContentPane().add(textFieldDuration);
		textFieldDuration.setColumns(10);

		textFieldPredecessor = new JTextField();
		textFieldPredecessor.setBounds(100, 95, 404, 20);
		frame.getContentPane().add(textFieldPredecessor);
		textFieldPredecessor.setColumns(10);

		JLabel lblInput = new JLabel("Inputs (seperate multiple arguments with \",\")");
		lblInput.setBounds(10, 10, 550, 15);
		frame.getContentPane().add(lblInput);

		//button, once pressed begin adding activity to activity list
		JButton btnAddActivity = new JButton("Add Activity");
		btnAddActivity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				queActivity();
			}
		});
		btnAddActivity.setBounds(10, 129, 102, 30);
		frame.getContentPane().add(btnAddActivity);

		//button, once pressed begin processing current activity list
		JButton btnProcess = new JButton("Process");
		btnProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processNetwork();
			}
		});
		btnProcess.setBounds(123, 129, 86, 30);
		frame.getContentPane().add(btnProcess);

		JLabel lblOutput = new JLabel("Activity Paths (descending order by duration)");
		lblOutput.setBounds(10, 170, 494, 15);
		frame.getContentPane().add(lblOutput);

		//button, once pressed clear current activity list
		JButton btnRestart = new JButton("Restart");
		btnRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nodeListModel.removeAllElements();
				pathListModel.removeAllElements();
				network.startNodesList.clear();
				network.nodeList.clear();
				activityQueue.clear();
			}
		});
		btnRestart.setBounds(374, 129, 130, 30);
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

		scrollPanePaths = new JScrollPane();
		scrollPanePaths.setBounds(10, 187, 494, 178);
		path_list.setFont(new Font("Consolas", Font.PLAIN, 12));
		scrollPanePaths.setViewportView(path_list);
		frame.getContentPane().add(scrollPanePaths);

		scrollPaneNodes = new JScrollPane();
		scrollPaneNodes.setBounds(514, 35, 208, 330);
		node_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneNodes.setViewportView(node_list);
		frame.getContentPane().add(scrollPaneNodes);

		Vector<Component> order = new Vector<Component>(3);
		order.add(textFieldActivityName);
		order.add(textFieldDuration);
		order.add(textFieldPredecessor);
		order.add(btnAddActivity);
		CustomTraversalPolicy newPolicy = new CustomTraversalPolicy(order);
		frame.setFocusTraversalPolicy(newPolicy);

		JLabel lblNodes = new JLabel("Node name : Duration : Dependencies");
		lblNodes.setBounds(514, 10, 225, 14);
		frame.getContentPane().add(lblNodes);

		node_list.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent le) {
				if(!le.getValueIsAdjusting()) {
	        int idx = node_list.getSelectedIndex();
					int nodeDuration;
	        if(idx != -1) {
						try{
							String input = JOptionPane.showInputDialog("Change the duration of " + node_list.getSelectedValue().name + ":");
							if(input == null)
								return;
							nodeDuration = Integer.parseInt(input);
						}
						catch(NumberFormatException ex){JOptionPane.showMessageDialog(null, "Duration is not an integer", "Duration not changed", JOptionPane.ERROR_MESSAGE);return;}
						ActivityNode node = node_list.getSelectedValue();
						node.duration = nodeDuration;
						scrollPaneNodes.revalidate();
						scrollPaneNodes.repaint();
					}
	      }
			}
    });

		node_list.setSelectionModel(new DefaultListSelectionModel() {
			public void setSelectionInterval(int index0, int index1) {
        if(index0 == index1) {
          if(isSelectedIndex(index0)) {
            removeSelectionInterval(index0, index0);
            return;
          }
        }
        super.setSelectionInterval(index0, index1);
      }

			@Override
      public void addSelectionInterval(int index0, int index1) {
        if(index0 == index1) {
          if(isSelectedIndex(index0)) {
            removeSelectionInterval(index0, index0);
            return;
          }
          super.addSelectionInterval(index0, index1);
        }
      }
		});

		JButton btnCriticalProcess = new JButton("Process (Critical)");
		btnCriticalProcess.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnCriticalProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				processCritical();
			}
		});
		btnCriticalProcess.setBounds(219, 129, 145, 30);
		frame.getContentPane().add(btnCriticalProcess);
	}

	private static boolean hasDuplicatePredecessors(String[] list) {
		ArrayList<String> result = new ArrayList<>();
		HashSet<String> set = new HashSet<>();
		for(String item : list) {
			if(!set.add(item))
				return true;
		}
		return false;
	}

	private static boolean hasEmptyPredecessor(String[] list) {
		for(String item : list) {
			if(item.equals("") && list.length > 1) //if there is empty string among more than 1 dependency
				return true;
		}
		return false;
	}

	//adds new node to queue and the network's nodeList,
	//if there is a syntactic error in the format of the user's input this is were the Exception will be thrown
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
		//check if a node as dependency on itself
		if(Arrays.asList(predecessorNameList).contains(strActivityName)) {JOptionPane.showMessageDialog(null, "Node can't depend on itself", "Activity Not Generated", JOptionPane.ERROR_MESSAGE);return;}
		//check if duplicate dependencies on same node
		if(hasDuplicatePredecessors(predecessorNameList)) {JOptionPane.showMessageDialog(null, "Node cannot have duplicate predecessors", "Activity Not Generated", JOptionPane.ERROR_MESSAGE);return;}
		//check if dependency on empty string
		if(hasEmptyPredecessor(predecessorNameList)) {JOptionPane.showMessageDialog(null, "Node cannot have empty predecessors", "Activity Not Generated", JOptionPane.ERROR_MESSAGE);return;}

		ActivityNode node = new ActivityNode(strActivityName, duration, predecessorNameList);
		network.addPotentialNode(node); //adds to the network's list of all nodes
		activityQueue.add(node);

		//print new node into node text panel
		nodeListModel.addElement(node);
		node_list.setModel(nodeListModel);
		scrollPaneNodes.revalidate();
		scrollPaneNodes.repaint();
		return;
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
		activityQueue.clear(); //everything from queue has been added so flush it
	}

	//checks that the network is a valid activity diagram, then process the nodes
	void process() {
		if(network.isEmpty())
		{JOptionPane.showMessageDialog(null, "No Activity Nodes detected", "Could Not Process Network", JOptionPane.ERROR_MESSAGE);return;}

		if(!network.allNodesDefinied(activityQueue))
		{JOptionPane.showMessageDialog(null, "Not all antecedent nodes defined", "Could Not Process Network", JOptionPane.ERROR_MESSAGE);network.removeNodes(activityQueue);activityQueue.clear();removeJunk();return;}

		if(!network.isAllNodesConnected())
		{JOptionPane.showMessageDialog(null, "Not all Nodes are connected", "Could Not Process Network", JOptionPane.ERROR_MESSAGE);network.removeNodes(activityQueue);activityQueue.clear();removeJunk();return;}

		if(network.isThereCycle(activityQueue))
		{JOptionPane.showMessageDialog(null, "Cycle detected in network", "Could Not Process Network", JOptionPane.ERROR_MESSAGE);network.removeNodes(activityQueue);activityQueue.clear();removeJunk();return;}

		network.processQueue(activityQueue); //sort the queue into topological order
		addActivities(); //string together the nodes
	}

	//processes then prints the list of all paths
	void processNetwork() {
		process();
		ArrayList<pathAndtotalDuration> pathAndDurationList = network.getPathLists();
		//clear output in GUI
		pathListModel.removeAllElements();
		//re-populate output with list of paths
		for(pathAndtotalDuration tmpPath: pathAndDurationList) {
			pathListModel.addElement(tmpPath.toString());
			path_list.setModel(pathListModel);
			System.out.println(tmpPath.toString());
		}
		scrollPanePaths.revalidate();
		scrollPanePaths.repaint();
		return;
	}

	//processes then prints the list of all critical paths
	void processCritical() {
		process();
		ArrayList<pathAndtotalDuration> pathAndDurationList = network.getPathLists();
		pathListModel.removeAllElements();
		int critcalPathDuration = pathAndDurationList.get(0).duration;
		for (pathAndtotalDuration traverse : pathAndDurationList) {
			if (traverse.duration == critcalPathDuration) {
				pathListModel.addElement(traverse.toString());
				path_list.setModel(pathListModel);
				System.out.println(traverse.toString());
			}
		}
		scrollPanePaths.revalidate();
		scrollPanePaths.repaint();
	}

	void removeJunk() {
		nodeListModel.removeAllElements();
		for(ActivityNode node: network.nodeList)
			nodeListModel.addElement(node);
	}

	void displayTextFromFile(String file_name) {
		//clear output in GUI
		pathListModel.removeAllElements();
		//re-populate output with text from file_name
		try {
			File file = new File(file_name);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				pathListModel.addElement(line);
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		path_list.setModel(pathListModel);
		scrollPanePaths.revalidate();
		scrollPanePaths.repaint();
		return;
	}

	void createReport() throws FileNotFoundException {
		JFileChooser reportSaver = new JFileChooser();
		if (reportSaver.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
		  File file_report = reportSaver.getSelectedFile();
		  PrintWriter printer_report = new PrintWriter(file_report);


		  printer_report.println("Title:" + file_report.getName());
		  LocalDateTime now = LocalDateTime.now();
		  printer_report.println("Time and Data:" + now.toString());

		  //print node list to report
		  printer_report.println("Node list (Name : Duration)");
		  ArrayList<ActivityNode> nodelist = network.nodeList;
		  Collections.sort(nodelist);
		  for(ActivityNode node : nodelist) {
			  printer_report.println(node.toString());
		  }
		  printer_report.println();

		  //print path lists to report
		  printer_report.println("Path list:");
		  ArrayList<pathAndtotalDuration> pathAndDurationList = network.getPathLists();
		  for(pathAndtotalDuration tmpPath: pathAndDurationList) {
			  printer_report.println(tmpPath.toString());
		  }
		  printer_report.close();
		  JOptionPane.showMessageDialog(null, file_report.getName() + " has been successfully generated", "Report Saved",  JOptionPane.INFORMATION_MESSAGE);
		  return;
		}
		JOptionPane.showMessageDialog(null, "File name not acceptable", "Could Not Generate Report", JOptionPane.ERROR_MESSAGE);
		return;
	}
}
