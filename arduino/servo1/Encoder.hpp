#include <Math.h>

class Encoder {
    private:
        int val;
        int encoder0PinA;
        int encoder0PinB;
        int encoder0Pos = 0;
        int encoder0PinALast = LOW;
        int n = LOW;
        char sbuffer[50];
    public:
        Encoder(int a, int b);
        void read();
};
