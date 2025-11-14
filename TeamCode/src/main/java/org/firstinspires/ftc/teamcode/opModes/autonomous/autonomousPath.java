package org.firstinspires.ftc.teamcode.opModes.autonomous;


import com.acmerobotics.roadrunner.Pose2d;

import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;


@Autonomous(name = "Autonomous Path", group = "Autonomous")
public class autonomousPath extends LinearOpMode {

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

        Actions.runBlocking(
                drive.actionBuilder(startPose)
                        .strafeToLinearHeading(scorePose.position, scorePose.heading)


                .splineToLinearHeading(new Pose2d(firstLinePose.position, firstLinePose.heading), firstLinePose.heading)
                .lineToY(-55)


                .splineToLinearHeading(new Pose2d(scorePose.position, scorePose.heading), scorePose.heading)

                .splineToLinearHeading(new Pose2d(secondLinePose.position, secondLinePose.heading), secondLinePose.heading)
                .lineToY(-55)

                .splineToLinearHeading(new Pose2d(scorePose.position, scorePose.heading), scorePose.heading)
                        .build()
        );
    }

}