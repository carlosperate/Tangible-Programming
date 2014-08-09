package communication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import emulation.GpioPinDigitalOutputWrapper;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import ast.Command;

public class BluetoothTransmitter implements ITransmitter, SerialPortEventListener{

	SerialPort serialPort = null;

	private static final String PORT_NAMES[] = {
		"/dev/tty.usbmodem",	// Mac OS X
		"/dev/rfcomm",			// Linux
		"/dev/usbdev",			// Linux
		"/dev/tty",				// Linux
		"/dev/serial",			// Linux
		"COM3",					// Windows
		"COM9"					// Windows
	};

	private String appName;
	private BufferedReader input;
	private OutputStream output;

	private static final int TIME_OUT = 1000;
	private static final int DATA_RATE = 9600;

	@Override
	public void setup(GpioPinDigitalOutputWrapper statusPin) {
		try{

			CommPortIdentifier portId = null;
			Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

			System.out.println("Trying:");
			while(portId == null && portEnum.hasMoreElements()){

				CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
				System.out.println("   port" + currPortId.getName());
				for(String portName : PORT_NAMES){
					if(currPortId.getName().equals(portName) || currPortId.getName().startsWith(portName)){
						serialPort = (SerialPort)currPortId.open(appName, TIME_OUT);
						portId = currPortId;
						System.out.println("Connected on port" + currPortId.getName());
					}
				}

			}

			if (portId == null) {
				System.out.println("Oops... Could not connect to Arduino");
				return;
			}

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			input = new BufferedReader(
					new InputStreamReader(
							serialPort.getInputStream()));

			return;
		}
		catch ( Exception e ) { 
			e.printStackTrace();
		}
	}

	@Override
	public void sendCommand(Command cmd) {
		sendCommand(cmd.outputId);
	}

	@Override
	public void sendCommand(int cmd) {
		try {
			System.out.println("Sending data: '" + cmd +"'");

			// open the streams and send the "y" character
			output = serialPort.getOutputStream();
			output.write(cmd);
		} 
		catch (Exception e) {
			System.err.println(e.toString());
			System.exit(0);
		}
	}

	@Override
	public void shutdown() {
		if ( serialPort != null ) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	@Override
	public void serialEvent(SerialPortEvent oEvent) {
		System.out.println("Event received: " + oEvent.toString());

		try {
			String inputLine = input.readLine();
			System.out.println(inputLine);


		}catch (Exception e) {
			System.err.println(e.toString());
		}
	}
}