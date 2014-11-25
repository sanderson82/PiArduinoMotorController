package sa.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleSocketServer implements Runnable{

	private int port=4444;
	private String serverName;
	private ServerCommandIf callback;

	public SimpleSocketServer(String serverName, int port, ServerCommandIf callback){

		this.port = port;
		this.serverName = serverName;
		this.callback = callback;
	}

	/**
	 * Gets the server name
	 * @return The server name
	 */
	public String getServerName()
	{
		return serverName;
	}

	public void log(String log)
	{
		System.out.println(serverName+": "+log);
	}

	/**
	 * Gets the port number
	 * @return The port number
	 */
	public int getPort()
	{
		return port;
	}

	public void run () {

		String response = null;
		String inputLine;
		ServerSocket listener = null;
		try {

			listener = new ServerSocket(port);
			Socket server;

			server = listener.accept();

			PrintWriter out = new PrintWriter(server.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));

			while ((inputLine = in.readLine()) != null) {
				try {
					// Client can force shutdown of the server connection
					if (inputLine.equals("shutdown")) {
						log("Shutting down");
						server.close();
					}
					
					log(inputLine);
					// Parse the command
					// TODO set the command to parse and send in char/char
					char command = inputLine.charAt(0);
					char value = inputLine.charAt(1);
					callback.onCommand(command,value);
					
					if(response !=null)
						out.println(response);

				} catch(IOException e) {
					e.printStackTrace();
					throw e;
				}
			}

			out.close();
			in.close();
			server.close();
		} catch (IOException ioe) {
			log("IOException on socket listen: " + ioe);
			ioe.printStackTrace();
		}
		finally
		{
			if(listener!=null)
				try {
					listener.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

}

