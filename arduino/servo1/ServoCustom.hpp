#include <Servo.h>
#include <Wire.h>
#include <Adafruit_PWMServoDriver.h>

class ServoCustom {
  private:
    Adafruit_PWMServoDriver servos;
    unsigned int pos0; // ancho de pulso en cuentas para pocicion 0°
    unsigned int pos180; // ancho de pulso en cuentas para la pocicion 180°
  public:
    ServoCustom();
    void attachServo(int count, char params[]);
    void writeServo(int count, char params[]);
    void detachServo(int n);
};
