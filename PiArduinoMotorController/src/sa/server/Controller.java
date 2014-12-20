package sa.server;

import java.io.IOException;

import com.pi4j.wiringpi.Serial;

/**
 * Controller class for the Pi-Arduino 
 * 
 * Starts up the serial communication server between the pi
 * and arduino
 * 
 * Starts the socket server for communication between the pi 
 * and the remote. 
 *
 */
public class Controller{
	
	private SimpleSocketServer server;
	
	public static int port = 4444; 
	
	public static void main(String[] args) {
		Controller c = new Controller();
	}
	
	public Controller()
	{
		System.out.println("Starting Serial Server");
		SerialServer ss = new SerialServer(Serial.DEFAULT_COM_PORT, 9600);
		System.out.println("Serial Server Started");
		
		System.out.println("Starting the command server");
		server = new SimpleSocketServer("Command Server", port, ss);
		try
		{
			server.start();
		}
		catch(IOException ioe)
		{
			System.out.println("IO Exception while starting the command server");
		}
	}
}
