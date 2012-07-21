package org.wololo.wastelands.core

import org.wololo.wastelands.core.event.Event

trait Subscriber extends scala.collection.mutable.Subscriber[Event, Publisher] {
  
}