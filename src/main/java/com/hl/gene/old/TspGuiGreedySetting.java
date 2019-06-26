package com.hl.gene.old;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * The Dialog of Settings of greedy algorithm
 * @author hl
 *
 */
public class TspGuiGreedySetting extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtStartCity;

	public int startCity = -1;//if it is <0, then means user CANCELED this dialog
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			TspGuiGreedySetting dialog = new TspGuiGreedySetting();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public TspGuiGreedySetting() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JLabel lblStartCity = new JLabel("start city");
			contentPanel.add(lblStartCity);
		}
		{
			txtStartCity = new JTextField();
			txtStartCity.setText("1");
			contentPanel.add(txtStartCity);
			txtStartCity.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						OKDialog();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						CancelDialog();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
//*************** My code ****************************************
	private void OKDialog()
	{
		String strStartCity = txtStartCity.getText();
		startCity = Integer.parseInt(strStartCity);
		this.setVisible(false);
	}
	
	private void CancelDialog()
	{
		startCity = -1;
		this.setVisible(false);
	}

}
