package communication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.trigger.GpioBlinkStopStateTrigger;

import ast.Command;

public class BluetoothTransmitter implements ITransmitter{

	public final UUID uuid = new UUID("1101", false);

	public final String name = "Window Echo Server";

	public final String url = "btspp://localhost:" + uuid + ";name=" + name + ";authenticate=false;encrypt=false;";

	LocalDevice local = null;

	StreamConnectionNotifier server = null;

	StreamConnection conn = null;

	InputStream reader = null;

	DataOutputStream writer = null;

	Thread readerMonitor = null;

	boolean readerMonitorRunning = true;
	
	boolean awaitingAcknowledgements = false;

	boolean acknowledgementReceived = false;			// received after sending command

	boolean acknowledgementComplete = false;			// received once command is complete

	GpioPinDigitalOutput bluetoothCommStatusLed;

	@Override
	public void setup(GpioPinDigitalOutput statusPin) {
		try{

			bluetoothCommStatusLed = statusPin;

			System.out.println("Setting device to be discoverable...");
			local = LocalDevice.getLocalDevice();
			local.setDiscoverable(DiscoveryAgent.GIAC);

			System.out.println("Start advertising service...");

			server = (StreamConnectionNotifier)Connector.open(url);

			System.out.println("Waiting for incoming connection...");

			bluetoothCommStatusLed.blink(500);

			conn = server.acceptAndOpen();

			System.out.println("Client Connected... ");

			reader = conn.openInputStream();

			writer = conn.openDataOutputStream();

			bluetoothCommStatusLed.blink(0);
			bluetoothCommStatusLed.setState(PinState.HIGH);

			// TODO Temp Wait to simulate hitting the GO button
			Thread.sleep(2000);

			// Start reader thread monitor
			readerMonitor = new Thread(new Runnable() {

				@Override
				public void run() {
					while(readerMonitorRunning){
						try {
							int data = reader.read();
							switch(data){
							case 0xFE:
								acknowledgementReceived = awaitingAcknowledgements ? true : false;
								break;
							case 0xFD:
								acknowledgementComplete = awaitingAcknowledgements ? true : false;
								break;
							case 0xFFFFFFFF:
								readerMonitorRunning = false;
								break;
							default:
								break;
							}
							
							System.out.println(">>> Receiving: " + String.format("%02X", data));
							
						} catch (IOException e) {
							e.printStackTrace();
						}

					}

					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});

			readerMonitor.start();

		}catch(Exception e){
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
			System.out.println("[BT] Sending Command: " + cmd);
			writer.write((byte)cmd);
			awaitingAcknowledgements = true;
			
			while(!(acknowledgementReceived && acknowledgementComplete)){
				Thread.sleep(50);
			}
			
			awaitingAcknowledgements = false;

			// reset acknowledgements
			acknowledgementReceived = false;
			acknowledgementComplete = false;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void shutdown() {
		if(writer != null){
			try {
				writer.write('e');
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			readerMonitorRunning = false;
			readerMonitor.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
