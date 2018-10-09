#include <Arduino.h>
#include <Wire.h>
#include <Adafruit_PWMServoDriver.h>
#include "ServoCustom.hpp"

ServoCustom::ServoCustom () {
  servos = Adafruit_PWMServoDriver(0x40);
  servos.begin();   
  servos.setPWMFreq(60); 
  // AJustar para el servo
  pos0=172; // ancho de pulso en cuentas para pocicion 0°
  pos180=565; // ancho de pulso en cuentas para la pocicion 180°

}

void ServoCustom::attachServo (int count, byte params[]) {
  // Con la placa no hacemos
  /*if (count>1) {
    int n = (int) params[1] - 48;
    myservo[n].attach(n);
    Serial.print("servo attached");
    Serial.println(n);
  }*/
}

void ServoCustom::writeServo (int count, byte params[]) {
  int deg = 0;
  int duty;
  // w - 4 - ZZ
  // El segundo parametro es de un byte o dos, empieza enel espacio (32)
  if (count > 2) {
    uint8_t n = (uint8_t) params[1] - 48;
    deg = (int) params[2] - 30;
    if(count > 3 && params[3] > 30) {
      deg += (int) params[3] - 30;
    }
    Serial.print("servo ");
    Serial.print(count);
    Serial.print(" -> ");
    Serial.println(deg);
    duty=map(deg, 0, 180, 172, 565);
    servos.setPWM(n, 0, duty);
  }
}

void ServoCustom::detachServo (int n) {
  // myservo[n].detach();
  // No hacemos
};
