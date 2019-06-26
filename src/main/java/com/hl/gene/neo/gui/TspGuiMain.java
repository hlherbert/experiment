package com.hl.gene.neo.gui;

//import com.hl.gene.old.*;

import com.hl.gene.neo.core.Cell;
import com.hl.gene.neo.core.CellFactory;
import com.hl.gene.neo.core.NeoGA;
import com.hl.gene.neo.tsp.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

//import javax.swing.*;
//import javax.swing.border.*;
//import java.awt.*;
//import java.awt.event.*;
//import java.io.*;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/

/**
 * The Main GUI of HL TSP Solver
 * 
 * @author hl
 * 
 */
public class TspGuiMain extends JFrame {

	private JPanel contentPane;

	// my components
	private JTextArea commandArea;
	private TspGuiPane canvasArea;

	// text for CommandPane
	final String commandIndicator = "-------";
	StringBuffer commandStrBuffer;
	CmdPrinter m_cmdPrinter;

	// tsp information
	private TspIO tspIO;
	private TspData tspData;
	int startCity = 1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TspGuiMain frame = new TspGuiMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TspGuiMain() {
		setTitle("Li Huang's TSP Solver");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OpenFile();
			}
		});
		mnFile.add(mntmOpen);

		JMenu mnEvolComp = new JMenu("Evolutionary Computing");
		menuBar.add(mnEvolComp);

		JMenuItem mntmGeneAlgo = new JMenuItem("Gene Algorithm");
		mntmGeneAlgo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SolveGA();
			}
		});
		mnEvolComp.add(mntmGeneAlgo);
		
		
//		JMenuItem mnuGACrowd = new JMenuItem("GACrowd");
//		mnuGACrowd.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				SolveGACrowd();
//			}
//		});
//		mnEvolComp.add(mnuGACrowd);
		

		JMenu mnHeuristic = new JMenu("Heuristic");
		menuBar.add(mnHeuristic);

