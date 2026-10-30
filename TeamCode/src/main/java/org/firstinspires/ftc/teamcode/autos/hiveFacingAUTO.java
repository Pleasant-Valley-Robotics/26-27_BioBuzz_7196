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
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.utility.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.utility.Storage;

@Config
@Autonomous(name = "practiceAuto", group = "Autonomous")
public class hiveFacingAUTO extends LinearOpMode {
    int flipAuto = 1;
    public DcMotorEx launcher = null;
    public DcMotorEx intake = null;
    public CRServo leftFeeder = null;
    public CRServo rightFeeder = null;
    public CRServo launcherFeeder = null;
    public IMU imu;
    public static MecanumDrive drive;
    @Override
    public void runOpMode() {

        while (!isStopRequested() && !opModeIsActive()) {
            //add anything for during initialization
            //telemetry.addData("Shots To Cycle", camera.findShotsToCycle());
            if (gamepad1.b) {
                flipAuto = 1;
                alliance = Storage.Alliance.RED;

            } else if (gamepad1.x) {
                flipAuto = -1;
                alliance = Storage.Alliance.BLUE;
            }

            telemetry.update();
            telemetry.addData("Press X","for BLUE");
            telemetry.addData("Press B","for RED");
            telemetry.addData("Selected Alliance", alliance);
        }
        Storage.autoRan = Storage.AutoRan.FACING_HIVE;
        Pose2d initialPose = new Pose2d(-55.135, 49.0834 * flipAuto, Math.toRadians(131.014) * flipAuto);
        drive = new MecanumDrive(hardwareMap, initialPose);

        launcher = hardwareMap.get(DcMotorEx.class, "launcher");
        intake = hardwareMap.get(DcMotorEx.class, "intake");

        leftFeeder = hardwareMap.get(CRServo.class, "leftFeeder");
        rightFeeder = hardwareMap.get(CRServo.class, "rightFeeder");
        launcherFeeder = hardwareMap.get(CRServo.class, "launcherFeeder");

        Vector2d endingPosition = new Vector2d(-61.6131, 11.5718 * flipAuto);

        waitForStart();

        if (isStopRequested()) return;
/*
        Vector2d indexPosition = new Vector2d(-22.2411, 6.7882);
        TrajectoryActionBuilder goToIndex = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(indexPosition, Math.toRadians(-150.2229))
                .waitSeconds(0.25);

            Actions.runBlocking(
                    new SequentialAction(
                            goToIndex.build()
                    )
            );

        for (int ShotsToCycle = camera.findShotsToCycle(); ShotsToCycle > 0; ShotsToCycle--)
        {
            if (isStopRequested()) return;
            Actions.runBlocking(
                    new SequentialAction(
                            launcher.IndexBall(),
                            new SleepAction(0.2)
                    )
            );
            telemetry.addData("Shots To Cycle", camera.findShotsToCycle());
            telemetry.update();
        }*/ // end indexing

        Vector2d shootPosition = new Vector2d(-30.6209, 21.5313 * flipAuto);
        TrajectoryActionBuilder goToShoot = drive.actionBuilder(drive.localizer.getPose())
                .strafeToLinearHeading(shootPosition, Math.toRadians(129.5463) * flipAuto)
                //.turnTo(129.0 * Math.PI/180.0*flipAuto)
                //.turnTo(Math.9PI/2)
                .waitSeconds(0.1); // first time moving to the shooting position

        Actions.runBlocking(
                new SequentialAction(
                        goToShoot.build()
                )
        ); // first round of shooting the pre-loaded artifacts

        Vector2d intakeOne = new Vector2d(-13.0, 30.0 * flipAuto);
        TrajectoryActionBuilder goToIntakeOne = drive.actionBuilder(drive.localizer.getPose())
                .strafeToLinearHeading(intakeOne, Math.toRadians(90.0) * flipAuto)
                //.turnTo(Math.toRadians(90)*flipAuto)
                .waitSeconds(0.01); //First set of artifacts

        Actions.runBlocking(
                new SequentialAction(
                        goToIntakeOne.build()
                )
        ); //moving to the first position to intake artifacts

        TrajectoryActionBuilder goToIntakeOneCollect = drive.actionBuilder(drive.localizer.getPose())
                .lineToYConstantHeading(52.0 * flipAuto)
                .waitSeconds(0.1); // First set of artifacts intaked

        Actions.runBlocking(
                new SequentialAction((
                        goToIntakeOneCollect.build()
                )
        )); // The robot moves forward as it collects the artifacts

        Vector2d shootPositionOne = new Vector2d(-30.6209, 21.5313 * flipAuto);
        TrajectoryActionBuilder goToShootOne = drive.actionBuilder(drive.localizer.getPose())
                .strafeToLinearHeading(shootPositionOne, 135.0 * (Math.PI/180.0 * flipAuto))
                //.turnTo(129.0*Math.PI/180.0*flipAuto)
                .waitSeconds(0.05); // first time moving to the shooting position

        Actions.runBlocking(
                new SequentialAction(
                        goToShootOne.build(),
                        new SleepAction(0.01)
                )
        ); // robot shoots the second round of artifacts after intaking

        Vector2d intakeTwo = new Vector2d(13.0, 30.0 * flipAuto);
        TrajectoryActionBuilder goToIntakeTwo = drive.actionBuilder(drive.localizer.getPose())
                .strafeToLinearHeading(intakeTwo, Math.toRadians(90.0) * flipAuto)
                //.turnTo(Math.toRadians(90) * flipAuto)
                .waitSeconds(0.01); //First set of artifacts

        Actions.runBlocking(
                new SequentialAction(
                        goToIntakeTwo.build()
                )
        ); //moving to the first position to intake artifacts

        TrajectoryActionBuilder goToIntakeTwoCollect = drive.actionBuilder(drive.localizer.getPose())
                .lineToYConstantHeading(52.0 * flipAuto)
                .waitSeconds(0.1); // First set of artifacts intaked

        Actions.runBlocking(
                new SequentialAction((
                        goToIntakeTwoCollect.build()
                )
        )); // The robot moves forward as it collects the artifacts

        TrajectoryActionBuilder goToIntakeTwoReturn = drive.actionBuilder(drive.localizer.getPose())
                .lineToYConstantHeading(45.0 * flipAuto)
                .lineToYConstantHeading(48.0 * flipAuto)
                .waitSeconds(0.1); // First set of artifacts intaked

        Actions.runBlocking(
                new SequentialAction((
                        goToIntakeTwoReturn.build()
                )
        )); // The robot moves forward as it collects the artifacts

        Vector2d shootPositionTwo = new Vector2d(-70.0, 5.0 * flipAuto);
        TrajectoryActionBuilder goToShootTwo = drive.actionBuilder(drive.localizer.getPose())
                .strafeToLinearHeading(shootPositionTwo, 90.0 * (Math.PI/180.0 * flipAuto))
                //.turnTo(129.0*Math.PI/180.0*flipAuto)
                .waitSeconds(0.05); // first time moving to the shooting position

        Actions.runBlocking(
                new SequentialAction(
                        goToShootTwo.build(),
                        new SleepAction(0.01)
                )
        ); // robot shoots the second round of artifacts after intaking

        Vector2d intakeThree = new Vector2d(36.0, 30.0 * flipAuto);
        TrajectoryActionBuilder goToIntakeThree = drive.actionBuilder(drive.localizer.getPose())
                .strafeToLinearHeading(intakeThree, Math.toRadians(90.0) * flipAuto)
                //.turnTo(Math.toRadians(90) * flipAuto)
                .waitSeconds(0.01); //First set of artifacts

        Actions.runBlocking(
                new SequentialAction(
                        goToIntakeThree.build()
                )
        ); //moving to the first position to intake artifacts

        TrajectoryActionBuilder goToIntakeThreeCollect = drive.actionBuilder(drive.localizer.getPose())
                .lineToYConstantHeading(48.0 * flipAuto)
                .waitSeconds(0.1); //

        Actions.runBlocking(
                new SequentialAction((
                        goToIntakeThreeCollect.build()
                )
        )); // The robot moves forward as it collects the artifacts

        /*
        TrajectoryActionBuilder goToEnd = drive.actionBuilder(drive.localizer.getPose())
                .strafeToLinearHeading(endingPosition, Math.toRadians(-177.7059))
                .waitSeconds(0.01);

        Actions.runBlocking(
                goToEnd.build()
        );
        */
        }
    }// End of the autonomous class
