package org.firstinspires.ftc.teamcode.autos;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
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
@Autonomous(name = "Blue Park", group = "Autonomous")
public class BluePark extends LinearOpMode {
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
        Pose2d initialPose = new Pose2d(-2.0297, 66.9326, Math.toRadians( 91.7138));
        drive = new MecanumDrive(hardwareMap, initialPose);
        robot.setUpRobot(hardwareMap);

        robot.launcher.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(125.0, 0.0, 1.0, 15.0));

        waitForStart();

        if (isStopRequested()) return;

        Vector2d parkPose = new Vector2d(34.8905, 63.315);
        TrajectoryActionBuilder goToParkPose = drive.actionBuilder(drive.localizer.getPose())
                .strafeToLinearHeading(parkPose, Math.toRadians(90.3652))
                .waitSeconds(0.25);

        Actions.runBlocking(
                new SequentialAction(
                        goToParkPose.build()
                )
        ); //The robot ends AUTO here

    }// End of the movement portion of the autonomous class

}// End of the autonomous class
