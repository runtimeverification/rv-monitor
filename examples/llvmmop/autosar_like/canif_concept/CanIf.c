#include "CanIf.h"

void CanIf_Init() {
  CanIf_PreInit_InitController(5, 4);
}

int CanIf_SetControllerMode(int Controller) {
  return 0;
}

int CanIf_GetControllerMode(int Controller) {
  return 0;
}

int CanIf_Transmit() {
  return 0;
}

void CanIf_PreInit_InitController(int Controller, int ConfigurationIndex) {
  CanIf_SetControllerMode(Controller);
}



