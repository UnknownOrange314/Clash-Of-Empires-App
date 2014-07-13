package engine.rts.model

import server.model.playerData.Player

object TroopDef{
    val DEAD_STATE="dead"
    val FREEZE_STATE="noMove"
    val NORMAL_STATE="canMove"
}

class TroopDef(val owner:Player) {
    var myState=TroopDef.NORMAL_STATE
    def die()=myState=TroopDef.DEAD_STATE
    def canMove()=(myState==TroopDef.NORMAL_STATE)
}
