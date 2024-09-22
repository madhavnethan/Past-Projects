import tester.*;
import javalib.worldimages.*;
import javalib.funworld.*;
import java.awt.Color;
import java.util.Random;


// Directions for Feeding Frenzy
//
// Once your fish spawns in, background fish will run across the screen at varying speeds. Your 
// goal is to eat fish smaller than your own fish using the arrow keys. Be careful as bumping
// into a fish larger than your own means losing the game. To win, your fish must become larger
// than every other fish in the pond. 
//
// Your fish's size is seen in the top right, which increases everytime you consume a fish.


// represents the game
class FishFrenzy extends World {
  int width;
  int height;
  PlayerFish player;
  ILoFish fishList;
  Random rand;

  // Constructor
  FishFrenzy(int width, int height, PlayerFish player, ILoFish fishList, Random rand) {
    this.width = width;
    this.height = height;
    this.player = player;
    this.fishList = fishList;
    this.rand = rand;
  }
  
  /*
  TEMPLATE:
  Fields:
  ... this.width ...                -- int
  ... this.height ...               -- int
  ... this.player ...               -- PlayerFish
  ... this.listFish ...             -- ILoFish
  ... this.rand ...                 -- Random

  Methods:
  ... this.onTick() ...             -- FishFrenzy
  ... this.makeScene() ...          -- WorldScene
  ... this.onKeyEvent(String) ...   -- World
  ... this.worldEnds() ...          -- WorldEnd
  ... this.lastScene(String) ...    -- WorldScene

  Methods for Fields:
  ... this.fishList.moveFish() ...                  -- ILoFish
  ... this.fishList.checkCollision(PlayerFish) ...  -- ILoFish
  ... this.rand.nextInt(int) ...                    -- int
  ... this.rand.nextBoolean() ...                   -- boolean
  ... this.player.draw(WorldScene) ...              -- WorldScene
  ... this.player.move(String) ...                  -- Player
  ... this.player.move(String) ...                  -- WorldScene
  */
 

  // Update the world state on each tick
  public FishFrenzy onTick() {
    this.fishList = this.fishList.moveFish();
    this.fishList = this.fishList.checkCollisions(this.player);
    // Periodically add new fish
    if (rand.nextInt(100) < 3) { // Adjust probability as needed
      this.fishList = new ConsLoFish(new BackgroundFish(0, 
          rand.nextInt(this.height), Color.RED, rand.nextInt(200) + 20, rand.nextInt(50) + 10, 
          rand.nextBoolean()), this.fishList);
    }
    return this;
  }
  
  public FishFrenzy onTickTest() {
    this.fishList = this.fishList.moveFish();
    this.fishList = this.fishList.checkCollisions(this.player);
    // Periodically add new fish
    this.fishList = new ConsLoFish(new BackgroundFish(0, 
        1650, Color.RED, 50, 30, true), this.fishList);
    
    return this;
  }

  // Render the world
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(this.width, this.height);
    scene = this.fishList.draw(scene);
    scene = this.player.draw(scene);
    scene = this.score(scene);
    return scene;
  }

  // Handle key events
  public World onKeyEvent(String ke) {
    return new FishFrenzy(this.width, this.height, 
        this.player.move(ke), this.fishList, this.rand);
  }
  
  // The end of the world images
  public WorldEnd worldEnds() {
    if (fishList.playerBigger(player)) {
      return new WorldEnd(true, this.lastScene("You Win!"));
    }
    else if (this.player.size == 0) {
      return new WorldEnd(true, this.lastScene("You Lose!"));
    }
    else {
      return new WorldEnd(false, this.makeScene());
    }
}
  
  public WorldScene score(WorldScene acc) {
    return acc.placeImageXY(new TextImage(Integer.toString(this.player.size), 30, Color.BLACK), 1600, 50);
  }

  // Display end scene with a message
  public WorldScene lastScene(String msg) {
    WorldScene scene = new WorldScene(this.width, this.height);
    return scene.placeImageXY(new TextImage(msg, 30, Color.RED), this.width / 2, this.height / 2);

  }
 
  
}

// Represents the fish on the screen
abstract class AFish {
  int posx;
  int posy;
  Color color;
  int size;
  int speed;
  boolean moving;

