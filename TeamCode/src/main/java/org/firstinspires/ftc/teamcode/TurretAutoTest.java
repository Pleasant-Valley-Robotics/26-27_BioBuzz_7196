package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.List;

@Autonomous(name = "Turret Auto Test")
public class TurretAutoTest extends OpMode {

    private Limelight3A limelight;
    private final TurretMechV2 turret = new TurretMechV2();

    @Override
    public void init() {

        limelight = hardwareMap.get(
                Limelight3A.class, "limelight");

        turret.init(hardwareMap, telemetry);

        telemetry.addLine("Turret Auto Initialized");
        telemetry.update();
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

            LLResultTypes.FiducialResult id32 = null;

            for (LLResultTypes.FiducialResult tag : aprilTags) {

                if (tag.getFiducialId() == 32) {
                    id32 = tag;
                    break;
                }
            }

            if (id32 != null) {

                double tx = id32.getTargetXDegrees();

                turret.update(tx);

                telemetry.addData("AprilTag", 32);
                telemetry.addData("TX", tx);

            } else {

                turret.update(null);
                telemetry.addLine("AprilTag 32 not detected");
            }

        } else {

            turret.update(null);
            telemetry.addLine("No valid Limelight data");
        }

        telemetry.update();
    }
}