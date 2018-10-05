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

	private DefaultListModel<String> listModel = new DefaultListModel<String>();
    private JList<String> list = new JList<String>(listModel);

    private JScrollPane scrollPaneOutput;
    private ActivityNetwork network;

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
				startNodes.clear();
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
		 if(network.getNodeByName(strActivityName) != null) {JOptionPane.showMessageDialog(null, "Node with same name already exists in network", "Activity Not Generated", JOptionPane.ERROR_MESSAGE);return;}
		 int duration;
		 //check if duration is valid
		 try{duration = Integer.parseInt(strDuration);}
	     catch (NumberFormatException ex){JOptionPane.showMessageDialog(null, "Duration is not an integer", "Activity Not Generated", JOptionPane.ERROR_MESSAGE);return;}

		 //create activity node..
		 network.addPotentialNode(strActivityName, duration, strPredecessor);
		 return;
	}

	void processNetwork() {
		if(network.isEmpty()) 
			{JOptionPane.showMessageDialog(null, "No Activity Nodes detected", "Could Not Process Network", JOptionPane.ERROR_MESSAGE);return;}
		
		if(network.isAllNodesConnected()) 
			{JOptionPane.showMessageDialog(null, "Not all Nodes are connected", "Could Not Process Network", JOptionPane.ERROR_MESSAGE);return;}
		
		if(network.isThereCycle()) 
			{JOptionPane.showMessageDialog(null, "Cycle detected in network", "Could Not Process Network", JOptionPane.ERROR_MESSAGE);return;}
		
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
