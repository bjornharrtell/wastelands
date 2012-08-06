package org.wololo.wastelands

sealed trait Event
sealed trait ClientEvent extends Event
sealed trait ServerEvent extends Event
sealed trait LocalEvent extends Event

case class Connect(id: String) extends ClientEvent()
case class InfoRequest(id: String) extends ClientEvent()
case class Disconnect(id: String) extends ClientEvent()
case class Shutdown(id: String) extends ClientEvent()

case class Connected(id: String) extends ServerEvent()
case class Info(id: String, msg: String) extends ServerEvent()

case class Trigger(event: Event) extends LocalEvent()
case class Heartbeat() extends Event()