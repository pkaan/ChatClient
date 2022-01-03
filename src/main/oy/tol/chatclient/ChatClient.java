package oy.tol.chatclient;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Original client by Antti Juustila: https://github.com/anttijuu/O3-chat-client
 * ChatClient is the console based UI for the ChatServer. It profides the
 * necessary functionality for chatting. The actual comms with the ChatServer
 * happens in the ChatHttpClient class.
 */

// Modified ChatClient
public class ChatClient implements ChatClientDataProvider {

	private static final String SERVER = "https://localhost:8001/";

	private static final int AUTO_FETCH_INTERVAL = 1000; // ms

	private String currentServer = SERVER; // URL of the server without paths.
	public static int serverVersion = 3;
	public double chatClientVersion = 2.0; // 25.11.2021

	private String username = null; // Registered & logged user.
	private String password = null; // The password in clear text.
	private String email = null; // Email address of user, needed for registering.
	private String nick = null; // Nickname, user can change the name visible in chats.

	private ChatHttpClient httpClient = null; // Client handling the requests & responses.

	private boolean autoFetch = false;
	private boolean loggedIn = false;
	private Timer autoFetchTimer = null;
	private boolean useHttps;
	private String certificateFileWithPath;

	private String allMessages;
	private JFrame errorMessage = new JFrame();

	// Edited! Original function from Antti Juustila
	// https://github.com/anttijuu/O3-chat-client"
	private void registerUser() {
		try {
			int response = httpClient.registerUser();
			if (response >= 200 || response < 300) {
				loggedIn = true;
				JOptionPane.showMessageDialog(errorMessage, "Registered successfully, you may start chatting!");

			} else {
				JOptionPane.showMessageDialog(errorMessage, "Failed to register. Error from server: " + response + " "
						+ httpClient.getServerNotification());

			}
		} catch (KeyManagementException | KeyStoreException | CertificateException | NoSuchAlgorithmException
				| FileNotFoundException e) {
			JOptionPane.showMessageDialog(errorMessage,
					"**** ERROR in server certificate. \n " + e.getLocalizedMessage());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(errorMessage,
					"**** ERROR in user registration with server " + currentServer + "\n" + e.getLocalizedMessage());
		}
	}

	// Edited! Original function from Antti Juustila
	// https://github.com/anttijuu/O3-chat-client"
	private int getNewMessages() {
		int count = 0;
		try {
			if (null != username && null != password) {
				int response = httpClient.getChatMessages();
				if (response >= 200 || response < 300) {
					loggedIn = true;
					if (serverVersion >= 3) {
						List<ChatMessage> messages = httpClient.getNewMessages();
						if (null != messages) {
							count = messages.size();
							for (ChatMessage message : messages) {
								allMessages = new StringBuilder().append(allMessages).append("\n" + "["
										+ message.sentAsString() + "] " + message.nick + " > " + message.message)
										.toString();
							}
							Gui.updateMessagesWindow(allMessages);
						}
					} else {
						List<String> messages = httpClient.getPlainStringMessages();
						if (null != messages) {
							count = messages.size();
							for (String message : messages) {
								allMessages = new StringBuilder().append(allMessages).append("\n" + message).toString();
							}
							Gui.updateMessagesWindow(allMessages);
						}
					}
				} else {
					JOptionPane.showMessageDialog(errorMessage,
							"**** Error from server: " + response + " " + httpClient.getServerNotification());
				}
			} else {
				JOptionPane.showMessageDialog(errorMessage, "Not yet registered or logged in!");
			}
		} catch (KeyManagementException | KeyStoreException | CertificateException | NoSuchAlgorithmException
				| FileNotFoundException e) {
			JOptionPane.showMessageDialog(errorMessage,
					"**** ERROR in server certificate \n" + e.getLocalizedMessage());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(errorMessage,
					" **** ERROR in getting messages from server " + currentServer + "\n" + e.getLocalizedMessage());

		}
		return count;
	}

	// Edited! Original function from Antti Juustila
	// https://github.com/anttijuu/O3-chat-client"
	private void postMessage(String message) {
		if (null != username) {
			try {
				int response = httpClient.postChatMessage(message);
				if (response < 200 || response >= 300) {
					JOptionPane.showMessageDialog(errorMessage,
							"Error from server: " + response + " " + httpClient.getServerNotification());
				}
			} catch (KeyManagementException | KeyStoreException | CertificateException | NoSuchAlgorithmException
					| FileNotFoundException e) {
				JOptionPane.showMessageDialog(errorMessage,
						"**** ERROR in server certificate \n" + e.getLocalizedMessage());
			} catch (IOException e) {
				JOptionPane.showMessageDialog(errorMessage,
						"**** ERROR in posting message to server " + currentServer + "\n" + e.getLocalizedMessage());
			}
		} else {
			JOptionPane.showMessageDialog(errorMessage, "Must register/login to server before posting messages!!");
		}
	}

	// Edited! Original function from Antti Juustila
	// https://github.com/anttijuu/O3-chat-client"
	private void autoFetch() {
		if (autoFetch) {
			if (null == autoFetchTimer) {
				autoFetchTimer = new Timer();
			}
			try {
				autoFetchTimer.scheduleAtFixedRate(new TimerTask() {
					@Override
					public void run() {
						// Check if autofetch was switched off.
						if (!autoFetch) {
							cancel();
						}
						if (getNewMessages() > 0) {
						}
						;
					}
				}, AUTO_FETCH_INTERVAL, AUTO_FETCH_INTERVAL);
			} catch (IllegalArgumentException | IllegalStateException | NullPointerException e) {
				JOptionPane.showMessageDialog(errorMessage, " **** Faulty timer usage: " + e.getLocalizedMessage());
				autoFetch = false;
			}
		}
	}

	// Edited! Original function from Antti Juustila
	// https://github.com/anttijuu/O3-chat-client"
	private void cancelAutoFetch() {
		if (null != autoFetchTimer) {
			autoFetchTimer.cancel();
			autoFetchTimer = null;
		}
		autoFetch = false;
	}

	@Override
	public String getServer() {
		return currentServer;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getNick() {
		return nick;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public int getServerVersion() {
		return serverVersion;
	}

	public boolean getUserStatus() {
		return loggedIn;
	}

	protected String getText() {
		return allMessages;
	}

	protected void setServer(String server) {
		currentServer = server;
	}

	protected void setUsername(String user) {
		username = user;
	}

	protected void setPassword(String passWord) {
		password = passWord;
	}

	protected void setNick(String nickname) {
		nick = nickname;
	}

	protected void setEmail(String eMail) {
		email = eMail;
	}

	protected void setAutofetchOn() {
		autoFetch = true;
	}

	protected void setUserStatus(boolean status) {
		loggedIn = status;
	}

	protected void resetTimer() {
		httpClient.setlatestDataFromServerNull();
	}

	protected void createHttpClient() {
		httpClient = new ChatHttpClient(this, certificateFileWithPath, useHttps);
	}

	protected void setHttps(boolean https) {
		useHttps = https;
	}

	protected void certificateFileWithPath(String path) {
		certificateFileWithPath = path;
	}

	protected void clearMessages() {
		allMessages = "";
	}
}
