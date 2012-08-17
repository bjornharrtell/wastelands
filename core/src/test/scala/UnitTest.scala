import org.junit.Test
import org.wololo.wastelands.core.unit._
import org.wololo.wastelands.core.Game
import org.wololo.wastelands.core.Coordinate
import org.wololo.wastelands.core.GameState
import org.wololo.wastelands.core.Player
import org.wololo.wastelands.core.event
import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import akka.testkit.TestKit
import org.specs2.mutable._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.wololo.wastelands.core.GamePlayerState
import org.specs2.specification.Scope

class TestClient extends GamePlayerState {

}

@RunWith(classOf[JUnitRunner])
class UnitTest extends TestKit(ActorSystem("test")) with Specification {

  "A unit at [1,1] with direction [0,-1]" should {
    "be at [2,2] after ticks" in new testgame {

      testUnit1 ! event.Order(Move(2, 2))

      // tick three turn lengths + 1 to trigger MoveTileStep
      val cooldownlength = new Cooldown(Turn(new Direction(0,0)), 0).length
      for (i <- 1 to (cooldownlength + 1) * 3) {
        game ! event.Tick()
        player ! event.Tick()
      }

      testUnit1.underlyingActor.state.position must_== new Coordinate(2, 2)
    }
  }

  "A unit at [1,1] with direction [0,-1]" should {
    "stay at [1,1] after ticks because destination is occupied" in new testgame {

      testUnit1 ! event.Order(Move(1, 2))

      // tick three turn lengths + 1 to trigger MoveTileStep
      // TODO: should read turn/move length from metadata
      for (i <- 1 to (15 * 100 + 1) * 3) {
        game ! event.Tick()
        player ! event.Tick()
      }

      testUnit1.underlyingActor.state.position must_== new Coordinate(1, 1)
    }
  }

  "Ordering a unit to move during cooldown" should {
    "result in no direction change" in new testgame {

      testUnit1 ! event.Order(Move(2, 1))

      testUnit1 ! event.Tick()
      testUnit1 ! event.Tick()
      testUnit1 ! event.Tick()
      
      testUnit1.underlyingActor.state.direction must_== new Direction(1, -1)

      testUnit1 ! event.Order(Move(5, 5))

      testUnit1 ! event.Tick()
      testUnit1 ! event.Tick()
      testUnit1 ! event.Tick()

      testUnit1.underlyingActor.state.direction must_== new Direction(1, -1)
    }
  }

  trait testgame extends Scope {
    val game = TestActorRef[Game]
    val testClient = new TestClient()
    val player = TestActorRef(new Player(testClient))
    player.underlyingActor.join(game)
    val testUnit1State = new ServerUnitState(player, game.underlyingActor, UnitTypes.TestUnit1, (1, 1), (0, -1))
    val testUnit1 = TestActorRef(new TestUnit1(testUnit1State))
    game.underlyingActor.units += (testUnit1 -> testUnit1State)
    player ! event.UnitCreated(testUnit1, game, testUnit1.underlyingActor.unitType, testUnit1State.position, testUnit1State.direction)
    val testUnit2State = new ServerUnitState(player, game.underlyingActor, UnitTypes.TestUnit2, (1, 2), (0, -1))
    val testUnit2 = TestActorRef(new TestUnit2(testUnit2State))
    game.underlyingActor.units += (testUnit2 -> testUnit2State)
    player ! event.UnitCreated(testUnit2, game, testUnit2.underlyingActor.unitType, testUnit2State.position, testUnit2State.direction)
  }
}
