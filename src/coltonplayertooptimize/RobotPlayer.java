package coltonplayertooptimize;

import battlecode.common.*;

import java.util.*;

/**
 * RobotPlayer is the class that describes your main robot strategy.
 * The run() method inside this class is like your main function: this is what we'll call once your robot
 * is created!
 */
public strictfp class RobotPlayer {

    /**
     * We will use this variable to count the number of turns this robot has been alive.
     * You can use static variables like this to save any information you want. Keep in mind that even though
     * these variables are static, in Battlecode they aren't actually shared between your robots.
     */
    static int turnCount = 0;
    static int carriersThisHqHasBuilt = 0;
    static int attackersThisHqHasBuilt = 0;
    static ArrayList<String> syms = new ArrayList<String>();
    static ArrayList<MapLocation> coordsOfOurHqs = new ArrayList<MapLocation>();
    static ArrayList<MapLocation> coordsOfEnemyHqs = new ArrayList<MapLocation>();
    static ArrayList<MapLocation> possibleCoordsOfEnemyHqsAlwaysThree = new ArrayList<MapLocation>();
    static ArrayList<MapLocation> possibleCoordsOfEnemyHqs = new ArrayList<MapLocation>();
    static MapLocation thisHqsEnemyHqMirror = null;
    static int amountOfLaunchersWhoChoseAHq = 0;
    static boolean enemyHqCoordsLocated = false;
    static MapLocation possibleEnemyHqFromVSym;
    static MapLocation possibleEnemyHqFromHSym;
    static MapLocation possibleEnemyHqFromRSym;
    static boolean symmetryFound;
    static int amountOfHqsThisHqKnows;
    static int amountOfHqsInThisGame;
    static MapLocation middlePos = null;
    //static boolean launcherBeenToMiddle = false;
    static boolean isScout = false;
    static int attackerIncrementer = 0;
    static MapLocation attackerIsAttackingThisLocation;
    static int numOfEnemyHqsInArray = 0;
    static int testCounter = 0;
    static boolean scoutResultFromPossibleHqLocation;
    static boolean scoutReturningHome = false;

    /**
     * KEEPING TRACK OF WHAT'S IN THE SHARED ARRAY
     * [0 ,    1-4     ,  5  ,       6-17       ,         18        ,         19          ,   20   ,     21         ,   22    ,     23     ,      24-63              ]
     * ind  hq coords  #ofhqs  enemy hq coords   #ofenemyhqsInArray   AtckHQEvenlyCounter    sym   scoutedEnemyHqLoc  hqOrNot  rewriteEhQ's
     */
    static final int hqStoringIndicatorIndex = 0;
    static final int hqCoordsStartingIndex = 1;
    static final int numOfHqsIndex = 5;
    static final int enemyHqCoordsStartingIndex = 6;
    static final int enemyHqCoordsEndingIndex = 17;
    static final int numOfEnemyHqsInArrayIndex = 18;
    static final int attackHqEvenlyCounterIndex = 19;
    static final int symIndex = 20;
    static final int scoutedEnemyHqLocationIndex = 21;
    static final int hqOrNotIndex = 22;
    static final int rewriteEnemyHqsIndex = 23;


    static MapLocation headquarter;
    static MapLocation islandLoc;
    static MapLocation closestWell;
    static boolean hasAnchor = false;
    static boolean collectAd = true;
    static boolean hasCollectedMana = true;
    static Direction currentDirection = null;
    static Set<MapLocation> adWells = new HashSet<>();
    static Set<MapLocation> mnWells = new HashSet<>();
    static MapLocation[] headquarterLocations = new MapLocation[GameConstants.MAX_STARTING_HEADQUARTERS];




    /**
     * A random number generator.
     * We will use this RNG to make some random moves. The Random class is provided by the java.util.Random
     * import at the top of this file. Here, we *seed* the RNG with a constant number (6147); this makes sure
     * we get the same sequence of numbers every time this code is run. This is very useful for debugging!
     */
    static final Random rng = new Random(6147);

    /** Array containing all the possible movement directions. */
    static final Direction[] directions = {
        Direction.NORTH,
        Direction.NORTHEAST,
        Direction.EAST,
        Direction.SOUTHEAST,
        Direction.SOUTH,
        Direction.SOUTHWEST,
        Direction.WEST,
        Direction.NORTHWEST,
    };

    /**
     * run() is the method that is called when a robot is instantiated in the Battlecode world.
     * It is like the main function for your robot. If this method returns, the robot dies!
     *
     * @param rc  The RobotController object. You use it to perform actions from this robot, and to get
     *            information on its current status. Essentially your portal to interacting with the world.
     **/
    @SuppressWarnings("unused")
    public static void run(RobotController rc) throws GameActionException {

        // Hello world! Standard output is very useful for debugging.
        // Everything you say here will be directly viewable in your terminal when you run a match!

        System.out.println("I'm a " + rc.getType() + " and I just got created! I have health " + rc.getHealth());

        // You can also use indicators to save debug notes in replays.
        // rc.setIndicatorString("Hello world!");

        while (true) {
            // This code runs during the entire lifespan of the robot, which is why it is in an infinite
            // loop. If we ever leave this loop and return from run(), the robot dies! At the end of the
            // loop, we call Clock.yield(), signifying that we've done everything we want to do.

            turnCount += 1;  // We have now been alive for one more turn!

            // Try/catch blocks stop unhandled exceptions, which cause your robot to explode.
            try {
                // The same run() function is called for every robot on your team, even if they are
                // different types. Here, we separate the control depending on the RobotType, so we can
                // use different strategies on different robots. If you wish, you are free to rewrite
                // this into a different control structure!
                switch (rc.getType()) {
                    case HEADQUARTERS:      runHeadquarters(rc);    break;
                    case CARRIER:           runCarrier(rc);         break;
                    case LAUNCHER:          runLauncher(rc);        break;
                    case BOOSTER:
                    case DESTABILIZER:
                    case AMPLIFIER:                                 break;
                }

            } catch (GameActionException e) {
                // Oh no! It looks like we did something illegal in the Battlecode world. You should
                // handle GameActionExceptions judiciously, in case unexpected events occur in the game
                // world. Remember, uncaught exceptions cause your robot to explode!
                System.out.println(rc.getType() + " Exception");
                e.printStackTrace();

            } catch (Exception e) {
                // Oh no! It looks like our code tried to do something bad. This isn't a
                // GameActionException, so it's more likely to be a bug in our code.
                System.out.println(rc.getType() + " Exception");
                e.printStackTrace();

            } finally {
                // Signify we've done everything we want to do, thereby ending our turn.
                // This will make our code wait until the next turn, and then perform this loop again.
                Clock.yield();
            }
            // End of loop: go back to the top. Clock.yield() has ended, so it's time for another turn!
        }

        // Your code should never reach here (unless it's intentional)! Self-destruction imminent...
    }

    /**
     * Run a single turn for a Headquarters.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    static void runHeadquarters(RobotController rc) throws Exception {
        // takes up 3-9 spots in the shared array (depending on how many hq's)
        MapLocation me = rc.getLocation();
        int width = rc.getMapWidth();
        int height = rc.getMapHeight();

        if (rc.getRoundNum() == 1) {
            // round one puts each hq into the shared array, and puts each hq's possible guesses of its doppelgänger
            // enemy hq into static lists (used on round 2)

            // put this hq into the array with our hq's
            storeOurHqToArray(rc, me);

            middlePos = new MapLocation((int) Math.round((double) rc.getMapWidth() / 2), (int) Math.round((double) rc.getMapWidth() / 2));

            // put the enemy hq coords to possibleCoordsOfEnemyHqs and possibleCoordsOfEnemyHqsAlwaysThree
            addEnemyHqCoordsToTheStaticLists(width, height, me);

            // amountOfHqsThisHqKnows = rc.readSharedArray(0) / 2;
            amountOfHqsInThisGame = rc.readSharedArray(numOfHqsIndex);
            // System.out.println("rc.getRobotCount() returns: " + rc.getRobotCount() + "\n");

        }
        if (rc.getRoundNum() == 2) {
            // try and guess the symmetry of the map on this turn. If you can't, then add a bunch of possible enemy hq

            giveCallingRobotAListOfOurHqs(rc);

            // guess the symmetry based on only our starting hq positions
            syms = guessSymmetryBasedOnOurInitialHqLocations();
            // rc.setIndicatorString(syms.toString());

            if (syms.size() > 1) {
                //symmetry wasn't guessed just based on our initialHqPositions, so try another way
                System.out.println("Before" + syms);
                syms = guessSymmetryBasedOnEnemyHqsWeCanSee(rc);
                System.out.println("After" + syms);
            }

            if (syms.size() == 1) {
                enemyHqCoordsLocated = true;
                writeTheSymToSharedArray(rc, syms.get(0));
                // we found the symmetry
                MapLocation theFabledLocation = possibleCoordsOfEnemyHqs.get(0);
                rc.setIndicatorDot(theFabledLocation, 0, 255, 0);
            } else {
                // didn't find the symmetry
                for (MapLocation possibleCoordsOfEnemyHq : possibleCoordsOfEnemyHqs) {
                    rc.setIndicatorDot(possibleCoordsOfEnemyHq, 0, 0, 255);
                }
            }
            //System.out.println(numOfEnemyHqsInArray);
            // write the current guesses to the array so that launchers can go to one of them, even if these change on round 3
            //System.out.println(numOfEnemyHqsInArray);
            //System.out.println(possibleCoordsOfEnemyHqs);
            for (MapLocation anEnemyHqCoords : possibleCoordsOfEnemyHqs) {
                numOfEnemyHqsInArray = rc.readSharedArray(numOfEnemyHqsInArrayIndex);
                rc.writeSharedArray((enemyHqCoordsStartingIndex + numOfEnemyHqsInArray), locationToInt(rc, anEnemyHqCoords));
                rc.writeSharedArray(numOfEnemyHqsInArrayIndex, numOfEnemyHqsInArray + 1);
            }
        }
        if (rc.getRoundNum() == 3) {
            rewriteTheEnemyHqPositions(rc);
        }
        rc.setIndicatorString(String.valueOf(rc.readSharedArray(rewriteEnemyHqsIndex)));
        if (rc.readSharedArray(rewriteEnemyHqsIndex) == rc.getRoundNum()) {
            System.out.println("rewriting the enemy hq positions");
            rc.setIndicatorString("rewriting the enemy hq positions");
            rewriteTheEnemyHqPositions(rc);
            int teller = amountOfHqsInThisGame*2;
            if ((rc.getID() == teller) || rc.getID() == (teller+1)) {
                rc.writeSharedArray(rewriteEnemyHqsIndex, 0);
            }
        }

        // if an attacker came back with enemy hq information, we can make another symmetry guess!
        if (rc.readSharedArray(scoutedEnemyHqLocationIndex) != 0) {
            System.out.println("got info from attackers");
            rc.setIndicatorString("GOT INFORMATION FROM THE ATTACKERS");
            // an attacker deposited the information about one of our guessed for enemy hq locations! let's try to
            // guess the symmetry again
            MapLocation scoutedMapPos = intToLocation(rc, rc.readSharedArray(scoutedEnemyHqLocationIndex));
            boolean hqOrNot = intToBoolean(rc.readSharedArray(hqOrNotIndex));
            syms = guessSymmetryBasedOnAttackerDepositedInformation(rc, scoutedMapPos, hqOrNot);

            if (syms.size() == 1) {
                enemyHqCoordsLocated = true;
                writeTheSymToSharedArray(rc, syms.get(0));
                // we found the symmetry
                MapLocation theFabledLocation = possibleCoordsOfEnemyHqs.get(0);
                rc.setIndicatorDot(theFabledLocation, 0, 255, 0);
            } else {
                // didn't find the symmetry
                for (MapLocation possibleCoordsOfEnemyHq : possibleCoordsOfEnemyHqs) {
                    rc.setIndicatorDot(possibleCoordsOfEnemyHq, 0, 0, 255);
                }
            }
            rc.writeSharedArray(scoutedEnemyHqLocationIndex, 0);
            rc.writeSharedArray(hqOrNotIndex, 0);
            System.out.println("wrote to rewriteEnemyHqsIndex");
            rc.writeSharedArray(rewriteEnemyHqsIndex, rc.getRoundNum()+1);
        }



        middlePos = new MapLocation((int) Math.round( (double) rc.getMapWidth() / 2), (int) Math.round( (double) rc.getMapWidth() / 2));

        // Pick a direction to build in.

        //what map locations we want to try spawning an attacker in, starting with most wanted, ending with the least wanted
        MapLocation[] attackerSpawnLocs = {
                me.add(me.directionTo(middlePos)),
                me.add(me.directionTo(middlePos).rotateRight()),
                me.add(me.directionTo(middlePos).rotateLeft()),
                me.add(me.directionTo(middlePos).rotateRight().rotateRight()),
                me.add(me.directionTo(middlePos).rotateLeft().rotateLeft()),
        };

        //what map locations we want to try spawning a carrier in, starting with most wanted, ending with the least wanted
        MapLocation[] carrierSpawnLocs = new MapLocation[directions.length];
        for (int i = 0; i < directions.length; i++) {
            Direction direction = directions[i];
            carrierSpawnLocs[i] = me.add(direction);
        }

        //rc.setIndicatorString("IM HERE");




        MapLocation hqLocation = rc.getLocation();
        Direction direction = directions[rng.nextInt(directions.length)];
        MapLocation spawnLocation = hqLocation.add(direction);

        //build attackers and carriers
        if (turnCount == 1) {
            addHqLocations(rc);
            lookForWellType(rc);
        }
        // Spawn three launchers to attack the other team
        if (turnCount <= 3) spawnRobot(rc, RobotType.LAUNCHER, spawnLocation);
        if (rc.canBuildAnchor(Anchor.STANDARD) && turnCount > 50) {
            rc.buildAnchor(Anchor.STANDARD);
        }
        if (collectAd && !adWells.isEmpty() && turnCount > 3) {
            spawnRobot(rc, RobotType.CARRIER, hqLocation.add(hqLocation.directionTo(adWells.iterator().next())));
            collectAd = false;
        } else if (!collectAd && !mnWells.isEmpty() && turnCount > 3) {
            spawnRobot(rc, RobotType.CARRIER, hqLocation.add(hqLocation.directionTo(mnWells.iterator().next())));
            collectAd = true;
        } else if (turnCount > 3){
            spawnRobot(rc, RobotType.CARRIER, spawnLocation);
        }
        spawnRobot(rc, RobotType.LAUNCHER, spawnLocation);

        // printSharedArray(rc);

//        //MapLocation attackerSpawnLocation = me.add(me.directionTo(middlePos)).add(me.directionTo(middlePos));
//        MapLocation attackerSpawnLocation = me.add(me.directionTo(middlePos));
//
//        rc.setIndicatorString(attackerSpawnLocation.toString());
//        MapLocation carrierSpawnLocationClose = attackerSpawnLocation;
//        MapLocation carrierSpawnLocationFar = attackerSpawnLocation;
//
//        WellInfo[] wells = rc.senseNearbyWells(RobotType.HEADQUARTERS.visionRadiusSquared);
//        if (wells.length > 0) {
////            for (WellInfo well : wells) {
////                Direction closestToWell = me.directionTo(well.getMapLocation());
////                carrierSpawnLocationClose = me.add(closestToWell);
////                carrierSpawnLocationFar = carrierSpawnLocationClose.add(closestToWell);
////            }
//            carrierSpawnLocationClose = me.add(me.directionTo(wells[0].getMapLocation()));
//            carrierSpawnLocationFar = carrierSpawnLocationClose.add(me.directionTo(wells[0].getMapLocation()));
//        }
//        if (turnCount <= 3) {
//            Direction dir = me.directionTo(middlePos);
//            Direction[] moveDirs = new Direction[5];
//            moveDirs[0] = dir;
//            moveDirs[1] = dir.rotateRight();
//            moveDirs[2] = dir.rotateLeft();
//            if (rc.canBuildRobot(RobotType.LAUNCHER, attackerSpawnLocation)){
//                rc.buildRobot(RobotType.LAUNCHER, attackerSpawnLocation);
//                rc.setIndicatorString("Spawned attacker at " + attackerSpawnLocation);
//                // increment the AttackHqEvenlyCounter
//                rc.writeSharedArray(18, rc.readSharedArray(18) + 1);
//            }
//        } else if (turnCount <= 7) {
//            if (rc.canBuildRobot(RobotType.CARRIER, carrierSpawnLocationFar)) {
//                rc.buildRobot(RobotType.CARRIER, carrierSpawnLocationFar);
//            } else if (rc.canBuildRobot(RobotType.CARRIER, carrierSpawnLocationClose)) {
//                rc.buildRobot(RobotType.CARRIER, carrierSpawnLocationClose);
//            }
//        } else {
//            if (rc.canBuildRobot(RobotType.LAUNCHER, attackerSpawnLocation)) {
//                rc.buildRobot(RobotType.LAUNCHER, attackerSpawnLocation);
//                rc.setIndicatorString("Spawned attacker at " + attackerSpawnLocation);
//                // increment the AttackHqEvenlyCounter
//                rc.writeSharedArray(18, rc.readSharedArray(18) + 1);
//            }
//            else if (rc.canBuildRobot(RobotType.CARRIER, carrierSpawnLocationFar)) {
//                rc.buildRobot(RobotType.CARRIER, carrierSpawnLocationFar);
//            } else if (rc.canBuildRobot(RobotType.CARRIER, carrierSpawnLocationClose)) {
//                rc.buildRobot(RobotType.CARRIER, carrierSpawnLocationClose);
//            }
//        }
        // printSharedArray(rc);
    }

    /**
     * Run a single turn for a Carrier.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    static void runCarrier(RobotController rc) throws GameActionException {
        // Get the location of the HQ
        if (turnCount == 1) findHq(rc);
        if (turnCount == 2) updateHqLocations(rc);
        if (rc.canTakeAnchor(headquarter, Anchor.STANDARD)) {
            rc.takeAnchor(headquarter, Anchor.STANDARD);
            hasAnchor = true;
        }
        // If carrier has an anchor go try and place it, else go get resources
        scanIslands(rc);
        if (hasCollectedMana) {
            getClosestAdWell(rc);
            if (closestWell != null && rc.canCollectResource(closestWell, -1)) {
                rc.collectResource(closestWell, -1);
            }
            if (totalResources(rc) == GameConstants.CARRIER_CAPACITY) {
                if (rc.getLocation().isAdjacentTo(headquarter)) {
                    for (ResourceType resourceType : ResourceType.values()) {
                        if (rc.canTransferResource(headquarter, resourceType, rc.getResourceAmount(resourceType))) {
                            rc.transferResource(headquarter, resourceType, rc.getResourceAmount(resourceType));
                            hasCollectedMana = false;
                            closestWell = null;
                        }
                    }
                } else {
                    moveTo(rc, headquarter);
                }
            }

        }
        else {
            getClosestManaWell(rc);
            if (closestWell != null && rc.canCollectResource(closestWell, -1)){
                rc.collectResource(closestWell, -1);
            }
            if (totalResources(rc) == GameConstants.CARRIER_CAPACITY) {
                if (rc.getLocation().isAdjacentTo(headquarter)) {
                    for (ResourceType resourceType : ResourceType.values()) {
                        if (rc.canTransferResource(headquarter, resourceType, rc.getResourceAmount(resourceType))) {
                            rc.transferResource(headquarter, resourceType, rc.getResourceAmount(resourceType));
                            hasCollectedMana = true;
                            closestWell = null;
                        }
                    }
                } else {
                    moveTo(rc, headquarter);
                }
            }

        }
        if (hasAnchor) {
            if (islandLoc == null) moveRandomDir(rc);
            else {
                moveTo(rc, islandLoc);
            }
            if(rc.canPlaceAnchor() && rc.senseTeamOccupyingIsland(rc.senseIsland(rc.getLocation())) == Team.NEUTRAL) {
                rc.placeAnchor();
                hasAnchor = false;
            }
        } else {
            if (totalResources(rc) == 0) {
                if (closestWell == null) moveRandomDir(rc);
                else if (!(rc.getLocation().isAdjacentTo(closestWell))) {
                    moveTo(rc, closestWell);
                }
            }
        }
    }

    /**
     * Run a single turn for a Launcher.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    static void runLauncher(RobotController rc) throws GameActionException {

        middlePos = new MapLocation((int) Math.round( (double) rc.getMapWidth() / 2), (int) Math.round( (double) rc.getMapWidth() / 2));
        amountOfHqsInThisGame = rc.readSharedArray(numOfHqsIndex);
        numOfEnemyHqsInArray = rc.readSharedArray(numOfEnemyHqsInArrayIndex);
        MapLocation me = rc.getLocation();

        // on every turn, the attackers get an array of all the enemy hq locations in the array
        //System.out.println("Before: " + coordsOfEnemyHqs);
        //printSharedArray(rc);
        giveCallingRobotAListOfEnemyHqs(rc);
        giveCallingRobotAListOfOurHqs(rc);
        //System.out.println("After: " + coordsOfEnemyHqs);
        //System.out.println(coordsOfEnemyHqs);

        // if # of our hqs == # of enemy hq locations in the array, those probable enemy locations are the real deal!
        if (amountOfHqsInThisGame == numOfEnemyHqsInArray) {
            enemyHqCoordsLocated = true;
        }
        if (turnCount == 1) {
            // SPAWNED :D
            middlePos = new MapLocation((int) Math.round( (double) rc.getMapWidth() / 2), (int) Math.round( (double) rc.getMapWidth() / 2));

        }

        int modNumber = amountOfHqsInThisGame;

//        // if the enemy hq coords are determined fs, we can beeline an enemy hq by setting attackerIsAttackingThisLocation
//        if (enemyHqCoordsLocated) {
//
//
//
//
//
//
//            // use this if we want to split up every attacker in the game evenly among the enemy hq positions
//            attackerIsAttackingThisLocation = intToLocation(rc, rc.readSharedArray(replaceThis + enemyHqCoordsStartingIndex));
//        }

        //printSharedArray(rc);
        //System.out.println("coords of enemy hqs: " + coordsOfEnemyHqs);
        MapLocation closestEnemyHq = coordsOfEnemyHqs.get(0);
        StringBuilder indString = new StringBuilder();
//        indString.append(closestEnemyHq);
//        indString.append(" ");
        if (!enemyHqCoordsLocated) {
            for (MapLocation coordsOfEnemyHq : coordsOfEnemyHqs) {
//            indString.append(coordsOfEnemyHq);
//            indString.append(" ");
//            indString.append(me.distanceSquaredTo(coordsOfEnemyHq));
//            indString.append(" ");
                if (me.distanceSquaredTo(coordsOfEnemyHq) < me.distanceSquaredTo(closestEnemyHq)) {
                    closestEnemyHq = coordsOfEnemyHq;
                    //indString.append(me.distanceSquaredTo(closestEnemyHq));
                }
            }
            attackerIsAttackingThisLocation = closestEnemyHq;
        } else {
            attackerIsAttackingThisLocation = coordsOfEnemyHqs.get(rc.getID() % numOfEnemyHqsInArray);
        }


        indString.append(" Atck ");
        indString.append(closestEnemyHq);
        indString.append(" | ");
        indString.append(me.distanceSquaredTo(closestEnemyHq));
        rc.setIndicatorString(String.valueOf(indString));


        // try attacking before movement
        if (rc.isActionReady()) {
            attackerAttackAround(rc);
        }

        //handle movement
        if (rc.isMovementReady()) {
            if (attackerIsAttackingThisLocation != null) {

                if (enemyHqCoordsLocated) {
                    scoutReturningHome = false;
                } else {
                    checkIfWeSeeAHqOnOurTravels(rc);
                }

                if (scoutReturningHome) {
                    MapLocation hqPos = getTheClosestHq(rc);
                    moveToThisLocation(rc, hqPos);
                    //rc.setIndicatorString("Heading home to " + hqPos);
                    //if (hqPos.isWithinDistanceSquared(me, GameConstants.DISTANCE_SQUARED_FROM_HEADQUARTER))
                    if (hqPos.isWithinDistanceSquared(me, RobotType.HEADQUARTERS.actionRadiusSquared)) {
                        //rc.setIndicatorString("in range of hq, but cant write.....");
                        if (rc.canWriteSharedArray(scoutedEnemyHqLocationIndex, locationToInt(rc, attackerIsAttackingThisLocation))) {
                            //rc.setIndicatorString("MADE IT HOME, depositing information to the array");
                            rc.writeSharedArray(scoutedEnemyHqLocationIndex, locationToInt(rc, attackerIsAttackingThisLocation));
                            rc.writeSharedArray(hqOrNotIndex, booleanToInt(scoutResultFromPossibleHqLocation));
                            scoutReturningHome = false;
                        }
                    }
                } else {
                    // moves the attacker closer to this location target
                    attackerMoveToLocation(rc, me);
                }

            } else {
                moveRandomly(rc);
            }
        }

        //try attacking after moving
        if (rc.isActionReady()) {
            attackerAttackAround(rc);
        }
    }

/*
------------------------------------------------------------------------------------------------------------------------
                                        GENERAL FUNCTIONS
------------------------------------------------------------------------------------------------------------------------
*/
    /**
     * simply gives the calling robot the knowledge of where all of our hqs are, for ease of access later on shall we
     * need it. Sets the coordsOfOurHqs static list
     * @param rc RobotController
     * @throws GameActionException from reading array
     */
    static void giveCallingRobotAListOfOurHqs(RobotController rc) throws GameActionException {
        // give the calling robot the coords of our hqs
        for (int i = hqCoordsStartingIndex; i < hqCoordsStartingIndex + amountOfHqsInThisGame; i++) {
            MapLocation Hq = intToLocation(rc, rc.readSharedArray(i));
            coordsOfOurHqs.add(Hq);
        }
    }

    /**
     * simply gives calling robot the locations of the enemy hqs that are in the shared array (all the enemy hq
     * positions that we know so far at this point). Sets the coordsOfEnemyHqs static list
     * @param rc RobotController
     * @throws GameActionException from reading array
     */
    static void giveCallingRobotAListOfEnemyHqs(RobotController rc) throws GameActionException {
        coordsOfEnemyHqs.clear();
        //System.out.println(enemyHqCoordsStartingIndex);
        //System.out.println(numOfEnemyHqsInArray + enemyHqCoordsStartingIndex);
        ArrayList<MapLocation> newCoordsOfEnemyHqs = null;
        for (int i = enemyHqCoordsStartingIndex; i < enemyHqCoordsStartingIndex + numOfEnemyHqsInArray; i++) {
            //System.out.println("RAN THE FOR LOOP");
            MapLocation Hq = intToLocation(rc, rc.readSharedArray(i));
            //System.out.println(Hq);
            coordsOfEnemyHqs.add(Hq);
        }
        //System.out.println("after: " + coordsOfEnemyHqs);
    }

    /**
     * prints the shared array
     * @param rc RobotController
     * @throws GameActionException from readSharedArray
     */
    static void printSharedArray(RobotController rc) throws GameActionException {
        StringBuilder sharedArray = new StringBuilder();
        int counter = 0;
        while (counter < GameConstants.SHARED_ARRAY_LENGTH) {
            sharedArray.append((rc.readSharedArray(counter)));
            sharedArray.append(", ");
            counter++;
        }
        System.out.println(sharedArray);
    }

    /**
     * puts a map location to an integer
     * @param rc RobotController
     * @param location MapLocation to convert
     * @return location in integer form
     */
    static int locationToInt(RobotController rc, MapLocation location) {
        if (location == null) return 0;
        return 1 + location.x + location.y * rc.getMapWidth();
    }

    /**
     * puts an integer to a map location
     * @param rc RobotController
     * @param integerLocation integer to convert
     * @return integer in location form
     */
    static MapLocation intToLocation(RobotController rc, int integerLocation) {
        if (integerLocation == 0) return null;
        integerLocation--;
        return new MapLocation(integerLocation % rc.getMapWidth(), integerLocation / rc.getMapWidth());
    }

    /**
     *
     * @param i
     * @return
     * @throws Exception if i is not 0 or 1
     */
    static boolean intToBoolean(int i) throws Exception {
        if (i == 0) return false;
        if (i == 1) return true;
        throw new Exception();
    }

    static int booleanToInt(boolean bool) {
        if (bool) return 1;
        else return 0;
    }

    /**
     * moves a robot randomly
     * @param rc RobotController
     * @throws GameActionException from move()
     */
    static void moveRandomly(RobotController rc) throws GameActionException {
        Direction randomDir = directions[rng.nextInt(directions.length)];
        if (rc.canMove(randomDir)) {
            rc.move(randomDir);
            //rc.setIndicatorString("Moving " + dir);
        }
    }

    /**
     * try moving to this location
     * @param rc
     * @param tryToMoveToThis location to move to
     * @throws GameActionException
     */
    static void moveToThisLocation(RobotController rc, MapLocation tryToMoveToThis) throws GameActionException {
        MapLocation me = rc.getLocation();
        Direction dir = me.directionTo(tryToMoveToThis);
        Direction[] moveDirs = new Direction[5];
        moveDirs[0] = dir;
        moveDirs[1] = dir.rotateRight();
        moveDirs[2] = dir.rotateLeft();
        moveDirs[3] = dir.rotateRight().rotateRight();
        moveDirs[4] = dir.rotateLeft().rotateLeft();

        for (int i = 0; i < moveDirs.length; i++) {
            Direction moveDir = moveDirs[i];
            if (rc.canMove(moveDir)) {
                rc.move(moveDir);
                break;
            }
            if (i == (moveDirs.length - 1)) {
                Direction randomDir = directions[rng.nextInt(directions.length)];
                if (rc.canMove(randomDir)) {
                    rc.move(randomDir);
                    // rc.setIndicatorString("Moving " + dir + " randomly");
                }
            }
        }
        if (rc.isMovementReady()) {
            // rc.setIndicatorString("Didn't move, will change this later hopefully");
        }
    }

    /**
     * moves robot to closest hq. PLEASE have coordsOfOurHqs set before calling this.
     * @param rc RobotController
     */
    static MapLocation getTheClosestHq(RobotController rc) {
        MapLocation me = rc.getLocation();
        MapLocation hqPos = coordsOfOurHqs.get(0);
        // System.out.println(coordsOfOurHqs);

        // find the closest hq to us
        for (MapLocation hqCoords : coordsOfOurHqs) {
            if (me.distanceSquaredTo(hqCoords) < me.distanceSquaredTo(hqPos)) {
                hqPos = hqCoords;
            }
        }
        return hqPos;
    }

/*
------------------------------------------------------------------------------------------------------------------------
                                          HQ FUNCTIONS
------------------------------------------------------------------------------------------------------------------------
*/
    static void clearEnemyHqsFromTheSharedArray(RobotController rc) throws GameActionException {
        numOfEnemyHqsInArray = 0;
        rc.writeSharedArray(numOfEnemyHqsInArrayIndex, 0);
        for (int i = enemyHqCoordsStartingIndex; i <= enemyHqCoordsEndingIndex; i++) {
            rc.writeSharedArray(i, 0);
        }
    }

    static void writeTheSymToSharedArray(RobotController rc, String sym) throws GameActionException {
        if (sym.equals("V")) rc.writeSharedArray(symIndex, 1);
        if (sym.equals("H")) rc.writeSharedArray(symIndex, 2);
        if (sym.equals("R")) rc.writeSharedArray(symIndex, 3);
    }

    /**
     * gives the calling hq its doppelgänger enemy hq's map location, could be useful later
     * @param sym the symmetry of the map.
     */
    static void getThisHqsMirroredEnemyHqLocation(String sym) {
        enemyHqCoordsLocated = true;
        if (sym.equals("V")) {
            thisHqsEnemyHqMirror = possibleCoordsOfEnemyHqsAlwaysThree.get(0);
        } else if (sym.equals("H")) {
            thisHqsEnemyHqMirror = possibleCoordsOfEnemyHqsAlwaysThree.get(1);
        } else if (sym.equals("R")) {
            thisHqsEnemyHqMirror = possibleCoordsOfEnemyHqsAlwaysThree.get(2);
        }
    }

    /**
     * simply just adds the possible enemy hqs that we know because of map symmetry. Adds to static lists, where these
     * lists contain the coords that this current hq thinks its doppelgänger enemy hq is (3 different locations, because
     * 3 different possible symmetries)
     * @param width width of the map
     * @param height height of the map
     * @param me this robots location
     */
    static void addEnemyHqCoordsToTheStaticLists(int width, int height, MapLocation me) {
        // this block puts the three possible locations the enemy hq can be based on its position into a list
        //horizontal possible loc
        possibleEnemyHqFromVSym = new MapLocation((width - me.x) - 1 , me.y);
        //vertical possible loc
        possibleEnemyHqFromHSym = new MapLocation(me.x , (height - me.y) - 1);
        //rotational possible loc
        possibleEnemyHqFromRSym = new MapLocation((width - me.x) - 1 , (height - me.y) - 1);
        possibleCoordsOfEnemyHqs.add(possibleEnemyHqFromVSym);
        possibleCoordsOfEnemyHqsAlwaysThree.add(possibleEnemyHqFromVSym);
        possibleCoordsOfEnemyHqs.add(possibleEnemyHqFromHSym);
        possibleCoordsOfEnemyHqsAlwaysThree.add(possibleEnemyHqFromHSym);
        possibleCoordsOfEnemyHqs.add(possibleEnemyHqFromRSym);
        possibleCoordsOfEnemyHqsAlwaysThree.add(possibleEnemyHqFromRSym);
    }

    /**
     * guess the symmetry of the map, only knowing our hq locations
     * @return the symmetries we have narrowed it down to after doing this process (["H", "V", "R"] if we didn't narrow
     * it at all, and only one of those would remain if we can narrow it down all the way
     */
    static ArrayList<String> guessSymmetryBasedOnOurInitialHqLocations() {
        ArrayList<String> syms = new ArrayList<>(3);
        syms.add("V");
        syms.add("H");
        syms.add("R");
        MapLocation originalCheckerHqPos = coordsOfOurHqs.get(0);
        //can only guess symmetries with more than 1 hq, so all three will remain valid after this
        if (coordsOfOurHqs.size() > 1) {
            for (int i = 1; i < coordsOfOurHqs.size(); i++) {
                MapLocation currentCheckerHqPos = coordsOfOurHqs.get(i);

                if (((originalCheckerHqPos.x < middlePos.x) && (currentCheckerHqPos.x > middlePos.x)) || (originalCheckerHqPos.x > middlePos.x) && (currentCheckerHqPos.x < middlePos.x)) {
                    //these two hqs are on opposite sides of the vertical symmetry line, so it CANNOT BE VERTICAL SYMMETRY
                    syms.remove("V");
                    //remove the enemy hq calculated with vertical symmetry from the possible enemy hq spots
                    possibleCoordsOfEnemyHqs.remove(possibleEnemyHqFromVSym);
                }
                if (((originalCheckerHqPos.y < middlePos.y) && (currentCheckerHqPos.y > middlePos.y)) || ((originalCheckerHqPos.y > middlePos.y) && (currentCheckerHqPos.y < middlePos.y))) {
                    //these two hqs are on opposite sides of the horizontal symmetry line, so it CANNOT BE HORIZONTAL SYMMETRY
                    syms.remove("H");
                    //remove the enemy hq calculated with horizontal symmetry from the possible enemy hq spots
                    possibleCoordsOfEnemyHqs.remove(possibleEnemyHqFromHSym);
                }
                if (false) {
                    // uhhhhh so how do eliminate rotational symmetry as an option
                    syms.remove("R");
                    possibleCoordsOfEnemyHqs.remove(possibleEnemyHqFromRSym);
                }
            }
        }
        return syms;
    }

    /**
     *
     * @param rc
     * @return
     * @throws GameActionException from senseNearbyRobots
     */
    static ArrayList<String> guessSymmetryBasedOnEnemyHqsWeCanSee(RobotController rc) throws GameActionException {
        MapLocation me = rc.getLocation();
        ArrayList<String> theActualSymmetry = new ArrayList<>();
        for (int i = 0; i < possibleCoordsOfEnemyHqs.size(); i++) {
        //System.out.println("looping through the possible coords of enemy hqs we have");
        MapLocation possibleHqPosition = possibleCoordsOfEnemyHqs.get(i);
            //if (me.distanceSquaredTo(possibleHqPosition) <= RobotType.HEADQUARTERS.visionRadiusSquared) {
            if (me.distanceSquaredTo(possibleHqPosition) <= 34) {
                // if the possible hq position is in range of this hq
                if (rc.canSenseRobotAtLocation(possibleHqPosition)) {
                    // if there's a headquarters at this location, great!
                    RobotInfo enemyRobot = rc.senseRobotAtLocation(possibleHqPosition);
                    // this possible hq spot is actually a hq!
                    possibleCoordsOfEnemyHqs.clear();
                    possibleCoordsOfEnemyHqs.add(enemyRobot.getLocation());
                    theActualSymmetry.add(syms.get(i));
                    // should be the symmetry of the map
                    return theActualSymmetry;
                } else {
                    // if there's no headquarters at this location, that's fine, we can narrow the possible spots down
                    possibleCoordsOfEnemyHqs.remove(i);
                    syms.remove(i);
                }
            }
        }

        // return original symmetry if we couldn't guess it here
        return syms;
    }

    static ArrayList<String> guessSymmetryBasedOnAttackerDepositedInformation(RobotController rc, MapLocation scoutedMapPos, boolean ifThereWasAHqThereOrNot) {
        System.out.println("Before: " + possibleCoordsOfEnemyHqs);
        ArrayList<String> theActualSymmetry = new ArrayList<>();
        for (int i = 0; i < possibleCoordsOfEnemyHqs.size(); i++) {
            MapLocation possibleHqPosition = possibleCoordsOfEnemyHqs.get(i);
            if (possibleHqPosition.equals(scoutedMapPos)) {
                System.out.println("THE LOCATIONS MATCH UP FOR " + scoutedMapPos);
                if (ifThereWasAHqThereOrNot) {
                    // this possible hq spot is actually a hq!
                    possibleCoordsOfEnemyHqs.clear();
                    possibleCoordsOfEnemyHqs.add(scoutedMapPos);
                    theActualSymmetry.add(syms.get(i));
                    // should be the symmetry of the map
                    return theActualSymmetry;
                } else {
                    // if there's no headquarters at this location, that's fine, we can narrow the possible spots down
                    possibleCoordsOfEnemyHqs.remove(i);
                    syms.remove(i);
                }
            }
        }
        return syms;
    }

    /**
     * write this hq location to the array
     * @param rc RobotController
     * @param me this robots location
     * @throws GameActionException from read/write to array
     */
    static void storeOurHqToArray(RobotController rc, MapLocation me) throws GameActionException {
        int indicator = rc.readSharedArray(hqStoringIndicatorIndex);

        indicator++;

        // System.out.println("writing " + locationToInt(rc, me) + " to index " + indicator);
        rc.writeSharedArray(indicator, locationToInt(rc, me));

        // if the first hq to write your coords to the array, also write how many hqs we have in the game
        if (indicator == 1) {
            rc.writeSharedArray(numOfHqsIndex, rc.getRobotCount());
        }
        rc.writeSharedArray(hqStoringIndicatorIndex, indicator);
    }

    /**
     *
     * @param rc
     * @throws GameActionException read and write to array
     */
    static void rewriteTheEnemyHqPositions(RobotController rc) throws GameActionException {
        if ((rc.getID() == 2) || rc.getID() == 3) clearEnemyHqsFromTheSharedArray(rc);

        // if a hq managed to guess the sym, and we haven't updated our possible hq spots with that information, do it!!!!!
        if ((rc.readSharedArray(symIndex) != 0) && (possibleCoordsOfEnemyHqs.size() > 1)) {
            possibleCoordsOfEnemyHqs.clear();
            possibleCoordsOfEnemyHqs.add(possibleCoordsOfEnemyHqsAlwaysThree.get(rc.readSharedArray(symIndex) - 1));
        }

        // write every enemy hq that we have guessed a location for into the array. If we guessed the symmetry
        // earlier, then each hq is only writing one location (the actual location). Otherwise, we are writing 2 or
        // more guesses for the enemy hq location.
        //System.out.println(numOfEnemyHqsInArray);
        for (MapLocation anEnemyHqCoords : possibleCoordsOfEnemyHqs) {
            numOfEnemyHqsInArray = rc.readSharedArray(numOfEnemyHqsInArrayIndex);
            rc.writeSharedArray((enemyHqCoordsStartingIndex + numOfEnemyHqsInArray), locationToInt(rc, anEnemyHqCoords));
            rc.writeSharedArray(numOfEnemyHqsInArrayIndex, numOfEnemyHqsInArray + 1);
        }
        if (rc.readSharedArray(symIndex) != 0) {
            enemyHqCoordsLocated = true;
            // we found the symmetry
            MapLocation theFabledLocation = possibleCoordsOfEnemyHqs.get(0);
            rc.setIndicatorDot(theFabledLocation, 0, 255, 0);
        } else {
            // didn't find the symmetry
            for (MapLocation possibleCoordsOfEnemyHq : possibleCoordsOfEnemyHqs) {
                rc.setIndicatorDot(possibleCoordsOfEnemyHq, 0, 0, 255);
            }
        }
    }

    /**
     * Using an array of map locations, spawn a person as close to that map location as possible
     * @param rc robot controller
     * @param spawnLocations array of locations to be tested if we can spawn there. Sorted by index for what we want to
     * try first (index 0 is first choice, last index is last choice)
     * @param robotType which type of robot we want to spawn
     * @throws GameActionException if buildRobot fails drastically (it won't dw)
     */
    static void spawnADude(RobotController rc, MapLocation[] spawnLocations, RobotType robotType) throws GameActionException {
        //rc.setIndicatorString("spawn a dude");
        // will always spawn a dude when fully completed
        for (int i = 0; i < spawnLocations.length; i++) {
            MapLocation spawnLoc = spawnLocations[i];
            //rc.setIndicatorString(spawnLoc.toString());
            //rc.setIndicatorString("Tryna build a boy at " + spawnLoc);
            if (rc.canBuildRobot(robotType, spawnLoc)) {
                rc.buildRobot(robotType, spawnLoc);
                //rc.setIndicatorString("Built " + robotType + " at " + spawnLoc);
                if (robotType == RobotType.LAUNCHER) {
                    attackersThisHqHasBuilt++;
                } else {
                    carriersThisHqHasBuilt++;
                }
                break;
            }
//            if (i+1 == spawnLocations.length) {
//                //no attacker was built with those supplied MapLocations
//                rc.setIndicatorString("Tried building " + robotType + " but couldn't");
//            }
        }
        if (rc.isActionReady()) {
            //rc.setIndicatorString("Didn't build anything my fault g");
        }
    }

/*
------------------------------------------------------------------------------------------------------------------------
                                        CARRIER FUNCTIONS
------------------------------------------------------------------------------------------------------------------------
*/





/*
------------------------------------------------------------------------------------------------------------------------
                                        ATTACKER FUNCTIONS
------------------------------------------------------------------------------------------------------------------------
*/

    /**
     * attacker tries attacking anything around them
     * @param rc RobotController
     * @throws GameActionException from senseNearbyRobots
     */
    static void attackerAttackAround(RobotController rc) throws GameActionException {
        int radius = rc.getType().actionRadiusSquared;
        Team opponent = rc.getTeam().opponent();
        RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);
        // rc.setIndicatorString(Arrays.toString(enemies));

        RobotInfo target = null;

        if (enemies.length > 0) {

            // the target is the random first in the array. Over each iteration, if another enemy in the array has
            // qualities that make it higher priority to attack than current target, then switch the target

            for (RobotInfo enemy : enemies) {
                if (enemy.getType() != RobotType.HEADQUARTERS) {
                    target = enemy;
                    break;
                }
            }

            RobotType targetEnemyRobotType = null;
            int targetEnemyHealth = 0;
            int targetEnemyDistance = 0;

            for (RobotInfo enemy: enemies) {
                // we cannot attack a headquarters!!! So make sure we don't even consider them!!!
                if (enemy.getType() != RobotType.HEADQUARTERS) {
                    // target qualities
                    targetEnemyRobotType = target.getType();
                    targetEnemyHealth = target.getHealth();
                    targetEnemyDistance = target.getLocation().distanceSquaredTo(rc.getLocation());

                    // current enemy to check qualities
                    RobotType checkerEnemyRobotType = enemy.getType();
                    int checkerEnemyHealth = enemy.getHealth();
                    int checkerEnemyDistance = enemy.getLocation().distanceSquaredTo(rc.getLocation());

                    // System.out.println("loc: " + enemy.getLocation() + " health: " + checkerEnemyHealth + " distance: " + checkerEnemyDistance);

                    // 1st priority: LAUNCHERS
                    // if this enemy is a launcher, and the target is not a launcher, switch the target to this enemy
                    if (checkerEnemyRobotType == RobotType.LAUNCHER && targetEnemyRobotType != RobotType.LAUNCHER) {
                        target = enemy;
                    }
                    // 2nd priority: THE LEAST HEALTH
                    // if this enemy has less health than the target, switch target to this enemy
                    else if (checkerEnemyHealth < targetEnemyHealth){
                        target = enemy;
                    }
                    // 3rd priority: THE CLOSEST
                    // if this enemy is closer than the target, switch target to this enemy
                    else if (checkerEnemyDistance < targetEnemyDistance) {
                        target = enemy;
                    }
                }
            }
            if (target != null) {
                rc.setIndicatorString("TARGET: " + targetEnemyRobotType + " AT " + target.getLocation() + " WITH HEALTH: " + targetEnemyHealth + " DISTANCE: " + targetEnemyDistance);
            }
        }

        if (target != null) {
            if (rc.canAttack(target.getLocation()))
                rc.attack(target.getLocation());
        }
    }

    /**
     * moves attacker towards a location
     * @param rc RobotController
     * @param me current location
     * @throws GameActionException from move()
     */
    static void attackerMoveToLocation(RobotController rc, MapLocation me) throws GameActionException {
        if (me.isAdjacentTo(attackerIsAttackingThisLocation)) {
            // vibe
        } else {
            // we aren't by the thing we wanna attack, move to it

            // if we have enemy hq, move to it
            // every attacker from this hq will just beeline its symmetrical enemy hq partner
            Direction dir = me.directionTo(attackerIsAttackingThisLocation);
            Direction[] moveDirs = new Direction[5];
            moveDirs[0] = dir;
            moveDirs[1] = dir.rotateRight();
            moveDirs[2] = dir.rotateLeft();
            moveDirs[3] = dir.rotateRight().rotateRight();
            moveDirs[4] = dir.rotateLeft().rotateLeft();

            for (int i = 0; i < moveDirs.length; i++) {
                Direction moveDir = moveDirs[i];
                if (rc.canMove(moveDir)) {
                    rc.move(moveDir);
                    // rc.setIndicatorString("Moving towards " + attackerIsAttackingThisLocation + " by going " + moveDir);
                    break;
                }
                if (i == (moveDirs.length - 1)) {
                    // couldn't move towards the location we wanna attack
                    Direction randomDir = directions[rng.nextInt(directions.length)];
                    if (rc.canMove(randomDir)) {
                        rc.move(randomDir);
                        // rc.setIndicatorString("Moving " + dir + " randomly");
                    }
                }
            }
            if (rc.isMovementReady()) {
                // rc.setIndicatorString("Didn't move, will change this later hopefully");
            }
        }
    }

    /**
     * Sets scoutResultFromPossibleHqLocation true/false based on if we got to a possible hq location, if there actually
     * was a hq there or not. Sets scoutReturningHome to true if we found a possible
     * @param rc
     * @throws GameActionException from reading array and sensing location
     */
    static void checkIfWeSeeAHqOnOurTravels(RobotController rc) throws GameActionException {
        MapLocation me = rc.getLocation();
        if (rc.readSharedArray(symIndex) == 0) {
            // if symIndex is 0, then enemy hq positions are NOT located, so we need to do some scouting
            if (me.isWithinDistanceSquared(attackerIsAttackingThisLocation, RobotType.LAUNCHER.visionRadiusSquared)) {
                // if we are at a hq location, and it's only a possible hq location, we need to report our findings
                // back to a hq, so we can guess the symmetry and know for sure where the other hqs are
                if (rc.canSenseLocation(attackerIsAttackingThisLocation)) {
                    rc.senseRobotAtLocation(attackerIsAttackingThisLocation);
                    RobotInfo possibleHq = rc.senseRobotAtLocation(attackerIsAttackingThisLocation);
                    if (possibleHq == null) {
                        // no hq here
                        scoutResultFromPossibleHqLocation = false;
                    } else {
                        // hq here
                        scoutResultFromPossibleHqLocation = true;
                    }
                    // now we want to go back to the closest hq
                    scoutReturningHome = true;
                    rc.setIndicatorString("Returning home");
                }
            }
        }
    }
