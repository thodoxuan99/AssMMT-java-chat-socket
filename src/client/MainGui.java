package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.text.StyledEditorKit.ForegroundAction;
import javax.swing.JLabel;

import javax.swing.DefaultListModel;
import javax.swing.JButton;

import tags.Tags;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

import java.awt.Color;
import javax.swing.JList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.SystemColor;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.ImageIcon;
import javax.swing.border.EtchedBorder;
import javax.swing.border.MatteBorder;

public class MainGui {

	private Client clientNode;
	private static String IPClient = "", nameUser = "", dataUser = "";
	private static int portClient = 0;
	private JFrame frameMainGui;
	private JTextField txtNameFriend;
	private JButton btnChat, btnExit;
	private JLabel lblLogo;
	private JLabel lblActiveNow;
	private static JList<String> listActive;
	private static JList<String> listFriend;
	private static JList<String> listState;
	private static JList<String> listRequestFr;
	
	static DefaultListModel<String> modelActive = new DefaultListModel<>();
	static DefaultListModel<String> modelFriend = new DefaultListModel<>();
	static DefaultListModel<String> modelState = new DefaultListModel<>();
	static DefaultListModel<String> modelRequestFr = new DefaultListModel<>();
	private JLabel lblNewLabel_1;
	private JButton btnNewButton;
	private JButton btnNewButton_1;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGui window = new MainGui();
					window.frameMainGui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainGui(String arg, int arg1, String name, String msg) throws Exception {
		IPClient = arg;
		portClient = arg1;
		nameUser = name;
		dataUser = msg;
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
		} catch(Exception e) {
			  System.out.println("Error setting native LAF: " + e);
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGui window = new MainGui();
					window.frameMainGui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainGui() throws Exception {
		initialize();
		clientNode = new Client(IPClient, portClient, nameUser, dataUser);
	}

	public static void updateActiveMainGui(String msg) {
		modelActive.addElement(msg);
	}
	public static void updateRequestFrMainGui(String msg) {
		modelRequestFr.addElement(msg);
	}
	
	public static void updateFriendMainGui(String msg,boolean onl) {
		if(onl) {
			modelFriend.addElement(msg);
			modelState.addElement("Online");
		}else {
			modelFriend.addElement(msg);
			modelState.addElement("Offline");
		}
		
		
	}
	
	
	public static void resetList() {
		modelFriend.clear();
		modelActive.clear();
		modelState.clear();
		modelRequestFr.clear();
	} 
	
	private void initialize() {
		frameMainGui = new JFrame();
		frameMainGui.setTitle("Menu Chat");
		frameMainGui.setResizable(false);
		frameMainGui.setBounds(100, 100, 500, 560);
		frameMainGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameMainGui.getContentPane().setLayout(null);

		JLabel lblHello = new JLabel("Welcome");
		lblHello.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblHello.setBounds(12, 82, 70, 16);
		frameMainGui.getContentPane().add(lblHello); 


		JLabel lblFriendsName = new JLabel("Name Friend: ");
		lblFriendsName.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblFriendsName.setBounds(12, 425, 110, 16);
		frameMainGui.getContentPane().add(lblFriendsName);
		
		txtNameFriend = new JTextField("");
		txtNameFriend.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		txtNameFriend.setColumns(10);
		txtNameFriend.setBounds(100, 419, 384, 28);
		frameMainGui.getContentPane().add(txtNameFriend);

		btnChat = new JButton("");
		btnChat.setFont(new Font("Segoe UI", Font.PLAIN, 13));

		btnChat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				String name = txtNameFriend.getText();
				for (int i = 0; i < Client.clientarray.size(); i++) {
					if(Client.clientarray.get(i).getName().equals(name)) {
						if(!Client.clientarray.get(i).getState()) {
							Tags.show(frameMainGui, name+" Offine", false); 
							return;
						}
						break;
					}
				}
				if (name.equals("") || Client.clientarray == null) {
					Tags.show(frameMainGui, "Invaild username", false);
					return;
				}
				
				if (name.equals(nameUser)) {
					Tags.show(frameMainGui, "This software doesn't support chat yourself function", false);
					return;
				}
				int size = Client.clientarray.size();
				for (int i = 0; i < size; i++) {
					if (name.equals(Client.clientarray.get(i).getName())) {
						try { 
							
							clientNode.intialNewChat(Client.clientarray.get(i).getHost(),Client.clientarray.get(i).getPort(), name);
							return;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				Tags.show(frameMainGui, "Friend is not found. Please wait to update your list friend", false);
			}
		}); 
		btnChat.setBounds(10, 465, 51, 44);
		frameMainGui.getContentPane().add(btnChat);
		btnChat.setIcon(new javax.swing.ImageIcon(MainGui.class.getResource("/image/chat.png")));
		btnExit = new JButton(""); 
		btnExit.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int result = Tags.show(frameMainGui, "Are you sure ?", true);
				if (result == 0) {
					try {
						clientNode.exit();
						frameMainGui.dispose();
					} catch (Exception e) {
						frameMainGui.dispose(); 
					}
				}
			}
		});
		btnExit.setBounds(421, 465, 51, 44);
		btnExit.setIcon(new javax.swing.ImageIcon(MainGui.class.getResource("/image/stop.png")));
		frameMainGui.getContentPane().add(btnExit);
		
		lblLogo = new JLabel("CONNECT WITH EVERYONE IN THE WORLD");
		lblLogo.setForeground(new Color(0, 0, 205));
		lblLogo.setIcon(new javax.swing.ImageIcon(MainGui.class.getResource("/image/connect.png")));
		lblLogo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblLogo.setBounds(51, 13, 413, 38);
		frameMainGui.getContentPane().add(lblLogo);
		
		lblActiveNow = new JLabel("List Account Active Now");
		lblActiveNow.setForeground(SystemColor.textHighlight);
		lblActiveNow.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblActiveNow.setBounds(12, 123, 218, 16);
		frameMainGui.getContentPane().add(lblActiveNow);
		
		listActive = new JList<>(modelActive);
		listActive.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		listActive.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		listActive.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String value = (String)listActive.getModel().getElementAt(listActive.locationToIndex(arg0.getPoint()));
				char[] array=txtNameFriend.getText().toCharArray();
				if(!txtNameFriend.getText().equals("")) {
					if (array[array.length-1]!=',')
						txtNameFriend.setText(txtNameFriend.getText()+","+value+",");
					else txtNameFriend.setText(txtNameFriend.getText()+value+",");
				}	
				else txtNameFriend.setText(txtNameFriend.getText()+value);
			}
		});
		listActive.setBounds(12, 152, 218, 251);
		frameMainGui.getContentPane().add(listActive);
		
		JLabel lblNewLabel = new JLabel("List Friend");
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblNewLabel.setForeground(new Color(0, 120, 215));
		lblNewLabel.setBounds(240, 123, 224, 16);
		frameMainGui.getContentPane().add(lblNewLabel);
		
		listFriend = new JList<String>(modelFriend);
		listFriend.setBorder(new MatteBorder(1, 1, 1, 0, (Color) SystemColor.activeCaptionBorder));
		listFriend.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		listFriend.setBounds(240, 152, 123, 115);
		listFriend.addMouseListener(new MouseAdapter() { 
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String value = (String)listFriend.getModel().getElementAt(listFriend.locationToIndex(arg0.getPoint()));
				char[] array=txtNameFriend.getText().toCharArray();
				if(!txtNameFriend.getText().equals("")) {
					if (array[array.length-1]!=',')
						txtNameFriend.setText(txtNameFriend.getText()+","+value+",");
					else txtNameFriend.setText(txtNameFriend.getText()+value+",");
				}	
				else txtNameFriend.setText(txtNameFriend.getText()+value);
			} 
		});
		frameMainGui.getContentPane().add(listFriend);
		
		
		
		
	
		listState = new JList<String>(modelState);
		listState.setBorder(new MatteBorder(1, 0, 1, 1, (Color) SystemColor.activeCaptionBorder));
		listState.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		listState.setBounds(362, 152, 120, 115);
		frameMainGui.getContentPane().add(listState);
		
		lblNewLabel_1 = new JLabel("List Friend Request");
		lblNewLabel_1.setForeground(SystemColor.textHighlight);
		lblNewLabel_1.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblNewLabel_1.setBounds(240, 278, 244, 16);
		frameMainGui.getContentPane().add(lblNewLabel_1);
		
		listRequestFr = new JList<String>(modelRequestFr);
		listRequestFr.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		listRequestFr.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		listRequestFr.setBounds(240, 305, 244, 98);
		listRequestFr.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) { 
				String value = (String)listRequestFr.getModel().getElementAt(listRequestFr.locationToIndex(arg0.getPoint()));
				char[] array=txtNameFriend.getText().toCharArray();
				if(!txtNameFriend.getText().equals("")) {
					if (array[array.length-1]!=',')
						txtNameFriend.setText(txtNameFriend.getText()+","+value+",");
					else txtNameFriend.setText(txtNameFriend.getText()+value+",");
				}	
				else txtNameFriend.setText(txtNameFriend.getText()+value);
			}
		});
		frameMainGui.getContentPane().add(listRequestFr);
		
		btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String name = txtNameFriend.getText();
				if (name.equals("") || Client.clientarray == null) {
					Tags.show(frameMainGui, "Invaild username", false);
					return;
				}
				if (name.equals(nameUser)) {
					Tags.show(frameMainGui, "This software doesn't support add friend yourself function", false);
					return;
				}
				int size = Client.clientarray.size();
				for (int i = 0; i < size; i++) {
					if (name.equals(Client.clientarray.get(i).getName())) {
						try { 
							
							clientNode.addFriend(name);
							return;
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
				Tags.show(frameMainGui, "Friend is not found. Please wait to update your list friend", false);
			
			}
		});
		btnNewButton.setIcon(new ImageIcon(MainGui.class.getResource("/image/addFr.png")));
		btnNewButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnNewButton.setSelectedIcon(null);
		btnNewButton.setBounds(153, 465, 51, 44);
		frameMainGui.getContentPane().add(btnNewButton);
		
		btnNewButton_1 = new JButton("");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = txtNameFriend.getText();
				if (name.equals("") || Client.clientarray == null) {
					Tags.show(frameMainGui, "Please choose User", false);
					return;
				}
				if (name.equals(nameUser)) {
					Tags.show(frameMainGui, "This software doesn't support accept friend yourself function", false);
					return;
				} 
				int size = Client.clientarray.size();
				for (int i = 0; i < size; i++) {
					if (name.equals(Client.clientarray.get(i).getName())) {
						try { 
							
							clientNode.acceptFriend(name);
							return;
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
				Tags.show(frameMainGui, "Friend is not found. Please wait to update your list friend", false);
			
			
			}
		});
		btnNewButton_1.setIcon(new ImageIcon(MainGui.class.getResource("/image/accept.png")));
		btnNewButton_1.setBounds(286, 465, 51, 44);
		frameMainGui.getContentPane().add(btnNewButton_1);
		
		JLabel lblNewLabel_2 = new JLabel(nameUser);
		lblNewLabel_2.setForeground(SystemColor.textHighlight);
		lblNewLabel_2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblNewLabel_2.setBounds(76, 82, 70, 16);
		frameMainGui.getContentPane().add(lblNewLabel_2);
		
		
			
	}
		

	public static int request(String msg, boolean type) {
		JFrame frameMessage = new JFrame();
		return Tags.show(frameMessage, msg, type);
	}
}
