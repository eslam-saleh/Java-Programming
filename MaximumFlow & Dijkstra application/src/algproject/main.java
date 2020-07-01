package algproject;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.Checkbox;
import java.awt.Choice;
import javax.swing.JCheckBox;


public class main {
	ArrayList<node>n;
	ArrayList<edge>e;
	private JFrame frame;
	private JTable table_1;
	private JTable table;
	private JTextField textField;
	private JTextField textField_3;
	Checkbox checkbox,checkBox;
	int id;
	Choice choice,choice_1,choice_2,choice_3;
	static Random random = new Random(100);

	/**
	 * Create the application.
	 */
	public main() {
		initialize();
		n=new ArrayList<node>();
		e=new ArrayList<edge>();
		id=0;
	}

	/**
	 * Initialize the contents of the frame.
	 */

    
	private void initialize() {
		setFrame(new JFrame());
		getFrame().getContentPane().setFont(new Font("Arial", Font.BOLD, 13));
		getFrame().setBounds(100, 100, 918, 634);
		getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getFrame().getContentPane().setLayout(null);

		JButton btnNewButton = new JButton("Add vertex");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addVertex();
			}
		});
		btnNewButton.setFont(new Font("Arial", Font.BOLD, 15));
		btnNewButton.setBounds(326, 101, 136, 42);
		getFrame().getContentPane().add(btnNewButton);

		JButton btnAddEdge = new JButton("Add edge");
		btnAddEdge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer Weight;
				String From=choice_1.getSelectedItem(),To=choice.getSelectedItem();
				try {
					Weight=Integer.parseInt(textField_3.getText());
				}
				catch(Exception x) {
					JOptionPane.showMessageDialog(null, "please enter valid number in Weight.");
					return;
				}
				if(Weight<0)
					JOptionPane.showMessageDialog(null, "please enter valid number in Weight.");
				for (int i = 0; i < table_1.getRowCount(); i++) {
					String Fromcheck=table_1.getValueAt(i, 0).toString(),Tocheck=table_1.getValueAt(i, 1).toString();
					boolean direc=Boolean.parseBoolean(table_1.getValueAt(i, 3).toString());
					if((From.equals(Fromcheck)&&To.equals(Tocheck)&&direc==false)||(From.equals(Tocheck)&&To.equals(Fromcheck)&&direc==false)) {
						JOptionPane.showMessageDialog(null, "edge already exist");
						return;
					}
					else if(From.equals(Fromcheck)&&To.equals(Tocheck)&&direc==true) {
						if(checkbox.getState()) {
							JOptionPane.showMessageDialog(null, "edge already exist");
							return;
						}
						else {
							for (int j = 0; j < table_1.getRowCount(); j++) {
								Fromcheck=table_1.getValueAt(j, 0).toString();Tocheck=table_1.getValueAt(j, 1).toString();
								direc=Boolean.parseBoolean(table_1.getValueAt(j, 3).toString());
								if(From.equals(Tocheck)&&To.equals(Fromcheck)&&direc==true) {
									JOptionPane.showMessageDialog(null, "edge already exist");
									return;
								}
							}
							DefaultTableModel model= (DefaultTableModel)table_1.getModel();
							model.addRow(new Object[] {(To),(From),(Integer.parseInt(textField_3.getText())),(true)});
							table_1.setValueAt(Integer.parseInt(textField_3.getText()), i, 2);
							return;
						}
					}
				}
				DefaultTableModel model= (DefaultTableModel)table_1.getModel();
				model.addRow(new Object[] {(From),(To),(Integer.parseInt(textField_3.getText())),(checkbox.getState())});
			}
		});
		btnAddEdge.setFont(new Font("Arial", Font.BOLD, 15));
		btnAddEdge.setBounds(326, 385, 419, 42);
		getFrame().getContentPane().add(btnAddEdge);

		JButton btnDelvertex = new JButton("Delete vertex");
		btnDelvertex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i=table.getSelectedRow();
				DefaultTableModel model= (DefaultTableModel)table.getModel();
				if(i>=0) {
					String ver=model.getValueAt(i, 0).toString();
					choice.remove(ver);
					choice_1.remove(ver);
					choice_2.remove(ver);
					choice_3.remove(ver);
					model.removeRow(i);
					//delete edges
					model= (DefaultTableModel)table_1.getModel();
					for (int j = 0; j < table_1.getRowCount(); j++) {
						if(model.getValueAt(j, 0).toString().equals(ver)||model.getValueAt(j, 1).toString().equals(ver)) {
							model.removeRow(j);
							j--;
						}
					}
				}
				else
					JOptionPane.showMessageDialog(null, "please select a vertex to delete.");
			}
		});
		btnDelvertex.setFont(new Font("Arial", Font.BOLD, 15));
		btnDelvertex.setBounds(326, 153, 136, 42);
		getFrame().getContentPane().add(btnDelvertex);
		
		JButton btnDij = new JButton("Dijkstra");
		btnDij.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main.this.n=new ArrayList<node>();
				for (int i = 0; i < table.getRowCount() ; i++) {
					n.add(new node(i, table.getValueAt(i, 0).toString(), n, table.getRowCount()));
				}
				main.this.e=new ArrayList<edge>();
				for (int i = 0; i < table_1.getRowCount() ; i++) {
					node n1=null,n2=null;
					for (int j = 0; j < n.size(); j++) {
						if(n.get(j).name.equals(table_1.getValueAt(i, 0))) {
							n1=n.get(j);
							break;
						}
					}
					for (int j = 0; j < n.size(); j++) {
						if(n.get(j).name.equals(table_1.getValueAt(i, 1))) {
							n2=n.get(j);
							break;
						}
					}
					boolean dir =Boolean.parseBoolean(table_1.getValueAt(i, 3).toString());
					main.this.e.add(new edge(n1, n2, Integer.parseInt(table_1.getValueAt(i, 2).toString()),dir));
				}
				node src=null,Dist=null;
				for (int i = 0; i < n.size(); i++) {
					if(n.get(i).name.equals(choice_2.getSelectedItem())) {
						src=n.get(i);
						break;
					}
				}
				for (int i = 0; i < n.size(); i++) {
					if(n.get(i).name.equals(choice_3.getSelectedItem())) {
						Dist=n.get(i);
						break;
					}
				}
				for (int i = 0; i < n.size(); i++) {
					n.get(i).id=i;
				}
				try {
					Dijkstra x=new Dijkstra(main.this.n,main.this.e,src,Dist,checkBox.getState());
                    x.setVisible(true);
                }
                catch(Exception x) {
                    JOptionPane.showMessageDialog(null, "No path between these nodes.");
                    return;
                }
			}
		});
		btnDij.setFont(new Font("Arial", Font.BOLD, 15));
		btnDij.setBounds(536, 193, 209, 42);
		getFrame().getContentPane().add(btnDij);
		
		JButton btnMaximumFlow = new JButton("Maximum Flow");
		btnMaximumFlow.setFont(new Font("Arial", Font.BOLD, 15));
		btnMaximumFlow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main.this.n=new ArrayList<node>();
				for (int i = 0; i < table.getRowCount() ; i++) {
					n.add(new node(i, table.getValueAt(i, 0).toString(), n, table.getRowCount()));
				}
				main.this.e=new ArrayList<edge>();
				for (int i = 0; i < table_1.getRowCount() ; i++) {
					node n1=null,n2=null;
					for (int j = 0; j < n.size(); j++) {
						if(n.get(j).name.equals(table_1.getValueAt(i, 0))) {
							n1=n.get(j);
							break;
						}
					}
					for (int j = 0; j < n.size(); j++) {
						if(n.get(j).name.equals(table_1.getValueAt(i, 1))) {
							n2=n.get(j);
							break;
						}
					}
					boolean dir =Boolean.parseBoolean(table_1.getValueAt(i, 3).toString());
					main.this.e.add(new edge(n1, n2, Integer.parseInt(table_1.getValueAt(i, 2).toString()),dir));
				}
				node s=null,t=null;
				for (int i = 0; i < n.size(); i++) {
					if(n.get(i).name.equals(choice_2.getSelectedItem())) {
						s=n.get(i);
						break;
					}
				}
				for (int i = 0; i < n.size(); i++) {
					if(n.get(i).name.equals(choice_3.getSelectedItem())) {
						t=n.get(i);
						break;
					}
				}
				MaximumFlow x=new MaximumFlow(main.this.n,main.this.e,s,t,checkBox.getState());
				x.setVisible(true);
			}
		});
		btnMaximumFlow.setBounds(536, 245, 209, 42);
		getFrame().getContentPane().add(btnMaximumFlow);
		
		JButton btnDeleteEdge = new JButton("Delete edge");
		btnDeleteEdge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i=table_1.getSelectedRow();
				DefaultTableModel model= (DefaultTableModel)table_1.getModel();
				if(i>=0) {
					model.removeRow(i);
				}
				else
					JOptionPane.showMessageDialog(null, "please select row to delete.");
			}
		});
		btnDeleteEdge.setFont(new Font("Arial", Font.BOLD, 15));
		btnDeleteEdge.setBounds(326, 441, 419, 42);
		getFrame().getContentPane().add(btnDeleteEdge);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 332, 297, 238);
		getFrame().getContentPane().add(scrollPane);
		
		table_1 = new JTable();
		scrollPane.setViewportView(table_1);
		table_1.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"From", "To", "Wieght", "Directed"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, Integer.class, Boolean.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				true, true, true, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(14, 51, 297, 237);
		getFrame().getContentPane().add(scrollPane_1);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Vertex Name"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		scrollPane_1.setViewportView(table);
		
		Label label = new Label("vertices");
		label.setFont(new Font("Arial", Font.BOLD, 15));
		label.setAlignment(Label.CENTER);
		label.setBounds(76, 10, 136, 35);
		getFrame().getContentPane().add(label);
		
		Label label_1 = new Label("edges");
		label_1.setFont(new Font("Arial", Font.BOLD, 15));
		label_1.setAlignment(Label.CENTER);
		label_1.setBounds(76, 294, 136, 35);
		getFrame().getContentPane().add(label_1);
		
		textField = new JTextField(10);
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
					addVertex();
			}
		});
		textField.requestFocus();
		textField.setBounds(326, 76, 136, 19);
		getFrame().getContentPane().add(textField);
		
		JLabel lblNewLabel = new JLabel("Vertex Name");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 13));
		lblNewLabel.setBounds(326, 58, 136, 13);
		getFrame().getContentPane().add(lblNewLabel);
		
		textField_3 = new JTextField(10);
		textField_3.setBounds(538, 356, 96, 21);
		getFrame().getContentPane().add(textField_3);
		
		JLabel lblFr = new JLabel("From");
		lblFr.setHorizontalAlignment(SwingConstants.CENTER);
		lblFr.setFont(new Font("Arial", Font.BOLD, 13));
		lblFr.setBounds(326, 333, 96, 13);
		getFrame().getContentPane().add(lblFr);
		
		JLabel lblTo = new JLabel("To");
		lblTo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTo.setFont(new Font("Arial", Font.BOLD, 13));
		lblTo.setBounds(432, 332, 96, 13);
		getFrame().getContentPane().add(lblTo);
		
		JLabel lblWeight = new JLabel("Weight");
		lblWeight.setHorizontalAlignment(SwingConstants.CENTER);
		lblWeight.setFont(new Font("Arial", Font.BOLD, 13));
		lblWeight.setBounds(538, 332, 96, 13);
		getFrame().getContentPane().add(lblWeight);
		
		checkbox = new Checkbox("Directed");
		checkbox.setFont(new Font("Arial", Font.BOLD, 13));
		checkbox.setBounds(640, 356, 105, 21);
		getFrame().getContentPane().add(checkbox);
		
		choice = new Choice();
		choice.setBounds(432, 356, 96, 21);
		getFrame().getContentPane().add(choice);
		
		choice_1 = new Choice();
		choice_1.setBounds(326, 356, 96, 21);
		getFrame().getContentPane().add(choice_1);
		
		choice_2 = new Choice();
		choice_2.setBounds(538, 153, 96, 19);
		getFrame().getContentPane().add(choice_2);
		
		choice_3 = new Choice();
		choice_3.setBounds(640, 153, 96, 21);
		getFrame().getContentPane().add(choice_3);
		
		JLabel lblStartNode = new JLabel("start node");
		lblStartNode.setHorizontalAlignment(SwingConstants.CENTER);
		lblStartNode.setFont(new Font("Arial", Font.BOLD, 13));
		lblStartNode.setBounds(536, 130, 96, 13);
		getFrame().getContentPane().add(lblStartNode);
		
		JLabel lblEndNode = new JLabel("end node");
		lblEndNode.setHorizontalAlignment(SwingConstants.CENTER);
		lblEndNode.setFont(new Font("Arial", Font.BOLD, 13));
		lblEndNode.setBounds(640, 130, 96, 13);
		getFrame().getContentPane().add(lblEndNode);
		
		checkBox = new Checkbox("Step by Step");
		checkBox.setFont(new Font("Arial", Font.BOLD, 13));
		checkBox.setBounds(751, 227, 115, 21);
		getFrame().getContentPane().add(checkBox);
		
		JButton btnNewButton_1 = new JButton("Reset");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel model;
				String ver;
				int table0=table.getRowCount(),table1=table_1.getRowCount();
				model= (DefaultTableModel)table.getModel();
				for (int j = 0; j < table0; j++) {
					ver=model.getValueAt(0, 0).toString();
					choice.remove(ver);
					choice_1.remove(ver);
					choice_2.remove(ver);
					choice_3.remove(ver);
					model.removeRow(0);
				}
				model= (DefaultTableModel)table_1.getModel();
				for (int j = 0; j < table1; j++) {
					model.removeRow(0);
				}				
			}
		});
		btnNewButton_1.setFont(new Font("Arial", Font.BOLD, 15));
		btnNewButton_1.setBounds(685, 552, 209, 35);
		frame.getContentPane().add(btnNewButton_1);

		getFrame().addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e){
				textField.requestFocus();
			}
		} );
	}

	private void addVertex() {
		if(textField.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "please write Vertex name to add.");
			textField.requestFocus();
			return;
		}
		int rowsCount= table.getRowCount();
		for(int i=0;i<rowsCount;i++) {
			if(table.getValueAt(i, 0).toString().equals(textField.getText())) {
				JOptionPane.showMessageDialog(null, "Vertex already exist.");
				textField.setText("");
				textField.requestFocus();
				return;
			}
		}
		DefaultTableModel model= (DefaultTableModel)table.getModel();
		model.addRow(new Object[] {(textField.getText())});
		choice.add(textField.getText());
		choice_1.add(textField.getText());
		choice_2.add(textField.getText());
		choice_3.add(textField.getText());
		textField.setText("");
		textField.requestFocus();
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
}
