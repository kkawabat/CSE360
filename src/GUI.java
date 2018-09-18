import java.awt.EventQueue;


import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;

import java.io.File;
import java.io.IOException;

public class GUI {

	private JFrame frame;
	final JFileChooser fc = new JFileChooser();

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
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFiles = new JMenu("Files");
		menuBar.add(mnFiles);
		
		JMenuItem mntmOpenInputFile = new JMenuItem("Select Input File");
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
		mnFiles.add(mntmOpenInputFile);
	}

}