//------------------------------------------------------------------------------------------
static void spawnRobot(RobotController rc, RobotType robot, MapLocation location) throws GameActionException {
    if(rc.canBuildRobot(robot, location)) rc.buildRobot(robot, location);
    else {
        Direction dir = directions[rng.nextInt(directions.length)];
        MapLocation spawnLocation = rc.getLocation().add(dir);
        if (rc.canBuildRobot(robot, spawnLocation)) rc.buildRobot(robot, spawnLocation);
    }
}
    /**
     * Make the robot move
     * @param rc Robot Controller
     * @param dir direction that the robot should move in
     */
    static void move(RobotController rc, Direction dir) throws GameActionException {
        if (rc.canMove(dir)) {
            rc.move(dir);
            if(rc.isMovementReady()) {
                if(rc.canMove(dir)) rc.move(dir);
            }
        }
        else moveRandomDir(rc);
    }
    static void moveRandomDir(RobotController rc) throws GameActionException {
        Direction dir = directions[rng.nextInt(directions.length)];
        if(rc.canMove(dir)) {
            rc.move(dir);
            if (rc.isMovementReady()) {
                if (rc.canMove(dir)) rc.move(dir);

            }
        }
    }
    static void moveTo(RobotController rc, MapLocation targetDir) throws GameActionException {
        Direction dir = rc.getLocation().directionTo(targetDir);
        if (rc.canMove(dir)) {
            rc.move(dir);
            if(rc.isMovementReady()) {
                if(rc.canMove(dir)) rc.move(dir);
            }
            currentDirection = null;
        } else {
            if (currentDirection == null) currentDirection = dir;
            for (int i = 0; i < 8; i++) {
                if (rc.canMove(currentDirection)) {
                    rc.move(currentDirection);
                    if(rc.isMovementReady()) {
                        if(rc.canMove(currentDirection)) rc.move(currentDirection);
                    }
                    currentDirection = currentDirection.rotateRight();
                    break;
                } else currentDirection = currentDirection.rotateLeft();
            }
        }
    }

    static void scanIslands(RobotController rc) throws GameActionException {
        int[] ids = rc.senseNearbyIslands();
        for(int id : ids) {
            if(rc.senseTeamOccupyingIsland(id) == Team.NEUTRAL) {
                MapLocation[] locs = rc.senseNearbyIslandLocations(id);
                if(locs.length > 0) {
                    islandLoc = locs[0];
                    break;
                }
            }
        }
    }
    static void getClosestManaWell(RobotController rc) throws GameActionException {
        MapLocation me = rc.getLocation();
        WellInfo[] wells = rc.senseNearbyWells(-1, ResourceType.MANA);
        if (wells.length >= 1) {
            closestWell = wells[0].getMapLocation();
            for (WellInfo well : wells) {
                if (me.distanceSquaredTo(well.getMapLocation()) < me.distanceSquaredTo(closestWell)) {
                    closestWell = well.getMapLocation();
                }
            }
        }
    }
    static void getClosestAdWell(RobotController rc) throws GameActionException {
        MapLocation me = rc.getLocation();
        WellInfo[] wells = rc.senseNearbyWells(-1, ResourceType.ADAMANTIUM);
        if (wells.length >= 1) {
            closestWell = wells[0].getMapLocation();
            for (WellInfo well : wells) {
                if (me.distanceSquaredTo(well.getMapLocation()) < me.distanceSquaredTo(closestWell)) {
                    closestWell = well.getMapLocation();
                }
            }
        }
    }
    static void getClosestWell(RobotController rc) throws GameActionException {
        MapLocation me = rc.getLocation();
        WellInfo[] wells = rc.senseNearbyWells();
        if (wells.length >= 1) {
            closestWell = wells[0].getMapLocation();
            for (WellInfo well : wells) {
                if (me.distanceSquaredTo(well.getMapLocation()) < me.distanceSquaredTo(closestWell)) {
                    closestWell = well.getMapLocation();
                }
            }
        }
    }
    static void lookForWellType(RobotController rc) {
        WellInfo[] wells = rc.senseNearbyWells();
        if (wells.length >= 1) {
            for (WellInfo well : wells) {
                if (well.getResourceType() == ResourceType.ADAMANTIUM) adWells.add(well.getMapLocation());
                else mnWells.add(well.getMapLocation());
            }
        }
    }
    static int totalResources(RobotController rc) {
        return rc.getResourceAmount(ResourceType.ADAMANTIUM) + rc.getResourceAmount(ResourceType.MANA)
                + rc.getResourceAmount(ResourceType.ELIXIR);
    }
    static void addHqLocations(RobotController rc) throws GameActionException {
        MapLocation me = rc.getLocation();
        for (int i = 0; i < GameConstants.MAX_STARTING_HEADQUARTERS; i++) {
            if (rc.readSharedArray(i) == 0) {
                if(rc.canWriteSharedArray(i, locationToInt(rc, me))) {
                    rc.writeSharedArray(i, locationToInt(rc, me));
                    break;
                }
            }
        }
    }
    static void updateHqLocations(RobotController rc) throws GameActionException {
        if (turnCount == 2) {
            for (int i = 0; i < GameConstants.MAX_STARTING_HEADQUARTERS; i++) {
                headquarterLocations[i] = intToLocation(rc, rc.readSharedArray(i));
                if (rc.readSharedArray(i) == 0) break;
            }
        }
    }
    static void findHq(RobotController rc) {
        RobotInfo[] robots = rc.senseNearbyRobots();
        for (RobotInfo robot : robots) {
            if (robot.getType() == RobotType.HEADQUARTERS && robot.getTeam() == rc.getTeam()) {
                headquarter = robot.getLocation();
                break;
            }
        }
    }

}

// ----------------------------------------------------------------------------------------------------------


