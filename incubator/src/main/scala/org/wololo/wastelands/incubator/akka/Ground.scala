package org.wololo.wastelands.incubator.akka

sealed trait Ground
sealed trait ImpassableGround extends Ground
sealed trait PassableGround extends Ground

case object Rock extends PassableGround
case object Sand extends PassableGround
case object Spice extends PassableGround
case object Dune extends PassableGround
case object Hill extends ImpassableGround