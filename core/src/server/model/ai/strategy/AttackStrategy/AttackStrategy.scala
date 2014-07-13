package server.model.ai.strategy.AttackStrategy

import server.model.ai.EasyComputerPlayer
import server.model.playerData.Region
import java.util.HashSet

abstract trait AttackStrategy {
    def attackTargets(p: EasyComputerPlayer, myTargets: HashSet[Region])
}
