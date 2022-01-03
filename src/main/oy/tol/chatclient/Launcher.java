package oy.tol.chatclient;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

// Launcher for the modified ChatClient with gui
public class Launcher {

	public static void main(String[] args) {
		JFrame errorMessage = new JFrame();
		// Undocumented feature: use third arg "-http" to use http instead of https.
		boolean useHttps = true;
		if (args.length >= 2) {
			System.out.println("Launching ChatClient with args " + args[0] + " " + args[1]);
			ChatClient.serverVersion = Integer.parseInt(args[0]);
			if (ChatClient.serverVersion < 2) {
				ChatClient.serverVersion = 2;
			} else if (ChatClient.serverVersion > 5) {
				ChatClient.serverVersion = 5;
			}
			if (args.length == 3 && "-http".equalsIgnoreCase(args[2])) {
				useHttps = false;
			}
		} else {
			System.out.println("Usage: java -jar chat-client-jar-file 2 ../localhost.cer");
			System.out.println("Where first parameter is the server version number (exercise number),");
			System.out.println("and the 2nd parameter is the server's client certificate file with path.");
			return;
		}
		Gui gui = new Gui();
		gui.setHttpsClient(args[1], useHttps);
		try {
			gui.setMethodsAccessible();
		} catch (NoSuchMethodException | SecurityException exception) {
			JOptionPane.showMessageDialog(errorMessage, "Error! " + exception.getLocalizedMessage());
		}
		gui.startGui();
	}
}
