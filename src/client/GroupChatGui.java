package client;

import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JEditorPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Label;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JPanel;

import data.DataFile;
import tags.Decode;
import tags.Encode;
import tags.Tags;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;


public class GroupChatGui {

	private static String URL_DIR = System.getProperty("user.dir");
	private static String TEMP = "/temp/";

	private ChatRoom chat;
	private Socket socketChat; 
	private String nameUser = "",nameCreator="", nameGuest="",nameFile=""; 
	private ArrayList<String> ListIP=null,ListGuest=null;
	private ArrayList<Integer> ListPort=null;
	private int port=0;
	private JFrame frameChatGui;
	private JPanel panelMessage;
	private JTextPane txtDisplayChat;
	private Label textState, lblReceive;
	private JLabel lblGuest;
	private JButton btnDisConnect, btnSend, btnChoose;
	public boolean isStop = false, isReceiveFile = false;
	private JProgressBar progressSendFile;
	private JTextField txtPath;
	private int portServer = 0;
	private JTextField txtMessage;
	private JScrollPane scrollPane;
	private JButton btnSmileBigIcon;
	private JButton btnCryingIcon;
	private JButton btnSmileCryingIcon;
	private JButton btnHeartEyeIcon;
	private JButton buttonScaredIcon;
	private JButton buttonSadIcon;
	private JButton btnNewButton;

