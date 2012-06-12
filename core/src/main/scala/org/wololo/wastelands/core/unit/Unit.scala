package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._
import org.wololo.wastelands.core.unit.order.Move
import org.wololo.wastelands.core.unit.order.Attack
import org.wololo.wastelands.core.unit.order.Guard
import org.wololo.wastelands.core.unit.action.MoveStep
import org.wololo.wastelands.core.unit.action.Turn
import org.wololo.wastelands.core.unit.action.Fire
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
   */
  def onActionComplete(action: Action) {
    if (action.CooldownTicks > 0) cooldowns += new Cooldown(action)

    val potentialAction = order.generateAction();
    
    if (potentialAction.isDefined && order.Type != action.order.Type && cooldowns.forall(p => !(p.action.Type == potentialAction.get.Type))) {
      this.action = potentialAction
      this.action.get.execute()
    } else {
      this.action = None
      // FIXME: guard will be issued at times when it should not be...
      if (action.CooldownTicks == 0) guard()
    }
  }

  /**
   * Handler for CooldownComplete events
   */
  def onCooldownComplete(cooldown: Cooldown) {
    cooldowns -= cooldown
    
    if (order.Type == cooldown.action.order.Type) {
      action = order.generateAction()
      if (action.isDefined) action.get.execute()
    }

    // FIXME: guard will be issued at times when it should not be...
    if (action.isEmpty) guard()
  }

  def onTileOccupation(e: TileOccupationEvent) {
    order = new Attack(this, e.unit)
  }

  /**
   * Issue guard order
   * 
   * Check for attack conditions, if true then go to attack immediately else issue the actual guard order 
   */
  def guard() {
    for (tile <- map.surroundingTiles(position, Range)) {
      tile.unit match {
        case Some(unit) if unit.player != player => return attack(tile.unit.get)
        case _ =>
      }
    }

    order = new Guard(this)
  }

  /**
   * Issue move order
   */
  def moveTo(position: Coordinate) {
    order = new Move(this, position)
    
    // generate action if there is no active Action or Cooldown for MoveStep or Turn
    if (action.isEmpty && 
        cooldowns.forall(p => !p.action.isInstanceOf[MoveStep]) &&
        cooldowns.forall(p => !p.action.isInstanceOf[Turn])) {
      action = order.generateAction()
      if (action.isDefined) action.get.execute()
    }
  }

  /**
   * Logic to initiate a tile step move
   * 
   * Note that the position is changed when the move is initiated
   */
  def moveTileStep() {
    moveDistance = 0.0

    val oldPosition = position.clone()

    position += direction

    publish(new TileStepEvent(this, oldPosition, position))
  }

  /**
   * Issue attack order
   */
  def attack(target: Unit) {
    order = new Attack(this, target)
    
    // generate action if there is no active Action or Cooldown for Fire, MoveStep or Turn
    // TODO: cooldown check should be based on the potential action to be generated
    if (action.isEmpty &&
        cooldowns.forall(p => !p.action.isInstanceOf[Fire]) &&
        cooldowns.forall(p => !p.action.isInstanceOf[MoveStep]) &&
        cooldowns.forall(p => !p.action.isInstanceOf[Turn])) {
      action = order.generateAction()
      if (action.isDefined) action.get.execute()
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