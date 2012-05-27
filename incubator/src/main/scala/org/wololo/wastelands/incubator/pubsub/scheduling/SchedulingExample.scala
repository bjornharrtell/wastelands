package org.wololo.wastelands.incubator.scheduling

import org.wololo.wastelands.incubator.pubsub._
import org.wololo.wastelands.incubator.pubsub.scheduling.oxbow._
import java.util.concurrent.ScheduledFuture

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

    System.exit(0)
  }
}

object EventScheduler extends ActionEventPublisher with Intervals with Schedules with JavaCalendars with FixedRateStrategy {

  val nThreads = 5

  def publishMuu = publish(new MuuEvent)

  def scheduleActionEvent(actionEvent: ActionEvent, delayedInMs: Long): ScheduledFuture[_]  = {
    schedule(publishMuu) onceIn delayedInMs.millis
  }
}