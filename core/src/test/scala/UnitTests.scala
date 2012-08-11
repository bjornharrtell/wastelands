import org.junit.Test
import org.wololo.wastelands.core.unit.Direction
import org.wololo.wastelands.core.unit.TestUnit1
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

  @Test
  def TestUnit = {
    BasicTests.game = TestActorRef[Game]
    val player = TestActorRef[TestPlayer]
    val testUnit = TestActorRef(new TestUnit1(player, BasicTests.game.underlyingActor, (1,1), Direction.Directions(0)))

    assert(testUnit.underlyingActor.position == (1,1))
    
    testUnit ! event.Move(2,2)
    
    for (i <- 1 to 6000) {
      BasicTests.game.underlyingActor.ticks += 1
      testUnit ! event.Tick()
    }
    
    assert(testUnit.underlyingActor.position == (2,2))
  }
}