  // Constructor
  AFish(int posx, int posy, Color color, int size, int speed, boolean moving) {
    this.posx = posx;
    this.posy = posy;
    this.color = color;
    this.size = size;
    this.speed = speed;
    this.moving = moving;
  }
  
  /*
  TEMPLATE:
  Fields:
  ... this.posx ...          -- int
  ... this.posy ...          -- int
  ... this.color ...         -- Color
  ... this.size ...          -- int
  ... this.speed ...         -- int
  ... this.moving ...        -- boolean

  Methods:
  ... this.draw(WorldScene) ...      -- WorldScene
  ... this.collidesWith(AFish) ...   -- boolean
  
  Methods for Fields: N/A
  */

  public OverlayOffsetImage fishDraw() {
    if (this.moving) {
      return new OverlayOffsetImage(new EllipseImage((int) (this.size * 2), 
          this.size, OutlineMode.SOLID, this.color), 
          -(size * 3/5),
          0,
          new RotateImage(new EquilateralTriangleImage(
              this.size, OutlineMode.SOLID, this.color), 90));
    }
    else {
      return new OverlayOffsetImage(new EllipseImage((int) (this.size * 2), 
          this.size, OutlineMode.SOLID, this.color), 
          (size * 3/5),
          0,
          new RotateImage(new EquilateralTriangleImage(
              this.size, OutlineMode.SOLID, this.color), 270));
    }
    
  }
  
  // Draws the actual fish on the world
  public WorldScene draw(WorldScene acc) {
    return acc.placeImageXY(this.fishDraw(), this.posx, this.posy);
  }

  // Checks to see if fish has collided with another fish
  public boolean collidesWith(AFish other) {
    return Math.abs(this.posx - other.posx) < this.size / 2 + other.size / 2 &&
           Math.abs(this.posy - other.posy) < this.size / 2 + other.size / 2;
  }
}

// Represents the player fish
class PlayerFish extends AFish {
  
  // Constructor
  PlayerFish(int posx, int posy, Color color, int size, int speed, boolean moving) {
    super(posx, posy, color, size, speed, moving);
  }
  
  /*
  TEMPLATE:
  Fields:
  ... this.posx ...       -- int
  ... this.posy ...       -- int
  ... this.color ...      -- Color
  ... this.size ...       -- int
  ... this.speed ...      -- int
  ... this.moving ...     -- boolean
  
  Methods:
  ... this.grow(AFish) ...     -- PlayerFish
  ... this.shrink(AFish) ...   -- PlayerFish
  ... this.move(String) ...    -- Player

  Methods for Fields: N/A
  */

  // Calculation of PlayerFish growing
  public PlayerFish grow(AFish eatenFish) {
    return new PlayerFish(0, this.posy, this.color, this.size += eatenFish.size / 2, this.speed, this.moving);
    // Example growth rate
  }
  
  // PlayerFish size goes to 0
  public PlayerFish shrink(AFish eatenFish) {
    return new PlayerFish(0, this.posy, this.color, this.size = 0, this.speed, this.moving);
  }

  // Function to move PlayerFish
  public PlayerFish move(String key) {
    if (this.posx > 1650) {
      return new PlayerFish(0, this.posy, this.color, this.size, this.speed, this.moving);
    }
    else if (this.posx < 0) {
      return new PlayerFish(1650, this.posy, this.color, this.size, this.speed, this.moving);
    }
    else if (this.posy < 0){
      return new PlayerFish(this.posx, 0, this.color, this.size, this.speed, this.moving);
    }
    else if (this.posy > 1000) {
      return new PlayerFish(this.posx, 1000, this.color, this.size, this.speed, this.moving);
    }
    else if (key.equals("up")) {
      return new PlayerFish(this.posx, this.posy - this.speed, this.color, 
          this.size, this.speed, this.moving);
    }
    else if (key.equals("right")) {
      return new PlayerFish(this.posx + this.speed, this.posy, this.color, 
          this.size, this.speed, true);
    }
    else if (key.equals("down")) {
      return new PlayerFish(this.posx, this.posy + this.speed, this.color, 
          this.size, this.speed, this.moving);
    }
    else if (key.equals("left")) {
      return new PlayerFish(this.posx - this.speed, this.posy, this.color, 
          this.size, this.speed, false);
    }
    else {
      return this;
    }
  }
  
}

