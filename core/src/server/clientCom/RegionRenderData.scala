package server.clientCom

import java.awt.Polygon
import java.util.ArrayList

/**
 * TODO: Fix name.
 * This class stores data for rendering regions. Fix the name and make it more clear.
 */
class RegionRenderData(val regionBounds:ArrayList[Polygon],val xLocs:ArrayList[Integer],val yLocs:ArrayList[Integer]) extends java.io.Serializable
