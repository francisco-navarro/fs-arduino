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
int count = 0;
char order;
char params[5];

void setup() {
  
  Serial.begin(19200);
  Serial.println("ready");
}

void loop() {
  
  if (Serial.available() > 0) {
    count = Serial.readBytes(params, 5);
    order = params[0];
    
    if(count > 0 && order !='\n') {
      // attach - a - 97
      if (order == 'a') {
        attachServo();
      } else if (order == 'w') {
        // w49
        writeServo();
      } else {
        Serial.println("_");
      }
    }
  }
  delay (10);   
}

int n = 0;
int deg = 0;
void attachServo() {
  
  if (count>1) {
    n = (int) params[1] - 48;
    myservo[n].attach(n);
    Serial.println("servo attached");
  }
}

void writeServo() {
  // w - 4 - ZZ
  // El segundo parametro es de un byte o dos, empieza enel espacio (32)
  if (count > 2) {
    n = (int) params[1] - 48;
    deg = (int) params[2] - 30;
    if(count > 3 && params[3] > 30) {
      deg += (int) params[3] - 30;
    }
    Serial.print("write ");
    Serial.println(deg);
    myservo[n].write(deg);
  }
  
}

void detachServo(int n) {
  myservo[n].detach();
}
