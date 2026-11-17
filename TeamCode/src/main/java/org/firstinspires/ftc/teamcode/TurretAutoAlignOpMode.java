
package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.List;

@TeleOp(name = "Turret Auto Align V2")
public class TurretAutoAlignOpMode extends OpMode {

    private Limelight3A limelight;
    private TurretMechV2 turret = new TurretMechV2();

    double[] stepSizes = {0.1, 0.01, 0.001, 0.0001, 0.00001};
    int stepIndex = 2;

    @Override
    public void init() {

        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        turret.init(hardwareMap);

        telemetry.addLine("Initialized all mechanisms");
    }

    @Override
    public void start() {

        limelight.start();
        turret.resetTimer();

    }

    @Override
    public void loop() {

        LLResult result = limelight.getLatestResult();

        if (result != null && result.isValid()) {

            List<LLResultTypes.FiducialResult> aprilTags =
                    result.getFiducialResults();

            LLResultTypes.FiducialResult id20 = null;

            for (LLResultTypes.FiducialResult tag : aprilTags) {
                if (tag.getFiducialId() == 20) {
                    id20 = tag;
                    break;
                }
            }

            if (id20 != null) {

                turret.update(id20.getTargetXDegrees());

                telemetry.addData("curID", id20.getFiducialId());
                telemetry.addData("tx", id20.getTargetXDegrees());

            } else {

                turret.update(null);
                telemetry.addLine("No AprilTag 20 detected");
            }

        } else {

            turret.update(null);
            telemetry.addLine("No valid Limelight data");
        }

        telemetry.update();
    }
}
