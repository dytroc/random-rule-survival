package com.github.dytroInc.randomrule.plugin

import com.github.dytroInc.randomrule.rules.RuleManager
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Dytro
 *
 * Forked from monun/tap-sample-plugin
 */
class RandomRuleSurvival : JavaPlugin() {
    companion object {
        var instance: RandomRuleSurvival? = null
    }

    val ruleManager = RuleManager(this)

    override fun onEnable() {
        instance = this
        ruleManager.start()
        println("Started Random Rule Survival!")
        server.apply {
            pluginManager.registerEvents(ruleManager, this@RandomRuleSurvival)
        }
    }

}
