#include <Arduino.h>
#include "MotorStep.hpp"


MotorStep::MotorStep () {

}

void MotorStep::move(char params[]) {
  int nStepper = (int) params[1] - 48;
  // el 0 está en 48 
  // Cada numero son 100 pies
  int n = (int) params[2] - 64;
  // 1000 pies - 53 pasos
  float onehundred = 52.8;
  // 50 pies
  float factor = onehundred/20.0;
  
  if (!stepper[nStepper]) {
    pinMode(nStepper, OUTPUT);
    pinMode(nStepper+1, OUTPUT);
    pinMode(nStepper+2, OUTPUT);
    pinMode(nStepper+3, OUTPUT);
  }
  
  Serial.print("write stepmotor_");
  Serial.print(nStepper);
  Serial.print(" | ");
  Serial.print(stepper[nStepper]);
  stepper[nStepper] += n * 50;
  Serial.print(" | ");
  Serial.print(stepper[nStepper]);
  Serial.print(" | ");
  Serial.print(n * 50);
  Serial.print("pies");
  Serial.print(" | ");
  Serial.print(n*factor);
  Serial.print(" pasos");

  //una vuelta 264 steps - byte c
  for(int s = 0; s<abs(n*factor); s++)
    n<0 ? stepForward(nStepper) : stepBackward(nStepper);

  digitalWrite(nStepper, 0);
  digitalWrite(nStepper+1, 0);
  digitalWrite(nStepper+2, 0);
  digitalWrite(nStepper+3, 0);
}

void MotorStep::stepForward(int nStepper) {
  for (int i = 0; i < 4; i++)
      {
        digitalWrite(nStepper, motorStepM[i][0]);
        digitalWrite(nStepper+1, motorStepM[i][1]);
        digitalWrite(nStepper+2, motorStepM[i][2]);
        digitalWrite(nStepper+3, motorStepM[i][3]);
        delay(2);
      }
}


void MotorStep::stepBackward(int nStepper) {
  for (int i = 0; i < 4; i++)
      {
        digitalWrite(nStepper, motorStepM[i][3]);
        digitalWrite(nStepper+1, motorStepM[i][2]);
        digitalWrite(nStepper+2, motorStepM[i][1]);
        digitalWrite(nStepper+3, motorStepM[i][0]);
        delay(2);
      }
};
