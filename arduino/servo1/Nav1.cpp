#include <Arduino.h>
#include "Nav1.hpp"
#include <LedControl.h>

Nav1::Nav1(int pin) {
  lc = new LedControl(38, 39, 40, 3);

  lc->shutdown(0, false); // The MAX72XX is in power-saving mode on startup, we have to do a wakeup call
  lc->setIntensity(0, 1); // Set the brightness to a medium values
  lc->clearDisplay(0);    // and clear the display

  // Display 1 inicializacion
  lc->shutdown(1, false); // The MAX72XX is in power-saving mode on startup, we have to do a wakeup call
  lc->setIntensity(1, 1); // Set the brightness to a medium values
  lc->clearDisplay(1);    // and clear the display
  // Display 2 inicializacion
  lc->shutdown(2, false); // The MAX72XX is in power-saving mode on startup, we have to do a wakeup call
  lc->setIntensity(2, 1); // Set the brightness to a medium values
  lc->clearDisplay(2);    // and clear the display
}

void Nav1::send(int count, byte params[]) {
  if (count == 4) {
    if (params[1] == '1') {
      Serial.println("Changing nav1 s");
      int num = params[2];
      int num2 = params[3];
      itoa((int)params[2], cstr, 10);


      lc->setChar(2, 4, '1', false);
      if (params[2] >= 10) {
        lc->setChar(2, 3, cstr[0], false);
        lc->setChar(2, 2, cstr[1], true);
      } else {
        lc->setChar(2, 3, '0', false);
        lc->setChar(2, 2, cstr[0], true);
      }


      itoa((int)params[3], cstr, 10);
      lc->setChar(2, 1, cstr[0], false);
      lc->setChar(2, 0, cstr[1], false);
    } else if (params[1] == '2') {
      Serial.println("Changing nav1 s");
      int num = params[2];
      int num2 = params[3];
      itoa((int)params[2], cstr, 10);


      lc->setChar(1, 2, '1', false);
      if (params[2] >= 10) {
        lc->setChar(1, 1, cstr[0], false);
        lc->setChar(1, 0, cstr[1], true);
      } else {
        lc->setChar(1, 1, '0', false);
        lc->setChar(1, 0, cstr[0], true);
      }


      itoa((int)params[3], cstr, 10);
      lc->setChar(2, 6, cstr[0], false);
      lc->setChar(2, 7, cstr[1], false);
    }  else if (params[1] == '3') {
      Serial.println("Changing com1 s");
      int num = params[2];
      int num2 = params[3];
      itoa((int)params[2], cstr, 10);


      lc->setChar(0, 0, '1', false);
      if (params[2] >= 10) {
        lc->setChar(1, 7, cstr[0], false);
        lc->setChar(1,6, cstr[1], true);
      } else {
        lc->setChar(1, 7, '0', false);
        lc->setChar(1, 6, cstr[0], true);
      }


      itoa((int)params[3], cstr, 10);
      lc->setChar(1, 5, cstr[0], false);
      lc->setChar(1, 4, cstr[1], false);
    } else if (params[1] == '4') {
      Serial.println("Changing com1 ");
      int num = params[2];
      int num2 = params[3];
      itoa((int)params[2], cstr, 10);


      lc->setChar(0, 7, '1', false);
      if (params[2] >= 10) {
        lc->setChar(0, 6, cstr[0], false);
        lc->setChar(0,5, cstr[1], true);
      } else {
        lc->setChar(0, 6, '0', false);
        lc->setChar(0, 5, cstr[0], true);
      }


      itoa((int)params[3], cstr, 10);
      lc->setChar(0, 4, cstr[0], false);
      lc->setChar(0, 3, cstr[1], false);
    }
  }
}
