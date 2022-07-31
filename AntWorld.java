import processing.core.PApplet;
import java.util.ArrayList;
import java.util.Iterator;

// Do not submit this file on Gradescope
/**
 * This class creates a simulation of any foraging behavior based on pheromone trails.
 * 
 * @author Chris
 *
 */
public class AntWorld extends PApplet {

  private ArrayList<Food> foodList;
  private ArrayList<Nest> nestList;

  // The number of pixels wide and high the window is. Feel free to change this to fit your
  // screen better.
  public static final int WORLD_SIZE = 500;


  /**
   * Constructor for AntWorld. Initializes arrayLists for food and nests.
   */
  public AntWorld() {
    this.foodList = new ArrayList<Food>();
    this.nestList = new ArrayList<Nest>();
  }


  /**
   * Controls settings for the PApplet class. We set the window size to be a square with each side
   * being WORLD_SIZE pixels long.
   */
  public void settings() {
    size(WORLD_SIZE, WORLD_SIZE);
  }


  /**
   * Performs and setup for the PApplet class in processing. Right now we only set the framerate.
   * Feel free to change the framerate if you wnat to see the simluation slower or if the simulation
   * begins to get to be too much for your computer.
   */
  public void setup() {
    frameRate(60);
  }

  /**
   * The draw() method is called every frame. It is the main place where all updates to the
   * simulation occur. It updates the state of all ants, food, pheromones, and nests, then draws
   * everything.
   */
  public void draw() {
    // Refresh the background. If we don't do this frames get drawn on top of each other.
    background(0);

    for (int j = 0; j < this.nestList.size(); j++) {
      Nest nest = this.nestList.get(j);
      ArrayList<Ant> antList = nest.getAntList();
      nest.getQueen().update(this);
      for (int i = 0; i < antList.size(); i++) {
        Ant ant = antList.get(i);
        if (ant.getId() == nest.getQueen().getId()) {
          continue;
        }
        ant.update(this);
      }

      // Update Pheromones using an iterator to remove any pheromones which are too weak to be
      // smelled. You are not expected to know how iterators work at this point.
      Iterator<AntPheromone> it = nest.getPheromoneList().iterator();
      while (it.hasNext()) {
        AntPheromone phero = it.next();
        phero.decrementStrength();
        if (phero.getStrength() < 1) {
          it.remove();
        }
      }
      // Draw pheromones, ants, the nest, and food
      for (int i = 0; i < nest.getPheromoneList().size(); i++) {
        nest.getPheromoneList().get(i).drawSelf(this);
      }
      nest.drawSelf(this);
      for (int i = 0; i < nest.getAntList().size(); i++) {
        nest.getAntList().get(i).drawSelf(this);
      }
    }
    for (int i = 0; i < this.foodList.size(); i++) {
      foodList.get(i).drawSelf(this);
    }

  }

  /**
   * Adds a nest to the AntWorld.
   * 
   * @param nest The nest to be added.
   */
  public void addNest(Nest nest) {
    this.nestList.add(nest);
  }

  /**
   * Removes a nest from AntWorld.
   * 
   * @param nest The nest to be removed.
   */
  public void removeNest(Nest nest) {
    this.nestList.remove(nest);
  }

  /**
   * Adds a food to the AntWorld.
   * 
   * @param food The food to be added.
   */
  public void addFood(Food food) {
    this.foodList.add(food);
  }

  /**
   * Removes a food from the AntWorld
   * 
   * @param food The food to be removed.
   */
  public void removeFood(Food food) {
    // Getting an object using indexOf in this way can be a little risky, but in this case it's
    // fine.
    int index = this.foodList.indexOf(food);
    this.foodList.remove(index);
  }

