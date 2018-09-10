#include <Math.h>
#include "ServoCustom.hpp"

float stepper[14];
int motorStepM [4][4] =
{
  {1, 0, 0, 0},
  {0, 1, 0, 0},
  {0, 0, 1, 0},
  {0, 0, 0, 1}
};

ServoCustom *servo;

String input;
int count = 0;
char order;
char params[5];

void setup() {
  servo = new ServoCustom();
  Serial.begin(115200);
  Serial.println("ready");
}

void loop() {
  if (Serial.available() > 0) {
    count = Serial.readBytesUntil('\n', params, 4);
    order = params[0];
    
    if(count > 0 && order !='\n') {
      if (order == 'a') {
        servo->attachServo(count, params);
      } else if (order == 'w') {
        servo->writeServo(count, params);
      } else if (order == 's') {
        motorStep();
      }
      Serial.println("_");
    }
  }
  delay (2);
}

void motorStep() {
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

void stepForward(int nStepper) {
  for (int i = 0; i < 4; i++)
      {
        digitalWrite(nStepper, motorStepM[i][0]);
        digitalWrite(nStepper+1, motorStepM[i][1]);
        digitalWrite(nStepper+2, motorStepM[i][2]);
        digitalWrite(nStepper+3, motorStepM[i][3]);
        delay(2);
      }
}

void stepBackward(int nStepper) {
  for (int i = 0; i < 4; i++)
      {
        digitalWrite(nStepper, motorStepM[i][3]);
        digitalWrite(nStepper+1, motorStepM[i][2]);
        digitalWrite(nStepper+2, motorStepM[i][1]);
        digitalWrite(nStepper+3, motorStepM[i][0]);
        delay(2);
      }
}
