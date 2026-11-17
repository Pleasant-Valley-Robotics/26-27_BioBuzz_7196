package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class TurretAutoAlignOpMode extends OpMode {
// why is AprilTagWebcam erroring // bacause this is Lmelight not webcam so here is a iffy test
    // private ApriltagWebcam apriltagWebcam = new AprilTagWebcam();
    private Limelight3A limelight;
    private TurretMech turret = new TurretMech();

    // -----------used to auto update P and D

    double[] stepSizes = {0.1, 0.01, 0.001, 0.0001, 0.00001};
    // Index to select the current step size from the array.
    int stepIndex = 2;


    @Override
    public void init() {
        // why is init erroring
       limelight.init(hardwareMap, telemetry);
       turret.init(hardwareMap);

       telemetry.addLine("Initiallized all mechanisms");
    }

    public void start() {
        turret.resetTimer();

    }

    @Override
    public void loop() {
    // vision logic (for webcam need to change to limelight)
        /* aprilTagWebcam.update();
           AprilTagDetection id20 = apriltagWebcam.getTagBySpecificId(20);
        */
// I think were supposed to use tx
        turret.update(id20);

        if (id20 != null) {
            telemetry.addData("curID", aprilTagWebcam);
        } else {
            telemetry.addLine("no Tag detected. Stopping turret motor");
        }


    }
}
