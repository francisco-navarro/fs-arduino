#include <Math.h>
#include "ServoCustom.hpp"
#include "MotorStep.hpp"

ServoCustom *servo;
MotorStep *motorStep;

String input;
int count = 0;
char order;
char params[5];

void setup() {
  servo = new ServoCustom();
  motorStep = new MotorStep();
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
  delay (2);
}
