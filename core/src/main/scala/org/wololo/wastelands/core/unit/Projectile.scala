package org.wololo.wastelands.core.unit

class Projectile(from: Unit, to:Unit) {
	val Ticks = 10
	var ticks = Ticks
	
	var distance = 1.0
	
	def tick() {
	  ticks -= 1
	  
	  distance -= ticks/Ticks
	}
}