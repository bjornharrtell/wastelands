package org.wololo.wastelands.incubator.pubsub

import scala.collection.mutable.Publisher
import scala.collection.mutable.Subscriber

object PubSubExample extends App {
  override def main(args: Array[String]) {
    val world = new World

    val cow = new Cow

    world.subscribe(cow)

    world.begin();
  }
}

/**
 * Events
 */
class WorldEvent

class BeginningEvent extends WorldEvent

class EndEvent extends WorldEvent

class World extends Publisher[WorldEvent] {
  type Pub = World

  def begin() {
    System.out.println("This is the beginning of the world.");
    publish(new BeginningEvent);

    for (val i <- 0 until 10000) {

    }

    System.out.println("This is the end of the world.");
    publish(new EndEvent);
  }
}

class Cow extends Subscriber[WorldEvent, World] {
  def notify(pub: World, event: WorldEvent): Unit = {
    event match {
      case be: BeginningEvent => System.out.println("Cow realizes that it's experiencing the beginning of the world.")
      case ee: EndEvent => System.out.println("Cow realizes that it's experiencing the end of the world.")
    }
  }
}