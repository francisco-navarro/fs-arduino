#include <LedControl.h>

class Nav1 {
  private:
    int count = 0;
    LedControl *lc;
    char cadena[8];
    String cadena2;
    int amount = 253;
  public: 
    Nav1(int pin);
};
