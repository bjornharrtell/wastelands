import org.junit.Test
import org.wololo.wastelands.core.unit._
import org.wololo.wastelands.core.Game
import org.wololo.wastelands.core.GameState
import org.wololo.wastelands.core.Player
import org.wololo.wastelands.core.event

import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import akka.testkit.TestKit

object BasicTests {
  var game: TestActorRef[Game] = null
}

class TestPlayer extends Player with GameState {
  def receive = {
    case e: event.Joined =>
  }
  
  def join = {
    BasicTests.game ! event.Join()
  }
}

class BasicTests extends TestKit(ActorSystem("test")) {

  /**
   * Check basic move logic
   */
  @Test
  def TestUnitMove = {
    BasicTests.game = TestActorRef[Game]
    val player = TestActorRef[TestPlayer]
    val testUnit = TestActorRef(new TestUnit1(player, BasicTests.game.underlyingActor, (1,1), Direction.Directions(0)))

    assert(testUnit.underlyingActor.position == (1,1))
    
    testUnit ! event.Order(Move(2,2))
    
    // tick game state forward to allow for three turns 
    // TODO: turn cooldown should be read from unit type constants
    for (i <- 1 to 6*3) {
      BasicTests.game.underlyingActor.ticks += 1
      testUnit ! event.Tick()
    }
    
    assert(testUnit.underlyingActor.position == (2,2))
  }
  
  /**
   * Check collision logic
   */
  @Test
  def TestUnitMoveCollision = {
    BasicTests.game = TestActorRef[Game]
    val player = TestActorRef[TestPlayer]
    val testUnit1 = TestActorRef(new TestUnit1(player, BasicTests.game.underlyingActor, (1,1), Direction.Directions(0)))
    val testUnit2 = TestActorRef(new TestUnit1(player, BasicTests.game.underlyingActor, (1,2), Direction.Directions(0)))

    testUnit1 ! event.Order(Move(1,2))
    
    // tick game state forward to allow for three turns 
    // TODO: turn cooldown should be read from unit type constants
    for (i <- 1 to 60*3) {
      // TODO: should be able to tick the game.. but it hasn't got the units as children!
      BasicTests.game.underlyingActor.ticks += 1
      testUnit1 ! event.Tick()
      testUnit2 ! event.Tick()
    }
    println(testUnit1.underlyingActor.position)
    assert(testUnit1.underlyingActor.position == (1,1))
  }
  
  /**
   * Check that turn cooldowns are delaying turns properly
   */
  @Test
  def TestUnitTurnCooldown = {
    BasicTests.game = TestActorRef[Game]
    val player = TestActorRef[TestPlayer]
    val testUnit = TestActorRef(new TestUnit1(player, BasicTests.game.underlyingActor, (1,1), new Direction(1,1)))

    assert(testUnit.underlyingActor.position == (1,1))
    
    testUnit ! event.Order(Move(2,1))
    
    testUnit ! event.Tick()
    testUnit ! event.Tick()
    testUnit ! event.Tick()
    
    assert(testUnit.underlyingActor.direction == (1,0))
    
    testUnit ! event.Order(Move(5,5))
    
    testUnit ! event.Tick()
    testUnit ! event.Tick()
    testUnit ! event.Tick()
    
    // direction should be the same since a turn cooldown is active
    assert(testUnit.underlyingActor.direction == (1,0))
  }
}