//		JMenuItem mntmGreedy = new JMenuItem("Greedy");
//		mntmGreedy.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				SolveGreedy();
//			}
//		});
//		mnHeuristic.add(mntmGreedy);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmInfo = new JMenuItem("Info");
		mntmInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Inform();
			}
		});
		mnHelp.add(mntmInfo);

		JMenuItem mntmHelp = new JMenuItem("Help");
		mntmHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Help();
			}
		});
		mnHelp.add(mntmHelp);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.5);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(splitPane, BorderLayout.CENTER);

		JScrollPane scrollPane = new JScrollPane();
		splitPane.setRightComponent(scrollPane);

		JTextArea txtrCommandArea = new JTextArea();
		scrollPane.setViewportView(txtrCommandArea);

		// ***************** my code *********************
		commandArea = txtrCommandArea; // command area
		canvasArea = new TspGuiPane(); // canvas area
		splitPane.setLeftComponent(canvasArea);
		m_cmdPrinter = new CmdPrinter();

		Init();
	}

	// ********************* NON-GUI
	// ********************************************
	private void Init() {
		commandStrBuffer = new StringBuffer();
		tspIO = new TspIO();
		tspIO.SetToStringMode(commandStrBuffer);
		tspData = null;
		m_cmdPrinter.SetOutputToConsole();//m_cmdPrinter.SetOutputToStringBuffer(commandStrBuffer);
	}

	// ************* COMMAND AREA ****************************************
	private void ClearCommandArea() {
		commandStrBuffer.setLength(0);
		commandArea.setText("");
	}

	// update the command text area,according to outStream
	private void UpdateCommandArea() {
		commandArea.setText(commandStrBuffer.toString());
	}

	// append command text
	private void PrintCommandln(String str) {
		commandStrBuffer.append(str);
		commandStrBuffer.append("\n");
		commandArea.append(str);
		commandArea.append("\n");
	}

	// append command title
	// e.g. ------ TSP File Opened --------
	// ------ Solve BFS ---------
	private void PrintCommandTitle(String str) {
		PrintCommandln(commandIndicator + str + commandIndicator);
	}

	// ******************** FILE MENU ***************************************
	// Open a tsp file
	private void OpenFile() {
		// Create File Chooser
		JFileChooser fileChooser = new JFileChooser();

		// Set current dir
		fileChooser.setCurrentDirectory(new File("."));
		fileChooser.setAcceptAllFileFilterUsed(false);
		// final String[][] fileENames = { { ".java", "JAVA Source code(*.java)"
		// },
		// { ".doc", "MS-Word 2003 File(*.doc)" },
		// { ".xls", "MS-Excel 2003 File(*.xls)" } };
		final String[][] fileENames = { { ".tsp", "TSPLIB file(*.tsp)" } };

		// Show all files
		fileChooser
				.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
					public boolean accept(File file) {
						return true;
					}

					public String getDescription() {
						return "All files(*.*)";
					}
				});

		// Add file types
		for (final String[] fileEName : fileENames) {
			fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
				public boolean accept(File file) {
					if (file.getName().endsWith(fileEName[0])
							|| file.isDirectory()) {
						return true;
					}
					return false;
				}

				public String getDescription() {
					return fileEName[1];
				}
			});
		}
		fileChooser.showDialog(null, null);

		// Read Tsp data
		File file = fileChooser.getSelectedFile();
		if (file == null) {
			tspData = null;
		} else {
			ClearCommandArea();
			PrintCommandTitle("Open TSP File.");
			String tspFilepath = file.getAbsolutePath();
			tspData = new TspData();
			tspIO.InputTSP(tspFilepath, tspData);
			UpdateCommandArea(); // show command

			// Show TspData in Canvas
			canvasArea.SetTsp(tspData, null);
		}
	}
	
	// ******************** Evolutionary computing MENU **********************************
	// Use Gene Algorithm to solve TSP
	private void SolveGA() {
		// have not input data"
		if (tspData == null) {
			PrintCommandTitle("FAIL: Please open a TSP file first.");
			return;
		}
		// ------ Gene Algorithm -------------
		PrintCommandTitle("Solve TSP use Gene Algorithm.");
		// Find shortest cycle
		NeoGA gaSolver = new NeoGA();
		TspProblem problem = new TspProblem();
		problem.ReadProblem(tspData);
		TspCellFactory cellFactory = new TspCellFactory();
		cellFactory.setProblem(problem);
		gaSolver.setParams(cellFactory, 500, 1000, 1,5);

		//.SetParameter(5, 500, 10000, 1, 0.6, 0.1);
		//TspSolution solution = gaSolver.SolveTspGA(tspData);
		TspCell bestCell = (TspCell)gaSolver.perform();
		TspSolution solution = new TspSolution();
		solution.path = bestCell.encodeGeneToPath();
		solution.pathcost = bestCell.computeTravelLength(solution.path);

		// Output TSP solution
		tspIO.OutputTSP(solution);
		UpdateCommandArea();
		// Show Tsp Solution in Canvas
		canvasArea.SetTsp(tspData, solution);
		
	}
	
	// Gene Algorithm with Wisdom of Crowd
