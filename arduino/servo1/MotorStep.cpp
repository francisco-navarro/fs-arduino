#include <Arduino.h>
#include "MotorStep.hpp"


MotorStep::MotorStep () {
  for(int i=0; i<14;i++)
    position[i] = 0;
}

void MotorStep::move(char params[]) {
  int nStepper = (int) params[1] - 48;
  // el 0 está en 48 
  // Cada numero son 1 step
  Serial.println("stepmotor order");
  
  int n = (uint32_t)params[2]<< 12
    | (uint32_t)params[3]<< 8
    |(uint32_t)params[4] << 4
    | (uint32_t)params[5];

  int diference = n - position[nStepper];
  
  if (!position[nStepper]) {
    Serial.print("setting pins from stepper");
    Serial.println(nStepper);
    pinMode(nStepper, OUTPUT);
    pinMode(nStepper+1, OUTPUT);
    pinMode(nStepper+2, OUTPUT);
    pinMode(nStepper+3, OUTPUT);
  }
  Serial.print("write stepmotor_");
  Serial.print(nStepper);
  Serial.print(" | ");
  Serial.print(diference);
  Serial.print(" pasos");
  position[nStepper] += diference;

  //una vuelta 520 steps - byte c
  for(int s = 0; s<abs(diference); s++)
    diference<0 ? stepForward(nStepper) : stepBackward(nStepper);

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
