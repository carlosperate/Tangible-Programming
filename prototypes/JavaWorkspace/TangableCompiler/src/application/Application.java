package application;

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
import interpreter.InterpreterVisitor;
import lexer.Lexer;
import ast.Program;
import core.LanguageDefinition;
import exceptions.SyntaxException;

public class Application {

	/**
	 * Global Bluetooth Transmitter
	 */
	final BluetoothTransmitter bluetoothTransmitter = new BluetoothTransmitter();

	/**
	 * GPIO Controller, Handling GPIO. Uses Singleton pattern
	 */
	final GpioController gpio = GpioFactory.getInstance();

	/**
	 * Pin to control Bluetooth status output
	 */
	private GpioPinDigitalOutput bluetoothCommLed;

	/**
	 * Pin to control ready LED
	 */
	private GpioPinDigitalOutput readyLed;

	/**
	 * Pin to control busy LED 
	 */
	private GpioPinDigitalOutput busyLed;

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

	public void start(){

		/**
		 * Setup HW GPIO Configuration
		 */
		System.out.println("Setting up GPIO Outputs...");

		bluetoothCommLed = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "Bluetooth Communication Status LED", PinState.LOW);

		readyLed = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "Ready LED", PinState.LOW);

		busyLed = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "Busy LED", PinState.LOW);

		System.out.println("Setting up GPIO Inputs...");

		goButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_06, "Go Button", PinPullResistance.PULL_UP);

		leftSwitch = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05, "Left Switch", PinPullResistance.PULL_UP);

		rightSwitch = gpio.provisionDigitalInputPin(RaspiPin.GPIO_01, "Right Switch", PinPullResistance.PULL_UP);
		
		bluetoothCommLed.setShutdownOptions(true, PinState.LOW);
		readyLed.setShutdownOptions(true, PinState.LOW);
		busyLed.setShutdownOptions(true, PinState.LOW);

		System.out.println("Testing LED's");
		// Switch each LED on for 1 second to show LED out working
		bluetoothCommLed.pulse(1000, true);
		readyLed.pulse(1000, true);
		busyLed.pulse(1000, true);

		//TODO devise a method to test inputs?
		goButton.addListener(new GoButtonPressedListener());
		leftSwitch.addListener(new LeftSwitchPressedListener());
		rightSwitch.addListener(new RightSwitchPressedListener());

		if(!LanguageDefinition.getInstance().SetupLanguage("langdefinition.xml")){
			System.err.println("Language Setup Failed");
			return;
		}

		System.out.println("Language Generation Complete");

		// Setup a new bluetoothTransmitter
		bluetoothTransmitter.setup(bluetoothCommLed);

		System.out.println("Entering Application Loop..");

		readyLed.setState(PinState.HIGH);
		
		while(true){
			// infinite loop while application is in use.
			// from this point the application is completely event driven
		}
	}

	public class GoButtonPressedListener implements GpioPinListenerDigital{
		@Override
		public void handleGpioPinDigitalStateChangeEvent(
				GpioPinDigitalStateChangeEvent event) {

			// prevents a 2 events being fired
			goButtonCount++;
			if((goButtonCount % 2) == 0){

				if(!runningTangableApplication){
					runningTangableApplication = true;
					
					// Switch LED states to show busy status
					readyLed.setState(PinState.LOW);
					busyLed.setState(PinState.HIGH);
					
					//TODO Code to capture image from webcam, and produce sample application string

					try {
						// Clear old application from lexer, and lex new token string
						Lexer.clear();
						Lexer.Lex("sampleProgram1.tang");
					} catch (SyntaxException e) {
						System.err.println("Lexing Failed");
						return;
					}

					System.out.println("Program Lexing Complete");

					// Parse the application. Statically creates an AST from the current Lexer
					Program p = new Program();
					p.parse();

					// Attach transmitter to interpreter
					InterpreterVisitor interpreter = new InterpreterVisitor();
					interpreter.addTransmitter(bluetoothTransmitter);

					// Interpret Tangible application
					p.acceptPreOrder(interpreter);

					// Set LED states to show ready status
					readyLed.setState(PinState.HIGH);
					busyLed.setState(PinState.LOW);
					
					runningTangableApplication = false;
				}
			}
		}
	}

	public class LeftSwitchPressedListener implements GpioPinListenerDigital{
		@Override
		public void handleGpioPinDigitalStateChangeEvent(
				GpioPinDigitalStateChangeEvent event) {

			leftSwitchCount++;

			if((leftSwitchCount % 2) == 0){

				System.out.println("Shutdown Sequence Started...");

				// TODO Shutdown Bluetooth correctly
				// TODO Any Shutdown require by BoeBot??
				
				System.exit(0);
			}
		}
	}

	public class RightSwitchPressedListener implements GpioPinListenerDigital{
		@Override
		public void handleGpioPinDigitalStateChangeEvent(
				GpioPinDigitalStateChangeEvent event) {
			rightSwitchCount++;

			if((rightSwitchCount % 2) == 0){
				System.out.println(event.getPin().getName() + " triggered Pressed");
				
				// TODO Define function of this switch
			}
		}
	}
	
	// Application Entry Point
	public static void main(String[] args){
		new Application().start();
	}
}
