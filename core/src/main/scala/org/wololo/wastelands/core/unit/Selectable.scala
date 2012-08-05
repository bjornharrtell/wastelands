package org.wololo.wastelands.core.unit

trait Selectable {
  var selected = false
  def select() = selected = true 
  def unselect() = selected = false
}