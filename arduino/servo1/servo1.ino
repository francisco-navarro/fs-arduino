#include <Math.h>
#include "ServoCustom.hpp"
#include "MotorStep.hpp"
#include "Encoder.hpp"

ServoCustom *servo;
MotorStep *motorStep;
Encoder *encoder1;

String input;
int count = 0;
char order;
char params[8];


void setup() {
  servo = new ServoCustom();
  motorStep = new MotorStep();
  encoder1 = new Encoder(3, 4);
  Serial.begin(115200);
  Serial.println("ready");
  
}

void loop() {
  if (Serial.available() > 0) {
    count = Serial.readBytesUntil('\n', params, 8);
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
  encoder1 -> read();
}
