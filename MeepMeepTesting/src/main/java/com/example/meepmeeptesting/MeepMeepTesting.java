package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        final Pose2d startPose = new Pose2d(-51,-48, Math.toRadians(230));
        final Pose2d scorePose = new Pose2d(-25, -20,Math.toRadians(230));
        final Pose2d firstLinePose = new Pose2d(-12, -22, Math.toRadians(270));
        final Pose2d secondLinePose = new Pose2d(12, -22, Math.toRadians(270));
        final Pose2d thirdLinePose = new Pose2d(36, -43, Math.toRadians(270));


        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(startPose.position, startPose.heading))
                .strafeToLinearHeading(scorePose.position, scorePose.heading)

                .splineToLinearHeading(new Pose2d(firstLinePose.position, firstLinePose.heading), firstLinePose.heading)
                .lineToY(-55)


                .splineToLinearHeading(new Pose2d(scorePose.position, scorePose.heading), scorePose.heading)
                .splineToLinearHeading(new Pose2d(secondLinePose.position, secondLinePose.heading), secondLinePose.heading)
                .lineToY(-55)

                 .splineToLinearHeading(new Pose2d(scorePose.position, scorePose.heading), scorePose.heading)
//                .splineToLinearHeading(new Pose2d(thirdLinePose.position, thirdLinePose.heading), thirdLinePose.heading)
//                .splineToLinearHeading(new Pose2d(scorePose.position, scorePose.heading), scorePose.heading)
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}