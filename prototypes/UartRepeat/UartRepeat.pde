/**********************************************************
* Repeats data received by the UART peripheral.
* Configured to work with the HC-05 Bluetooth module.
***********************************************************/
byte SerialInByte;
byte ArudinoLed = 13; // LED on pin 13
byte ToggleBit = 0x00;

void setup() {
  /* Default for HC-05 is 9600 baud, 8 bitm no parity, 1 stop */
  Serial.begin(9600);
  
  /* Wait for serial port to connect. Needed for Leonardo only */
  //while (!Serial);
  
  Serial.println("Send a Byte (0-255) to the Arduino");
  
  /* Arduino board LED conf */
  pinMode(ArudinoLed, OUTPUT);
}

void loop() {
  /* Wait for data  */
  if( Serial.available() > 0 ) {    
    SerialInByte = Serial.read();
    Serial.println("Received: 0x");
    Serial.println(SerialInByte, HEX);
    
    /* Toggle LED, not ideal but fast*/
    digitalWrite(ArudinoLed, (ToggleBit&0x01));  // if it's a 0 (zero) tun LED off
    ToggleBit++;
  }
  
  delay(100);
}
