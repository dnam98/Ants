//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: Nest Class
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

public class Nest {
  public static int FOOD_SIZE_THRESHOLD = 12; // number of food it takes to increase the size of
                                              // the nest by 1
  public static int MAX_NEST_SIZE = 50; // The most ants that can be in a single nest
  public static int MIN_ANT_COUNT = 3; // The minimum number of each type of ant that the nest
                                       // should to try to have at all times

  private int x; // the x position of the nest
  private int y; // the y position of the nest
  private int food; // the amount of food in the nest
  private int size; // current size of the nest
  private int[] color = new int[3]; // Array storing red, green, and blue values of the color of
                                    // the nest
  private ArrayList<AntPheromone> pheromoneList; // List of all active pheromones in the nest
  private ArrayList<Ant> antList; // List of all live ants in the nest, including the queen
  private QueenAnt queen; // The queen ant of the nest
  private int numScouts; // The current number of scouts in the nest
  private int numWarriors; // The current number of warriors in the nest
  private int numHarvesters; // The current number of harvesters in the nest

  // Constructor of Nest class.
  public Nest(int x, int y, int r, int g, int b) {
    this.food = 0;
    this.size = 0;
    this.x = x;
    this.y = y;
    this.numScouts = 0;
    this.numWarriors = 0;
    this.numHarvesters = 0;
    pheromoneList = new ArrayList<AntPheromone>();
    antList = new ArrayList<Ant>();
    this.color[0] = r;
    this.color[1] = g;
    this.color[2] = b;
  }

  // Getter & Setter methods

  /**
   * getter method for x position of the nest
   * 
   * @return x position of the nest
   */
  public int getX() {
    return x;
  }

  /**
   * setter method for x position of the nest
   * 
   * @param x - x position of the nest
   */
  public void setX(int x) {
    this.x = x;
  }

  /**
   * getter method for y position of the nest
   * 
   * @return y position of the nest
   */
  public int getY() {
    return y;
  }

  /**
   * setter method for y position of the nest
   * 
   * @param y - y position of the nest
   */
  public void setY(int y) {
    this.y = y;
  }

  /**
   * getter method for number of foods in the nest
   * 
   * @return number of foods in the nest
   */
  public int getFood() {
    return food;
  }

  /**
   * setter method for number of foods in the nest
   * 
   * @param food - number of foods in the nest
   */
  public void setFood(int food) {
    this.food = food;
  }

  /**
   * getter method for the size of the nest
   * 
   * @return size of the nest
   */
  public int getSize() {
    return size;
  }

  /**
   * setter method for the size of the nest
   * 
   * @param size - size of the nest
   */
  public void setSize(int size) {
    this.size = size;
  }

  /**
   * getter method for an array of color
   * 
   */
  public int[] getColor() {
    return this.color;
  }

  /**
   * setter method for an array of color
   * 
   */
  public void setColor(int[] color) {
    this.color = color;
  }

  /**
   * getter method for an ArrayList of ant pheromones
   * 
   * @return ArrayList of ant pheromones
   */
  public ArrayList<AntPheromone> getPheromoneList() {
    return pheromoneList;
  }

  /**
   * setter method for an ArrayList of ant pheromones
   * 
   * @param pheromoneList - ArrayList of ant pheromones
   */
  public void setPheromoneList(ArrayList<AntPheromone> pheromoneList) {
    this.pheromoneList = pheromoneList;
  }

  /**
   * getter method for an ArrayList of ants
   * 
   * @return ArrayList of ants
   */
  public ArrayList<Ant> getAntList() {
    return antList;
  }

  /**
   * setter method for an ArrayList of ants
   * 
   * @param antList - ArrayList of ants
   */
  public void setAntList(ArrayList<Ant> antList) {
    this.antList = antList;
  }

  /**
   * getter method for queen
   * 
   * @return queen
   */
  public QueenAnt getQueen() {
    return queen;
  }

  /**
   * setter method for queen
   * 
   * @param queen
   */
  public void setQueen(QueenAnt queen) {
    this.queen = queen;
  }

  /**
   * getter method for number of scout ants
   * 
   * @return number of scout ants
   */
  public int getNumScouts() {
    return numScouts;
  }

  /**
   * setter method for number of scout ants
   * 
   * @param numScouts - number of scout ants
   */
  public void setNumScouts(int numScouts) {
    this.numScouts = numScouts;
  }

  /**
   * getter method for number of warrior ants
   * 
   * @return - number of warrior ants
   */
  public int getNumWarriors() {
    return numWarriors;
  }

  /**
   * setter method for number of warrior ants
   * 
   * @param numWarriors - number of warrior ants
   */
  public void setNumWarriors(int numWarriors) {
    this.numWarriors = numWarriors;
  }

  /**
   * getter method for number of harvester ants
   * 
   * @return number of harvester ants
   */
  public int getNumHarvesters() {
    return numHarvesters;
  }

  /**
   * setter method for number of harvester ants
   * 
   * @param numHarvesters - number of harvester ants
   */
  public void setNumHarvesters(int numHarvesters) {
    this.numHarvesters = numHarvesters;
  }

  /**
   * Adds singular antPheromone object to pheromoneList array
   * 
   * @param antPheromone
   */
  public void addPheromone(AntPheromone antPheromone) {
    this.getPheromoneList().add(antPheromone);
  }

  /**
   * Adds singular ant object to antList array If ant object is a queen ant, set queen to this ant
   * 
   * @param ant - The ant which is added to the antList array
   */
  public void addAnt(Ant ant) {
    // If ant is a queen, set queen to this ant object
    if (ant instanceof QueenAnt) {
      this.queen = (QueenAnt) ant;
    }

    else if (ant instanceof WarriorAnt) {
      this.numWarriors++;
    }

    else if (ant instanceof HarvesterAnt) {
      this.numHarvesters++;
    }

    else if (ant instanceof ScoutAnt) {
      this.numScouts++;
    }

    this.getAntList().add(ant);
  }

  /**
   * Remove ant object when destroyed depending on type
   * 
   * @param ant - Ant to be removed
   * @param aW  - Ant world
   */
  public void removeAnt(Ant ant, AntWorld aW) {
    if (ant instanceof QueenAnt) {
      aW.removeNest(this);
    }

    else if (ant instanceof ScoutAnt) {
      this.numScouts--;
    }

    else if (ant instanceof WarriorAnt) {
      this.numWarriors--;
    }

    else if (ant instanceof HarvesterAnt) {
      this.numHarvesters--;
    }

    this.antList.remove(ant);
  }

  /**
   * Increments the number of food in the nest. If food is divisible by foodSizeThreshold, also
   * increment size of the nest as well.
   * 
   */
  public void addFood() {
    this.food++;

    if ((this.food % FOOD_SIZE_THRESHOLD) == 0) {
      this.size++;
    }
    
    queen.addFood();
  }

  /**
   * Draw a circle of the nest's size
   * 
   * @param gw
   */
  public void drawSelf(AntWorld aW) {
    aW.fill(this.color[0], this.color[1], this.color[2]);
    aW.noStroke();
    aW.ellipse(this.x, this.y, this.size, this.size);
  }

}
