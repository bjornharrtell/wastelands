package org.wololo.wastelands

sealed trait Event

case class Connect() extends Event
case class Disconnect() extends Event
case class Connected() extends Event
case class Shutdown() extends Event