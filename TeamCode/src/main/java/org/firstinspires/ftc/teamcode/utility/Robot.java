package org.firstinspires.ftc.teamcode.utility;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Robot {
    public DcMotorEx frontLeftDrive = null;
    public DcMotorEx frontRightDrive = null;
    public DcMotorEx backLeftDrive = null;
    public DcMotorEx backRightDrive = null;
    public DcMotorEx launcher = null;
    public DcMotorEx intake = null;

    public CRServo leftFeeder = null;
    public CRServo rightFeeder = null;
    public CRServo launcherFeeder = null;

    public IMU imu;
    public ElapsedTime runtime = new ElapsedTime();

    double targetVelocity = 3400.0;

    //The following is a hardware mapping function which can be imported to hardware map the robot
    public void setupRobot(HardwareMap hardwareMap) {
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
        leftFeeder.setDirection(CRServo.Direction.REVERSE);
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

    public void setUpRobot(HardwareMap hardwareMap) {
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
                leftFeeder.setPower(0.0);
                rightFeeder.setPower(0.0);
                runtime.reset();
                runtime.startTime();
            }
            double vel = launcher.getVelocity();
            packet.put("Launcher Velocity", vel);
            if (vel > targetVelocity) {
                double tim = runtime.seconds();
                packet.put("runtime", tim);
                launcher.setVelocity(targetVelocity);
                leftFeeder.setPower(1.0);
                rightFeeder.setPower(1.0);
                startShooting = true;

            } else if (!startShooting) {
                launcher.setVelocity(targetVelocity);
                runtime.reset();
                runtime.startTime();
            }
            if (runtime.seconds() > 0.50) {
                leftFeeder.setPower(0.0);
                rightFeeder.setPower(0.0);
                launcher.setVelocity(0.0);
                return false;
            }
            return true;
        }
    }//End of the shooting function
}
