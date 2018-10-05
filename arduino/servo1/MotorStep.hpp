class MotorStep {
  private:
    long position[14];
    int motorStepM[4][4] =
      {
        {1, 0, 0, 0},
        {0, 1, 0, 0},
        {0, 0, 1, 0},
        {0, 0, 0, 1}
      };
  public: 
    MotorStep();
    void move(char params[]);
    void stepForward(int nStepper);
    void stepBackward(int nStepper);
};
