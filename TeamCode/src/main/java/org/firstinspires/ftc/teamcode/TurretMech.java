package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.openftc.apriltag.AprilTagDetection;

public class TurretMech {

    private DcMotorEx turret;

    private double kP = 0.0001;
    private double kD = 0.0000;
    private double goalX = 0;
    private double lastError = 0;
    private double angleTolerance = 0.2;
    private final double MAX_POWER = 0.6;
    private double power = 0;
    private final ElapsedTime timer = new ElapsedTime();


    public void init(HardwareMap hwMap) {
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
        kP = newKD;
    }

    public double getkD() {
        return kD;
    }
// making sure turret timer starts at the same time as other timers.
    public void resetTimer () {
        timer.reset();
    }

    //why isn't update blue?
   public void update(AprilTagDetection curID) {
        double deltaTime = timer.seconds();
        timer.reset();
// "&& curID.id == 20" is only for the specific april tage 20, it can be removed
        if (curID == null && curID.id == 20) {
            turret.setPower(0);
            lastError = 0;
            return;
        }

        // --------------start PD controller-------------------
// IDk why ftcPose is and why it's erroring
       double error = goalX - curID.ftcPose.bearingdouble;
       double pTerm = error * kP;

       double dTerm = 0;
       if (deltaTime > 0) {
           dTerm = ((error - lastError) / deltaTime) * kD;
       }

       if (Math.abs(error) < angleTolerance) {
           power = 0;
       } else {
           power = Range.clip(pTerm + dTerm, -MAX_POWER, MAX_POWER);
       }

       // safety encoder check OR magnetic limit switch check, OR other safeties.

        turret.setPower(power);
        lastError = error;

   }

}
