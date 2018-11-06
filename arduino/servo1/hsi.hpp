#include <Math.h>
class HSI {
  private:
    int motorStepM[4][4] =
      {
        {1, 1, 0, 0},
        {0, 1, 1, 0},
        {0, 0, 1, 1},
        {1, 0, 0, 1}
      };
    int heading = 0;
  public: 
    HSI();
    void move(byte params[]);
    void stepForward(int nStepper);
    void stepBackward(int nStepper);
};
