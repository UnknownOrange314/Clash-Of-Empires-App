package server.model.mapData

import java.util.ArrayList
import server.model.playerData.Region

trait MapGen {
	def generateMap(m: GameMap):ArrayList[Region]
}