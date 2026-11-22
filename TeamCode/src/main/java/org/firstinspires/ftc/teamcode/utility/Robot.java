package org.firstinspires.ftc.teamcode.utility;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Robot {
    public static DcMotorEx frontLeftDrive;
    public static DcMotorEx frontRightDrive;
    public static DcMotorEx backLeftDrive;
    public static DcMotorEx backRightDrive;
    public static DcMotorEx launcher;
    public static DcMotorEx intake;

    public static CRServo leftFeeder;
    public static CRServo rightFeeder;
    public static CRServo launcherFeeder;

    public IMU imu;
    public static ElapsedTime runtime = new ElapsedTime();

    //The following is a hardware mapping function which can be imported to hardware map the robot
    public void setUpRobot(HardwareMap hardwareMap) {
        //Main drivetrain motors hardware mapping
        frontLeftDrive = hardwareMap.get(DcMotorEx.class, "frontLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotorEx.class, "frontRightDrive");
        backLeftDrive = hardwareMap.get(DcMotorEx.class, "backLeftDrive");
        backRightDrive = hardwareMap.get(DcMotorEx.class, "backRightDrive");

        //Endefector motors hardware mapping
        launcher = hardwareMap.get(DcMotorEx.class, "launcher");
        intake = hardwareMap.get(DcMotorEx.class, "intake");

        //CR Servos hardware mapping
        leftFeeder = hardwareMap.get(CRServo.class, "leftFeeder");
        rightFeeder = hardwareMap.get(CRServo.class, "rightFeeder");
        launcherFeeder = hardwareMap.get(CRServo.class, "launcherFeeder");

        //This sets the direction of the motors to ensure they're moving in the correct direction
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        leftFeeder.setDirection(CRServo.Direction.FORWARD);
        rightFeeder.setDirection(CRServo.Direction.REVERSE);
        launcherFeeder.setDirection(CRServo.Direction.REVERSE);

        frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

//        RevHubOrientationOnRobot.LogoFacingDirection logoDirection =
//                RevHubOrientationOnRobot.LogoFacingDirection.LEFT;
//        RevHubOrientationOnRobot.UsbFacingDirection usbDirection =
//                RevHubOrientationOnRobot.UsbFacingDirection.DOWN;
//
//        imu = hardwareMap.get(IMU.class, "imu");
//        RevHubOrientationOnRobot orientationOnRobot = new
//                RevHubOrientationOnRobot(logoDirection, usbDirection);
//        imu.initialize(new IMU.Parameters(orientationOnRobot));
    }

    public static class shootBall implements Action {
        public boolean initialized = false;
        public boolean startShooting = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                initialized = true;
                startShooting = false;
                launcher.setVelocity(0.0);
                launcherFeeder.setPower(0.0);
                runtime.reset();
                runtime.startTime();
            }
            double vel = launcher.getVelocity();
            packet.put("Launcher Velocity", vel);
            if (vel > 1250.0) {
                double tim = runtime.seconds();
                packet.put("runtime", tim);
                launcher.setVelocity(1250.0);
                new SleepAction(2.0);
                launcherFeeder.setPower(1.0);
                startShooting = true;

            } else if (!startShooting) {
                launcher.setVelocity(1250.0);
                runtime.reset();
                runtime.startTime();
            }
            if (runtime.seconds() > 1.0) {
                launcherFeeder.setPower(0.0);
                launcher.setVelocity(0.0);
                return false;
            }
            return true;
        }
    }//End of the shooting function

    public static class intakeBall implements Action {
        public boolean initialized = false;
        public boolean startIntaking = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                initialized = true;
                startIntaking = false;
                intake.setPower(0.0);
                leftFeeder.setPower(0.0);
                rightFeeder.setPower(0.0);
                runtime.reset();
                runtime.startTime();
            }
            double pow = intake.getPower();
            packet.put("Launcher Velocity", pow);
            if (pow > 0.9) {
                double tim = runtime.seconds();
                packet.put("runtime", tim);
                intake.setPower(1.0);
                new SleepAction(0.5);
                leftFeeder.setPower(1.0);
                rightFeeder.setPower(1.0);
                startIntaking = true;

            } else if (!startIntaking) {
                intake.setPower(1.0);
                runtime.reset();
                runtime.startTime();
            }
            if (runtime.seconds() > 3.0) {
                intake.setPower(0.0);
                leftFeeder.setPower(0.0);
                rightFeeder.setPower(0.0);
                return false;
            }
            return true;
        }
    }//End of the intaking function

    //The actions below are used by the TwoTip AUTOs. They let the robot do two things at once
    //(for example drive while the launcher spins up) and shoot a whole load without stopping the launcher.

    //Sets the launcher speed and finishes right away, so the robot can drive while the launcher
    //gets up to speed. The launcher keeps spinning until stopLauncher is run.
    public static class spinLauncher implements Action {
        private final double velocity; //Launcher speed in encoder ticks per second

        public spinLauncher(double velocity) {
            this.velocity = velocity;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            launcher.setVelocity(velocity);
            return false;
        }
    }//End of the launcher spin up function

    public static class stopLauncher implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            launcherFeeder.setPower(0.0);
            launcher.setVelocity(0.0);
            return false;
        }
    }//End of the launcher stop function

    //Shoots a whole load of balls (a volley). The feeder only runs while the launcher is at speed,
    //so it pauses by itself after every shot until the launcher has recovered.
    //The launcher slows down for a moment every time a ball goes through it. The volley counts
    //those dips, so it ends as soon as the last ball is out instead of running for a fixed time.
    //The launcher is left spinning at the end so the next volley does not have to spin up again.
    public static class shootVolley implements Action {
        //These can be tuned on the real robot
        public static double VELOCITY_TOLERANCE = 30.0; //How close counts as "at speed" (the hub reports in steps of 20)
        public static double AT_SPEED_TIME = 0.1;       //Seconds the launcher must hold its speed before the first ball is fed
        public static double SHOT_DIP = 60.0;           //When the launcher drops this far below its speed, one ball has been shot
        public static double AFTER_SHOT_TIME = 0.1;     //Seconds the robot stays still after the last ball, so it is clear of the launcher
        public static double MAX_FEED_PAUSE = 1.5;      //Most extra seconds spent waiting for the launcher to recover
        public static double SPIN_UP_TIMEOUT = 3.0;     //Stop waiting after this long so the AUTO can never get stuck here

        private final double velocity; //Launcher speed for this shooting spot, in encoder ticks per second
        private final int balls;       //How many balls are in the robot
        private final double feedTime; //Most seconds the feeder runs. This ends the volley if a ball was not counted
                                       //or the robot holds fewer balls than expected.

        private boolean initialized = false;
        private boolean startShooting = false;
        private boolean gaveUpWaiting = false;
        private final ElapsedTime sinceStart = new ElapsedTime();
        private final ElapsedTime atSpeed = new ElapsedTime();
        private final ElapsedTime feedTimer = new ElapsedTime();
        private final ElapsedTime loopTimer = new ElapsedTime();
        private final ElapsedTime sinceLastShot = new ElapsedTime();
        private double feederSeconds = 0.0;
        private boolean feederOn = false;
        private boolean readyForDip = true;
        private int ballsShot = 0;

        public shootVolley(double velocity, int balls, double feedTime) {
            this.velocity = velocity;
            this.balls = balls;
            this.feedTime = feedTime;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                initialized = true;
                launcherFeeder.setPower(0.0);
                launcher.setVelocity(velocity);
                sinceStart.reset();
                atSpeed.reset();
            }
            double vel = launcher.getVelocity();
            packet.put("Launcher Velocity", vel);
            boolean atTargetSpeed = Math.abs(vel - velocity) <= VELOCITY_TOLERANCE;

            if (!startShooting) {
                //Wait until the launcher has reached its speed and stayed there for a moment
                if (!atTargetSpeed) {
                    atSpeed.reset();
                }
                if (atSpeed.seconds() >= AT_SPEED_TIME) {
                    startShooting = true;
                    feedTimer.reset();
                    loopTimer.reset();
                } else if (sinceStart.seconds() >= SPIN_UP_TIMEOUT) {
                    //The launcher never settled (low battery?). Shoot anyway instead of waiting forever.
                    startShooting = true;
                    gaveUpWaiting = true;
                    feedTimer.reset();
                    loopTimer.reset();
                }
                return true;
            }

            //Count how long the feeder has really been running (pauses do not count)
            if (feederOn) {
                feederSeconds += loopTimer.seconds();
            }
            loopTimer.reset();
            packet.put("runtime", feederSeconds);

            //Count the balls: a dip in the launcher speed means a ball just went through
            if (readyForDip && vel < velocity - SHOT_DIP) {
                ballsShot++;
                readyForDip = false;
                sinceLastShot.reset();
            } else if (!readyForDip && atTargetSpeed) {
                readyForDip = true;
            }
            packet.put("Balls Shot", ballsShot);

            if (ballsShot >= balls) {
                //Every ball is out. Stay still for a moment, then the volley is done.
                feederOn = false;
                launcherFeeder.setPower(0.0);
                return sinceLastShot.seconds() < AFTER_SHOT_TIME;
            }

            feederOn = atTargetSpeed || gaveUpWaiting;
            launcherFeeder.setPower(feederOn ? 1.0 : 0.0);

            if (feederSeconds > feedTime || feedTimer.seconds() > feedTime + MAX_FEED_PAUSE) {
                launcherFeeder.setPower(0.0);
                return false;
            }
            return true;
        }
    }//End of the volley shooting function

    //Turns the intake and the two feeder wheels on and finishes right away, so the robot can
    //already be intaking while it drives up to the balls
    public static class intakeOn implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            intake.setPower(1.0);
            leftFeeder.setPower(1.0);
            rightFeeder.setPower(1.0);
            return false;
        }
    }//End of the intake on function

    public static class intakeOff implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            intake.setPower(0.0);
            leftFeeder.setPower(0.0);
            rightFeeder.setPower(0.0);
            return false;
        }
    }//End of the intake off function
    }
