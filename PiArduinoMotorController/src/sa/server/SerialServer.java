package sa.server;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataListener;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.SerialPortException;

public class SerialServer implements ServerCommandIf{
	
	private final Serial serial = SerialFactory.createInstance();
		
	public SerialServer(String port, int baudrate)
	{
		init(port, baudrate);
	}
	
	private void init(String port, int baudrate)
	{
        // create and register the serial data listener
        serial.addListener(new SerialDataListener() {
            @Override
            public void dataReceived(SerialDataEvent event) {
                // print out the data received to the console
                System.out.print(event.getData());
            }            
        });
                
        try {
            // open the default serial port provided on the GPIO header
            //serial.open(Serial.DEFAULT_COM_PORT, 9600);
        	serial.open(port, baudrate);
            
        }
        catch(SerialPortException ex) {
            System.out.println(" ==>> SERIAL SETUP FAILED : " + ex.getMessage());
            return;
        }
		
	}


	@Override
	public void onCommand(char command, char value) {
		try
		{
			serial.write(command);
			serial.write(value);
			
		}
		catch (IllegalStateException ex)
		{
			System.out.println("Error while transmitting command");
		}
	}
	
}
