package org.wololo.wastelands.incubator.scheduling

import compat.Platform
import java.util.concurrent.{TimeUnit, Executors}
import org.wololo.wastelands.incubator.pubsub._

object SchedulingExample extends App {
  override def main(args: Array[String]) {
    val world = new World

    val cow = new Cow

    world.subscribe(cow)

    EventScheduler.subscribe(cow)

    //timed events
    EventScheduler.scheduleActionEvent(new MuuEvent, 1000)
    EventScheduler.scheduleActionEvent(new MuuEvent, 3000)
    EventScheduler.scheduleActionEvent(new MuuEvent, 6000)
    EventScheduler.scheduleActionEvent(new MuuEvent, 9000)
    //EventScheduler.publishMuu

    world.begin();
  }
}

//TODO: find out how to use a real scheduler so we don't need to spawn a new thread for each event.
object EventScheduler extends ActionEventPublisher {

  def publishMuu = publish(new MuuEvent)

  def scheduleActionEvent(actionEvent: ActionEvent, delayedInMs: Long) {
    new Thread(new Runnable() {
      override def run() {
          Thread.sleep(delayedInMs)
          publish(actionEvent)
      }
    }).start();
  }

}