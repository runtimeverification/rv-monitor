#ifndef CAN_IF
#define CAN_IF

void CanIf_Init();

int CanIf_SetControllerMode(int Controller);

int CanIf_GetControllerMode(int Controller);

int CanIf_Transmit();

void CanIf_PreInit_InitController(int Controller, int ConfigurationIndex);

#endif
