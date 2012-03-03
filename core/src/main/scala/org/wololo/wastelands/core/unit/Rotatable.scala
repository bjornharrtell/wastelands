package org.wololo.wastelands.core.unit

/**
 * @deprecated in favor of order/action stuff
 */
trait Rotatable extends Tickable {
  self: Unit =>

  val RotatePauseTicks = 30
  private var rotatePauseTicksCounter = RotatePauseTicks

  var targetDirection: Direction = direction.clone

  override def tick(): Unit = {
    if (direction != targetDirection) tickRotate

    super.tick
  }

  def tickRotate() {
    rotatePauseTicksCounter -= 1;

    if (rotatePauseTicksCounter == 0) {
      direction.turnTowards(targetDirection)

      rotatePauseTicksCounter = RotatePauseTicks;
    }
  }
}