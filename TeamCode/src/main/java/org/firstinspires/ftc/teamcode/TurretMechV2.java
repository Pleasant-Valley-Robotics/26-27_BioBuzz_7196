// imports
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TurretMechV2 {

    private DcMotorEx turret;

    private double kP = 0.017; // Proportional Gain
    private double kD = 0.0005; //  Derivative Gain
    private double goalX = 0; // Target Location
    private double lastError = 0;
    private double angleTolerance = 0.2; // Angle Tolerance
    private final double MAX_POWER = 0.8; // Power Limit
    private double power = 0;
    private final ElapsedTime timer = new ElapsedTime();

    private boolean firstDetection = true;

    private Telemetry telemetry;

    public void init(HardwareMap hwMap, Telemetry telemetry) {
    this.telemetry = telemetry; // FIXED: assign the parameter to the field

    turret = hwMap.get(DcMotorEx.class, "turret");
    turret.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
}

    // KP stuff
    public void setkP(double newKP) {
        kP = newKP;
    }

    public double getkP() {
        return kP;
    }

    // KD stuff
    public void setkD(double newKD) {
        kD = newKD;
    }

    public double getkD() {
        return kD;
    }

    // making sure turret timer starts at the same time as other timers.
    public void resetTimer() {
        timer.reset();
        firstDetection = true;
    }

    // Limelight update using horizontal TX
    public void update(Double curTX) {

        double deltaTime = timer.seconds();
        timer.reset();

        // Stop turret if no AprilTag is detected
        if (curTX == null) {
            turret.setPower(0);
            lastError = 0;
            firstDetection = true;
            return;
        }

        // --------------start PD controller-------------------

        double error = goalX - curTX;
        double pTerm = error * kP;

        double dTerm = 0;
        if (!firstDetection && deltaTime > 0.001) {
            dTerm = ((error - lastError) / deltaTime) * kD;
        }

        if (Math.abs(error) < angleTolerance) {
            power = 0;
        } else {
            power = Range.clip(pTerm + dTerm, -MAX_POWER, MAX_POWER);
        }

        // Telemetry to be displayed
        telemetry.addData("TX", curTX);
        telemetry.addData("Error", error);
        telemetry.addData("P Term", pTerm);
        telemetry.addData("D Term", dTerm);
        telemetry.addData("Motor Power", power);

        // safety encoder check OR magnetic limit switch check, OR other safeties.
        // Limit Switches?

        turret.setPower(power);
        lastError = error;
        firstDetection = false;

    }

}
