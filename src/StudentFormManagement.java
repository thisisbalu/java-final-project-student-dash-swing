import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JTextField;
import com.toedter.calendar.JDateChooser;

import net.proteanit.sql.DbUtils;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;

/**
 * 
 * @author Bala Subramanyam Lanka
 * Student ID: 2014356
 *
 */

public class StudentFormManagement {

	private JFrame frame;
	private JTextField txtStudentName;
	private JTextField txtID;
	private JTextField txtFees;
	private JTextField txtSearch;
	private JTable table;
	private JDateChooser dateChooser;
	Connection connection = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StudentFormManagement window = new StudentFormManagement();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void refreshTable() {
		try {
			String query = "select * from StudentInfo";
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
			pst.close();
			rs.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

	}

	public void clearTxtFields() {
		txtID.setText(null);
		txtStudentName.setText(null);
		txtFees.setText(null);
		dateChooser.setDate(null);
	}

	/**
	 * Create the application.
	 */
	public StudentFormManagement() {
		initialize();
		connection = SqlLiteConnection.dbConnector();
		refreshTable();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.YELLOW);
		frame.setBounds(100, 100, 862, 596);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblTitle = new JLabel("LaSalle College - Student Dashboard");
		lblTitle.setForeground(Color.RED);
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 32));
		lblTitle.setBounds(121, 11, 599, 56);
		frame.getContentPane().add(lblTitle);

		JLabel lblSubTitle = new JLabel("International School - Montreal Canada");
		lblSubTitle.setFont(new Font("Tahoma", Font.BOLD, 22));
		lblSubTitle.setBounds(206, 60, 446, 38);
		frame.getContentPane().add(lblSubTitle);

		JLabel lblLogoImage = new JLabel("New label");
		lblLogoImage.setFont(new Font("Tahoma", Font.PLAIN, 7));
		lblLogoImage.setIcon(new ImageIcon(StudentFormManagement.class.getResource("/download.png")));
		lblLogoImage.setBounds(27, 109, 182, 183);
		frame.getContentPane().add(lblLogoImage);

		JLabel lblStudentInfo = new JLabel("Student Information");
		lblStudentInfo.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblStudentInfo.setBounds(338, 93, 192, 31);
		frame.getContentPane().add(lblStudentInfo);

		JLabel lblID = new JLabel("ID");
		lblID.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblID.setBounds(296, 153, 117, 24);
		frame.getContentPane().add(lblID);

		JLabel lblStudentName = new JLabel("Student Name");
		lblStudentName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblStudentName.setBounds(296, 201, 117, 24);
		frame.getContentPane().add(lblStudentName);

		JLabel lblFees = new JLabel("Fees");
		lblFees.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblFees.setBounds(296, 252, 117, 24);
		frame.getContentPane().add(lblFees);

		JLabel lblDateOfBirth = new JLabel("Date of Birth");
		lblDateOfBirth.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDateOfBirth.setBounds(296, 303, 117, 24);
		frame.getContentPane().add(lblDateOfBirth);

		txtStudentName = new JTextField();
		txtStudentName.setBounds(409, 205, 159, 20);
		frame.getContentPane().add(txtStudentName);
		txtStudentName.setColumns(10);

		txtID = new JTextField();
		txtID.setColumns(10);
		txtID.setBounds(409, 157, 159, 20);
		frame.getContentPane().add(txtID);

		txtFees = new JTextField();
		txtFees.setColumns(10);
		txtFees.setBounds(409, 256, 159, 20);
		frame.getContentPane().add(txtFees);

		dateChooser = new JDateChooser();
		dateChooser.setBounds(409, 307, 159, 20);
		frame.getContentPane().add(dateChooser);

		JButton btnInsert = new JButton("Insert");
		btnInsert.setIcon(new ImageIcon(StudentFormManagement.class.getResource("/button_blue_add.png")));
		btnInsert.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnInsert.setBounds(654, 183, 140, 56);
		btnInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String query = "insert into StudentInfo (ID, Name, Fees, DateOfBirth) values (?,?,?,?)";
					PreparedStatement pst = connection.prepareStatement(query);
					pst.setInt(1, Integer.parseInt(txtID.getText()));
					pst.setString(2, txtStudentName.getText());
					pst.setString(3, txtFees.getText());
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
					String stringDate = sdf.format(dateChooser.getDate());
					pst.setString(4, stringDate);
					pst.execute();
					JOptionPane.showMessageDialog(null, "Student Inserted");
					pst.close();
				} catch (SQLException e1) {
					if (e1.getMessage().contains("SQLITE_CONSTRAINT")) {
						JOptionPane.showMessageDialog(null, "Student ID already exists");
					}
					e1.printStackTrace();
				}
				refreshTable();
			}
		});
		frame.getContentPane().add(btnInsert);

		JButton btnUpdate = new JButton("Update");
		btnUpdate.setIcon(new ImageIcon(StudentFormManagement.class.getResource("/button_red_stop.png")));
		btnUpdate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnUpdate.setBounds(654, 233, 140, 56);
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
					String stringDate = sdf.format(dateChooser.getDate());
					String query = "update StudentInfo set ID='" + txtID.getText() + "', Name ='"
							+ txtStudentName.getText() + "', Fees='" + txtFees.getText() + "', DateOfBirth='"
							+ stringDate + "' where ID='" + txtID.getText() + "'";
					PreparedStatement pst = connection.prepareStatement(query);
					pst.execute();
					JOptionPane.showMessageDialog(null, "Student Updated");
					pst.close();

				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				refreshTable();
			}
		});
		frame.getContentPane().add(btnUpdate);

		JButton btnDelete = new JButton("Delete");
		btnDelete.setIcon(new ImageIcon(StudentFormManagement.class.getResource("/button_pink_close.png")));
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnDelete.setBounds(654, 287, 140, 56);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String query = "delete from StudentInfo where ID='" + txtID.getText() + "'";
					PreparedStatement pst = connection.prepareStatement(query);
					pst.execute();
					JOptionPane.showMessageDialog(null, "Student Removed");
					pst.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				refreshTable();
			}
		});
		frame.getContentPane().add(btnDelete);

		JButton btnNew = new JButton("New");
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearTxtFields();
			}
		});
		btnNew.setIcon(new ImageIcon(StudentFormManagement.class.getResource("/button_violet_delete.png")));
		btnNew.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnNew.setBounds(654, 130, 140, 56);
		frame.getContentPane().add(btnNew);

		JLabel lblSearch = new JLabel("Search by Student Name : ");
		lblSearch.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblSearch.setBounds(137, 370, 220, 38);
		frame.getContentPane().add(lblSearch);

		txtSearch = new JTextField();
		txtSearch.setBounds(366, 370, 237, 38);
		frame.getContentPane().add(txtSearch);
		txtSearch.setColumns(10);
		txtSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					if (txtSearch.getText().isEmpty()) {
						refreshTable();
						return;
					}
					String query = "select * from StudentInfo where Name = ?";
					PreparedStatement pst = connection.prepareStatement(query);
					pst.setString(1, txtSearch.getText());
					ResultSet rs = pst.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(rs));
					pst.close();
					rs.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(95, 424, 644, 105);
		frame.getContentPane().add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				try {
					int row = table.getSelectedRow();
					String ID = (table.getModel().getValueAt(row, 0)).toString();
					String query = "select * from StudentInfo where ID='" + ID + "'";
					PreparedStatement pst = connection.prepareStatement(query);
					ResultSet rs = pst.executeQuery();
					while (rs.next()) {

						txtID.setText(rs.getString("ID"));
						txtStudentName.setText(rs.getString("Name"));
						txtFees.setText(rs.getString("Fees"));
						try {
							Date date = new SimpleDateFormat("dd-MM-yyyy").parse(rs.getString("DateOfBirth"));
							dateChooser.setDate(date);
						} catch (ParseException e1) {
							e1.printStackTrace();
							System.out.print("Error while parsing date");
						}
					}
					pst.close();
					rs.close();

				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
}
