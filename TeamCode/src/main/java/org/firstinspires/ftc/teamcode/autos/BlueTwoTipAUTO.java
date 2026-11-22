package org.firstinspires.ftc.teamcode.autos;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.AccelConstraint;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.teamcode.utility.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.utility.Robot;
import org.firstinspires.ftc.teamcode.utility.Storage;

//Goes for two HIVE TIPS in AUTO. This code has only been tested in a simulator.
//Read TWO_TIP_AUTO_NOTES.md (in this folder) before running it on the real robot.
@Config
@Autonomous(name = "BlueTwoTipAUTO", group = "Autonomous")
public class BlueTwoTipAUTO extends LinearOpMode {
    Robot robot = new Robot();
    public static MecanumDrive drive;

    //These can be tuned on the real robot
    public static double SHOOT_VELOCITY = 1240.0;    //Launcher speed. Every shooting spot is 36 in from the CELL opening, so one speed fits all
    public static int BALLS = 4;                     //Balls in the robot for every volley
    public static double FEED_TIME = 2.4;            //Most seconds the feeder runs in one volley (the volley ends earlier when it has counted every ball)
    public static double MAX_ACCEL = 70.0;           //How hard the robot speeds up in this AUTO (inches per second, per second)
    public static double MAX_DECEL = -50.0;          //How hard the robot slows down in this AUTO

    //Path correction: how hard RoadRunner pulls the robot back onto the path when the Pinpoint says it is off.
    //MecanumDrive has these at 0.0 (no correction). This AUTO needs correction, so it uses the numbers below
    //while it runs and puts the old numbers back when it ends. The other OpModes drive the same as before.
    public static double AXIAL_GAIN = 3.0;           //Forwards and backwards
    public static double LATERAL_GAIN = 3.0;         //Sideways
    public static double HEADING_GAIN = 4.0;         //Turning

    @Override
    public void runOpMode() {
        Storage.autoRan = Storage.AutoRan.FACING_HIVE;

        //Here the initial position is set for the AUTO (the same spot as BlueHiveFacingAUTO)
        Pose2d initialPose = new Pose2d(-63.0198, 13.6450, Math.toRadians(-179.4131));
        drive = new MecanumDrive(hardwareMap, initialPose);
        robot.setUpRobot(hardwareMap);

        //RoadRunner's feedforward numbers (kS, kV, kA) were tuned with the drive motors in
        //RUN_WITHOUT_ENCODER. setUpRobot switches them to RUN_USING_ENCODER, which makes the robot
        //run slower than RoadRunner expects at speed, so it is switched back for this AUTO.
        Robot.frontLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Robot.frontRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Robot.backLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Robot.backRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        robot.launcher.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(125.0, 0.0, 2.5, 15.0));

        //All three shooting spots are 36 in from the middle of the CELL opening, with the launcher
        //(the back of the robot) pointing at the opening.
        //Back side shooting spot, for the CELL that starts facing up
        Vector2d shootBack = new Vector2d(-51.6, 13.2);
        //Audience side shooting spot, right behind the FLOWER on the audience wall, for the CELL
        //that faces up after the first TIP
        Vector2d shootAudience = new Vector2d(50.0, 23.4);
        double shootAudienceHeading = Math.toRadians(17.2);
        //Second audience side shooting spot, closer to the blue wall FLOWER
        Vector2d shootAudienceWide = new Vector2d(43.3, 35.9);
        double shootAudienceWideHeading = Math.toRadians(40.0);

        //This AUTO has a lot to do in 30 seconds, so it speeds up and slows down harder than the default
        AccelConstraint fast = new ProfileAccelConstraint(MAX_DECEL, MAX_ACCEL);

        //The whole AUTO is built here, before START, so no time is lost building paths during the 30 seconds
        Action auto = drive.actionBuilder(initialPose)
                //1. Back off the wall while the launcher spins up, then shoot the 4 pre-loaded pollen.
                //   3 of them in the CELL make the HIVE TIP.
                .afterTime(0.0, new Robot.spinLauncher(SHOOT_VELOCITY))
                .strafeToLinearHeading(shootBack, Math.toRadians(180), null, fast)
                .stopAndAdd(new Robot.shootVolley(SHOOT_VELOCITY, BALLS, FEED_TIME))

