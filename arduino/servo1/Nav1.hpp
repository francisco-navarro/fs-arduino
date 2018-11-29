#include <LedControl.h>

class Nav1 {
  private:
    int count = 0;
    LedControl *lc;
    char cstr[16];
    int amount = 253;
  public: 
    Nav1(int pin);
    void send(int count, byte params[]);
};
