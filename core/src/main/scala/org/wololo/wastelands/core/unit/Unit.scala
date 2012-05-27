package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._
import org.wololo.wastelands.core.unit.order.Move
import org.wololo.wastelands.core.unit.order.Attack
import org.wololo.wastelands.core.unit.order.Guard
import org.wololo.wastelands.core.unit.action.Fire;
import java.io.File
import org.wololo.wastelands.core.event.Event
import scala.collection.mutable.ArrayBuffer

case class TileStepEvent(val unit: Unit, val from: Coordinate, val to: Coordinate) extends Event
case class OrderEvent() extends Event

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

  var cooldowns = ArrayBuffer[Cooldown]()

  val Velocity = 0.04
  var moveDistance = 0.0
  var direction: Direction = Direction.fromTileIndex((math.random * 7 + 1).toInt)

  val Range = 2
  val AttackStrength = 2
  var alive = true
  var hp = 10
  var explode = false
  var exploding = false

  def notify(pub: Publisher, event: Event) {
    event match {
      case e: ActionCompleteEvent => onActionComplete(e.action)
      case e: CooldownCompleteEvent => onCooldownComplete(e.cooldown)
      case e: TileOccupationEvent => onTileOccupation(e)
    }
  }

  def order = _order

  /**
   * Setter for order
   * 
   * Will call dispose and trigger event.
   * 
   * NOTE: Perhaps this shouldn't be implemented as a property but for now it is to
   * avoid setting order in the wrong way.
   */
  def order_=(order: Order): scala.Unit = {
    _order.dispose()
    _order = order
    publish(new OrderEvent())
  }

  /**
   * Handler for ActionComplete events
   *
   * If the current order is changed from the one initiating the completed action and
   * if the new order action has no active cooldown generate a new action from the
   * order or else do nothing but set the current action to None.
   */
  def onActionComplete(action: Action) {
    if (action.CooldownTicks > 0) cooldowns += new Cooldown(action)

    val localOrder = order

    if (localOrder != action.order && cooldowns.forall(p => !p.action.isInstanceOf[localOrder.generatesAction])) {
      this.action = order.generateAction()
      if (this.action.isEmpty) guard()
    } else {
      this.action = None;
    }
  }

  /**
   * Handler for CooldownComplete events
   *
   * 
   * If the unit has the same order as the action causing this cooldown generate a new action from the order
   */
  def onCooldownComplete(cooldown: Cooldown) {
    cooldowns -= cooldown
    
    if (order == cooldown.action.order) {
      action = order.generateAction()

      if (action.isEmpty) guard()
    }
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
    if (cooldowns.forall(p => !p.action.isInstanceOf[org.wololo.wastelands.core.unit.action.Move])) {
      order = new Move(this, position)
      if (action.isEmpty) action = order.generateAction()
    }
  }

  def moveTileStep() {
    moveDistance = 0.0

    val oldPosition = position.clone()

    position += direction

    publish(new TileStepEvent(this, oldPosition, position))
  }

  def attack(target: Unit) {
    if (cooldowns.forall(p => !p.action.isInstanceOf[Fire])) {
      order = new Attack(this, target)
      if (action.isEmpty) action = order.generateAction()
    }
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