package org.firstinspires.ftc.teamcode.autos;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.teamcode.utility.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.utility.Robot;
import org.firstinspires.ftc.teamcode.utility.Storage;

@Config
@Autonomous(name = "BlueParkAUTO", group = "Autonomous")
public class BlueParkAUTO extends LinearOpMode {
    Robot robot = new Robot();
    public static MecanumDrive drive;

    @Override
    public void runOpMode() {

        while (!isStopRequested() && !opModeIsActive()) {
            //add anything for during initialization
            //telemetry.addData("Shots To Cycle", camera.findShotsToCycle());
            //Here, we pick the alliance that we're on, and this automatically does the math to invert the coordinates per the alliance
            //The telemetry here tells the user which button to press for the designated alliance
            telemetry.update();
            telemetry.addLine("BLUE ALLIANCE");
        }
        Storage.autoRan = Storage.AutoRan.AWAY_HIVE;

        //Here the initial position is set for the AUTO
        Pose2d initialPose = new Pose2d(0.0, 66.0, Math.toRadians(-90.0));
        drive = new MecanumDrive(hardwareMap, initialPose);
        robot.setUpRobot(hardwareMap);

        robot.launcher.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(390.0, 0.0, 0.0, 15.0));

        //Here is all the hardware mapping for the motors and servos defined above
//        launcher = hardwareMap.get(DcMotorEx.class, "launcher");
//        intake = hardwareMap.get(DcMotorEx.class, "intake");
//
//        //CR Servos hardware mapping
//        leftFeeder = hardwareMap.get(CRServo.class, "leftFeeder");
//        rightFeeder = hardwareMap.get(CRServo.class, "rightFeeder");
//        launcherFeeder = hardwareMap.get(CRServo.class, "launcherFeeder");

        waitForStart();

        if (isStopRequested()) return;

        Vector2d parkPose = new Vector2d(-28.9042, -64.6785);
        TrajectoryActionBuilder goToParkPose = drive.actionBuilder(drive.localizer.getPose())
                .strafeToLinearHeading(parkPose, Math.toRadians(-0.78))
                .waitSeconds(0.25);

        Actions.runBlocking(
                new SequentialAction(
                        goToParkPose.build()
                )
        );//The robot finishes intaking its first set of pollen here

    }// End of the movement portion of the autonomous class
}// End of the autonomous class
