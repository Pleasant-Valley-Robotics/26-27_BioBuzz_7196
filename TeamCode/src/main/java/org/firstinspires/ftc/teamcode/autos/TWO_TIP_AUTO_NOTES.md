# Two TIP AUTO: read this before running it on the robot

This branch adds two new AUTOs, `BlueTwoTipAUTO` and `RedTwoTipAUTO`. Each one tries to TIP the HIVE twice in AUTO.

A mentor wrote this code with an AI assistant (Claude) and tested it only in a simulator. **It has never run on the real robot.** Treat every number in it as a starting guess.

## What changed in your code

Nothing you wrote was edited. The branch starts from your "Comp changes" commit and adds:

- `autos/BlueTwoTipAUTO.java` and `autos/RedTwoTipAUTO.java` (new files)
- Five new actions at the bottom of `utility/Robot.java`: `spinLauncher`, `stopLauncher`, `shootVolley`, `intakeOn`, `intakeOff`
- This notes file

Your AUTOs, your TeleOp, `shootBall`, `intakeBall` and `MecanumDrive` are the same as before. If you merge this branch, your OpModes behave exactly as they do today.

## What the AUTO does

These are the blue positions. Red is the same route turned 180 degrees, so every x and y has the opposite sign.

1. Backs off the wall to (-51.6, 13.2) while the launcher spins up, then shoots the 4 pre-loaded POLLEN. Three in the CELL TIP the HIVE.
2. Slides to the GARDEN corner, still facing the back wall, and takes the 4 POLLEN there.
3. Crosses to the audience side and shoots from (50.0, 23.4) at the CELL that now faces up.
4. Takes 4 POLLEN from the bottom of the FLOWER on the audience wall and shoots again.
5. Drives to the FLOWER on the blue wall, takes 4 more, and shoots from (43.3, 35.9). Eight POLLEN in the second CELL make TIP two.
6. Parks in the LOADING ZONE.

It starts on the same spot as `BlueHiveFacingAUTO` (red: `RedHiveFacingAUTO`).

## Simulator results

300 runs for each alliance:

- Two TIPS in 82% of blue runs and 83% of red runs.
- At least one TIP in about 9 of 10 runs.
- No TIP in about 1 of 10 runs. This happens when two of the first four shots miss. The robot cannot tell that the HIVE did not TIP, so it keeps shooting at the wrong CELL.
- PARK and LEAVE in every run.
- The AUTO ends at about 29.1 s, so there is less than 1 second to spare.

## Check these on the real robot first

1. **FLOWER pickup.** The simulator assumes the intake takes the bottom POLLEN out of a FLOWER when the robot presses against it. Nobody has tried this with the real intake. Try it by hand in TeleOp before anything else. Steps 4 and 5 depend on it.
2. **Launcher speed.** `SHOOT_VELOCITY` is 1240. In the simulator the best speed from your back wall start is 1325, but at the meet you lowered yours to 1250. That suggests the real launcher throws about 6% harder than the simulator thinks. Start near 1170 and test shots from (-51.6, 13.2) with the back of the robot pointing at the CELL. Every shooting spot is 36 in from the CELL opening, so one speed should work for all three.
3. **First drive.** This AUTO is fast. It speeds up at 70 inches per second every second and crosses the field twice. For the first runs set `MAX_ACCEL` to 30 and `MAX_DECEL` to -30, clear the field, and keep a hand on STOP. At those settings it will not finish in 30 seconds. That is fine for a first look at the path.
4. **Path correction.** `MecanumDrive` has its feedback gains at 0.0. This AUTO needs them, so it sets `AXIAL_GAIN` 3, `LATERAL_GAIN` 3 and `HEADING_GAIN` 4 while it runs and puts the old values back when it ends. These are simulator guesses. If the robot shakes or wobbles along the path, lower them. `ManualFeedbackTuner` is the right tool for tuning them.
5. **Drive motor mode.** The AUTO switches the drive motors to `RUN_WITHOUT_ENCODER`, because that is what RoadRunner's feedforward numbers expect. Your TeleOp sets its own mode again in init.
6. **GARDEN pickup.** Your Comp changes moved your GARDEN target 4 in deeper. This AUTO comes at the GARDEN from a different direction (facing the back wall, stopping at (-63.6, 63.4)), so check where it really needs to stop.
7. **Shot counting.** `shootVolley` counts dips in launcher speed to know when each POLLEN has left. It expects a dip of at least 60 ticks per second (`SHOT_DIP`). If the real dip is smaller, the volley still shoots, but it runs the feeder for the full `FEED_TIME` and the AUTO may run out of time before the park.

## Numbers you can tune

These are at the top of each AUTO file and show up in FTC Dashboard:

`SHOOT_VELOCITY`, `BALLS`, `FEED_TIME`, `MAX_ACCEL`, `MAX_DECEL`, `AXIAL_GAIN`, `LATERAL_GAIN`, `HEADING_GAIN`

The settings for `shootVolley` are at the top of that class in `Robot.java`.

## What the simulator guessed

- Launch angle: 72 degrees.
- About 3 of 4 shots score from 36 in.
- The feeder moves one POLLEN every 0.45 s.
- The launcher loses 12% of its speed on each shot.
- The first TIP needs 3 POLLEN (the CELL starts with 3 NECTAR) and the second needs 8.

If the real robot is different from these, the results will be different too.
