#include <Math.h>

class Switch {
    private:
        int pin;
        char sbuffer[50];
        unsigned long time;
    public:
        Switch(int a);
        void read();
};