// Represents the background fish
class BackgroundFish extends AFish {
  
  //Constructor
  BackgroundFish(int posx, int posy, Color color, int size, int speed, boolean moving) {
    super(posx, posy, color, size, speed, moving);
  }
  
  /*
  TEMPLATE:
  Fields:
  ... this.posx ...       -- int
  ... this.posy ...       -- int
  ... this.color ...      -- Color
  ... this.size ...       -- int
  ... this.speed ...      -- int
  ... this.moving ...     -- boolean
  
  Methods:
  ... this.move() ...     -- BackgroundFish
  ... this.getSize ...    -- int

  Methods for Fields: N/A
  */
  
  // Function to move BackgroundFish
  public BackgroundFish move() {
    if (this.posx > 1650) {
      return new BackgroundFish(0, this.posy, this.color, this.size, this.speed, this.moving);
    }
    else if (this.posx < 0) {
      return new BackgroundFish(1650, this.posy, this.color, this.size, this.speed, this.moving);
    }
    else if (this.moving) {
      return new BackgroundFish(this.posx + this.speed, this.posy, this.color, 
          this.size, this.speed, this.moving);
    }
    else {
      return new BackgroundFish(this.posx - this.speed, this.posy, this.color, 
          this.size, this.speed, this.moving);
    }
  }
  
  public int getSize() {
    return this.size;
  }
  
}

// List of Background Fish
interface ILoFish {
  
  // Function that moves the list of fish
  ILoFish moveFish();
  
  // Function that checks to see if PlayerFish collides with a BackgroundFish from the list
  ILoFish checkCollisions(PlayerFish player);
  
  // Draws the list of BackgroundFish
  WorldScene draw(WorldScene acc);
  
  //Checks to see if player is bigger than every fish on the screen
  boolean playerBigger(PlayerFish player);
}

class MtLoFish implements ILoFish {
  
  
  /*
  TEMPLATE:
  Fields: N/A
  
  Methods:
  ... this.moveFish() ...                 -- ILoFish
  ... this.checkCollisions(Player) ...    -- ILoFish
  ... this.draw(WorldScene) ...           -- WorldScene
  ... this.playerBigger(PlayerFish) ...       -- boolean

  Methods for Fields: N/A
  */
  
  //Function that moves the list of fish
  public ILoFish moveFish() {
    return this;
  }

  // Function that checks to see if PlayerFish collides with a BackgroundFish from the list
  public ILoFish checkCollisions(PlayerFish player) {
    return this;
  }

  // Draws the list of BackgroundFish
  public WorldScene draw(WorldScene acc) {
    return acc;
  }
  
  //Checks to see if player is bigger than every fish on the screen
  public boolean playerBigger(PlayerFish player) {
    return true;
  }
  
}

class ConsLoFish implements ILoFish {
  BackgroundFish first;
  ILoFish rest;

  /*
  TEMPLATE:
  Fields: 
  ... this.first ...     -- Background
  ... this.rest ...      -- ILoFish
  
  Methods:
  ... this.moveFish() ...                     -- ILoFish
  ... this.checkCollisions(PlayerFish) ...    -- ILoFish
  ... this.draw(WorldScene) ...               -- WorldScene
  ... this.playerBigger(PlayerFish) ...       -- boolean

  Methods for Fields:
  ... this.fish.move() ...                       -- ILoFish
  ... this.rest.moveFish() ...                   -- Background
  ... this.first.collidesWIth(AFish) ...         -- boolean
  ... this.rest.checkCollision(PlayerFish) ...   -- ILoFish
  ... this.first.draw(WorldScene) ...            -- WorldScene
  ... this.rest.playerBigger(PlayerFish) ...     -- boolean
  ... this.first.getSize() ...                   -- int

  */
  
  // Constructor
  ConsLoFish(BackgroundFish first, ILoFish rest) {
    this.first = first;
    this.rest = rest;
  }

  //Function that moves the list of fish
  public ILoFish moveFish() {
    return new ConsLoFish(this.first.move(), this.rest.moveFish());
  }
  
