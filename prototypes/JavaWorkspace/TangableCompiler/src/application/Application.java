package application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import communication.BluetoothTransmitter;
import imageRecongnition.TopCodesController;
import interpreter.InterpreterVisitor;
import lexer.Lexer;
import ast.Command;
import ast.Program;
import core.LanguageDefinition;
import emulation.ControlBox;
import emulation.GpioPinDigitalOutputWrapper;
import exceptions.SyntaxException;

public class Application {
	
	/**
	 * Global Bluetooth Transmitter
	 */
	final BluetoothTransmitter bluetoothTransmitter = new BluetoothTransmitter();

	/**
	 * GPIO Controller, Handling GPIO. Uses Singleton pattern
	 */
	private GpioController gpio;

	/**
	 * Pin to control Bluetooth status output
	 */
	private GpioPinDigitalOutputWrapper bluetoothCommLed;

	/**
	 * Pin to control ready LED
	 */
	private GpioPinDigitalOutputWrapper readyLed;

	/**
	 * Pin to control busy LED 
	 */
	private GpioPinDigitalOutputWrapper busyLed;

	/**
	 * Pin to control green Go Button
	 */
	private GpioPinDigitalInput goButton;

	/**
	 * Pin to control left switch state
	 */
	private GpioPinDigitalInput leftSwitch;

	/**
	 * Pin to control right switch state
	 */
	private GpioPinDigitalInput rightSwitch;
	
	/**
	 * Topcode Interpreter
	 */
	private TopCodesController topcodes;

	/**
	 * Counter used to measure number of go button events fired
	 */
	private int goButtonCount = 0;

	/**
	 * Counter used to measure number of left switch events fired
	 */
	private int leftSwitchCount = 0;

	/**
	 * Counter used to measure number of right switch events fired
	 */
	private int rightSwitchCount = 0;

	/**
	 * Flag to show is tangible application is currently being processes.
	 * Prevents the user from starting a second application while busy
	 */
	private boolean runningTangableApplication = false;
	
	InterpreterVisitor interpreter;

