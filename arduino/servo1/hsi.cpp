#include <Arduino.h>
#include "hsi.hpp"

HSI::HSI () {
  
}


void HSI::move(byte params[]) {
  
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
