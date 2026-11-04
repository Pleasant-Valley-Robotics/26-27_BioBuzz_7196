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
@Autonomous(name = "hiveFacingAUTO", group = "Autonomous")
public class HiveFacingAUTO extends LinearOpMode {
    double flipAuto = 1; //This variable allows us to invert coordinates in order to change alliancev77
    Robot robot = new Robot();
    public static MecanumDrive drive;

    @Override
    public void runOpMode() {

        while (!isStopRequested() && !opModeIsActive()) {
            //add anything for during initialization
            //telemetry.addData("Shots To Cycle", camera.findShotsToCycle());
            //Here, we pick the alliance that we're on, and this automatically does the math to invert the coordinates per the alliance
            if (gamepad1.x) {
                flipAuto = 1.0;
                alliance = Storage.Alliance.BLUE;
            } else if (gamepad1.b) {
                flipAuto = -1.0;
                alliance = Storage.Alliance.RED;
            }
            //The telemetry here tells the user which button to press for the designated alliance
            telemetry.update();
            telemetry.addData("Press X","for BLUE");
            telemetry.addData("Press B","for RED");
            telemetry.addData("Selected Alliance", alliance);
        }
        Storage.autoRan = Storage.AutoRan.FACING_HIVE;

        //Here the initial position is set for the AUTO
        Pose2d initialPose = new Pose2d(62.6454 * flipAuto, -9.7231 * flipAuto, Math.toRadians(-0.3999) * flipAuto);
        drive = new MecanumDrive(hardwareMap, initialPose);
        robot.setUpRobot(hardwareMap);

        robot.launcher.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(360.0, 0.0, 0.0, 13.0));

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

        Vector2d shootPositionStart = new Vector2d(62.4582 * flipAuto, -14.497);
        TrajectoryActionBuilder goToShootStart = drive.actionBuilder(drive.localizer.getPose())
                .strafeToLinearHeading(shootPositionStart, Math.toRadians(-0.6157) * flipAuto)
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
        Vector2d intakePosOne = new Vector2d(67.7271 * flipAuto, -47.9442);
        TrajectoryActionBuilder goToIntakeOne = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(intakePosOne, Math.toRadians(-61.702) * flipAuto)
                .waitSeconds(0.25);

        Vector2d intakePosOneClose = new Vector2d(65.0403 * flipAuto, -58.973);
        TrajectoryActionBuilder goToIntakeOneClose = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(intakePosOneClose, Math.toRadians(-61.702) * flipAuto)
                .waitSeconds(0.25);
// -48.4889
        //The robot moves from its current position to the target coordinates
            Actions.runBlocking(
                    new SequentialAction(
                            goToIntakeOne.build(),
                            new SleepAction(0.1),
                            goToIntakeOneClose.build(),
                            new SleepAction(0.1),
                            new Robot.intakeBall(),
                            new SleepAction(3.0)
                    )
            );//The robot finishes intaking its first set of pollen here

        //After intaking the pollen, the robot goes to the shoot position

        Actions.runBlocking(
                new SequentialAction(
                        goToShootStart.build(),
                        new SleepAction(0.1),
                        new Robot.shootBall(), // First ball is shot
                        new SleepAction(0.1),
                        new Robot.shootBall(), // Second ball is shot
                        new SleepAction(0.1),
                        new Robot.shootBall(), // Third ball is shot
                        new SleepAction(0.1),
                        new Robot.shootBall(), // Fourth ball is shot
                        new SleepAction(0.1)
                )
        ); //Robot finishes its second cycle of shooting pollen

        Vector2d parkPose = new Vector2d(-28.9042 * flipAuto, -64.6785);
        TrajectoryActionBuilder goToParkPose = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(parkPose, Math.toRadians(-0.78) * flipAuto)
                .waitSeconds(0.25);

        Actions.runBlocking(
                new SequentialAction(
                        goToParkPose.build()
                )
        );//The robot finishes intaking its first set of pollen here

//        TrajectoryActionBuilder goToIntakeOneCollect = drive.actionBuilder(drive.localizer.getPose())
//                .lineToYConstantHeading(52.0 * flipAuto)
//                .waitSeconds(0.1); // First set of artifacts intaked

//        Vector2d shootPositionOne = new Vector2d(-30.6209, 21.5313 * flipAuto);
//        TrajectoryActionBuilder goToShootOne = drive.actionBuilder(drive.localizer.getPose())
//                .strafeToLinearHeading(shootPositionOne, 135.0 * (Math.PI/180.0 * flipAuto))
//                //.turnTo(129.0*Math.PI/180.0*flipAuto)
//                .waitSeconds(0.05); // first time moving to the shooting position

    /*
        TrajectoryActionBuilder goToEnd = drive.actionBuilder(drive.localizer.getPose())
                .strafeToLinearHeading(endingPosition, Math.toRadians(-177.7059))
                .waitSeconds(0.01);

        Actions.runBlocking(
                goToEnd.build()
        );
     */
    }// End of the movement portion of the autonomous class
}// End of the autonomous class
