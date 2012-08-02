package org.wololo.wastelands

sealed trait Event

case class Connect(id: Int) extends Event
case class Disconnect(id: Int) extends Event
case class Connected(id: Int) extends Event
case class Shutdown(id: Int) extends Event