#include <Arduino.h>
#include "Flaps.hpp"

Flaps::Flaps (int pin) {
  analogPin = pin;
  value = 0;
}

void Flaps::read () {
  value = analogRead(analogPin);          // realizar la lectura analógica raw
  int newPosition = map(value, 0, 1023, 0, 100);  // convertir a porcentaje
  newPosition = newPosition;
  if (position != newPosition) {
      Serial.print("Flaps:");
    Serial.println(newPosition);
    position = newPosition;
  }
}
