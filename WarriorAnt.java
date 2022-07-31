//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: Warrior Ant
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

public class WarriorAnt extends Ant {
  public static int DAMAGE = 20; // Damage the warrior ant does in a single attack
  public static int WARRIOR_SPEED = 4; //Speed of the warrior ant

  // Constructor
  public WarriorAnt(String id, Nest nest) {
    super(id, nest);
    this.health = 150;
  }

  /**
   * Damage the enemy ant
   * 
   * @param enemy - enemy ant object
   */
  public void attack(Ant enemy) {
    enemy.takeDamage(DAMAGE);
  }

  /**
   * Override of Ant update method to fit Warrior behavior.
   */
  @Override
  public void update(AntWorld aW) {
    super.update(aW);

    Ant enemy;

    // Outline 2: Check if enemy ant is in range
    try {
      enemy = aW.getNearestEnemyAntInRange(this);
    } catch (ResourceNotFoundException e) {
      enemy = null;
    }

    // Outline 3 & 4: Enemy in range
    if (enemy != null) {

      // Outline 3: Enemy within WARRIOR_SPEED
      if (PApplet.dist(this.x, this.y, enemy.getX(), enemy.getY()) <= WARRIOR_SPEED) {
        this.attack(enemy);
        this.excludeIDs.clear();
        return;
      }

      // Outline 4: Enemy not within WARRIOR_SPEED
      else {
        this.moveTowardsCoordinates(enemy.getX(), enemy.getY());
        return;
      }
    }

    // Outline 5,6,7,8,9: No enemy in range
    else {
      AntPheromone antPheromone;

      // Outline 5: Search for "war" pheromones
      try {
        antPheromone = aW.getFarthestPheromoneInRange(this, "war");

        // Outline 7-"war": Pheromone within 0.01
        // TODO: Fix creatorID
        if (PApplet.dist(this.x, this.y, antPheromone.getX(), antPheromone.getY()) <= 0.01) {
          this.excludeIDs.add(antPheromone.getCreatorID());
          return;
        }

        // Outline 8-"war": Pheromone not within 0.01
        else {
          this.moveTowardsCoordinates(antPheromone.getX(), antPheromone.getY());
          return;
        }
      } catch (ResourceNotFoundException e) {
      }

      // Outline 6: Search for "death" pheromones
      try {
        antPheromone = aW.getFarthestPheromoneInRange(this, "death");

        // Outline 7-"death": Pheromone within 0.01
        if (PApplet.dist(this.x, this.y, antPheromone.getX(), antPheromone.getY()) <= 0.01) {
          this.excludeIDs.add(antPheromone.getCreatorID());
          return;
        }

        // Outline 8-"death": Pheromone not within 0.01
        else {
          this.moveTowardsCoordinates(antPheromone.getX(), antPheromone.getY());
          return;
        }
      } catch (ResourceNotFoundException e) {
      }

      // Outline 9: Mill about within RANGE of the nest
      this.millAbout(Ant.RANGE);
    }

  }

  @Override
  public void drawSelf(AntWorld aW) {
    int[] color = this.nest.getColor();
    aW.fill(color[0], color[1], color[2]);
    aW.noStroke();
    aW.triangle(this.x, this.y + 4, this.x - 3, this.y - 4, this.x + 3, this.y - 4);
  }

}
