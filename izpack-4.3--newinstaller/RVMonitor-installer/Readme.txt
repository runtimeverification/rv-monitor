Runtime Verification Prediction
-------------------------------

RV Prediction runs through these phases:

Instrumentation    - The program is instrumented using soot.
Logging            - The instrumented program is run to collect execution logs.
Prediction         - Prediction occurs. For race detection this consists of an
                     un-ordered read-write or write-write conflict to a shared 
                     variable.
Relay              - The program is re-executed to demonstrate the predicted
                     race.
                     
                     
                     
-- Prepare
RV Prediction uses Z3 to verify races. To install Z3, following the instruction
at http://z3.codeplex.com/SourceControl/latest#README

-- Running (demo with command line)

./compile
./runall demo.Example

-- Running (Eclipse projects)

1. Instrument -- in rv-predict-inst, run rvpredict.instrumentation.Main demo.Example
2. Logging    -- in run-record, run edu.uiuc.run.Main demo.Example
3. Prediction -- in run-predict-engine, run NewRVPredict demo.Example
4. Replay     -- in run-replay, run edu.uiuc.run.Main demo.Example 1

** demo.Example is the program main class name. 1 means to replay schedule 1 (the first schedule)