                //2. Slide over to the GARDEN corner. The robot keeps facing the back wall, so at the end
                //   it pushes its intake straight into the line of pollen.
                .afterTime(0.0, new Robot.spinLauncher(SHOOT_VELOCITY))
                .afterTime(0.3, new Robot.intakeOn())
                .setTangent(Math.toRadians(95))
                .splineToConstantHeading(new Vector2d(-63.6, 63.4), Math.toRadians(180), null, fast)
                .waitSeconds(0.25)

                //3. Back out, cross to the audience side (around the HIVE frame) and shoot the GARDEN
                //   pollen into the CELL that is facing up now. The robot drives backwards most of the
                //   way and turns around at the end.
                .setTangent(Math.toRadians(-25))
                .splineToConstantHeading(new Vector2d(-40.0, 47.0), Math.toRadians(-10), null, fast)
                .afterTime(0.0, new Robot.intakeOff())
                .splineToConstantHeading(new Vector2d(14.0, 40.0), Math.toRadians(-5), null, fast)
                .splineToSplineHeading(new Pose2d(shootAudience, shootAudienceHeading), Math.toRadians(-40), null, fast)
                .stopAndAdd(new Robot.shootVolley(SHOOT_VELOCITY, BALLS, FEED_TIME))

                //4. Take the 4 pollen out of the bottom of the audience wall FLOWER and shoot them.
                //   The target is 2 in past the FLOWER on purpose, so the intake stays pressed against it.
                .afterTime(0.0, new Robot.intakeOn())
                .strafeToLinearHeading(new Vector2d(60.0, 24.0), Math.toRadians(0), null, fast)
                .waitSeconds(0.3)
                .strafeToLinearHeading(shootAudience, shootAudienceHeading, null, fast)
                .stopAndAdd(new Robot.intakeOff())
                .stopAndAdd(new Robot.shootVolley(SHOOT_VELOCITY, BALLS, FEED_TIME))

                //5. Get the 4 pollen from the blue wall FLOWER. The robot drives there backwards
                //   (it is fastest straight forwards or backwards) and swings its intake around at the end.
                .afterTime(0.6, new Robot.intakeOn())
                .setTangent(Math.toRadians(150))
                .splineToSplineHeading(new Pose2d(30.0, 37.5, Math.toRadians(-20)), Math.toRadians(165), null, fast)
                .splineToConstantHeading(new Vector2d(2.0, 43.0), Math.toRadians(170), null, fast)
                .splineToSplineHeading(new Pose2d(-24.0, 60.0, Math.toRadians(90)), Math.toRadians(100), null, fast)
                .waitSeconds(0.45)

                //6. Come back and shoot them from the closer shooting spot
                .setTangent(Math.toRadians(-80))
                .afterTime(0.8, new Robot.intakeOff())
                .splineToLinearHeading(new Pose2d(shootAudienceWide, shootAudienceWideHeading), Math.toRadians(-15), null, fast)
                .stopAndAdd(new Robot.shootVolley(SHOOT_VELOCITY, BALLS, FEED_TIME))

                //7. Park in the blue LOADING ZONE. The robot does not turn, it only has to reach
                //   into the zone with its front corner.
                .stopAndAdd(new Robot.stopLauncher())
                .strafeTo(new Vector2d(40.0, 54.0), null, fast)
                .build();

        while (!isStopRequested() && !opModeIsActive()) {
            telemetry.addLine("BLUE ALLIANCE");
            telemetry.update();
        }

        waitForStart();

        if (isStopRequested()) return;

        //Turn on path correction for this AUTO only (the numbers are at the top of this file)
        double oldAxialGain = MecanumDrive.PARAMS.axialGain;
        double oldLateralGain = MecanumDrive.PARAMS.lateralGain;
        double oldHeadingGain = MecanumDrive.PARAMS.headingGain;
        MecanumDrive.PARAMS.axialGain = AXIAL_GAIN;
        MecanumDrive.PARAMS.lateralGain = LATERAL_GAIN;
        MecanumDrive.PARAMS.headingGain = HEADING_GAIN;

        try {
            Actions.runBlocking(auto);
        } finally {
            //"finally" also runs when the AUTO is stopped early, so the old numbers always come back
            MecanumDrive.PARAMS.axialGain = oldAxialGain;
            MecanumDrive.PARAMS.lateralGain = oldLateralGain;
            MecanumDrive.PARAMS.headingGain = oldHeadingGain;
        }
    }// End of the movement portion of the autonomous class

}// End of the autonomous class
