package com.github.dytroInc.randomrule.rules

import com.github.dytroInc.randomrule.plugin.RandomRuleSurvival
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.inventory.ItemStack

class Rule {
    var name: String = ""
    var description: String = ""
    var helpfulness: Helpfulness = Helpfulness.NEUTRAL

    var interact: (InteractData.() -> Unit)? = null

    fun name(arg: String) = run { this.name = arg }
    fun description(arg: String) = run { this.description = arg }
    fun helpful(arg: Helpfulness) = run { this.helpfulness = arg }
    fun interact(arg: (InteractData.() -> Unit)) = run { this.interact = arg }

    fun place() {
        val manager = RandomRuleSurvival.instance?.ruleManager
        if (manager != null) {
            when (helpfulness) {
                Helpfulness.GOOD -> {
                    manager.goodRules.add(this)
                }
                Helpfulness.BAD -> {
                    manager.badRules.add(this)
                }
                Helpfulness.NEUTRAL -> {
                    manager.neutralRules.add(this)
                }
            }
        }

    }


}

fun createRule(rule: Rule.() -> Unit) {
    return Rule().apply(rule).place()

}

enum class Helpfulness {
    BAD, NEUTRAL, GOOD
}

class InteractData(val action: Action, val block: Block?, val item: ItemStack?, val player: Player) {
    var isCancelled = false
    fun cancel() = run { isCancelled = true }
}