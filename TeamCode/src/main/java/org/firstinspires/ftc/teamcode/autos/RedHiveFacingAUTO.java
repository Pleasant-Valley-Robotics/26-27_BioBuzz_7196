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
        Pose2d initialPose = new Pose2d(-49.3885, 3.6707, Math.toRadians(-178.9458));
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

        Vector2d shootPositionStart = new Vector2d( -49.5093, 6.2717);
        TrajectoryActionBuilder goToShootStart = drive.actionBuilder(drive.localizer.getPose())
                .strafeToLinearHeading(shootPositionStart, Math.toRadians(-179.7521))
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
        Vector2d intakePosOne = new Vector2d(-49.9468, 41.1602);
        TrajectoryActionBuilder goToIntakeOne = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(intakePosOne, Math.toRadians(143.407))
                .waitSeconds(0.25);

        Vector2d intakePosOneClose = new Vector2d(-58.4471, 53.6043);
        TrajectoryActionBuilder goToIntakeOneClose = goToIntakeOne.endTrajectory().fresh()
                .strafeToLinearHeading(intakePosOneClose, Math.toRadians(96.4867))
                .waitSeconds(0.25);

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

        Vector2d parkPose = new Vector2d(33.0409, 54.4091);
        TrajectoryActionBuilder goToParkPose = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(parkPose, Math.toRadians(91.1721))
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
