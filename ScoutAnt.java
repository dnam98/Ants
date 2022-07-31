import processing.core.PApplet;

/**
 * The Scout ant which looks for enemies and food and reports them back to the nest.
 * @author Chris
 *
 */
public class ScoutAnt extends Ant {
  public static int MAX_PATH_LENGTH = 500; //how long before we go home and try again
  public static int SCOUT_SPEED = 6; //the speed of the scout ant
   private boolean foundFood; 
   private boolean foundEnemy;
   private boolean returningHome; //whether or not we are returning home
   private int numPathsFound; //the total number of things we've found
   private int pathLength; //the current length of our path

  public ScoutAnt(String id, Nest nest) {
    super(id, nest);
    this.health = 50;
    this.foundFood = false;
    this.foundEnemy = false;
    this.returningHome = false;
    this.numPathsFound = 0;
    this.pathLength = 0;
  }
  
  /**
   * Override of Ant update method to fit scout behavior.
   */
  @Override 
  public void update(AntWorld aW) {
    super.update(aW);
    //Return home
    if (this.returningHome) {
      if (Math
          .abs(PApplet.dist(this.nest.getX(), this.nest.getY(), this.x, this.y) - 0) < 0.01) {
        if (this.foundEnemy) {
          this.foundEnemy = false;
          this.nest.getQueen().reportPheromone("war");
        }
        if (this.foundFood) {
          this.foundFood = false;
          this.nest.getQueen().reportPheromone("food");
        }
        this.returningHome = false;
        this.pathLength = 0;
        return;
      }
      this.moveTowardsCoordinates(this.nest.getX(), this.nest.getY());
      //If we have food or found an enemy we want to drop a pheromone
      if (this.foundEnemy) {
        this.nest.addPheromone(new AntPheromone("war", this.getId()+Integer.toString(this.numPathsFound),this.x, this.y));
      }
      if (this.foundFood) {
        this.nest.addPheromone(new AntPheromone("food", this.getId()+Integer.toString(this.numPathsFound),this.x, this.y));
      }
      return;
    }
    
    //Look for nearby enemies
    Ant nearestEnemy;
    try {
      nearestEnemy = aW.getNearestEnemyAntInRange(this);
      if (Math
          .abs(PApplet.dist(nearestEnemy.getX(), nearestEnemy.getY(), this.x, this.y) - 0) < 0.01) {
        this.numPathsFound++;
        this.foundEnemy = true;
        this.returningHome = true;
        return;
      }
      this.moveTowardsCoordinates(nearestEnemy.getX(), nearestEnemy.getY());
      return;
    } catch (ResourceNotFoundException e) {}

    //Look for nearby food
    Food nearestFood;
    try {
      nearestFood = aW.getNearestFoodInRange(this);
      if (Math
          .abs(PApplet.dist(nearestFood.getX(), nearestFood.getY(), this.x, this.y) - 0) < 0.01) {
        this.numPathsFound++;
        this.returningHome = true;
        this.foundFood = true;
        return;
      }
      this.moveTowardsCoordinates(nearestFood.getX(), nearestFood.getY());
      return;
    } catch (ResourceNotFoundException e) {}
    
    //If we've reached the max path length, give up and go home to try again
    if (this.pathLength>ScoutAnt.MAX_PATH_LENGTH) {
      this.returningHome = true;
      return;
    }
    
    //Still searching, move randomly
    this.pathLength++;
    this.moveRandomly();
  }
  
  /**
   * Draws the ant in the AntWorld aW.
   * 
   * @param aW The AntWorld to draw the ant in.
   */
  @Override
  public void drawSelf(AntWorld aW) {
    int[] color = this.nest.getColor();
    aW.fill(color[0],color[1],color[2]);
    aW.noStroke();
    aW.ellipse(this.x, this.y, 3, 3);
  }

}
