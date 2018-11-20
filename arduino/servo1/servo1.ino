#include <Math.h>
#include "ServoCustom.hpp"
#include "MotorStep.hpp"
#include "Encoder.hpp"
#include "Flaps.hpp"
#include "hsi.hpp"
#include "Nav1.hpp"

ServoCustom *servo;
MotorStep *motorStep;
Encoder *encoder1;
Flaps *flaps;
HSI *hsi;
Nav1 *nav1;

String input;
int count = 0;
char order;
byte params[8];


void setup() {
  servo = new ServoCustom();
  motorStep = new MotorStep();
  encoder1 = new Encoder(30, 31);
  flaps = new Flaps(A1);
  nav1 = new Nav1(38);
    Serial.begin(115200);
  //hsi = new HSI();

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
      } else if (order == 'h') {
        hsi->move(params);
      } else if (order === 'n') {
        
      }
      Serial.println("_");
    }
  }
  reads();
  delay (10);
}

String reads() {
  encoder1 -> read();
  //flaps -> read();
}
