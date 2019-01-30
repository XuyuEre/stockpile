package notjoe.stockpile.block

import net.minecraft.state.property.BooleanProperty
import net.minecraft.util.math.Direction

object StockpileProperties {
  final val IsOpen = BooleanProperty.create("is_open")

  object Connection {
    final val North = BooleanProperty.create("north")
    final val South = BooleanProperty.create("south")
    final val East = BooleanProperty.create("east")
    final val West = BooleanProperty.create("west")
    final val Up = BooleanProperty.create("up")
    final val Down = BooleanProperty.create("down")

    final val All = Array(North, South, East, West, Up, Down)

    def propertyForDirection(d: Direction): BooleanProperty = d match {
      case Direction.NORTH => North
      case Direction.SOUTH => South
      case Direction.EAST => East
      case Direction.WEST => West
      case Direction.UP => Up
      case Direction.DOWN => Down
    }
  }

}
