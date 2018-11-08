#include <Arduino.h>
#include "hsi.hpp"

const int motor1 = 34;
const int motor2 = 38;
const int switchPos = 32;

HSI::HSI () {
  
  int readswitch = 1;
  Serial.println("Init HSI");
  // initialize
  pinMode(motor1, OUTPUT);
  pinMode(motor1+1, OUTPUT);
  pinMode(motor1+2, OUTPUT);
  pinMode(motor1+3, OUTPUT);

  while(readswitch == 1) {
    readswitch = digitalRead(32);
    stepBackward(motor1);
  }
    
    
  Serial.println("End Init HSI 1");
  delay(250);
  
  pinMode(motor2, OUTPUT);
  pinMode(motor2+1, OUTPUT);
  pinMode(motor2+2, OUTPUT);
  pinMode(motor2+3, OUTPUT);
  readswitch = 1;
  
  while(readswitch == 1) {
    stepBackward(motor2);
    readswitch = digitalRead(32);
  }

    digitalWrite(motor1,0);
    digitalWrite(motor1+1, 0);
    digitalWrite(motor1+2, 0);
    digitalWrite(motor1+3, 0);

    digitalWrite(motor2,0);
    digitalWrite(motor2+1, 0);
    digitalWrite(motor2+2, 0);
    digitalWrite(motor2+3, 0);
  
   Serial.println("End Init HSI 2");
}


void HSI::move(byte params[]) {
  
  int nStepper = (int) params[1] - 48;
  Serial.print("Move to motor ");
  Serial.print(nStepper);
  long n = //(uint8_t)params[2]<< 24 |
    (uint8_t)params[3]<< 16 |
     ((uint8_t) params[4]) << 8 & 0xFF00 |
     ((uint8_t)params[5]) & 0xFF;

  Serial.print(" degrees ");
  Serial.println(n);
}


void HSI::stepForward(int nStepper) {
  for (int i = 0; i < 4; i++)
      {
        digitalWrite(nStepper, motorStepM[i][0]);
        digitalWrite(nStepper+1, motorStepM[i][1]);
        digitalWrite(nStepper+2, motorStepM[i][2]);
        digitalWrite(nStepper+3, motorStepM[i][3]);
        delay(2);
      }
}


void HSI::stepBackward(int nStepper) {
  for (int i = 0; i < 4; i++)
      {
        digitalWrite(nStepper, motorStepM[i][3]);
        digitalWrite(nStepper+1, motorStepM[i][2]);
        digitalWrite(nStepper+2, motorStepM[i][1]);
        digitalWrite(nStepper+3, motorStepM[i][0]);
        delay(2);
      }
};
