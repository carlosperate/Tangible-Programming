package communication;

import com.pi4j.io.gpio.GpioPinDigitalOutput;

import emulation.GpioPinDigitalOutputWrapper;
import ast.Command;

public interface ITransmitter {
	
	void setup(GpioPinDigitalOutputWrapper communicationStatus);

	void sendCommand(int cmd);
	void sendCommand(Command cmd);

	void shutdown();
}
