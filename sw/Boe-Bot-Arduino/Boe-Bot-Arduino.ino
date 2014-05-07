// Tangible Robot
// Scott Thomson 20 March 2014

// Designed for BOEBot with Arduino Shield and Arduino Uno or Duemilanove
// Receives commands via a Bluetooth Serial port to move the robot based on the command received.

// Receives 0xFE -> Responds with 0xFE -> Beeps -> Sends 0xFD
// Receives l, r, f, b -> Moves robot -> Sends 0xFD

// Version 0.04
// Version Notes
// 0.01 - Untested but compiles.
// 0.02 - Tested - Responds to serial commands correctly, Fixed servo pin allocation moved to pins 10 and 11.
// 0.03 - Untested - Modified delays for movement to be #defines to make it easier to adjust them. Defines for ACK and DONE too.
// 0.04 - Tested on Boe-Bot - Delays modified for 15cm movement. Additional confirmation beeps added.

// Connection Details
// Left Motor on Pin 11
// Right Motor on Pin 10
// Piezo Buzzer on Pin 4

#include <Servo.h>

// Movement Delays
// Turn Delay is how long the robot will move to complete a 90 degree turn
#define TURN_DELAY 530
// Turn Delay is how long the robot will move to move the length of the robot (1 unit)
#define MOVE_DELAY 789

//ICD Definitions
//ACK is the response sent when the Arduino recieves an expected command from via the serial link
#define ACK 0xFE
//DONE is the response sent when the Arduino has completed the commanded action.
#define DONE 0xFD

Servo servoLeft;
Servo servoRight;

void setup()
{
  Serial.begin(9600);
  tone(4, 3000, 1000);
  delay(1000);
  servoLeft.attach(11);
  servoRight.attach(10);
  servoLeft.writeMicroseconds(1500);     // Stop the motors so their is no jitter
  servoRight.writeMicroseconds(1500);  
}

void loop()
{
  digitalWrite(13, HIGH);   // set the LED on
  delay(1000);              // wait for a second
  digitalWrite(13, LOW);    // set the LED off
  delay(1000);              // wait for a second     
}

void serialEvent()
{
  while(Serial.available())
  {
    char inChar = (char)Serial.read();
    if (inChar == ACK || inChar == 't')
    {
      Serial.write(ACK);  //echo to say we are here.
      tone(4, 3000, 250);  //beep to make the connection known
      delay(500);
      tone(4, 3000, 250);  //beep to make the connection known
      Serial.write(DONE);  //echo to say we are done.
    }
    else if (inChar == 'f')
    {
      Serial.write(ACK);  //echo to say we are here.
      moveForward();
      Serial.write(DONE);  //echo to say we are done. 
    }
    else if (inChar == 'b')
    {
      Serial.write(ACK);  //echo to say we are here.
      moveBackward();
      Serial.write(DONE);  //echo to say we are done. 
    }
    else if (inChar == 'l')
    {
      Serial.write(ACK);  //echo to say we are here.
      turnLeft();
      Serial.write(DONE);  //echo to say we are done. 
    }
    else if (inChar == 'r')
    {
      Serial.write(ACK);  //echo to say we are here.
      turnRight();
      Serial.write(DONE);  //echo to say we are done. 
    }
  } 
}

void turnLeft()
{
  servoLeft.writeMicroseconds(1300);     // Rotate left in place
  servoRight.writeMicroseconds(1300);
  delay(TURN_DELAY);
  servoLeft.writeMicroseconds(1500);     // Stop
  servoRight.writeMicroseconds(1500); 
}

void turnRight()
{
  servoLeft.writeMicroseconds(1700);     // Rotate right in place
  servoRight.writeMicroseconds(1700);
  delay(TURN_DELAY);
  servoLeft.writeMicroseconds(1500);     // Stop
  servoRight.writeMicroseconds(1500); 
}

void moveForward()
{
  servoLeft.writeMicroseconds(1700);     // Full speed forward
  servoRight.writeMicroseconds(1300);
  delay(MOVE_DELAY);
  servoLeft.writeMicroseconds(1500);     // Stop
  servoRight.writeMicroseconds(1500); 
}

void moveBackward()
{
  servoLeft.writeMicroseconds(1300);     // Full speed back
  servoRight.writeMicroseconds(1700);
  delay(MOVE_DELAY);
  servoLeft.writeMicroseconds(1500);     // Stop
  servoRight.writeMicroseconds(1500); 
}






