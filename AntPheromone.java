
/**
 * Class to represent a part of a pheromone trail in AntWorld.
 * 
 * @author Chris
 *
 */
public class AntPheromone{
  private int x; //the x position of the pheromone
  private int y; // the y position of the pheromone
  private int strength; //the strength of the scent of the pheromone
  private static final int size = 5; //how large to draw pheromones
  private String creatorID; //the ID of the ant that left this pheromone
  private String type; //Must be either "food", "war", or "death"
  
  /**
   * Constructor for AntPheromone. Takes in the id of the Ant that created it, and a starting 
   * location. 
   * 
   * @param id The id of the creating Ant.
   * @param xLocation The x location of the pheromone.
   * @param yLocation The y location of the pheromone.
   */
  public AntPheromone(String type, String id, int xLocation, int yLocation) {
    this.x = xLocation;
    this.y = yLocation;
    this.creatorID = id;
    this.type = type;
    this.strength = 300;
  }
  
  /**
   * Getter for the creatorID instance variable
   * 
   * @return The id of the ant who created this pheromone.
   */
  public String getCreatorID() {
    return this.creatorID;
  }
  
  /**
   * @return
   */
  public String getType() {
    return type;
  }

  /**
   * @param type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Simulates the passage of time, making the pheromone weaker.
   */
  public void decrementStrength() {
    this.strength-=1;
  }
  
  
  /**
   * The getter for x.
   * 
   * @return The x position of the pheromone.
   */
  public int getX() {
    return this.x;
  }
  
  /**
   * The getter for y.
   * 
   * @return The y position of the pheromone.
   */
  public int getY() {
    return this.y;
  }
  
  /**
   * The getter for strength.
   * 
   * @return The current strength of the smell of the pheromone.
   */
  public int getStrength() {
    return this.strength;
  }
  
  /**
   * Draws the pheromone in the AntWorld aW.
   * 
   * @param aW The Antworld to draw in.
   */
  public void drawSelf(AntWorld aW) {
    if (this.type=="war") {
      aW.fill(196,108,41,this.strength/2);
      aW.noStroke();
      aW.ellipse(this.x, this.y, AntPheromone.size, AntPheromone.size);
    }
    else if (this.type=="food") {
      aW.fill(50,200,50,this.strength/2);
      aW.noStroke();
      aW.ellipse(this.x, this.y, AntPheromone.size, AntPheromone.size);
    } else if (this.type=="death") {
      aW.fill(255,50,50,75);
      aW.noStroke();
      aW.ellipse(this.x, this.y, 25, 25);
    } else {
      aW.fill(100,100,100,this.strength/3);
      aW.noStroke();
      aW.ellipse(this.x, this.y, AntPheromone.size, AntPheromone.size);
    }

  }

}
