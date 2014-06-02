package emulation;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

import emulation.ControlBox.EmulatedLED;

public class GpioPinDigitalOutputWrapper {
	
	public boolean mode = false; // true = hardware, false = software
	
	public GpioPinDigitalOutput hardwareLed;
	
	public EmulatedLED softwareLed;
	
	public void setHardwareLed(GpioPinDigitalOutput hardwareLed){
		this.hardwareLed = hardwareLed;
		
		mode = true;
	}
	
	public void setSoftwareLed(EmulatedLED softwareLed){
		this.softwareLed = softwareLed;
		
		mode = false;
	}
	
	public void pulse(long milliseconds, boolean blockingCall){
		if(mode){
			hardwareLed.pulse(milliseconds, blockingCall);
		}else{
			softwareLed.pulse(milliseconds, blockingCall);
		}
	}
	
	public void blink(long milliseconds){
		if(mode){
			hardwareLed.blink(milliseconds);
		}else{
			softwareLed.blink(milliseconds);
		}
	}
	
	public void turnOn(){
		if(mode){
			hardwareLed.setState(PinState.HIGH);
		}else{
			softwareLed.turnOn();
		}
	}
	
	public void turnOff(){
		if(mode){
			hardwareLed.setState(PinState.LOW);
		}else{
			softwareLed.turnOff();
		}
	}
	
}