	public GroupChatGui(String user,int portt, String creatorName,  ArrayList<String>ListIPP, ArrayList<Integer>ListPortt, ArrayList<String>ListGuestt) {
		nameUser = user;
		this.nameCreator=creatorName;
		this.ListIP=ListIPP;
		this.ListGuest=ListGuestt;
		this.ListPort=ListPortt;
		this.port=portt;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GroupChatGui window = new GroupChatGui(nameUser,port, nameCreator, ListIP, ListPort,ListGuest,0); 
					window.frameChatGui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace(); 
				}
			}
		});
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GroupChatGui window = new GroupChatGui();
					window.frameChatGui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void updateChat_receive(String msg) {
		appendToPane(txtDisplayChat, "<div class='left' style='width: 40%; background-color: #f1f0f0;'>"+
				"<table class='bang' style='width: 100%;'>"
				+ "<tr align='left'>"
				+ "<td style='width: 10%;color: blue;'>" + nameGuest +"</td> "
				+ "<td style='width: 40%;'>" + msg +"</td>" 
				+ "<td style='width: 50%; '></td>"
				+"</tr>"
				+ "</table>"
				+"</div>");
	}

	public void updateChat_send(String msg) {
		appendToPane(txtDisplayChat, "<table class='bang' style='clear:both; width: 100%;'>"
				+ "<tr align='right'>"
				+ "<td style='width: 50%; '></td>"
				+ "<td style='width: 40%; background-color: #0084ff; color: white;'>" + msg +"</td>"
				+ "<td style='width: 10%;'>" + "You" +"</td> "
				+"</tr>"
				+ "</table>");
	}
	
	public void updateChat_notify(String msg) {
		appendToPane(txtDisplayChat, "<table class='bang' style='color: white; clear:both; width: 100%;'>"
				+ "<tr align='right'>"
				+ "<td style='width: 50%; '></td>"
				+ "<td style='width: 40%; background-color: #f1c40f;'>" + msg +"</td>" 
				+ "<td style='width: 40%;'>" + "You" +"</td>" 
				+"</tr>"
				+ "</table>");
	}

	
	public void updateChat_send_Symbol(String msg) {
		appendToPane(txtDisplayChat, "<table style='width: 100%;'>"
				+ "<tr align='right'>"
				+ "<td style='width: 50%;'></td>"
				+ "<td style='width: 40%;background-color: #0084ff;'>" + msg +"</td> "
				+ "<td style='width: 10%;'>" + "You" +"</td> "
				+ "</tr>"
				+ "</table>");
	}
	
	public GroupChatGui() {
		initialize();
	}

	public GroupChatGui(String user,int port, String creatorName, ArrayList<String>ListIP, ArrayList<Integer>ListPort, ArrayList<String>ListGuest,int a)
			throws Exception {
		nameUser = user;
		this.nameCreator=creatorName;
		this.ListIP=ListIP;
		this.ListGuest=ListGuest;
		this.ListPort=ListPort;
		this.port=port;
		ServerSocket socketserver = new ServerSocket(port);
		ArrayList<Socket> connect=new ArrayList<Socket>();
		Socket temp;
		for (int i = 0; i < ListIP.size(); i++) {
			if(!ListGuest.get(i).equals(nameUser)) {
				temp=new Socket(InetAddress.getByName(ListIP.get(i)),ListPort.get(i));
				connect.add(temp);
			}
		}
		initialize();
		chat = new ChatRoom(connect,socketserver, nameUser, ListGuest); 
		chat.start();
	}

	private void initialize() {
		File fileTemp = new File(URL_DIR + "/temp");
		if (!fileTemp.exists()) {
			fileTemp.mkdirs();
		}
		frameChatGui = new JFrame();
		frameChatGui.setTitle("Private Chat");
		frameChatGui.setResizable(false);
		frameChatGui.setBounds(200, 200, 673, 645);
		frameChatGui.getContentPane().setLayout(null);
		frameChatGui.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		JLabel lblClientIP = new JLabel(nameUser);
		lblClientIP.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		lblClientIP.setBounds(30, 6, 113, 40);
		lblClientIP.setIcon(new javax.swing.ImageIcon(GroupChatGui.class.getResource("/image/user_chat.png")));
		frameChatGui.getContentPane().add(lblClientIP);

		panelMessage = new JPanel();
		panelMessage.setBounds(6, 363, 649, 201);
		panelMessage.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Message"));
		frameChatGui.getContentPane().add(panelMessage);
		panelMessage.setLayout(null);

		txtMessage = new JTextField("");
		txtMessage.setBounds(10, 21, 479, 62);
		panelMessage.add(txtMessage);
		txtMessage.setColumns(10);

		btnSend = new JButton("");
		btnSend.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnSend.setBounds(551, 33, 65, 39);
		btnSend.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnSend.setContentAreaFilled(false);
		panelMessage.add(btnSend);
		btnSend.setIcon(new ImageIcon(GroupChatGui.class.getResource("/image/send.png")));
		
		btnChoose = new JButton("");
		btnChoose.setBounds(551, 152, 50, 36);
		panelMessage.add(btnChoose);
		btnChoose.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnChoose.setIcon(new javax.swing.ImageIcon(GroupChatGui.class.getResource("/image/attachment.png")));
		btnChoose.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System
						.getProperty("user.home")));
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = fileChooser.showOpenDialog(frameChatGui);
				if (result == JFileChooser.APPROVE_OPTION) {
					String path_send = (fileChooser.getSelectedFile()
							.getAbsolutePath()) ;
					System.out.println(path_send);
					nameFile = fileChooser.getSelectedFile().getName();
					txtPath.setText(path_send);
				}
			}
		});
		btnChoose.setBorder(BorderFactory.createEmptyBorder());
		btnChoose.setContentAreaFilled(false);
		
		txtPath = new JTextField("");
		txtPath.setBounds(76, 163, 433, 25);
		panelMessage.add(txtPath);
		txtPath.setEditable(false);
		txtPath.setColumns(10);
				
				
		Label label = new Label("Path");
		label.setBounds(10, 166, 39, 22);
		panelMessage.add(label);
		label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		
		JButton btnSendLike = new JButton("");
		btnSendLike.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msg = "<img src='" + GroupChatGui.class.getResource("/image/like.png") +"'></img>";
				try {
					chat.sendMessage(Encode.sendMessageGroup(nameUser,msg));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				updateChat_send_Symbol(msg);
			}
		});
		btnSendLike.setBackground(new Color(240, 240, 240));
		btnSendLike.setBounds(501, 31, 50, 43);
		btnSendLike.setIcon(new javax.swing.ImageIcon(GroupChatGui.class.getResource("/image/like.png")));
		//transparent button
		btnSendLike.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnSendLike.setContentAreaFilled(false);


		panelMessage.add(btnSendLike);
		
		JButton btnSmileIcon = new JButton("");
		btnSmileIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String msg = "<img src='" + GroupChatGui.class.getResource("/image/smile.png") +"'></img>";
				System.out.println("Tin nhan truoc khi bi encode: " +  msg);
				System.out.println("Tin nhan sau khi bi encode: " +  Encode.sendMessage(msg));
				try {
					chat.sendMessage(Encode.sendMessageGroup(nameUser,msg));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				updateChat_send_Symbol(msg);
			}
		});
		btnSmileIcon.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnSmileIcon.setContentAreaFilled(false);
		btnSmileIcon.setBounds(62, 96, 50, 36);
		btnSmileIcon.setIcon(new javax.swing.ImageIcon(GroupChatGui.class.getResource("/image/smile.png")));
		panelMessage.add(btnSmileIcon);
		
		final Label label_1 = new Label("Icon");
		label_1.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		label_1.setBounds(10, 107, 39, 22);
		panelMessage.add(label_1);
		
		btnSmileBigIcon = new JButton("");
		btnSmileBigIcon.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnSmileBigIcon.setContentAreaFilled(false);
		btnSmileBigIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String msg = "<img src='" + GroupChatGui.class.getResource("/image/smile_big.png") +"'></img>";
				try {
					chat.sendMessage(Encode.sendMessageGroup(nameUser,msg));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				updateChat_send_Symbol(msg);
			}		});
		btnSmileBigIcon.setBounds(124, 96, 50, 36);
		panelMessage.add(btnSmileBigIcon);
		btnSmileBigIcon.setIcon(new javax.swing.ImageIcon(GroupChatGui.class.getResource("/image/smile_big.png")));
		
		btnCryingIcon = new JButton("");
		btnCryingIcon.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnCryingIcon.setContentAreaFilled(false);
		btnCryingIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String msg = "<img src='" + GroupChatGui.class.getResource("/image/crying.png") +"'></img>";
				try {
					chat.sendMessage(Encode.sendMessageGroup(nameUser,msg));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				updateChat_send_Symbol(msg);
			}
		});
		btnCryingIcon.setBounds(186, 96, 65, 36);
		panelMessage.add(btnCryingIcon);
		btnCryingIcon.setIcon(new javax.swing.ImageIcon(GroupChatGui.class.getResource("/image/crying.png")));
		
		btnSmileCryingIcon = new JButton("");
		btnSmileCryingIcon.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnSmileCryingIcon.setContentAreaFilled(false);
		btnSmileCryingIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String msg = "<img src='" + GroupChatGui.class.getResource("/image/smile_cry.png") +"'></img>";
				try {
					chat.sendMessage(Encode.sendMessageGroup(nameUser,msg));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				updateChat_send_Symbol(msg);
			}
		});
		btnSmileCryingIcon.setBounds(255, 96, 56, 39);
		panelMessage.add(btnSmileCryingIcon);
		btnSmileCryingIcon.setIcon(new javax.swing.ImageIcon(GroupChatGui.class.getResource("/image/smile_cry.png")));
		
		btnHeartEyeIcon = new JButton("");
		btnHeartEyeIcon.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnHeartEyeIcon.setContentAreaFilled(false);
		btnHeartEyeIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String msg = "<img src='" + GroupChatGui.class.getResource("/image/heart_eye.png") +"'></img>";
				try {
					chat.sendMessage(Encode.sendMessageGroup(nameUser,msg));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				updateChat_send_Symbol(msg);
			}
		});
		btnHeartEyeIcon.setBounds(323, 96, 75, 36);
		panelMessage.add(btnHeartEyeIcon);
		btnHeartEyeIcon.setIcon(new javax.swing.ImageIcon(GroupChatGui.class.getResource("/image/heart_eye.png")));
		
		buttonScaredIcon = new JButton("");
		buttonScaredIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msg = "<img src='" + GroupChatGui.class.getResource("/image/scared.png") +"'></img>";
				try {
					chat.sendMessage(Encode.sendMessageGroup(nameUser,msg));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				updateChat_send_Symbol(msg);
			}
		});
		buttonScaredIcon.setIcon(new javax.swing.ImageIcon(GroupChatGui.class.getResource("/image/scared.png")));
		buttonScaredIcon.setContentAreaFilled(false);
		buttonScaredIcon.setBorder(new EmptyBorder(0, 0, 0, 0));
		buttonScaredIcon.setBounds(394, 96, 75, 36);
		panelMessage.add(buttonScaredIcon);
		
		buttonSadIcon = new JButton("");
		buttonSadIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msg = "<img src='" + GroupChatGui.class.getResource("/image/sad.png") +"'></img>";
				try {
					chat.sendMessage(Encode.sendMessageGroup(nameUser,msg));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				updateChat_send_Symbol(msg);
			}
		});
		buttonSadIcon.setIcon(new javax.swing.ImageIcon(GroupChatGui.class.getResource("/image/sad.png")));
		buttonSadIcon.setContentAreaFilled(false);
		buttonSadIcon.setBorder(new EmptyBorder(0, 0, 0, 0));
		buttonSadIcon.setBounds(476, 96, 75, 36);
		panelMessage.add(buttonSadIcon);
		
		//action when press button Send
		btnSend.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				if (isStop) {
					updateChat_send(txtMessage.getText().toString());
					txtMessage.setText(""); //reset text Send
					return;
				}
				String msg = txtMessage.getText();
				if (msg.equals(""))
					return;
				try {
					chat.sendMessage(Encode.sendMessageGroup(nameUser,msg));
					updateChat_send(msg);
					txtMessage.setText("");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		txtMessage.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent arg0) {

			}

			@Override
			public void keyReleased(KeyEvent arg0) {

			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					String msg = txtMessage.getText();
					if (isStop) {
						updateChat_send(txtMessage.getText().toString());
						txtMessage.setText("");
						return;
					}
					if (msg.equals("")) {
						txtMessage.setText("");
						txtMessage.setCaretPosition(0);
						return;
					}
					try {
						chat.sendMessage(Encode.sendMessage(msg));
						updateChat_send(msg);
						txtMessage.setText("");
						txtMessage.setCaretPosition(0);
					} catch (Exception e) {
						txtMessage.setText("");
						txtMessage.setCaretPosition(0);
					}
				}
			}
		});

		btnDisConnect = new JButton("LEAVE");
		btnDisConnect.setIcon(new ImageIcon(GroupChatGui.class.getResource("/image/leave.png")));
		btnDisConnect.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnDisConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int result = Tags.show(frameChatGui, "Are you sure to close chat with account: "
						+ nameGuest, true);
				if (result == 0) {
					try {
						isStop = true;
						frameChatGui.dispose();
						chat.sendMessage(Tags.CHAT_CLOSE_TAG);
						chat.stopChat();
						System.gc();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		btnDisConnect.setBounds(540, 6, 113, 40);
		frameChatGui.getContentPane().add(btnDisConnect);

		progressSendFile = new JProgressBar(0, 100);
		progressSendFile.setBounds(170, 577, 388, 14);
		progressSendFile.setStringPainted(true);
		frameChatGui.getContentPane().add(progressSendFile);
		progressSendFile.setVisible(false);

		textState = new Label("");
		textState.setBounds(6, 570, 158, 22);
		textState.setVisible(false);
		frameChatGui.getContentPane().add(textState);

		lblReceive = new Label("Receiving ...");
		lblReceive.setBounds(564, 577, 83, 14);
		lblReceive.setVisible(false);
		frameChatGui.getContentPane().add(lblReceive);
		
		txtDisplayChat = new JTextPane();
		txtDisplayChat.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtDisplayChat.setEditable(false);
		txtDisplayChat.setContentType( "text/html" );
		txtDisplayChat.setMargin(new Insets(6, 6, 6, 6));
		txtDisplayChat.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
		txtDisplayChat.setBounds(1, 59, 647, 231);
		appendToPane(txtDisplayChat, "<div class='clear' style='background-color:white'></div>"); //set default background
			    
		frameChatGui.getContentPane().add(txtDisplayChat);
	
		scrollPane = new JScrollPane(txtDisplayChat);
		scrollPane.setBounds(6, 59, 649, 291);
		frameChatGui.getContentPane().add(scrollPane);
		
		JLabel lblNewLabel_1 = new JLabel("to");
		lblNewLabel_1.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		lblNewLabel_1.setBounds(193, 23, 20, 14);
		frameChatGui.getContentPane().add(lblNewLabel_1);
		
		lblGuest = new JLabel(nameGuest);
		lblGuest.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		lblGuest.setIcon(new ImageIcon(GroupChatGui.class.getResource("/image/user_chat.png")));
		lblGuest.setBounds(286, 6, 119, 40);
		frameChatGui.getContentPane().add(lblGuest);
		
	
		
	}
 
	public class ChatRoom extends Thread {

		private ArrayList<Socket> connect_out;
		private ArrayList<String> ListGuest;
		private ObjectOutputStream outPeer;
		private ObjectInputStream inPeer;
		private ServerSocket server;
		private boolean continueSendFile = true, finishReceive = false; 
		private int sizeOfSend = 0, sizeOfData = 0, sizeFile = 0,
				sizeReceive = 0;
		private String nameFileReceive = "";
		private InputStream inFileSend;
		private DataFile dataFile;

		public ChatRoom(ArrayList<Socket>connection, ServerSocket socketserver,String name, ArrayList<String> guest)
				throws Exception {
			connect_out = connection;
			ListGuest=guest;
			nameUser=name;
			server=socketserver;
		}

		@Override
		public void run() {
			super.run();
			OutputStream out = null;
			Socket connector;
			while (!isStop) {
				try {
					connector=server.accept();
					inPeer = new ObjectInputStream(connector.getInputStream());
					String msg = (String) inPeer.readObject();		
					ArrayList<String> getData = Decode.getMessageGroup(msg);
					nameGuest=getData.get(0);
					updateChat_receive(getData.get(1));
					} catch (Exception e) {
					}
				}
			}

		
		//void send Message
		public synchronized void sendMessage(String msg) throws Exception {
			for (int i = 0; i < connect_out.size(); i++) {
				outPeer = new ObjectOutputStream(connect_out.get(i).getOutputStream());
				outPeer.writeObject(msg);
				outPeer.flush();
			}
			
			} 

		public void stopChat() {
		} 
}

	public void copyFileReceive(InputStream inputStr, OutputStream outputStr,
			String path) throws IOException {
		byte[] buffer = new byte[1024];
		int lenght;
		while ((lenght = inputStr.read(buffer)) > 0) {
			outputStr.write(buffer, 0, lenght);
		}
		inputStr.close();
		outputStr.close();
		File fileTemp = new File(path);
		if (fileTemp.exists()) {
			fileTemp.delete();
		}
	}
	
	// send html to pane
	  private void appendToPane(JTextPane tp, String msg){
	    HTMLDocument doc = (HTMLDocument)tp.getDocument();
	    HTMLEditorKit editorKit = (HTMLEditorKit)tp.getEditorKit();
	    try {
	    	
	      editorKit.insertHTML(doc, doc.getLength(), msg, 0, 0, null);
	      tp.setCaretPosition(doc.getLength());
	      
	    } catch(Exception e){
	      e.printStackTrace();
	    }
	  }
	 }
