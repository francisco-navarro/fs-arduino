#include <Servo.h>

class ServoCustom {
  private:
    Servo myservo[14];
  public:
    ServoCustom();
    void attachServo(int count, char params[]);
    void writeServo(int count, char params[]);
    void detachServo(int n);
};