	public void start(){

		topcodes = new TopCodesController();
		
		bluetoothCommLed = new GpioPinDigitalOutputWrapper();
		readyLed = new GpioPinDigitalOutputWrapper();
		busyLed = new GpioPinDigitalOutputWrapper();
		
		if(OS.isWindows()){
			System.out.println("Windows OS");
			System.out.println("Starting Software Emulation...");

			ControlBox cb = new ControlBox();
			
			System.out.println("Testing LED's");
			cb.testLEDs();
			
			bluetoothCommLed.setSoftwareLed(cb.getBluetoothLed());
			readyLed.setSoftwareLed(cb.getReadyLed());
			busyLed.setSoftwareLed(cb.getBusyLed());
			
			cb.addGoButtonListener(new GoButtonPressedListener());
			cb.addRightSwitchListener(new LeftSwitchPressedListener());
			cb.addLeftSwitchListener(new RightSwitchPressedListener());
			
		}else if(OS.isUnix()){
			System.out.println("Unix OS");
			System.out.println("Configuring Hardware...");
			/**
			 * Setup HW GPIO Configuration on linux
			 */
			gpio = GpioFactory.getInstance();
			
			System.out.println("Setting up GPIO Outputs...");
			GpioPinDigitalOutput bluetoothHWLed = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "Bluetooth Communication Status LED", PinState.LOW);
			GpioPinDigitalOutput readyHWLed = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "Ready LED", PinState.LOW);
			GpioPinDigitalOutput busyHWLed = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "Busy LED", PinState.LOW);
			bluetoothHWLed.setShutdownOptions(true, PinState.LOW);
			readyHWLed.setShutdownOptions(true, PinState.LOW);
			busyHWLed.setShutdownOptions(true, PinState.LOW);
			
			bluetoothCommLed.setHardwareLed(bluetoothHWLed);
			readyLed.setHardwareLed(readyHWLed);
			busyLed.setHardwareLed(busyHWLed);
			
	
			System.out.println("Setting up GPIO Inputs...");
			goButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_06, "Go Button", PinPullResistance.PULL_UP);
			leftSwitch = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05, "Left Switch", PinPullResistance.PULL_UP);
			rightSwitch = gpio.provisionDigitalInputPin(RaspiPin.GPIO_01, "Right Switch", PinPullResistance.PULL_UP);
			
	
			System.out.println("Testing LED's");
			
			// Switch each LED on for 1 second to show LED out working
			bluetoothCommLed.pulse(1000, true);
			readyLed.pulse(1000, true);
			busyLed.pulse(1000, true);
	
			//TODO devise a method to test inputs?
			goButton.addListener(new GoButtonPressedListener());
			leftSwitch.addListener(new LeftSwitchPressedListener());
			rightSwitch.addListener(new RightSwitchPressedListener());

		}
		if(!LanguageDefinition.getInstance().SetupLanguage("langdefinition.xml")){
			System.err.println("Language Setup Failed");
			return;
		}

		System.out.println("Language Generation Complete");

		// Setup a new bluetoothTransmitter
		bluetoothTransmitter.setup(bluetoothCommLed);

		System.out.println("Entering Application Loop..");

		readyLed.turnOn();
		
		while(true){
			// infinite loop while application is in use.
			// from this point the application is completely event driven
		}
	}

	public class GoButtonPressedListener implements GpioPinListenerDigital, ActionListener{
		@Override
		public void handleGpioPinDigitalStateChangeEvent(
				GpioPinDigitalStateChangeEvent event) {
			 GoButtonAction();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			 GoButtonAction();
		}
		
		public void GoButtonAction(){

			// prevents 2 events being fired
			goButtonCount++;
			if((goButtonCount % 2) == 0){

				if(!runningTangableApplication){
					runningTangableApplication = true;
					
					// Switch LED states to show busy status
					readyLed.turnOff();
					busyLed.turnOn();

					try {
						// Clear old application from lexer, and lex new token string
						Lexer.clear();
						Lexer.Lex(topcodes.codeNumbers(), ",");
						//Lexer.Lex("sampleProgram1.tang");
					} catch (SyntaxException e) {
						System.err.println("Lexing Failed");
						return;
					}

					System.out.println("Program Lexing Complete");

					// Parse the application. Statically creates an AST from the current Lexer
					Program p = new Program();
					p.parse();

					// Attach transmitter to interpreter
					interpreter = new InterpreterVisitor();
					interpreter.addTransmitter(bluetoothTransmitter);

					// Interpret Tangible application
					p.acceptPreOrder(interpreter);

					// Set LED states to show ready status
					readyLed.turnOn();
					busyLed.turnOff();
					
					runningTangableApplication = false;
				}
			}
		}
	}

	public class LeftSwitchPressedListener implements GpioPinListenerDigital, ActionListener{
		
		@Override
		public void handleGpioPinDigitalStateChangeEvent(
				GpioPinDigitalStateChangeEvent event) {
			LeftSwitchAction();
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			LeftSwitchAction();
		}
		
		public void LeftSwitchAction(){
			leftSwitchCount++;

			if((leftSwitchCount % 2) == 0){

				System.out.println("Shutdown Sequence Started...");

				String ls = "sudo halt";

					try {
						Runtime.getRuntime().exec(ls);
					} catch (IOException e) {
						e.printStackTrace();
					}
				
				System.exit(0);
			}
		}
	}

	public class RightSwitchPressedListener implements GpioPinListenerDigital, ActionListener{
		
		@Override
		public void handleGpioPinDigitalStateChangeEvent(
				GpioPinDigitalStateChangeEvent event) {
			RightSwitchAction();
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			RightSwitchAction();
		}
		
		public void RightSwitchAction(){
			rightSwitchCount++;

			if((rightSwitchCount % 2) == 0){
				System.out.println("Right Switch triggered");
				
				interpreter.transmitters.get(0).sendCommand(254);
				
			}
		}
	}
	
	// Application Entry Point
	public static void main(String[] args){
		new Application().start();
	}
}
