//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: Ant Class
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

import java.util.ArrayList;
import java.util.Random;
import processing.core.PApplet;

/**
 * This base abstract class for all other types of ants.
 * 
 * @author Chris
 *
 */
public abstract class Ant {
  //Class Variables
  public static final int RANGE = 50; //the smelling range of the ant
  public static double WIGGLE_CHANCE = 0.5; //the chance that the ant wiggles while walking

  //Instance Variables
  protected int x;
  protected int y;
  protected String id;
  protected Nest nest;
  protected int health;

  protected ArrayList<String> excludeIDs;

  public Ant(String id, Nest nest) {
    this.nest = nest;
    this.id = id;
    this.x = nest.getX();
    this.y = nest.getY();
    this.excludeIDs = new ArrayList<String>();
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public Nest getNest() {
    return nest;
  }

  public void setNest(Nest nest) {
    this.nest = nest;
  }

  public String getId() {
    return id;
  }

  public void setExcludeIDs(ArrayList<String> excludeIDs) {
    this.excludeIDs = excludeIDs;
  }

  public ArrayList<String> getExcludeIDs() {
    return this.excludeIDs;
  }
  
  /**
   * Causes the ant to take damage.
   * @param damage The amount of damage to be taken. 
   */
  public void takeDamage(int damage) {
    this.health -= damage;
  }
  
  /**
   * @param aW The AntWorld object the nest belongs to.
   */
  public void update(AntWorld aW) {
    if (this.health<0) {
      this.nest.addPheromone(new AntPheromone("death", this.id, this.x, this.y));
      this.nest.removeAnt(this, aW);
    }
  }
  
  /**
   * Draws the ant in the AntWorld aW.
   * 
   * @param aW The AntWorld to draw the ant in.
   */
  public abstract void drawSelf(AntWorld aW);

  /*
   * CODE BELOW THIS POINT DOES NOT NEED TO BE CHANGED These instance and class variables are for
   * use in the movement code. The methods handle the math behind moving on the coordinate grid.
   * Feel free to look them over to gain a better understanding of how movement works, but please
   * do not change them.
   * 
   * You should only call moveTowardsCoordinates and moveRandomly. No other methods or variables
   * should be accessed.
   */
  
  /**
   * @param x The x coordinate to mill about.
   * @param y The y coordinate to mill about.
   * @param maxDistance The farthest allowed distance from (x,y) to move.
   */
  protected void millAbout(int x, int y, int maxDistance) {
    if (PApplet.dist(this.x, this.y, x, y) >= maxDistance){
      this.direction = this.direction+Math.PI;
      this.moveTowardsCoordinates(x, y);
    } else {
      this.moveRandomly();
    }
  }
 
  /**
   * This method can be called to mill about an ant's own Nest.
   * @param maxDistance
   */
  protected void millAbout(int maxDistance) {
    this.millAbout(this.nest.getX(), this.nest.getY(), maxDistance);
  }

  /**
   * A random number generator shared by the class. Allows us to have deterministic randomness.
   */
  public static Random rand = new Random(2);

  /**
   * The current direction the ant is moving in radians. Used to handle random movement.
   */
  private double direction = rand.nextDouble() * Math.PI * 2;

  /**
   * Changes this.x and this.y to move speed closer to the provided coordinates. Also has a
   * random chance for a small "wiggle" to happen in the ant's movement to make ant movement look
   * marginally more realistic.
   * 
   * @param x The x coordinate to move towards.
   * @param y The y coordinate to move towards.
   */
  protected void moveTowardsCoordinates(int x, int y) {
    int speed = this.getSpeed();
    float distance = PApplet.dist(this.x, this.y, x, y);
    // If we're within speed of our target, move there directly
    if (distance <= speed) {
      this.x = x;
      this.y = y;
      return;
    }

    // Calculate the angle between the axis and our target coordinates
    double angle = Math.atan2(y - this.y, x - this.x);

    // Randomly add a small wiggle, a change in direction, to the ant's movement
    if (Ant.rand.nextDouble() < Ant.WIGGLE_CHANCE) {
      angle += Math.signum(Ant.rand.nextDouble() - 0.5) * (Ant.rand.nextDouble() * Math.PI / 3);
    }

    // Update the ant's current coordinates after moving it's speed
    this.x += Math.round(speed * Math.cos(angle));
    this.y += Math.round(speed * Math.sin(angle));

    // Check to make sure we didn't move out of bounds
    this.checkCoordinates();
  }
  
  private int getSpeed() {
    if (this instanceof WarriorAnt) {
      return WarriorAnt.WARRIOR_SPEED;  
    }
    if (this instanceof HarvesterAnt) {
      return HarvesterAnt.HARVESTER_SPEED;    
    }
    if (this instanceof QueenAnt) {
      return QueenAnt.QUEEN_SPEED;      
    }
    if (this instanceof ScoutAnt) {
      return ScoutAnt.SCOUT_SPEED;
    }
    return 0;
  }


  /**
   * Moves the ant randomly in a similar direction to it's current direction
   */
  protected void moveRandomly() {
    int speed = this.getSpeed();
    // This turns the any either left or right a random amount, up to PI/3 radians
    this.direction +=
        Math.signum(Ant.rand.nextDouble() - 0.5) * (Ant.rand.nextDouble() * Math.PI / 4);

    // Update the ant's current coordinates after moving it's speed
    this.x += Math.round(speed * Math.cos(this.direction));
    this.y += Math.round(speed * Math.sin(this.direction));

    // Check to make sure we didn't move out of bounds
    this.checkCoordinates();
  }

  /**
   * Checks the ant's coordinates to make sure it didn't move out of bounds, and correct if it did.
   */
  private void checkCoordinates() {
    // Check if we've moved off of the top or left side of the window.
    if (this.x < 0) {
      this.x = -1 * this.x;
    }
    if (this.y < 0) {
      this.y = -1 * this.y;
    }

    // Check if we've moved off the bottom or right side of the window.
    if (this.x > AntWorld.WORLD_SIZE) {
      this.x = 2 * AntWorld.WORLD_SIZE - this.x;
    }
    if (this.y > AntWorld.WORLD_SIZE) {
      this.y = 2 * AntWorld.WORLD_SIZE - this.y;
    }
  }
}
