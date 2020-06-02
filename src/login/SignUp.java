package login;

import java.awt.EventQueue;
import java.util.Random;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import tags.Encode;
import tags.Tags;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.JPasswordField;

public class SignUp {
 private static String NAME_FAILED = "THIS NAME CONTAINS INVALID CHARACTER. PLEASE TRY AGAIN";
 private static String NAME_EXSIST = "THIS NAME IS ALREADY USED. PLEASE TRY AGAIN";
 private static String SERVER_NOT_START = "TURN ON SERVER BEFORE START";

 private Pattern checkName = Pattern.compile("[_a-zA-Z][_a-zA-Z0-9]*");

 private JFrame frameSignUpForm;
 private JTextField txtPort;
 private JLabel lblError;
 private String name = "", IP = "",pass="",confirmPass="";
 private JTextField txtIP;	
 private JTextField txtUsername;
 private JPasswordField passwordField;
 private JPasswordField passwordField_1;
 private JLabel lblNewLabel_1;

 public static void main(String[] args) {
  EventQueue.invokeLater(new Runnable() {
   public void run() {
    try {
     SignUp window = new SignUp();
     window.frameSignUpForm.setVisible(true);
    } catch (Exception e) {
     e.printStackTrace();
    }
   }
  });
 }

 public SignUp(String IP) {
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
	     SignUp window=new SignUp();
	     window.frameSignUpForm.setVisible(true);
	    } catch (Exception e) {
	     e.printStackTrace();
	    }
	   }
	  });
 }
public SignUp() {
	initialize();
}
 private void initialize() {
  frameSignUpForm = new JFrame();
  frameSignUpForm.setTitle("Sign Up");
  frameSignUpForm.setResizable(false);
  frameSignUpForm.setBounds(100, 100, 517, 343);
  frameSignUpForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  frameSignUpForm.getContentPane().setLayout(null);

  JLabel lblWelcome = new JLabel("Connect With Server\r\n");
  lblWelcome.setForeground(UIManager.getColor("RadioButtonMenuItem.selectionBackground"));
  lblWelcome.setFont(new Font("Segoe UI", Font.PLAIN, 18));
  lblWelcome.setBounds(27, 13, 312, 48);
  frameSignUpForm.getContentPane().add(lblWelcome);

  JLabel lblHostServer = new JLabel("IP Server");
  lblHostServer.setFont(new Font("Segoe UI", Font.PLAIN, 13));
  lblHostServer.setBounds(47, 74, 86, 20);
  frameSignUpForm.getContentPane().add(lblHostServer);

  JLabel lblPortServer = new JLabel("Port Server");
  lblPortServer.setFont(new Font("Segoe UI", Font.PLAIN, 13));
  lblPortServer.setBounds(349, 77, 79, 14);
  frameSignUpForm.getContentPane().add(lblPortServer);

  txtPort = new JTextField();
  txtPort.setFont(new Font("Segoe UI", Font.PLAIN, 13));
  txtPort.setText("8080");
  txtPort.setEditable(false);
  txtPort.setColumns(10);
  txtPort.setBounds(429, 70, 65, 28);
  frameSignUpForm.getContentPane().add(txtPort);

  JLabel lblUserName = new JLabel("User Name");
  lblUserName.setFont(new Font("Segoe UI", Font.PLAIN, 13));
  lblUserName.setBounds(10, 134, 106, 38);
  frameSignUpForm.getContentPane().add(lblUserName);
  lblUserName.setIcon(new javax.swing.ImageIcon(Login.class.getResource("/image/user.png")));

  lblError = new JLabel("");
  lblError.setBounds(66, 287, 399, 20);
  frameSignUpForm.getContentPane().add(lblError);

  txtIP = new JTextField();
  txtIP.setBounds(128, 70, 185, 28);
  frameSignUpForm.getContentPane().add(txtIP);
  txtIP.setColumns(10);
  
  txtUsername = new JTextField();
  txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
  txtUsername.setColumns(10);
  txtUsername.setBounds(128, 138, 366, 28);
  frameSignUpForm.getContentPane().add(txtUsername);
  
  passwordField = new JPasswordField();
  passwordField.setBounds(128, 175, 366, 28);
  frameSignUpForm.getContentPane().add(passwordField);
  
  JLabel lblNewLabel = new JLabel("Password");
  lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
  lblNewLabel.setBounds(61, 177, 55, 22);
  frameSignUpForm.getContentPane().add(lblNewLabel);
  
  JButton btnNewButton = new JButton("Sign Up");
  btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
  btnNewButton.addActionListener(new ActionListener() {
  	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) {

  	    name = txtUsername.getText();
  	    pass=passwordField.getText();
  	    confirmPass=passwordField_1.getText();
  	    lblError.setVisible(false);
  	    IP = txtIP.getText();


  	    //must edit here
  	    if (checkName.matcher(name).matches() && !IP.equals("")) {
  	     try {
  	      Random rd = new Random();
  	      int portPeer = 10000 + rd.nextInt() % 1000;
  	      InetAddress ipServer = InetAddress.getByName(IP);
  	      int portServer = Integer.parseInt("8080");
  	      Socket socketClient = new Socket(ipServer, portServer);

  	      String msg = Encode.getCreateAccount(name,pass,confirmPass,Integer.toString(portPeer));
  	      ObjectOutputStream serverOutputStream = new ObjectOutputStream(socketClient.getOutputStream());
  	      serverOutputStream.writeObject(msg); 
  	      serverOutputStream.flush();
  	      ObjectInputStream serverInputStream = new ObjectInputStream(socketClient.getInputStream());
  	      msg = (String) serverInputStream.readObject();

  	      socketClient.close();
  	      if (msg.equals(Tags.SESSION_DENY_TAG)) {
  	       lblError.setText(NAME_EXSIST);
  	       lblError.setVisible(true);
  	       return;
  	      }
  	      //						new menuGUI(IP, portPeer, "toan", msg);
  	      
  	     } catch (Exception e1) {
  	      lblError.setText(SERVER_NOT_START);
  	      lblError.setVisible(true);
  	      e1.printStackTrace();
  	     }
  	    }
  	    else {
  	     lblError.setText(NAME_FAILED);
  	     lblError.setVisible(true);
  	     lblError.setText(NAME_FAILED);
  	    }
  	  new Login("");
  	frameSignUpForm.dispose();
  	}
  });
  btnNewButton.setBounds(388, 244, 106, 38);
  frameSignUpForm.getContentPane().add(btnNewButton);
  
  passwordField_1 = new JPasswordField();
  passwordField_1.setBounds(128, 214, 366, 29);
  frameSignUpForm.getContentPane().add(passwordField_1);
  
  lblNewLabel_1 = new JLabel("Confirm Password");
  lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
  lblNewLabel_1.setBounds(10, 220, 106, 14);
  frameSignUpForm.getContentPane().add(lblNewLabel_1);
  lblError.setVisible(false);


 }
}
