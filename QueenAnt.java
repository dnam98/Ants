//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: Queen Ant
// Course: CS 300 Summer 2021
//
// Author: Dongwon Nam
// Email: dnam9@wisc.edu
// Lecturer: Chris Magnano
//
///////////////////////// ALWAYS CREDIT OUTSIDE HELP //////////////////////////
//
// Persons: NONE
// Online Sources: NONE
//
///////////////////////////////////////////////////////////////////////////////

public class QueenAnt extends Ant {
  public static final int FOOD_PER_ANT = 3; // Number of food it takes to make an ant
  public static final int MAX_NEST_DISTANCE = 20; // max distance the queen should ever be from its
                                                  // nest
  public static int QUEEN_SPEED = 1; // speed of the queen ant

  private boolean foundEnemies; // true if any ants created any pheromone trails to enemy ants
  private boolean foundFood; // true if any ants created any pheromone trails to food
  private int numFood; // current number of food the queen has (to make new ants)
  public int numChildren; // total number of children the queen has created

  // Constructor
  public QueenAnt(String id, Nest nest) {
    super(id, nest);
    this.health = 1000;
    this.numFood = 0;
    this.foundEnemies = false;
    this.foundFood = false;
    this.numChildren = 0;
  }

  /**
   * Getter method for foundEnemies
   * 
   * @return true if found enemies
   */
  public boolean getFoundEnemies() {
    return foundEnemies;
  }

  /**
   * Setter method for foundEnemies
   * 
   * @param foundEnemies - true if found enemies
   */
  public void setFoundEnemies(boolean foundEnemies) {
    this.foundEnemies = foundEnemies;
  }

  /**
   * Getter method for foundFood
   * 
   * @return true if found food
   */
  public boolean getFoundFood() {
    return foundFood;
  }

  /**
   * Setter method for foundFood
   * 
   * @param foundFood - true if found food
   */
  public void setFoundFood(boolean foundFood) {
    this.foundFood = foundFood;
  }

  /**
   * Getter method for numFood
   * 
   * @return number of food
   */
  public int getNumFood() {
    return numFood;
  }

  /**
   * Setter method for numFood
   * 
   * @param numFood - number of food
   */
  public void setNumFood(int numFood) {
    this.numFood = numFood;
  }

  /**
   * Getter method for numChildren
   * 
   * @return number of children queen ant made
   */
  public int getNumChildren() {
    return numChildren;
  }

  /**
   * Setter method for numChildren
   * 
   * @param numChildren - number of children queen ant made
   */
  public void setNumChildren(int numChildren) {
    this.numChildren = numChildren;
  }

  /**
   * Add a single new ant to the nest. It decides which type of ant to create. If Warrior,
   * Harvester, or Scout ants in the nest are less than MIN_ANT_COUNT, create that type. Priority
   * is Warrior > Harvester, Scout.
   * 
   */
  public void makeAnt() {

    // Check if each type of ants are less than the minimum ant count
    if (this.nest.getNumWarriors() < Nest.MIN_ANT_COUNT) {
      Ant newAnt = new WarriorAnt(this.getId() + Integer.toString(this.numChildren), this.nest);
      this.nest.addAnt(newAnt);
      this.numChildren++;
      this.numFood -= FOOD_PER_ANT;
      return;
    }

    else if (this.nest.getNumHarvesters() < Nest.MIN_ANT_COUNT) {
      Ant newAnt = new HarvesterAnt(this.getId() + Integer.toString(this.numChildren), this.nest);
      this.nest.addAnt(newAnt);
      this.numChildren++;
      this.numFood -= FOOD_PER_ANT;
      return;
    }

    else if (this.nest.getNumScouts() < Nest.MIN_ANT_COUNT) {
      Ant newAnt = new ScoutAnt(this.getId() + Integer.toString(this.numChildren), this.nest);
      this.nest.addAnt(newAnt);
      this.numChildren++;
      this.numFood -= FOOD_PER_ANT;
      return;
    }

    // Enough of each type of ant, and foundEnemies is true
    if (this.foundEnemies == true) {
      Ant newAnt = new WarriorAnt(this.getId() + Integer.toString(this.numChildren), this.nest);
      this.nest.addAnt(newAnt);
      this.numChildren++;
      this.numFood -= FOOD_PER_ANT;
      this.foundEnemies = false;
      return;
    }

    // Enough of each type of ant, foundFood is true, foundEnemies is false
    if (this.foundEnemies == false && foundFood == true) {
      Ant newAnt = new HarvesterAnt(this.getId() + Integer.toString(this.numChildren), this.nest);
      this.nest.addAnt(newAnt);
      this.numChildren++;
      this.numFood -= FOOD_PER_ANT;
      this.foundFood = false;
      return;
    }

    // None of the above are true
    Ant newAnt = new ScoutAnt(this.getId() + Integer.toString(this.numChildren), this.nest);
    this.nest.addAnt(newAnt);
    this.numChildren++;
    this.numFood -= FOOD_PER_ANT;
  }

  /**
   * Override of Ant update method to fit queen ant behavior.
   */
  @Override
  public void update(AntWorld aW) {
    super.update(aW);
    super.millAbout(MAX_NEST_DISTANCE);
  }

  /**
   * Method that ants should call when they reach the nest while creating a pheromone trail. Allows
   * the queen to get information about what kind of ants are needed to be created. If type is
   * "food", set foundFood to true If type is "war", set foundEnemies to true
   * 
   * @param type - type of the ant
   */
  public void reportPheromone(String type) {
    if (type.equalsIgnoreCase("food")) {
      this.foundFood = true;
    }

    if (type.equalsIgnoreCase("war")) {
      this.foundEnemies = true;
    }
  }

  /**
   * Called from the Nest class whenever addFood is called in a Nest object.
   */
  public void addFood() {
    this.numFood++;
    if (this.numFood > FOOD_PER_ANT && (nest.getNumHarvesters() + nest.getNumScouts()
        + nest.getNumWarriors() + 1) < Nest.MAX_NEST_SIZE) {
      this.makeAnt();
    }
  }

  /**
   * Draws the queen ant
   */
  @Override
  public void drawSelf(AntWorld aW) {
    aW.fill(255, this.health, this.health);
    aW.noStroke();
    aW.ellipse(this.x - 3, this.y, 5, 5);
    aW.ellipse(this.x, this.y, 7, 7);
    aW.ellipse(this.x + 3, this.y, 9, 9);
  }

}
