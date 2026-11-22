
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "Feature Bot Drive Test", group = "Feature Bot")
public class FeatureBotDriveTest extends LinearOpMode {

    // Drivetrain motors
    private DcMotorEx frontLeftDrive;
    private DcMotorEx frontRightDrive;
    private DcMotorEx backLeftDrive;
    private DcMotorEx backRightDrive;

    @Override
    public void runOpMode() {

        // Connect motors using their Robot Configuration names
        frontLeftDrive = hardwareMap.get(DcMotorEx.class, "frontLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotorEx.class, "frontRightDrive");
        backLeftDrive = hardwareMap.get(DcMotorEx.class, "backLeftDrive");
        backRightDrive = hardwareMap.get(DcMotorEx.class, "backRightDrive");

        // Copied from kitbot teleop code

        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);

        // Use open-loop motor control
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Stop motors when power isn't needed
        frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addLine("Feature Bot Drive Initialized");
        telemetry.addLine("Waiting for start...");
        telemetry.update();

        waitForStart();

        // Reduced speed for initial testing
        double driveScale = 0.5;
        // ^^^^^change this later^^^^^^and remove drive scale

        // Joystick controls
        while (opModeIsActive()) {

            // Read gamepad 1 also copied from kitbot
            double axial = -gamepad1.left_stick_y;
            double lateral = gamepad1.left_stick_x;
            double yaw = gamepad1.right_stick_x;

            // Mecanum movement calculations from kitbot
            double frontLeftPower = axial + lateral + yaw;
            double frontRightPower = axial - lateral - yaw;
            double backLeftPower = axial - lateral + yaw;
            double backRightPower = axial + lateral - yaw;

            // learned this from ChatGPT
            double max = Math.max(
                    Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower)),
                    Math.max(Math.abs(backLeftPower), Math.abs(backRightPower))
            );

            if (max > 1.0) {
                frontLeftPower /= max;
                frontRightPower /= max;
                backLeftPower /= max;
                backRightPower /= max;
            }


            frontLeftDrive.setPower(frontLeftPower * driveScale);
            frontRightDrive.setPower(frontRightPower * driveScale);
            backLeftDrive.setPower(backLeftPower * driveScale);
            backRightDrive.setPower(backRightPower * driveScale);

            telemetry.addData("FL wheel Power", frontLeftPower * driveScale);
            telemetry.addData("FR wheel Power", frontRightPower * driveScale);
            telemetry.addData("BL wheel Power", backLeftPower * driveScale);
            telemetry.addData("BR wheel Power", backRightPower * driveScale);
            telemetry.update();

            // Stop all drivetrain motors when OpMode ends (ChatGPT recomendation)
            frontLeftDrive.setPower(0);
            frontRightDrive.setPower(0);
            backLeftDrive.setPower(0);
            backRightDrive.setPower(0);
        }
    }
}
