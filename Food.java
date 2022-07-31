
/**
 * Class to represent a single piece of food in AntWorld.
 * 
 * @author Chris
 *
 */
public class Food {
  private int x; //x position of the food instance
  private int y; //y position of the food instance
  private static final int size = 6; //How large to draw the food
  
  /**
   * Constructor for Food class. Creates a piece of food at the given location.
   * 
   * @param xLocation The x position of the food instance
   * @param yLocation The y position of the food instance
   */
  public Food(int xLocation, int yLocation) {
    this.x = xLocation; 
    this.y = yLocation;
  }
  
  /**
   * Getter for x
   * 
   * @return The x position of the food instance.
   */
  public int getX() {
    return this.x;
  }
  
  /**
   * Getter for y
   * 
   * @return The y position of the food instance.
   */
  public int getY() {
    return this.y;
  }
  
  /**
   * Draws the food in the AntWorld aW.
   * 
   * @param aW
   */
  public void drawSelf(AntWorld aW) {
    aW.fill(0,255,0);
    aW.noStroke();
    aW.ellipse(this.x, this.y, Food.size, Food.size);
  }
}
