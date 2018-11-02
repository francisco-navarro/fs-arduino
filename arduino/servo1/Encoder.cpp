#include <Arduino.h>
#include "Encoder.hpp"
#include <Math.h>

Encoder::Encoder (int a, int b) {
  encoder0PinA = a;
  encoder0PinB = b;
  pinMode (encoder0PinA, INPUT);
  pinMode (encoder0PinB, INPUT);
}


void Encoder::read () {
  n = digitalRead(encoder0PinA);
  if ((encoder0PinALast == LOW) && (n == HIGH)) {
    if (digitalRead(encoder0PinB) == LOW) {
      encoder0Pos--;
      sprintf(sbuffer, "[encoder%d:-]", encoder0PinA, encoder0Pos);
    } else {
      encoder0Pos++;
      sprintf(sbuffer, "[encoder%d:+]", encoder0PinA, encoder0Pos);
    }
    //sprintf(sbuffer, "[encoder1:%d]", encoder0Pos);
    Serial.println(sbuffer);
  }
  encoder0PinALast = n;
}
