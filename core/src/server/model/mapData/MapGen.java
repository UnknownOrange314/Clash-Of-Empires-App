package server.model.mapData;


import java.util.ArrayList;
import server.model.playerData.Region;

interface MapGen {
	ArrayList<Region> generateMap(GameMap m);
}