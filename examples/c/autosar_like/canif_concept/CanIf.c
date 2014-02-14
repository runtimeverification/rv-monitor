#include "CanIf.h"

void CanIf_Init() {
  __RVC_Can_If_if_init();
  CanIf_PreInit_InitController(5, 4);
}

int CanIf_SetControllerMode(int Controller) {
  __RVC_Can_If_set_controller_mode();
}

int CanIf_GetControllerMode(int Controller) {
  __RVC_Can_If_get_controller_mode();
}

int CanIf_Transmit() {
  __RVC_Can_If_transmit();
}

void CanIf_PreInit_InitController(int Controller, int ConfigurationIndex) {
  __RVC_Can_If_preinit_controller();
  CanIf_SetControllerMode(Controller);
}



