package client.controller

/**
 * This class represents a market command that is sent when a player attempts to buy or sell a resource.
 * @param instruction The instruction.
 * @param resource  The resource that is being bought or sold.
 */
class MarketCommand(val instruction: String, val resource: String) extends java.io.Serializable