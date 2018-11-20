#include <Arduino.h>
#include "Nav1.hpp"
#include <LedControl.h>

Nav1::Nav1 (int pin) {
  lc = &LedControl(38,39,40,3);

  lc->shutdown(0,false); // The MAX72XX is in power-saving mode on startup, we have to do a wakeup call
  lc->setIntensity(0,1); // Set the brightness to a medium values
  lc->clearDisplay(0);   // and clear the display

  // Display 1 inicializacion
  lc->shutdown(1,false); // The MAX72XX is in power-saving mode on startup, we have to do a wakeup call
  lc->setIntensity(1,1); // Set the brightness to a medium values
  lc->clearDisplay(1);   // and clear the display
  // Display 1 inicializacion
  lc->shutdown(2,false); // The MAX72XX is in power-saving mode on startup, we have to do a wakeup call
  lc->setIntensity(2,1); // Set the brightness to a medium values
  lc->clearDisplay(2);   // and clear the display

  delay(500);

  cadena2 = String(amount);

  lc->setChar(0,6,cadena2.charAt(0),false);  // Muestra ese caracter
  lc->setChar(0,5,cadena2.charAt(1),true);
  lc->setChar(0,4,cadena2.charAt(2),false);
  cadena2 = String(amount+317);
  lc->setChar(1,6,cadena2.charAt(0),false);  // Muestra ese caracter
  lc->setChar(1,5,cadena2.charAt(1),true);
  lc->setChar(1,4,cadena2.charAt(2),false);
  lc->setChar(1,3,'-',false);

 lc->setChar(2,1,cadena2.charAt(2),false);
 lc->setChar(2,2,cadena2.charAt(2),false);
 lc->setChar(2,3,cadena2.charAt(2),false);
 lc->setChar(2,4,cadena2.charAt(2),false);
 lc->setChar(2,5,cadena2.charAt(2),false);
}
