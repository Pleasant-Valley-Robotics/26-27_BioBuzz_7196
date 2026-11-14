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
import org.firstinspires.ftc.teamcode.utility.Storage;
import org.firstinspires.ftc.teamcode.utility.Robot;

@Config
@Autonomous(name = "BlueHiveFacingAUTO", group = "Autonomous")
public class BlueHiveFacingAUTO extends LinearOpMode {
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
        Storage.autoRan = Storage.AutoRan.FACING_HIVE;

        //Here the initial position is set for the AUTO
        Pose2d initialPose = new Pose2d(62.6454, -9.7231, Math.toRadians(-0.3999));
        drive = new MecanumDrive(hardwareMap, initialPose);
        robot.setUpRobot(hardwareMap);

        robot.launcher.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(125.0, 0.0, 1.0, 15.0));

        waitForStart();

        if (isStopRequested()) return;

        Vector2d shootPositionStart = new Vector2d(62.4582, -12.497);
        TrajectoryActionBuilder goToShootStart = drive.actionBuilder(drive.localizer.getPose())
                .strafeToLinearHeading(shootPositionStart, Math.toRadians(-0.6157))
                .waitSeconds(0.1);

        Actions.runBlocking(
                new SequentialAction(
                        goToShootStart.build(),
                        new SleepAction(0.3),
                        new Robot.shootBall(), // First ball is shot
                        new SleepAction(0.25),
                        new Robot.shootBall(), // Second ball is shot
                        new SleepAction(0.25),
                        new Robot.shootBall(), // Third ball is shot
                        new SleepAction(0.25),
                        new Robot.shootBall(), // Fourth ball is shot
                        new SleepAction(0.1)
                )
        ); //Robot finishes its first cycle of shooting pollen

        //Here is the sleep action/wait block to place inside the Actions.runBlocking(): new SleepAction(1.0)

        //Here we first start off by shooting our 4 pre-loaded pollen from our kit-bot

        //Intake position one is defined here using the x & y coordinates along with the heading of the robot (yaw)
        Vector2d intakePosOne = new Vector2d(67.7271, -47.9442);
        TrajectoryActionBuilder goToIntakeOne = drive.actionBuilder(drive.localizer.getPose())
                .strafeToLinearHeading(intakePosOne, Math.toRadians(-61.702))
                .waitSeconds(0.25);
//
//        Vector2d intakePosOneClose = new Vector2d(76.3553, -57.8639);
//        TrajectoryActionBuilder goToIntakeOneClose = drive.actionBuilder(drive.localizer.getPose())
//                .strafeToLinearHeading(intakePosOneClose, Math.toRadians( -86.3134))
//                .waitSeconds(0.25);
//
//        //The robot moves from its current position to the target coordinates
            Actions.runBlocking(
                    new SequentialAction(
                            goToIntakeOne.build(),
                            new SleepAction(0.1)
//                            goToIntakeOneClose.build(),
//                            new SleepAction(0.1),
//                            new Robot.intakeBall(),
//                            new SleepAction(3.0)
                    )
            );//The robot finishes intaking its first set of pollen here
//
//        //After intaking the pollen, the robot goes to the shoot position
//
//        Actions.runBlocking(
//                new SequentialAction(
//                        goToShootStart.build(),
//                        new SleepAction(0.1),
//                        new Robot.shootBall(), // First ball is shot
//                        new SleepAction(0.1),
//                        new Robot.shootBall(), // Second ball is shot
//                        new SleepAction(0.1),
//                        new Robot.shootBall(), // Third ball is shot
//                        new SleepAction(0.1),
//                        new Robot.shootBall(), // Fourth ball is shot
//                        new SleepAction(0.1)
//                )
//        ); //Robot finishes its second cycle of shooting pollen

        Vector2d parkPose = new Vector2d(-40.6005, -64.785);
        TrajectoryActionBuilder goToParkPose = drive.actionBuilder(drive.localizer.getPose())
                .strafeToLinearHeading(parkPose, Math.toRadians(-0.78))
                .waitSeconds(0.25);

        Actions.runBlocking(
                new SequentialAction(
                        goToParkPose.build()
                )
        );//The robot parks in the loading zone, signaling the end of AUTO

    }// End of the movement portion of the autonomous class

}// End of the autonomous class
