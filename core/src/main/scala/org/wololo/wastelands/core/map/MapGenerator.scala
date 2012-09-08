package org.wololo.wastelands.core.map
import scala.collection.mutable.ArrayBuffer

class MapGenerator(width: Int, height: Int) {

  def generate(map: Array[Tile]) {
    val spice = makeLayer(0.2, -0.2)
    val filteredSpice = filter(makeLayer(0.2, -0.6), spice)
    // TODO: remove small spice deposits
    apply(filteredSpice, map, Tile.Spice)

    val dunes = makeLayer(0.1, -0.2, 5)
    val filteredDunes = filter(makeLayer(0.2, -0.2), dunes)
    apply(filteredDunes, map, Tile.Dunes)
    
    // TODO: generate rock layer
  }

  def apply(layer: ArrayBuffer[Boolean], map: Array[Tile], tileType: Int) {
    map zip layer foreach { case (tile, apply) => if (apply) tile.baseType = tileType }
  }

  def filter(filter: ArrayBuffer[Boolean], layer: ArrayBuffer[Boolean]) = {
    (layer zip filter map { case (tile, filter) => if (filter) (false, filter) else (tile, filter) } unzip)._1
  }

  def makeLayer(density: Double, threshold: Double, scaleY: Int = 1) = {
    val seed = math.random * 1000

    val layer = ArrayBuffer.fill(width * height) { false }
    for {
      y <- 0 until width;
      x <- 0 until height
    } {
      val noise = SimplexNoise.noise((x.toDouble / width / density) + seed, (y.toDouble / height / (density * scaleY)) + seed)
      if (noise > (threshold)) layer(y * width + x) = true
    }
    layer
  }
}