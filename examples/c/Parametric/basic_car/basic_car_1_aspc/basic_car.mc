#include <stdlib.h>
#include <stdio.h>

typedef struct car {
    int speed;
    int headlight_state;
    int windshield_wiper_state;
    char * driver_name;
} car;

car * init_car(char * driver_name) {
    car * my_car = malloc(sizeof(car));
    my_car->speed = 0;
    my_car->headlight_state = 0;
    my_car->windshield_wiper_state = 0;
    my_car->driver_name = driver_name;
    return my_car;
}

void accelerate(car * my_car, int speed) {
    my_car->speed += speed;
}

void toggle_headlights(car * my_car) {
    my_car->headlight_state = !my_car->headlight_state;
}

void toggle_wipers(car * my_car) {
    my_car->windshield_wiper_state = !my_car->windshield_wiper_state;
}

void destroy(car * my_car) {
    free(my_car);
}

int main(void) {
    car *car1, *car2, *car3;
    fprintf(stdout, "Test 1\n");
    fprintf(stderr, "Test 1\n");

    car1 = init_car("Kevin");
    car2 = init_car("Britney");
    destroy(car1);

    toggle_headlights(car2);
    toggle_wipers(car2);
    accelerate(car2, 250);
    // This should fail
    toggle_headlights(car1);

    destroy(car2);
}
