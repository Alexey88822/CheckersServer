import java.awt.*;
import javax.swing.*;

import EnumConstants.Checkers;
import Session.HandleSession;

import java.io.*;
import java.net.*;
import java.util.Date;

public class ServerApp extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	//Frame components
	private JScrollPane scroll;
	private JTextArea information;
	private JLabel title;
	
	//Network properties
	private ServerSocket serverSocket;
	int sessionNo;
	
	public ServerApp(){
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		
		title = new JLabel("Server");
		information = new JTextArea();
		information.setBackground(Color.BLACK);
		information.setForeground(Color.WHITE);
		scroll = new JScrollPane(information);
		
		add(title,BorderLayout.NORTH);
		add(scroll, BorderLayout.CENTER);
	}	
	
	//Establish connection and wait for Clients
	public void startRunning(){
		
		try{
			
			PropertyManager pm = PropertyManager.getInstance();
			int port = pm.getPort();
			String adress = pm.getAdress();
			serverSocket = new ServerSocket(port);
			information.append(adress);
			information.append(new Date() + ":- Сервер запущен с портом "+ port + " \n");
			sessionNo = 1;			
			
			while(true){
				information.setForeground(Color.GREEN);
				information.append(new Date()+ ":- Сессия "+ sessionNo + " начата\n");
				
				//Wait for player 1
				Socket player1 = serverSocket.accept();
				information.append(new Date() + ":- Игрок 1 присоединён к игре ");
				information.append(player1.getInetAddress().getHostAddress() + "\n");
				
				//Notification to player1 that's he's connected successfully
				new DataOutputStream(player1.getOutputStream()).writeInt(Checkers.PLAYER_ONE.getValue());
				
				//Wait for player 2
				Socket player2 = serverSocket.accept();
				information.append(new Date() + ":- Игрок 2 присоединён к игре");
				information.append(player2.getInetAddress().getHostAddress() +"\n");
				
				//Notification to player2 that's he's connected successfully
				new DataOutputStream(player2.getOutputStream()).writeInt(Checkers.PLAYER_TWO.getValue());
				
				//Increase Session number
				sessionNo++;
				
				// Create a new thread for this session of two players
				HandleSession task = new HandleSession(player1, player2);
				new Thread(task).start();
			}
		}catch(Exception ex){			
			ex.printStackTrace();
			System.exit(0);
		}				
	}
}
