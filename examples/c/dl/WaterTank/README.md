This example demonstrates RV monitor's dL monitoring capabilities using
a simple waterTank simulation.

Setup
-----

 - Install RV-Monitor by calling `mvn package` at the top level.
   Maven will take care of downloading and installing the correct version
   of KeyMaeraX.
 - Install Mathamatica (version 10) or above. Configure KeYmaera X to use
   Mathematica using the Web UI. The KeYmaera X web-UI can started (from the top
   level as follows) using `java -jar rv-monitor/src/main/resources/keymaerax.jar`.
   Instructions for configuring Mathematica can also be found at the
   [KeYmaera X's source](https://github.com/LS-Lab/KeYmaeraX-release#configuration).


Running
-------

  - Run make to synthesize the monitor library and an instrumented simulation binary.
  - The dL specification is defined in file level_limit.rvm. The formula to
    monitor is provided under the "dL" tag. RV-Monitor will attempt to prove the
    formula using a simple tactic, and call ModelPlex to synthesize a C model monitor.
  - Run the instrumented binary as such `./test`. DL violations in the
    simulation are printed to stdout.
