package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._
import org.wololo.wastelands.core.unit.order.Move
import org.wololo.wastelands.core.unit.order.Attack
import org.wololo.wastelands.core.unit.order.Guard
import java.io.File

class TileStepEvent(val unit: Unit, val from: Coordinate, val to: Coordinate) extends Event
class OrderEvent() extends Event

/**
 * Base abstract implementation for units
 */
abstract class Unit(val game: Game, val player: Int, val position: Coordinate) extends Selectable with Publisher with Subscriber {
  type Pub = Unit

  val fireSound = game.vmContext.soundFactory.create(new File("sounds/laser.ogg"))
  val explodeSound = game.vmContext.soundFactory.create(new File("sounds/explosion.ogg"))

  var isOnScreen = false
  val ScreenBounds: Rect = (0, 0, 0, 0)

  val map = game.map

  map.tiles(position).unit = Option(this)

  private var _order: Order = new Guard(this)
  var action: Option[Action] = None

  val Velocity = 0.04
  var moveDistance = 0.0
  var direction: Direction = (math.random * 7 + 1).toInt

  val Range = 2
  val AttackStrength = 2
  var alive = true
  var hp = 10
  var explode = false
  var exploding = false

  def notify(pub: Publisher, event: Event) {
    event match {
      case e: ActionCompleteEvent => onActionComplete()
      case e: TileOccupationEvent => onTileOccupation(e)
    }
  }

  def order = _order

  def order_=(order: Order): scala.Unit = {
    _order.dispose()
    _order = order
    publish(new OrderEvent())
  }

  def onActionComplete() {
    action = order.generateAction()

    if (action.isEmpty) guard()
  }

  def onTileOccupation(e: TileOccupationEvent) {
    order = new Attack(this, e.unit)
  }

  def guard() {
    for (tile <- map.surroundingTiles(position, Range)) {
      tile.unit match {
        case Some(unit) if unit.player != player => return attack(tile.unit.get)
        case _ =>
      }
    }

    order = new Guard(this)
  }

  def moveTo(position: Coordinate) {
    order = new Move(this, position)
    if (action.isEmpty) action = order.generateAction()
  }

  def moveTileStep() {
    moveDistance = 0.0

    val oldPosition = position.clone()

    position += direction

    publish(new TileStepEvent(this, oldPosition, position))
  }

  def attack(target: Unit) {
    order = new Attack(this, target)
    if (action.isEmpty) action = order.generateAction()
  }

  def damage(damage: Int) {
    hp -= damage

    if (hp <= 0) kill()
  }

  def kill() {
    game.map.tiles(position).unit = None
    if (!explode && !exploding) {
      explode = true
      explodeSound.play()
    }
  }
}