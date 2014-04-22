package communication;

import ast.Command;

public interface ITransmitter {
	
	void setup();

	void sendCommand(int cmd);
	void sendCommand(Command cmd);

	void shutdown();
}