  // Function that checks to see if PlayerFish collides with a BackgroundFish from the list
  public ILoFish checkCollisions(PlayerFish player) {
    if (this.first.collidesWith(player) && (player.size > this.first.size)) {
        player.grow(this.first);
        return this.rest.checkCollisions(player);
    }
    else if (this.first.collidesWith(player) && (player.size <= this.first.size)) {
        player.shrink(this.first);
        return this.rest.checkCollisions(player);
      }
    else {
      return new ConsLoFish(this.first, this.rest.checkCollisions(player));
    }
  }
  
  // Draws the list of BackgroundFish
  public WorldScene draw(WorldScene acc) {
    return this.rest.draw(this.first.draw(acc));
  }
  
  // Checks to see if player is bigger than every fish on the screen
  public boolean playerBigger(PlayerFish player) {
    if(this.first.getSize() < player.size) {
      return this.rest.playerBigger(player);
    }
    else {
      return false;
    }
  }
  
  
}

// Examples for animation
class ExamplesAnimation {
  
  // Define constants for the game world size
  int WIDTH = 1650;
  int HEIGHT = 1000;
    
  // Create a random object with a fixed seed for predictable randomness
  Random rand = new Random(42);
  
  // Create sample fish
  BackgroundFish f0 = new BackgroundFish(0, 1000, Color.RED, 50, 30, true);
  BackgroundFish f1 = new BackgroundFish(0, rand.nextInt(1650), Color.RED, 30, 12, true);
  BackgroundFish f2 = new BackgroundFish(0, rand.nextInt(1650), Color.RED, 20, 23, true);
  BackgroundFish f3 = new BackgroundFish(0, rand.nextInt(1650), Color.RED, 10, 14, true);
  BackgroundFish f4 = new BackgroundFish(0, rand.nextInt(1650), Color.RED, 30, 12, true);
  BackgroundFish f5 = new BackgroundFish(0, rand.nextInt(1650), Color.RED, 20, 23, true);
  BackgroundFish f6 = new BackgroundFish(0, rand.nextInt(1650), Color.RED, 10, 14, false);
  BackgroundFish f7 = new BackgroundFish(0, rand.nextInt(1650), Color.RED, 60, 22, false);
  BackgroundFish f8 = new BackgroundFish(0, rand.nextInt(1650), Color.RED, 20, 13, false);
  BackgroundFish f9 = new BackgroundFish(0, rand.nextInt(1650), Color.RED, 10, 24, false);
  BackgroundFish f10 = new BackgroundFish(500, 500, Color.RED, 10, 4, false);
  BackgroundFish f11 = new BackgroundFish(477, 500, Color.RED, 10, 4, true);
  BackgroundFish f12 = new BackgroundFish(12, 130, Color.RED, 30, 12, true);
  BackgroundFish f13 = new BackgroundFish(23, 763, Color.RED, 20, 23, true);
  BackgroundFish f14 = new BackgroundFish(14, 248, Color.RED, 10, 14, true);
  BackgroundFish f15 = new BackgroundFish(0, 1000, Color.RED, 10, 30, true);
  
  // Create player fish
  PlayerFish player = new PlayerFish(875, 500, Color.BLUE, 20, 30, true);
  PlayerFish playerTest = new PlayerFish(500, 500, Color.BLUE, 20, 30, true);
  
  // Create lists of fish
  ILoFish mt = new MtLoFish();
  ILoFish lof0 = new ConsLoFish(f0, mt);
  ILoFish lof1 = new ConsLoFish(f1, mt);
  ILoFish lof2 = new ConsLoFish(f2, lof1);
  ILoFish lof3 = new ConsLoFish(f3, lof2);
  ILoFish lof4 = new ConsLoFish(f4, lof3);
  ILoFish lof5 = new ConsLoFish(f5, lof4);
  ILoFish lof6 = new ConsLoFish(f6, lof5);
  ILoFish lof7 = new ConsLoFish(f7, lof6);
  ILoFish lof8 = new ConsLoFish(f8, lof7);
  ILoFish lof9 = new ConsLoFish(f9, lof8);
  ILoFish lof10 = new ConsLoFish(f12, mt);
  ILoFish lof11 = new ConsLoFish(f13, lof10);
  ILoFish lof12 = new ConsLoFish(f14, lof11);
  ILoFish lof13 = new ConsLoFish(f0, lof12);
  ILoFish lof14 = new ConsLoFish(f15, mt);
  ILoFish lof15 = new ConsLoFish(f10, mt);
  
  
  // Create game worlds
  FishFrenzy initWorld = new FishFrenzy(WIDTH, HEIGHT, player, mt, rand);
  FishFrenzy worldWithFish = new FishFrenzy(WIDTH, HEIGHT, player, lof3, rand);
  FishFrenzy testWorldWithFish = new FishFrenzy(WIDTH, HEIGHT, playerTest, lof1, rand);
  
