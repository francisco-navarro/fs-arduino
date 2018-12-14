#include <Arduino.h>
#include "Switch.hpp"
#include <Math.h>

Switch::Switch (int a) {
  pin = a;
  pinMode (pin, OUTPUT);
}


void Switch::read () {
  int val = digitalRead(pin);
  if (val == 1 &&  millis() - time > 500) {
    time = millis();
    sprintf(sbuffer, "[switch%d:1]", pin);
    Serial.println(sbuffer);
  }
}
