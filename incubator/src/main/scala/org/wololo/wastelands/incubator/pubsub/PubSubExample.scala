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
class ActionEvent

class WorldCreateEvent extends ActionEvent

class WorldEndEvent extends ActionEvent

class MuuEvent extends ActionEvent

class ActionEventPublisher extends Publisher[ActionEvent] {
  type Pub = ActionEventPublisher
}

class World extends ActionEventPublisher {

  def begin() {
    System.out.println("This is the beginning of the world.")
    publish(new WorldCreateEvent)

 /*   for(i <- 1 until 100000){

    }*/
    this.synchronized{
      wait(10000)
    }

    System.out.println("This is the end of the world.")
    publish(new WorldEndEvent)
  }
}

class Cow extends Subscriber[ActionEvent, ActionEventPublisher] {
  def notify(pub: ActionEventPublisher, event: ActionEvent) {
    event match {
      case be: WorldCreateEvent => System.out.println("Cow realizes that it's experiencing the beginning of the world.")
      case ee: WorldEndEvent => System.out.println("Cow realizes that it's experiencing the end of the world.")
      case me: MuuEvent => System.out.println("Muuuuuu!")
    }
  }
}