//	private void SolveGACrowd(){
//
//// ---------- 1 Test open file --------------
//		// have not input data"
////		if (tspData == null) {
////			PrintCommandTitle("FAIL: Please open a TSP file first.");
////			return;
////		}
//
////--------- Test files --------------------
//		String[] files = new String[6];
//		files[0]= "./data/Random11.tsp";
//		files[1]= "./data/Random22.tsp";
//		files[2]= "./data/Random44.tsp";
//		files[3]= "./data/Random77.tsp";
//		files[4]= "./data/Random97.tsp";
//		files[5]= "./data/Random222.tsp";
//		if (tspData==null)
//			tspData = new TspData();
//
////-----------2 Test multiple file ------------------
////
//		for (int i=0;i<6;i++)
//		{
//			tspIO.InputTSP(files[i], tspData);
//			UpdateCommandArea(); // show command
//
//			// ------ GA Crowd -------------
//			PrintCommandTitle("Solve TSP use Gene Algorithm with Wisdom of Crowd.");
//			// Find shortest cycle
//			TspCrowdGA crowdGaSolver = new TspCrowdGA(m_cmdPrinter);
//			//TspSolution solution = crowdGaSolver.SolveTspCrowdGA(tspData);
//			TspSolution solution = crowdGaSolver.SolveTspCrowdGA(tspData);
//			// Output TSP solution
//			tspIO.OutputTSP(solution);
//			UpdateCommandArea();
//			// Show Tsp Solution in Canvas
//			canvasArea.SetTsp(tspData, solution);
//		}
//
		
		//----------3 Test one File Random22---------------------
//		tspIO.InputTSP(files[1], tspData);
//		UpdateCommandArea(); // show command
//	
//		// ------ GA Crowd -------------
//		PrintCommandTitle("Solve TSP use Gene Algorithm with Wisdom of Crowd.");
//		// Find shortest cycle
//		TspCrowdGA crowdGaSolver = new TspCrowdGA(m_cmdPrinter);
//		//TspSolution solution = crowdGaSolver.SolveTspCrowdGA(tspData);
//		TspSolution solution = crowdGaSolver.SolveTspCrowdGA(tspData);
//		// Output TSP solution
//		tspIO.OutputTSP(solution);
//		UpdateCommandArea();
//		// Show Tsp Solution in Canvas
//		canvasArea.SetTsp(tspData, solution);
//	}
	
	// ******************** HEURISTIC MENU **********************************
	// Use Greedy algorithm to solve TSP
//	private void SolveGreedy() {
//		// have not input data"
//		if (tspData == null) {
//			PrintCommandTitle("FAIL: Please open a TSP file first.");
//			return;
//		}
//		// set the start city
//		TspGuiGreedySetting setting = new TspGuiGreedySetting();
//		setting.setModal(true);
//		setting.setVisible(true);
//
//		if (setting.startCity <= 0) // not valid, or user canceled.
//			return;
//		startCity = setting.startCity;
//
//		// ------ Greedy -------------
//		PrintCommandTitle("Solve TSP use Greedy.");
//		PrintCommandln("start city=" + startCity);
//		// Find shortest cycle
//		TspGreedy greedySolver = new TspGreedy();
//		TspSolution solution = greedySolver.SolveTspGreedy(tspData, startCity);
//		// Output TSP solution
//		tspIO.OutputTSP(solution);
//		UpdateCommandArea();
//		// Show Tsp Solution in Canvas
//		canvasArea.SetTsp(tspData, solution);
//	}

	// ******************* HELP MENU ******************************************
	private void Inform() {
		String s = "A.I Project. Traveling Salesman Problem \n"
				+ "Solve an instance of a TSP problem by different algorithm.\n"
				+ "Data for each problem will be supplied in a .tsp file (a plain text file).\n";

		JOptionPane.showMessageDialog(this, s, "Project Info",
				JOptionPane.PLAIN_MESSAGE);
	}

	private void Help() {
		String s = "This is a GUI program for A.I. Project."
				+ "Now Please run \"TspGuiMain\" as the main program.\n"
				+ "1. Choose menu \"File\"->\"Open\", load a TSP file.\n"
				+ "2. Choose menu \"Heuristic\" --> \"Greedy\", use greedy strategy to solve this TSP.\n"
				+ "3. See the solution graph on the top, and the output in \"command box\" below.\n"
				+ "4. Choose menu \"Evolutionary computing\" --> \"Gene Algorithm\", to see the solution by Gene Algorithm.\n";
		JOptionPane.showMessageDialog(this, s, "Project Info",
				JOptionPane.PLAIN_MESSAGE);
	}
}