  /**
   * Returns the closest food object to the ant's position if it is within the Ant's range.
   * 
   * @param ant The ant which is searching for food.
   * @return The found food object.
   * @throws RescourceNotFoundException Thrown if there is no food in range of the ant.
   */
  public Food getNearestFoodInRange(Ant ant) throws ResourceNotFoundException {
    float minFoodDist = Float.MAX_VALUE;
    int minFoodInd = -1;
    for (int i = 0; i < this.foodList.size(); i++) {
      Food currentFood = this.foodList.get(i);
      float foodDist = dist(ant.getX(), ant.getY(), currentFood.getX(), currentFood.getY());
      if (foodDist > Ant.RANGE) {
        continue;
      } else if (foodDist < minFoodDist) {
        minFoodDist = foodDist;
        minFoodInd = i;
      }
    }
    if (minFoodInd == -1) {
      throw new ResourceNotFoundException();
    }
    return this.foodList.get(minFoodInd);
  }

  /**
   * Returns the closest enemy ant (any ant from another nest) to the ant's position if it is within
   * the Ant's range.
   * 
   * @param ant The ant which is searching for food.
   * @return The found Ant object.
   * @throws ResourceNotFoundException Thrown if there is no enemy in range of the ant.
   */
  public Ant getNearestEnemyAntInRange(Ant ant) throws ResourceNotFoundException {
    float minEnemyDist = Float.MAX_VALUE;
    Ant closestEnemy = null;
    for (int j = 0; j < this.nestList.size(); j++) {
      Nest nest = this.nestList.get(j);
      // we don't want wants from our own nest
      if (nest == ant.getNest()) {
        continue;
      }
      ArrayList<Ant> antList = nest.getAntList();
      for (int i = 0; i < antList.size(); i++) {
        Ant curAnt = antList.get(i);
        float enemyDist = dist(ant.getX(), ant.getY(), curAnt.getX(), curAnt.getY());
        if (enemyDist > Ant.RANGE) {
          continue;
        } else if ((closestEnemy == null) || (enemyDist < minEnemyDist)) {
          minEnemyDist = enemyDist;
          closestEnemy = curAnt;
        }
      }
    }
    if (closestEnemy == null) {
      throw new ResourceNotFoundException();
    }
    return closestEnemy;
  }

  /**
   * Returns the pheromone object farthest from the ant's nest if it is within the Ant's range.
   * 
   * @param ant The ant which is searching for food.
   * @return The found AntPheromone object.
   * @throws ResourceNotFoundException Thrown if there is no pheromone in range of the ant.
   */
  public AntPheromone getFarthestPheromoneInRange(Ant ant, String type)
      throws ResourceNotFoundException {
    // Using Float.MIN_VALUE is a useful starting place to find the farthest pheromone,
    // since there is no way to be smaller than Float.MIN_VALUE
    float maxNestDist = Float.MIN_VALUE;
    int minPheroInd = -1;
    Nest nest = ant.getNest();
    ArrayList<AntPheromone> pheromoneList = nest.getPheromoneList();
    ArrayList<String> excludeIDs = ant.getExcludeIDs();
    for (int i = 0; i < pheromoneList.size(); i++) {
      AntPheromone currentPhero = pheromoneList.get(i);
      if (!currentPhero.getType().equals(type)) {
        continue;
      }
      if (excludeIDs.contains(currentPhero.getCreatorID())) {
        continue;
      }
      // The pheromone distance from the ant
      float pheroDist = dist(ant.getX(), ant.getY(), currentPhero.getX(), currentPhero.getY());
      // The pheromone distance from the ant's nest
      float nestDist = dist(nest.getX(), nest.getY(), currentPhero.getX(), currentPhero.getY());
      if ((pheroDist > Ant.RANGE)) {
        continue;
      } else if (maxNestDist < nestDist) {
        maxNestDist = nestDist;
        minPheroInd = i;
      }
    }
    if (minPheroInd == -1) {
      throw new ResourceNotFoundException();
    }
    return pheromoneList.get(minPheroInd);
  }

