package gardenerTest;

import battlecode.common.*;

import java.util.Random;

public class Gardener {
    public static void run(RobotController rc) {
        try {
            final float octa_con = 0.46926627053f;
            final float octa_con2 = 2.61312592975f;
            int status = 0;

            Random rand = new Random(rc.getID());
            int offset = rand.nextInt(6);
            Direction face = new Direction(360f * rand.nextFloat());

            while (true) {
                System.out.println(status);

                switch (status) {
                    case 0:
                        MapLocation center = rc.getLocation().add(face.opposite(), octa_con2-2);
                        if (rc.onTheMap(center, octa_con2+1) &&
                                !rc.isCircleOccupiedExceptByThisRobot(center, octa_con2+1)) {
                            status++;
                        } else {
                            if (rc.canMove(face)) rc.move(face);
                            else {
                                while (!rc.canMove(face)) {
                                    face = new Direction(360f * rand.nextFloat());
                                }
                                rc.move(face);
                            }
                        }
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                        if (rc.canPlantTree(face)) {
                            rc.plantTree(face);
                            if (rc.canMove(face.rotateRightDegrees(112.5f), octa_con)) {
                                rc.move(face.rotateRightDegrees(112.5f), octa_con);
                                face = face.rotateRightDegrees(45);
                                status++;
                            }
                        }
                        break;
                    case 8:
                        if (rc.canMove(face.opposite(), octa_con2-2)) {
                            rc.move(face.opposite(), octa_con2-2);
                            status++;
                        }
                    case 9:
                    default:

                }

                TreeInfo[] myTrees = rc.senseNearbyTrees(-1, rc.getTeam());
                if (myTrees.length > 0) {
                    TreeInfo lowest = myTrees[0];

                    for (TreeInfo tree : myTrees) {
                        if (tree.getHealth() < lowest.getHealth() && rc.canWater(tree.getID())) lowest = tree;
                    }
                    if (rc.canWater(lowest.getID())) rc.water(lowest.getID());
                }

                Clock.yield();
            }
        } catch (GameActionException e) {
            System.out.println(e.getMessage());
            rc.disintegrate();
        }
    }
}