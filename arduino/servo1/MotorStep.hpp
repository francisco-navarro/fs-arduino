#include <Math.h>
class MotorStep {
  private:
    long position[14];
    long speed[14];
    int motorStepM[4][4] =
      {
        {1, 1, 0, 0},
        {0, 1, 1, 0},
        {0, 0, 1, 1},
        {1, 0, 0, 1}
      };
  public: 
    MotorStep();
    void move(byte params[]);
    void stepForward(int nStepper);
    void stepBackward(int nStepper);
};
