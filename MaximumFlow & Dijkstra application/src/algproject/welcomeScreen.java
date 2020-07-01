package algproject;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;

public class welcomeScreen {

	private JFrame frame;
	private JTable table;
	static welcomeScreen window;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new welcomeScreen();
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
	public welcomeScreen() {
		initialize();
		DefaultTableModel model= (DefaultTableModel)table.getModel();
		model.addRow(new Object[] {"Abdelrahman Hesham Shaker",20170152});
		model.addRow(new Object[] {"Tarek Alaa Ali",20170134});		
		model.addRow(new Object[] {"Eslam Saleh Mohammed",20170046});
		model.addRow(new Object[] {"Eslam Mohammed Mahmoud",20170049});
		model.addRow(new Object[] {"Thoria Hamdy El-Sayed",20170082});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 895, 443);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("Start");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main m=new main();
				m.getFrame().setVisible(true);
				window.frame.setVisible(false);
			}
		});
		btnNewButton.setBounds(322, 367, 178, 29);
		frame.getContentPane().add(btnNewButton);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(211, 188, 408, 103);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setFont(new Font("Tahoma", Font.BOLD, 13));
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Names", "IDs"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Integer.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, true
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		scrollPane.setViewportView(table);
		
		JLabel lblNewLabel = new JLabel("Submitted by:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel.setBounds(201, 165, 259, 20);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon("Untitled1.png"));
		label.setBounds(10, 10, 93, 132);
		frame.getContentPane().add(label);
		
		JLabel label1 = new JLabel("");
		label1.setIcon(new ImageIcon("Untitled.png"));
		label1.setBounds(719, 10, 152, 132);
		frame.getContentPane().add(label1);
		
		JLabel lblNewLabel_1 = new JLabel("Supervised by:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_1.setBounds(201, 301, 133, 20);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Dr. Eng. Moustafa Reda AbdALLAH Eltantawi");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(211, 324, 408, 20);
		frame.getContentPane().add(lblNewLabel_2);

		JLabel lblNewLabel_4 = new JLabel("Analysis and Design of Algorithms");
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel_4.setBounds(191, 10, 457, 45);
		frame.getContentPane().add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("Final Project     June 2020");
		lblNewLabel_5.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel_5.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5.setBounds(191, 55, 457, 45);
		frame.getContentPane().add(lblNewLabel_5);

		JLabel lblNewLabel_6 = new JLabel("Graph Manipulation");
		lblNewLabel_6.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_6.setFont(new Font("Tahoma", Font.BOLD, 40));
		lblNewLabel_6.setBounds(191, 110, 457, 45);
		frame.getContentPane().add(lblNewLabel_6);

		JLabel lblNewLabel_3 = new JLabel("");
		lblNewLabel_3.setIcon(new ImageIcon("Untitled2.png"));
		lblNewLabel_3.setBounds(0, 0, 931, 545);
		frame.getContentPane().add(lblNewLabel_3);
		
		
		
	}
}
