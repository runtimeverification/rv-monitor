#include "CanIf.h"

void CanIf_Init() {
  CanIf_PreInit_InitController(5, 4);
}

int CanIf_SetControllerMode(int Controller) {
}

int CanIf_GetControllerMode(int Controller) {
}

int CanIf_Transmit() {
}

void CanIf_PreInit_InitController(int Controller, int ConfigurationIndex) {
  CanIf_SetControllerMode(Controller);
}



