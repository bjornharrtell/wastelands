package org.wololo.wastelands

class Event

case object Connect extends Event
case object Disconnect extends Event
case object Stop extends Event
case object Shutdown extends Event