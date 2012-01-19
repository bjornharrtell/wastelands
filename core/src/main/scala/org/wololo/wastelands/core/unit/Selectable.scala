package org.wololo.wastelands.core.unit

trait Selectable extends AbstractUnit {
  var selected = false
  def select() { selected = true }
  def unselect() { selected = false }
}