package sa.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class SimpleSocketServer{

	private int port=4444;
	private String serverName;
	private ServerCommandIf callback;
	private Thread serverThread = null;
	private ServerSocket server;
	private volatile boolean running;


	public SimpleSocketServer(String serverName, int port, ServerCommandIf callback)
	{
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


	public synchronized void start () throws IOException
	{
		if (serverThread != null) throw new IllegalStateException ("The receiver is already started.");

		serverThread = new Thread(new Runnable ()
		{
			@Override
			public void run ()
			{
				PrintWriter out = null;
				BufferedReader in = null;
				
				Socket socket = null;
				try
				{
					running = true;
					server = new ServerSocket(port);
					socket = server.accept();
					
					while (running)
					{
						String response = null;
						String inputLine;
						out = new PrintWriter(socket.getOutputStream(), true);
						in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

						while ((inputLine = in.readLine()) != null) {
							// Client can force shutdown of the server connection
							if (inputLine.equals("shutdown")) {
								log("Shutting down");
								server.close();
							}

							log("Received Input "+inputLine);
							// Parse the command
							// TODO set the command to parse and send in char/char
							char command = inputLine.charAt(0);
							char value = inputLine.charAt(1);
							callback.onCommand(command,value);
							log("Command "+(int)command+" value="+(int)value);

							if(response !=null)
								out.println(response);
						}
					}
				}
				catch (SocketException e)
				{

				}
				catch (IOException e)
				{

				}
				finally
				{
					if(out!=null)
					{
							out.close();
					}
					if(in!=null)
					{
						try {
							in.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					if(socket!=null)
					{
						try {
							socket.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					if(server!=null)
					{
						try {
							server.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

			}
		}, getServerName());
		serverThread.start ();
	}

	public synchronized void stop ()
	{

		if(serverThread == null) return;

		running = false;
		try
		{
			if (server != null)
				server.close ();
		}
		catch (IOException e)
		{

		}
		serverThread = null;
	}

	/**
	 * Gets the port number
	 * @return The port number
	 */
	public int getPort()
	{
		return port;
	}

}

