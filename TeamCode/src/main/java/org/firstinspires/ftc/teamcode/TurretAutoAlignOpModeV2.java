
package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.List;

@TeleOp(name = "Turret Auto Align V2")
public class TurretAutoAlignOpModeV2 extends OpMode {

    private Limelight3A limelight;
    private TurretMechV2 turret = new TurretMechV2();




    @Override
    public void init() {

        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        turret.init(hardwareMap, telemetry);


        telemetry.addLine("Initialized all mechanisms");
        telemetry.addLine("Testing");
    }

    @Override
    public void start() {

        limelight.start();
        turret.resetTimer();

        telemetry.addLine("Testing");
        telemetry.update();
    }

    @Override
    public void loop() {

        // Get latest Limelight results
        LLResult result = limelight.getLatestResult();

        if (result != null && result.isValid()) {

            // Get detected AprilTags
            List<LLResultTypes.FiducialResult> aprilTags =
                    result.getFiducialResults();

            LLResultTypes.FiducialResult id32 = null;

            // Look for AprilTag ID 32
            for (LLResultTypes.FiducialResult tag : aprilTags) {
                if (tag.getFiducialId() == 32) {
                    id32 = tag;
                    break;
                }
            }

            if (id32 != null) {

                turret.update(id32.getTargetXDegrees());

                telemetry.addData("curID", id32.getFiducialId());
                telemetry.addData("tx", id32.getTargetXDegrees());

            } else {
                turret.update(null); // change 0.0 to null
                telemetry.addLine("No AprilTag 32 detected");
            }

        } else {
            turret.update(null); // change 0.0 to null
            telemetry.addLine("No valid Limelight data");
        }

        telemetry.addLine("Testing");
        telemetry.update();
    }
}
