//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: Harvester Ant
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

import processing.core.PApplet;

public class HarvesterAnt extends Ant {
  public static int HARVESTER_SPEED = 4; // Speed of the harvester ant

  private int numFood; // Number of food the harvester ant has brought back to the nest
  private boolean hasFood; // True if harvester ant is carrying food

  // Constructor
  public HarvesterAnt(String id, Nest nest) {
    super(id, nest);
    this.health = 100;
    this.numFood = 0;
    this.hasFood = false;
  }

  /**
   * Getter method for numFood
   * 
   * @return number of food brought back to the nest
   */
  public int getNumFood() {
    return numFood;
  }

  /**
   * Setter method for numFood
   * 
   * @param numFood - number of food brought back to the nest
   */
  public void setNumFood(int numFood) {
    this.numFood = numFood;
  }

  /**
   * Getter method for hasFood
   * 
   * @return true if the harvester ant brought food back to the nest
   */
  public boolean getHasFood() {
    return hasFood;
  }

  /**
   * Setter method for hasFood
   * 
   * @param hasFood - true if the harvester ant brought food back to the nest
   */
  public void setHasFood(boolean hasFood) {
    this.hasFood = hasFood;
  }

  /**
   * Updates the ant for a new frame.
   * 
   * @param aW The AntWorld object.
   */
  public void update(AntWorld aW) {
    super.update(aW);
    Food food;
    AntPheromone antPheromone;

    if (this.hasFood == true) {
      // Have food and within 0.01 of nest
      if (PApplet.dist(this.x, this.y, this.nest.getX(), this.nest.getY()) <= 0.01) {
        this.nestWithFood();
      }
      // Have food but not within 0.01 of nest
      else {
        this.backToNest();
      }
    }
    // Doesn't have food, look for food in range
    else {
      try {
        food = aW.getNearestFoodInRange(this);
      } catch (ResourceNotFoundException e) {
        food = null;
      }

      // Food is in range
      if (food != null) {
        // Food within 0.01
        if (PApplet.dist(this.x, this.y, food.getX(), food.getY()) <= 0.01) {
          this.foodInRange(food, aW);
        }
        // Food not within 0.01
        else {
          this.moveTowardsCoordinates(food.getX(), food.getY());
        }
      }

      // Food is not in range, search for pheromones
      else {
        try {
          antPheromone = aW.getFarthestPheromoneInRange(this, "food");
        } catch (ResourceNotFoundException e) {
          antPheromone = null;
        }

        // Found pheromone
        if (antPheromone != null) {

          // Pheromone within 0.01
          if (PApplet.dist(this.x, this.y, antPheromone.getX(), antPheromone.getY()) <= 0.01) {
            this.pheromoneInRange(antPheromone);
          }

          // Pheromone not within 0.01
          else {
            this.moveTowardsCoordinates(antPheromone.getX(), antPheromone.getY());
          }
        }

        // Move randomly if nothing found
        else {
          super.millAbout(nest.getSize());
        }
      }
    }
  }

  /**
   * Reach the nest with food
   */
  private void nestWithFood() {
    this.setHasFood(false);
    this.nest.addFood();
    this.numFood++;
    this.nest.getQueen().reportPheromone("food");
  }

  /**
   * Carrying food back to the nest
   */
  private void backToNest() {
    AntPheromone antPheromone = new AntPheromone("food", this.id, this.x, this.y);
    this.nest.addPheromone(antPheromone);
    this.moveTowardsCoordinates(this.nest.getX(), this.nest.getY());
  }

  /**
   * Food within 0.01, so ant carries the food
   * 
   * @param food
   * @param aW
   */
  private void foodInRange(Food food, AntWorld aW) {
    aW.removeFood(food);
    this.setHasFood(true);
    this.excludeIDs.clear();
  }

  /**
   * Pheromone within 0.01
   * 
   * @param antPheromone
   */
  private void pheromoneInRange(AntPheromone antPheromone) {
    this.excludeIDs.add(antPheromone.getCreatorID());
  }

  /**
   * Draws the harvester ant
   */
  @Override
  public void drawSelf(AntWorld aW) {
    int[] color = this.nest.getColor();
    aW.fill(color[0], color[1], color[2]);
    aW.noStroke();
    aW.ellipse(this.x, this.y, 5, 5);
  }

}