  /**
   * A simulation where there is a block of food in each corner and a nest in the middle.
   * 
   * @param antWorld The AntWorld object to create the simulation in.
   */
  public static void fourCorners(AntWorld antWorld) {
    for (int i = 0; i < 15; i++) {
      for (int j = 0; j < 15; j++) {
        Food newFood1 = new Food(i * 4, j * 4);
        Food newFood2 = new Food(WORLD_SIZE - (15 * 4) + i * 4, j * 4);
        Food newFood3 = new Food(i * 4, WORLD_SIZE - (15 * 4) + j * 4);
        Food newFood4 = new Food(WORLD_SIZE - (15 * 4) + i * 4, WORLD_SIZE - (15 * 4) + j * 4);
        antWorld.addFood(newFood1);
        antWorld.addFood(newFood2);
        antWorld.addFood(newFood3);
        antWorld.addFood(newFood4);
      }
    }
    Nest nest = new Nest(WORLD_SIZE / 2, WORLD_SIZE / 2, 200, 200, 255);
    antWorld.addNest(nest);
    nest.addAnt(new QueenAnt("queen", nest));
    for (int i = 0; i < 3; i++) {
      nest.addAnt(new WarriorAnt("w" + Integer.toString(i), nest));
      nest.addAnt(new HarvesterAnt("h" + Integer.toString(i), nest));
      nest.addAnt(new HarvesterAnt("h" + Integer.toString(i), nest));
      nest.addAnt(new ScoutAnt("s" + Integer.toString(i), nest));
    }
  }

  /**
   * A simulation where there is a single large line of food and a nest in the middle.
   * 
   * @param antWorld The AntWorld object to create the simulation in.
   */
  public static void bigLine(AntWorld antWorld) {
    for (int i = 0; i < 50; i++) {
      for (int j = 0; j < 15; j++) {
        Food newFood = new Food(WORLD_SIZE / 2 - 100 + i * 4, WORLD_SIZE / 2 - 150 + j * 2);
        antWorld.addFood(newFood);
      }
    }
    Nest nest = new Nest(WORLD_SIZE / 2, WORLD_SIZE / 2, 200, 200, 255);
    antWorld.addNest(nest);
    nest.addAnt(new QueenAnt("queen", nest));
    for (int i = 0; i < 3; i++) {
      nest.addAnt(new WarriorAnt("w" + Integer.toString(i), nest));
      nest.addAnt(new HarvesterAnt("h" + Integer.toString(i), nest));
      nest.addAnt(new ScoutAnt("s" + Integer.toString(i), nest));
    }
  }

  /**
   * A simulation where there are two nests and a line of food between them.
   * 
   * @param antWorld The AntWorld object to create the simulation in.
   */
  public static void twoNests(AntWorld antWorld) {
    for (int i = 0; i < 50; i++) {
      for (int j = 0; j < 15; j++) {
        Food newFood = new Food(WORLD_SIZE / 2 - 100 + i * 4, WORLD_SIZE / 2 + j * 2);
        antWorld.addFood(newFood);
      }
    }
    Nest nest1 = new Nest(WORLD_SIZE / 2, WORLD_SIZE / 8, 200, 200, 255);
    antWorld.addNest(nest1);
    nest1.addAnt(new QueenAnt("queen", nest1));
    for (int i = 0; i < 3; i++) {
      nest1.addAnt(new WarriorAnt("w" + Integer.toString(i), nest1));
      nest1.addAnt(new HarvesterAnt("h" + Integer.toString(i), nest1));
      nest1.addAnt(new ScoutAnt("s" + Integer.toString(i), nest1));
    }

    Nest nest2 = new Nest(WORLD_SIZE / 2, WORLD_SIZE - WORLD_SIZE / 8, 255, 100, 190);
    antWorld.addNest(nest2);
    nest2.addAnt(new QueenAnt("queen", nest2));
    for (int i = 0; i < 3; i++) {
      nest2.addAnt(new WarriorAnt("w" + Integer.toString(i), nest2));
      nest2.addAnt(new HarvesterAnt("h" + Integer.toString(i), nest2));
      nest2.addAnt(new ScoutAnt("s" + Integer.toString(i), nest2));
    }
  }


