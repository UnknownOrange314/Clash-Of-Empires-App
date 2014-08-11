package client.controller

/**
 * This class represents the data sent when a player clicks a button on the region interface.
 */
class RegionCommand(val command:String,val owner:Int,val name:String) extends java.io.Serializable