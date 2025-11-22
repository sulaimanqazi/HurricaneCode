package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepBlueAndRedAuto {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        // Declare our first bot
        RoadRunnerBotEntity myFirstBot = new DefaultBotBuilder(meepMeep)
                // We set this bot to be blue
                .setColorScheme(new ColorSchemeBlueDark())
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();
        final Pose2d startPose = new Pose2d(-51,-48, Math.toRadians(230));
        final Pose2d scorePose = new Pose2d(-25, -20,Math.toRadians(230));
        final Pose2d firstLinePose = new Pose2d(-12, -22, Math.toRadians(270));
        final Pose2d secondLinePose = new Pose2d(12, -22, Math.toRadians(270));
        final Pose2d thirdLinePose = new Pose2d(36, -43, Math.toRadians(270));
        myFirstBot.runAction(myFirstBot.getDrive().actionBuilder(startPose)
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

        // Declare out second bot
        RoadRunnerBotEntity mySecondBot = new DefaultBotBuilder(meepMeep)
                // We set this bot to be red
                .setColorScheme(new ColorSchemeRedDark())
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();
        Pose2d startPoseM     = new Pose2d(-51, 48, Math.toRadians(135));
        Pose2d scorePoseM      = new Pose2d(-25, 20, Math.toRadians(135));
        Pose2d firstLinePoseM  = new Pose2d(-12, 22, Math.toRadians(90));
        Pose2d secondLinePoseM = new Pose2d(12, 22, Math.toRadians(90));
        Pose2d thirdLinePoseM  = new Pose2d(-36, 43, Math.toRadians(90));

        mySecondBot.runAction(mySecondBot.getDrive().actionBuilder(startPoseM)
                .strafeToLinearHeading(scorePoseM.position, scorePoseM.heading)

                .splineToLinearHeading(new Pose2d(firstLinePoseM.position, firstLinePoseM.heading), firstLinePoseM.heading)
                .lineToY(55)


                .splineToLinearHeading(new Pose2d(scorePoseM.position, scorePoseM.heading), scorePoseM.heading)
                .splineToLinearHeading(new Pose2d(secondLinePoseM.position, secondLinePoseM.heading), secondLinePoseM.heading)
                .lineToY(55)
                .lineToY(-40)
                .splineToLinearHeading(new Pose2d(scorePoseM.position, scorePoseM.heading), scorePoseM.heading)
//                .splineToLinearHeading(new Pose2d(thirdLinePoseM.position, thirdLinePoseM.heading), thirdLinePoseM.heading)
//                .splineToLinearHeading(new Pose2d(scorePoseM.position, scorePoseM.heading), scorePoseM.heading)
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                // Add both of our declared bot entities
                .addEntity(myFirstBot)
                .addEntity(mySecondBot)
                .start();
    }
}