  /**
   * A simulation where there are two nests and a line of food between them.
   * 
   * @param antWorld The AntWorld object to create the simulation in.
   */
  public static void competingCorners(AntWorld antWorld) {
    for (int i = 0; i < 15; i++) {
      for (int j = 0; j < 15; j++) {
        Food newFood1 = new Food(i * 4, j * 4);
        Food newFood2 = new Food(WORLD_SIZE - (15 * 4) + i * 4, j * 4);
        Food newFood3 = new Food(i * 4, WORLD_SIZE - (15 * 4) + j * 4);
        Food newFood4 = new Food(WORLD_SIZE - (15 * 4) + i * 4, WORLD_SIZE - (15 * 4) + j * 4);
        antWorld.addFood(newFood1);
        antWorld.addFood(newFood2);
        antWorld.addFood(newFood3);
        antWorld.addFood(newFood4);
      }
    }
    Nest nest1 = new Nest(WORLD_SIZE / 2, WORLD_SIZE / 8, 200, 200, 255);
    antWorld.addNest(nest1);
    nest1.addAnt(new QueenAnt("queen", nest1));
    for (int i = 0; i < 3; i++) {
      nest1.addAnt(new WarriorAnt("w" + Integer.toString(i), nest1));
      nest1.addAnt(new HarvesterAnt("h" + Integer.toString(i), nest1));
      nest1.addAnt(new ScoutAnt("s" + Integer.toString(i), nest1));
    }

    Nest nest2 = new Nest(WORLD_SIZE / 2, WORLD_SIZE - WORLD_SIZE / 8, 255, 100, 190);
    antWorld.addNest(nest2);
    nest2.addAnt(new QueenAnt("queen", nest2));
    for (int i = 0; i < 3; i++) {
      nest2.addAnt(new WarriorAnt("w" + Integer.toString(i), nest2));
      nest2.addAnt(new HarvesterAnt("h" + Integer.toString(i), nest2));
      nest2.addAnt(new ScoutAnt("s" + Integer.toString(i), nest2));
    }
  }

  /**
   * A simulation where the nest is inside a large block of food.
   * 
   * @param antWorld The AntWorld object to create the simulation in.
   */
  public static void inTheMiddle(AntWorld antWorld) {
    for (int i = 0; i < 50; i++) {
      for (int j = 0; j < 50; j++) {
        Food newFood = new Food(WORLD_SIZE / 2 - 100 + i * 4, WORLD_SIZE / 2 - 100 + j * 4);
        antWorld.addFood(newFood);
      }
    }
    Nest nest = new Nest(WORLD_SIZE / 2, WORLD_SIZE / 2, 200, 200, 255);
    antWorld.addNest(nest);
    nest.addAnt(new QueenAnt("queen", nest));
    for (int i = 0; i < 3; i++) {
      nest.addAnt(new WarriorAnt("w" + Integer.toString(i), nest));
      nest.addAnt(new HarvesterAnt("h" + Integer.toString(i), nest));
      nest.addAnt(new ScoutAnt("s" + Integer.toString(i), nest));
    }
  }

  public static void main(String[] args) {
    String[] processingArgs = {"AntWorld"};
    AntWorld antWorld = new AntWorld();
    PApplet.runSketch(processingArgs, antWorld);
    // Comment and uncomment these lines to try out different scenarios.
    //AntWorld.fourCorners(antWorld);
    //AntWorld.bigLine(antWorld);
    AntWorld.competingCorners(antWorld);
    //AntWorld.twoNests(antWorld);
    //AntWorld.inTheMiddle(antWorld);
  }
}
