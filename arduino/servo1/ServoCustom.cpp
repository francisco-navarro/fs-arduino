#include <Arduino.h>
#include <Servo.h>
#include "ServoCustom.hpp"

ServoCustom::ServoCustom () {
  // de momento nada
}

void ServoCustom::attachServo (int count, char params[]) {
  if (count>1) {
    int n = (int) params[1] - 48;
    myservo[n].attach(n);
    Serial.print("servo attached");
    Serial.println(n);
  }
}

void ServoCustom::writeServo (int count, char params[]) {
  int deg = 0;
  // w - 4 - ZZ
  // El segundo parametro es de un byte o dos, empieza enel espacio (32)
  if (count > 2) {
    int n = (int) params[1] - 48;
    deg = (int) params[2] - 30;
    if(count > 3 && params[3] > 30) {
      deg += (int) params[3] - 30;
    }
    Serial.print("write ");
    Serial.print(n);
    Serial.print(" -> ");
    Serial.println(deg);
    myservo[n].write(deg);
  }
}

void ServoCustom::detachServo (int n) {
  myservo[n].detach();
};
