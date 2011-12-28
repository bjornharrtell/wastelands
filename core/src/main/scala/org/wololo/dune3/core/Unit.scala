package org.wololo.dune3.core

class Unit (map: Map, startX: Int, startY: Int) {

  var x = startX
  var y = startY
  
  map.removeShade(x, y)
  
  def render() {
    
  }
  
  def move(dx: Int, dy: Int) {
    if (x>=map.Width) return
    
    x += dx
    y += dy
    
    map.removeShade(x, y)
  }
}