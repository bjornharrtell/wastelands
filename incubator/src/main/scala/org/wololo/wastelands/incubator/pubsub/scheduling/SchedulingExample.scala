package org.wololo.wastelands.incubator.scheduling

import compat.Platform
import org.wololo.wastelands.incubator.pubsub._
import java.util.concurrent.{ScheduledExecutorService, TimeUnit, Executors}
import scheduling.oxbow.{Interval, Schedules, FixedDelayStrategy}
import org.wololo.wastelands.incubator.pubsub.scheduling.oxbow._

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

object EventScheduler extends ActionEventPublisher with Intervals with Schedules with JavaCalendars with FixedRateStrategy {

  val nThreads = 5

  def publishMuu = publish(new MuuEvent)

  def scheduleActionEvent(actionEvent: ActionEvent, delayedInMs: Long) {
    schedule(publishMuu) onceIn delayedInMs.millis
    
    /*new Thread(new Runnable() {
      override def run() {
          Thread.sleep(delayedInMs)
          publish(actionEvent)
      }
    }).start();*/
  }
}