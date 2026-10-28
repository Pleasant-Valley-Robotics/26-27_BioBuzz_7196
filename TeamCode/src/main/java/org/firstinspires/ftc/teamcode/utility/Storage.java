package org.firstinspires.ftc.teamcode.utility;

public class Storage {
    public static enum Alliance{
        RED,
        BLUE
    }

    public static Alliance alliance = Alliance.RED;

    public static enum AutoRan {
        FACING_HIVE,
        AWAY_HIVE
    }

    public static AutoRan autoRan = AutoRan.FACING_HIVE;
}
