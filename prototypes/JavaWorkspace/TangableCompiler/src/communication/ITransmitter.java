package communication;

import com.pi4j.io.gpio.GpioPinDigitalOutput;

import ast.Command;

public interface ITransmitter {
	
	void setup(GpioPinDigitalOutput communicationStatus);

	void sendCommand(int cmd);
	void sendCommand(Command cmd);

	void shutdown();
}
