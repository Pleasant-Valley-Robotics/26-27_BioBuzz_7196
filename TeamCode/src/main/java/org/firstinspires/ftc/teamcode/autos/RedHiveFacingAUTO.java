package org.firstinspires.ftc.teamcode.autos;

import static org.firstinspires.ftc.teamcode.utility.Storage.alliance;

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
@Autonomous(name = "RedHiveFacingAUTO", group = "Autonomous")
public class RedHiveFacingAUTO extends LinearOpMode {
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
            telemetry.addLine("RED ALLIANCE");
        }
        Storage.autoRan = Storage.AutoRan.FACING_HIVE;

        //Here the initial position is set for the AUTO
        Pose2d initialPose = new Pose2d(63.0198, -13.6450, Math.toRadians(-0.4131));
        drive = new MecanumDrive(hardwareMap, initialPose);
        robot.setUpRobot(hardwareMap);

        robot.launcher.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(125.0, 0.0, 2.5, 15.0));

        waitForStart();

        if (isStopRequested()) return;

//        Vector2d shootPositionStart = new Vector2d( -50.5409, 19.3727);
//       TrajectoryActionBuilder goToShootStart = drive.actionBuilder(drive.localizer.getPose())
//                .strafeToLinearHeading(shootPositionStart, Math.toRadians(154.86))
//                .waitSeconds(0.1);
        // -40.5093, 12.2717, -179.7521
        Actions.runBlocking(
                new SequentialAction(
                        new SleepAction(0.3),
                        new Robot.shootBall(), // First ball is shot
                        new SleepAction(0.75),
                        new Robot.shootBall(), // Second ball is shot
                        new SleepAction(0.75),
                        new Robot.shootBall(), // Third ball is shot
                        new SleepAction(0.75),
                        new Robot.shootBall() // Fourth ball is shot
//                        new SleepAction(0.1)
                )
        ); //Robot finishes its first cycle of shooting pollen

        //Here is the sleep action/wait block to place inside the Actions.runBlocking(): new SleepAction(1.0)

        //Here we first start off by shooting our 4 preloaded pollen from our kit-bot

//        Intake position one is defined here using the x & y coordinates along with the heading of the robot (yaw)
        Vector2d intakePosOne = new Vector2d(63.6597, -46.3874);
        TrajectoryActionBuilder goToIntakeOne = drive.actionBuilder(drive.localizer.getPose())
                .strafeToLinearHeading(intakePosOne, Math.toRadians(-89.9845))
                .waitSeconds(0.1);

        Actions.runBlocking(
                new SequentialAction(
                        goToIntakeOne.build(),
                        new SleepAction(0.1)
                )
        );//The robot finishes intaking its first set of pollen here
//
        Vector2d intakePosOneClose = new Vector2d(63.194, -65.9051);
        TrajectoryActionBuilder goToIntakeOneClose = drive.actionBuilder(drive.localizer.getPose())
                .strafeToLinearHeading(intakePosOneClose, Math.toRadians(-90.555))
                .waitSeconds(0.1);
//        TrajectoryActionBuilder goToIntakeOneClose = drive.actionBuilder(drive.localizer.getPose())
//                .lineToYLinearHeading(-65.9051, -90.555)
//                .waitSeconds(0.1); // First set of artifacts intaked

        //The robot moves from its current position to the target coordinates
        Actions.runBlocking(
                new SequentialAction(
                        goToIntakeOneClose.build(),
                        new SleepAction(0.1),
                        new Robot.intakeBall(),
                        new SleepAction(3.0)
                )
        );//The robot finishes intaking its first set of pollen here

        //After intaking the pollen, the robot goes to the shoot position

        Vector2d shootPosition = new Vector2d(60.5884, -27.1743);
        TrajectoryActionBuilder goToShootPosition = drive.actionBuilder(drive.localizer.getPose())
                .strafeToLinearHeading(shootPosition, Math.toRadians(-15.5802))
                .waitSeconds(0.25);

        Actions.runBlocking(
                new SequentialAction(
                        goToShootPosition.build(),
                        new SleepAction(0.3),
                        new Robot.shootBall(), // First ball is shot
                        new SleepAction(0.75),
                        new Robot.shootBall(), // Second ball is shot
                        new SleepAction(0.75),
                        new Robot.shootBall(), // Third ball is shot
                        new SleepAction(0.75),
                        new Robot.shootBall(), // Fourth ball is shot
                        new SleepAction(0.1)
                )
        ); //Robot finishes its second cycle of shooting pollen

        Vector2d parkPose = new Vector2d(-31.9036, -59.148);
        TrajectoryActionBuilder goToParkPose = drive.actionBuilder(drive.localizer.getPose())
                .strafeToLinearHeading(parkPose, Math.toRadians(-60.923))
                .waitSeconds(0.25);

        Actions.runBlocking(
                new SequentialAction(
                        goToParkPose.build()
                )
        ); //The robot ends AUTO here

//        TrajectoryActionBuilder goToIntakeOneCollect = drive.actionBuilder(drive.localizer.getPose())
//                .lineToYConstantHeading(52.0 * flipAuto)
//                .waitSeconds(0.1); // First set of artifacts intaked

//        Vector2d shootPositionOne = new Vector2d(-30.6209, 21.5313 * flipAuto);
//        TrajectoryActionBuilder goToShootOne = drive.actionBuilder(drive.localizer.getPose())
//                .strafeToLinearHeading(shootPositionOne, 135.0 * (Math.PI/180.0 * flipAuto))
//                //.turnTo(129.0*Math.PI/180.0*flipAuto)
//                .waitSeconds(0.05); // first time moving to the shooting position

    }// End of the movement portion of the autonomous class

}// End of the autonomous class
