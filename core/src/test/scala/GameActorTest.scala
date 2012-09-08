import org.wololo.wastelands.core._
import org.wololo.wastelands.core.unit._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import akka.testkit.TestActorRef
import akka.testkit.TestKit
import org.specs2.mutable._
import akka.actor.ActorSystem
import org.specs2.specification.Scope
import org.wololo.wastelands.core.server.TestUnit1
import org.wololo.wastelands.core.server.GameActor
import org.wololo.wastelands.core.server.TestUnit2

@RunWith(classOf[JUnitRunner])
class GameActorTest extends TestKit(ActorSystem("test")) with Specification {

  "A game" should {
    "should handle late joins" in new testgame {

      testClient1.underlyingActor.join(game)
      
      // TODO: create units the standard way...
      
      val testUnit1 = TestActorRef(new TestUnit1(testClient1, game.underlyingActor, (1, 1), (0, -1)))
      game.underlyingActor.units = game.underlyingActor.units :+ testUnit1
      testClient1 ! event.UnitCreated(testUnit1, game, testUnit1.underlyingActor.unitType, testUnit1.underlyingActor.position, testUnit1.underlyingActor.direction)
      val testUnit2 = TestActorRef(new TestUnit2(testClient1, game.underlyingActor, (1, 2), (0, -1)))
      game.underlyingActor.units = game.underlyingActor.units :+ testUnit2
      testClient1 ! event.UnitCreated(testUnit2, game, testUnit2.underlyingActor.unitType, testUnit2.underlyingActor.position, testUnit2.underlyingActor.direction)
      
      Thread.sleep(1000)
      
      testClient2.underlyingActor.join(game)
      
      Thread.sleep(1000)
      
      testClient1.underlyingActor.units.size must_== 2
      testClient2.underlyingActor.units.size must_== 2
    }

    trait testgame extends Scope {
      val game = TestActorRef[GameActor]
      val testClient1 = TestActorRef[TestClient]
      val testClient2 = TestActorRef[TestClient]
      
    }
  }
}