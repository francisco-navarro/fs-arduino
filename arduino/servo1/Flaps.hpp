#include <Math.h>

class Flaps {
    private:
        int analogPin;
        int value;
        int position;
    public:
        Flaps(int pin);
        void read();
};
