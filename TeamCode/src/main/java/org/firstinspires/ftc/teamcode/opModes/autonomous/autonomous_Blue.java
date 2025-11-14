package org.firstinspires.ftc.teamcode.opModes.autonomous;


import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;

import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;


@Autonomous(name = "Blue_Auto", group = "Autonomous")
public class autonomous_Blue extends LinearOpMode {

    private MecanumDrive drive;

    @Override
    public void runOpMode() {
        final Pose2d startPose = new Pose2d(-51,-48, Math.toRadians(230));
        final Pose2d scorePose = new Pose2d(-25, -20,Math.toRadians(230));
        final Pose2d firstLinePose = new Pose2d(-12, -22, Math.toRadians(270));
        final Pose2d secondLinePose = new Pose2d(12, -22, Math.toRadians(270));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);

        waitForStart();

        if (isStopRequested()) return;


        // Build paths
        Action firstScorePath = drive.actionBuilder(startPose)
                .strafeToLinearHeading(scorePose.position, scorePose.heading)
                .splineToLinearHeading(new Pose2d(firstLinePose.position, firstLinePose.heading), firstLinePose.heading)
                .build();

        Action alignWithfirstLine = drive.actionBuilder(new Pose2d(38, -45, Math.toRadians(180)))
                .splineToLinearHeading(new Pose2d(scorePose.position, scorePose.heading), scorePose.heading)
                .splineToLinearHeading(new Pose2d(secondLinePose.position, secondLinePose.heading), secondLinePose.heading)

                .build();
        Action intake = drive.actionBuilder(new Pose2d(38, -45, Math.toRadians(180)))
                .splineToLinearHeading(new Pose2d(scorePose.position, scorePose.heading), scorePose.heading)
                .splineToLinearHeading(new Pose2d(secondLinePose.position, secondLinePose.heading), secondLinePose.heading)

                .build();

        Action path3 = drive.actionBuilder(new Pose2d(51, -10, Math.toRadians(145)))
                .splineTo(new Vector2d(15, -42), Math.toRadians(270))
                .splineTo(new Vector2d(-10, -17), Math.toRadians(130))
                .waitSeconds(0.5)
                .splineTo(new Vector2d(-10, -42), Math.toRadians(120))
                .splineTo(new Vector2d(-15, -30), Math.toRadians(115))
                .build();

//        Action alignNow = new LimelightAlignActions.AlignForSeconds(
//                hardwareMap,
//                "limelight",
//                "leftFront","rightFront","leftBack","rightBack",
//                2.0 // seconds to align


        Actions.runBlocking(
                drive.actionBuilder(startPose)
                        .strafeToLinearHeading(scorePose.position, scorePose.heading)

                        .splineToLinearHeading(new Pose2d(firstLinePose.position, firstLinePose.heading), firstLinePose.heading)
                        .lineToY(-55)


                        .splineToLinearHeading(new Pose2d(scorePose.position, scorePose.heading), scorePose.heading)
                        .splineToLinearHeading(new Pose2d(secondLinePose.position, secondLinePose.heading), secondLinePose.heading)
                        .lineToY(-55)

                        .splineToLinearHeading(new Pose2d(scorePose.position, scorePose.heading), scorePose.heading)
                        .waitSeconds(2)
                        .build()
        );
    }

}