  // Sample WorldScene for testing
  WorldScene ws = new WorldScene(WIDTH, HEIGHT);
  
  // Test the draw method for fish and lists of fish
  boolean testDraw(Tester t) {
    return t.checkExpect(this.f1.draw(this.ws),
        ws.placeImageXY(new EllipseImage(60, 30, OutlineMode.SOLID, Color.RED), 0, 130))
        && t.checkExpect(lof1.draw(this.ws), ws.placeImageXY(new EllipseImage(60, 30, OutlineMode.SOLID, Color.RED), 0, 130));
  }
  
  boolean testCollidesWith(Tester t) {
    return t.checkExpect(this.player.collidesWith(f10), true) &&
        t.checkExpect(this.player.collidesWith(f12), false) &&
        t.checkExpect(this.player.collidesWith(f11), false);
  }
  
  boolean testGrow(Tester t) {
    return t.checkExpect(this.playerTest.grow(f3), new PlayerFish(0, 500, Color.BLUE, 25, 30, true));
  }
  
  boolean testShrink(Tester t) {
    return t.checkExpect(this.playerTest.shrink(f3), new PlayerFish(0, 500, Color.BLUE, 0, 30, true));
  }
  
  boolean testOnTick(Tester t) {
    return t.checkExpect(this.worldWithFish.onTickTest(), new FishFrenzy(1000, 1000, player, lof13, rand))
        && t.checkExpect(this.initWorld.onTickTest(), new FishFrenzy(1000, 1000, player, lof0, rand));
  }
  
//  boolean testMakeScene(Tester t) {
//    return t.checkExpect(testWorldWithFish.makeScene(), null);
//  }
  
  boolean testLastScene(Tester t) {
    WorldScene scene = new WorldScene(1000, 1000);
    
    return t.checkExpect(worldWithFish.lastScene("You Win!"), 
        scene.placeImageXY(new TextImage("You Win!", 30, Color.RED), 500, 500))
        && t.checkExpect(worldWithFish.lastScene("You Lose!"), 
            scene.placeImageXY(new TextImage("You Lose!", 30, Color.RED), 500, 500));
  }
  
  boolean testMove(Tester t) {   
    return t.checkExpect(f10.move(), new BackgroundFish(496, 500, Color.RED, 10, 4, false))
        && t.checkExpect(playerTest.move("right"), new PlayerFish(530, 500, Color.BLUE, 20, 30, true));
    
  }
  
  boolean testGetSize(Tester t) {
    return t.checkExpect(f10.getSize(), 10)
        && t.checkExpect(f12.getSize(), 30)
        && t.checkExpect(f13.getSize(), 20);
  }
  
  boolean testPlayerBigger(Tester t) {
    return t.checkExpect(lof14.playerBigger(playerTest), true) && 
           t.checkExpect(lof12.playerBigger(playerTest), false);
  }

  boolean testMoveFish(Tester t) {
    return t.checkExpect(lof15.moveFish(), new ConsLoFish(new BackgroundFish(496, 500, Color.RED, 10, 4, false), mt)) &&
           t.checkExpect(mt.moveFish(), mt);
  }  
  
  //Test big bang to start the world (may need manual verification)
  boolean testBigBang(Tester t) {
    FishFrenzy world = new FishFrenzy(WIDTH, HEIGHT, player, lof9, rand);
    int worldWidth = WIDTH;
    int worldHeight = HEIGHT;
    double tickRate = 0.1;
    return world.bigBang(worldWidth, worldHeight, tickRate);
  }
}
