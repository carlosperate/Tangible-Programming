package emulation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ControlBox extends JFrame{


	public JPanel mainPanel;
	public JPanel ledPanel, switchPanel;
	public JLabel neutralSwitchLabel;
	public EmulatedLED bluetoothLed, readyLed, busyLed;
	public JButton goButton, rightSwitchButton, leftSwitchButton;

	public ControlBox(){
		setTitle("Emulator Control Box");
		setSize(800, 600);

		// Initialise components
		mainPanel = new JPanel(new BorderLayout());
		ledPanel = new JPanel(new GridLayout(1, 3));
		switchPanel = new JPanel(new GridLayout(1, 3));

		neutralSwitchLabel = new JLabel();
		bluetoothLed = new EmulatedLED("Bluetooth", Color.RED);
		readyLed = new EmulatedLED("Ready", Color.GREEN);
		busyLed = new EmulatedLED("Busy", Color.GREEN);

		goButton = new JButton("GO");
		rightSwitchButton = new JButton("R-Switch");
		leftSwitchButton = new JButton("L-Switch");

		// Custom Settings
		neutralSwitchLabel.setHorizontalAlignment(SwingConstants.CENTER);
		bluetoothLed.setHorizontalAlignment(SwingConstants.CENTER);
		readyLed.setHorizontalAlignment(SwingConstants.CENTER);
		busyLed.setHorizontalAlignment(SwingConstants.CENTER);

		// Build Components
		ledPanel.add(bluetoothLed);
		ledPanel.add(readyLed);
		ledPanel.add(busyLed);

		switchPanel.add(leftSwitchButton);
		switchPanel.add(neutralSwitchLabel);
		switchPanel.add(rightSwitchButton);

		mainPanel.add(ledPanel, BorderLayout.NORTH);
		mainPanel.add(goButton, BorderLayout.CENTER);
		mainPanel.add(switchPanel, BorderLayout.SOUTH);

		setContentPane(mainPanel);
		pack();

		// Start in middle of the screen
		Dimension dim = getToolkit().getScreenSize(); 
		setLocation(((dim.width-getWidth())/2)-getWidth()/2,((dim.height-getHeight())/2)-getHeight()/2); 

		setVisible(true);
	}

	public void testLEDs(){
		try {
			bluetoothLed.turnOn();
			Thread.sleep(1000);
			bluetoothLed.turnOff();
			readyLed.turnOn();
			Thread.sleep(1000);
			readyLed.turnOff();
			busyLed.turnOn();
			Thread.sleep(1000);
			busyLed.turnOff();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public EmulatedLED getBluetoothLed(){
		return bluetoothLed;
	}
	
	public EmulatedLED getReadyLed(){
		return readyLed;
	}
	
	public EmulatedLED getBusyLed(){
		return busyLed;
	}
	
	public void addGoButtonListener(ActionListener action){
		goButton.addActionListener(action);
	}
	
	public void addRightSwitchListener(ActionListener action){
		leftSwitchButton.addActionListener(action);
	}
	
	public void addLeftSwitchListener(ActionListener action){
		rightSwitchButton.addActionListener(action);
	}

	public class EmulatedLED extends JLabel{

		public boolean state;	// true - on, false - off

		public Color color;


		public EmulatedLED(String text, Color color){
			super(text);
			this.color = color;
		}

		public void toggleState(){
			if(state){
				turnOff();
			}else{
				turnOn();
			}
		}

		public void turnOn(){
			setForeground(color);
			state = true;
		}

		public void turnOff(){
			setForeground(Color.BLACK);
			state = true;
		}

		public void pulse(long milliseconds, boolean blockingCall){

		}
		
		public void blink(long milliseconds){
			
		}

	}
}
