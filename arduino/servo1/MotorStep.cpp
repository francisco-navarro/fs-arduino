#include <Arduino.h>
#include <Math.h>
#include "MotorStep.hpp"


MotorStep::MotorStep () {
  for(int i=0; i<14;i++)
    position[i] = 0;
}

void MotorStep::move(byte params[]) {
  int nStepper = (int) params[1] - 48;
  // el 0 está en 48 
  // Cada numero son 1 step
  //Serial.print("stepmotor order ");
  short symbol = 1;
  short lastSymbol = 1;
  short margin = 0;
  long n = //(uint8_t)params[2]<< 24 |
    (uint8_t)params[3]<< 16 |
     ((uint8_t) params[4]) << 8 & 0xFF00 |
     ((uint8_t)params[5]) & 0xFF;

     

  int diference = n - position[nStepper];
  int lastSpeed = speed[nStepper];

  if(abs(position[nStepper]) > 1) {
    symbol = diference<0 ? -1 : 1;
    lastSymbol = lastSpeed<0 ? -1 : 1;
    //if(abs(diference) < 15) diference += symbol;
    if(abs(diference) < 4) diference += symbol;
    //if(symbol!=lastSymbol && abs(diference) < 10) {
      //Serial.print(" Change direction ");
      //margin += symbol;
    //}
    
  }
  //Serial.print(" to motor ");Serial.print(nStepper);

  if (abs(diference) > 1 && abs(diference)<1000){
   //Serial.print(" diference ");Serial.print(diference);
    
    
    //Serial.print(": "); 
    //Serial.println(n);
     
    if (!position[nStepper]) {
      pinMode(nStepper, OUTPUT);
      pinMode(nStepper+1, OUTPUT);
      pinMode(nStepper+2, OUTPUT);
      pinMode(nStepper+3, OUTPUT);
    }
  
    //una vuelta 520 steps - byte c
    for(int s = 0; s<abs(diference + margin); s++)
      diference<0 ? stepForward(nStepper) : stepBackward(nStepper);
  
    position[nStepper] += diference;
    speed[nStepper] = diference;
  
    digitalWrite(nStepper, 0);
    digitalWrite(nStepper+1, 0);
    digitalWrite(nStepper+2, 0);
    digitalWrite(nStepper+3, 0);
  } else {
    Serial.print("-");
  }
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
