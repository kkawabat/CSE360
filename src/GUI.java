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
	private ActivityNode startNode = null;

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
				addActivity();
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
				startNode = null;
				listModel.removeAllElements();
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
	
	void addActivity() {
		 String strActivityName = textFieldActivityName.getText();
		 String strDuration = textFieldDuration.getText();
		 String strPredecessor = textFieldPredecessor.getText();
		 
		 textFieldActivityName.setText("");
		 textFieldDuration.setText("");
		 textFieldPredecessor.setText("");
		 
		//check if name is valid
		 if(strActivityName.equals("")) {JOptionPane.showMessageDialog(null, "Node must have a name", "Activity Not Generated", JOptionPane.ERROR_MESSAGE);return;}
		 //check if node with same name exists
		 if(getNodeByName(strActivityName, this.startNode) != null) {JOptionPane.showMessageDialog(null, "Node with same name already exists in network", "Activity Not Generated", JOptionPane.ERROR_MESSAGE);return;}
		 int duration;
		 //check if duration is valid
		 try{duration = Integer.parseInt(strDuration);}
	     catch (NumberFormatException ex){JOptionPane.showMessageDialog(null, "Duration is not an integer", "Activity Not Generated", JOptionPane.ERROR_MESSAGE);return;}
		 
		 //create activity node..
		 ActivityNode node = new ActivityNode(strActivityName, duration);
		 //..if no startNode initialized set node as startNode
		 if(this.startNode == null) {
			 //check that startNode has no predecessors
			 if(!strPredecessor.equals("")) {JOptionPane.showMessageDialog(null, "Start node must not have predecessor", "Activity Not Generated", JOptionPane.ERROR_MESSAGE);return;}
			 this.startNode = node;
		 }else {
			 //parse user input of predecessors and set predecessors and successors for the activity nodes involved
			 String[] predecessorNameList = parsePredecessorFromString(strPredecessor);
			 for(String name: predecessorNameList) {
				 ActivityNode tmpPredecessor = getNodeByName(name, this.startNode); 
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
	
	ActivityNode getNodeByName(String name, ActivityNode curNode) {
		if(curNode == null) {
			return null;
		} 
		
		if(curNode.name.equals(name)) {
			return curNode;
		}
		
		ActivityNode tmpNode;
		for(ActivityNode node: curNode.getSucessors()) {
			tmpNode = getNodeByName(name, node);
			if(tmpNode != null) {
				return tmpNode;
			}
		}
		return null;
	}
	
	ArrayList<pathAndtotalDuration> getPathLists(ActivityNode node){
		ArrayList<pathAndtotalDuration> curPathAndDuration = new ArrayList<pathAndtotalDuration>();
		ArrayList<ActivityNode> sucessors = node.getSucessors();
		if(sucessors == null || sucessors.isEmpty()) {
			curPathAndDuration.add(new pathAndtotalDuration(node.name, node.duration));
		}else {
			for(ActivityNode tmpNode: sucessors) {
				curPathAndDuration.addAll(getPathLists(tmpNode));
			}
			for(pathAndtotalDuration tmpPathAndDuration: curPathAndDuration ) {
				tmpPathAndDuration.path = node.name + "->" + tmpPathAndDuration.path; 
				tmpPathAndDuration.duration += node.duration;
			}
		}
		return curPathAndDuration;
	}
	
	void processNetwork() {
		if(this.startNode == null) {
			{JOptionPane.showMessageDialog(null, "No Activity Node detected", "Could Not Process Network", JOptionPane.ERROR_MESSAGE);return;}
		}
		ArrayList<pathAndtotalDuration> pathAndDurationList = getPathLists(this.startNode);
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
