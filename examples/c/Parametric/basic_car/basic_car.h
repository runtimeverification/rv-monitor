#ifndef BASIC_CAR
#define BASIC_CAR

typedef struct car {
    int speed;
    int headlight_state;
    int windshield_wiper_state;
    char * driver_name;
} car;

car * init_car(char * driver_name);
void accelerate(car * my_car, int speed);
void toggle_headlights(car * my_car);
void toggle_wipers(car * my_car);
void destroy(car * my_car);

#endif
