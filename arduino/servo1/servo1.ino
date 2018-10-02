#include <Math.h>
#include "ServoCustom.hpp"
#include "MotorStep.hpp"

ServoCustom *servo;
MotorStep *motorStep;

String input;
int count = 0;
char order;
char params[5];

char sbuffer[50];

// -- encoder
int val;
int encoder0PinA = 3;
int encoder0PinB = 4;
int encoder0Pos = 0;
int encoder0PinALast = LOW;
int n = LOW;

void setup() {
  servo = new ServoCustom();
  motorStep = new MotorStep();

  // -- setup reads
  pinMode (encoder0PinA, INPUT);
  pinMode (encoder0PinB, INPUT);
  // -- end setup reads 
  Serial.begin(115200);
  Serial.println("ready");
  
}

void loop() {
  if (Serial.available() > 0) {
    count = Serial.readBytesUntil('\n', params, 4);
    order = params[0];
    
    if(count > 0 && order !='\n') {
      if (order == 'a') {
        servo->attachServo(count, params);
      } else if (order == 'w') {
        servo->writeServo(count, params);
      } else if (order == 's') {
        motorStep->move(params);
      }
      Serial.println("_");
    }
  }
  reads();
  delay (10);
}

String reads() {
  n = digitalRead(encoder0PinA);
  if ((encoder0PinALast == LOW) && (n == HIGH)) {
    if (digitalRead(encoder0PinB) == LOW) {
      encoder0Pos--;
      sprintf(sbuffer, "[encoder1:-]", encoder0Pos);
    } else {
      encoder0Pos++;
      sprintf(sbuffer, "[encoder1:+]", encoder0Pos);
    }
    //sprintf(sbuffer, "[encoder1:%d]", encoder0Pos);
    Serial.println(sbuffer);
  }
  encoder0PinALast = n;
}
