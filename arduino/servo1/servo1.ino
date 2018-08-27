/* Sweep
 by BARRAGAN <http://barraganstudio.com>
 This example code is in the public domain.

 modified 8 Nov 2013
 by Scott Fitzgerald
 http://www.arduino.cc/en/Tutorial/Sweep
*/

#include <Servo.h>

Servo myservo[14];

String input;
String order;
String param1;
String param2;

void setup() {
  
  Serial.begin(9600);
  Serial.println("Arduino ready");
}

void loop() {
  
  if (Serial.available() > 0) {
    input = Serial.readString();
    input.trim();

    if(input.length() > 2) {
      // Captura : ORDEN;PARAM1;PARAM2;
      order = input.substring(0,3);
      param1 = input.substring(4,6);
      param2 = input.substring(7,10);
  
      //Serial.println("Order "+ order + " param " + param1 + "," + param2);

      // ATT - SERVO ATTACH - N
      if (order.equalsIgnoreCase("ATT"))
        attachServo(param1.toInt());
      // WSR - WRITE SERVO - N - POS
      if (order.equalsIgnoreCase("WSR"))
        writeServo(param1.toInt(), param2.toInt());
    }
    

    input = "";
  }
  delay (400);   
}

void attachServo(int n) {
  delay(500);
  Serial.println("\nAttach servo "+n);
  myservo[n].attach(n);
}

void writeServo(int n, int pos) {
  myservo[n].write(pos);
}

void detachServo(int n) {
  Serial.println("\nDetach servo "+n);
  myservo[n].detach();
}
