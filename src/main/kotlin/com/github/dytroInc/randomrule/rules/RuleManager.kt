package com.github.dytroInc.randomrule.rules

import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import java.text.DecimalFormat
import kotlin.random.Random.Default.nextInt

class RuleManager(private val instance: JavaPlugin) : Listener {


    // SETTINGS 설정

    private val display = DecimalFormat("0.0")

    private val badRulesWeight = 5
    private val neutralRulesWeight = 4
    private val goodRulesWeight = 1

    private val intermissionTime = 5f
    private val mainTime = 180f


    val badRules = arrayListOf<Rule>()
    val neutralRules = arrayListOf<Rule>()
    val goodRules = arrayListOf<Rule>()

    private var intermission = true
    private var time = intermissionTime

    private var currentRule: Rule? = null
    private val bossBar = BossBar.bossBar(
        Component.text(""),
        1f,
        BossBar.Color.YELLOW,
        BossBar.Overlay.NOTCHED_12
    )


    fun start() {
        Rules.createBad()
        Rules.createNeutral()
        Rules.createGood()
        val totalWeight = badRulesWeight + neutralRulesWeight + goodRulesWeight
        object : BukkitRunnable() {
            override fun run() {
                time -= 0.1f
                Bukkit.getOnlinePlayers().forEach {
                    it.showBossBar(bossBar)
                }
                if (time < 0) {
                    intermission = !intermission
                    time = if (intermission) intermissionTime else mainTime
                    currentRule = if (intermission) null else when (nextInt(totalWeight)) {
                        in 0 until goodRulesWeight -> {
                            goodRules.random()
                        }
                        in 0 until neutralRulesWeight + goodRulesWeight -> {
                            neutralRules.random()
                        }
                        else -> {
                            badRules.random()
                        }
                    }
                    if (!intermission) {
                        Bukkit.getOnlinePlayers().forEach {
                            it.sendMessage("${ChatColor.GOLD}${currentRule?.name}${ChatColor.WHITE} - ${ChatColor.AQUA}${currentRule?.description}")
                        }
                    }
                }
                if (intermission) {
                    bossBar.name(
                        Component.text("휴식 시간 | ${display.format(time)}초")
                    )
                    bossBar.progress(time / intermissionTime)

                } else if (!intermission) {
                    bossBar.name(
                        Component.text("현재 룰: ${currentRule?.name} | ${display.format(time)}초")
                    )
                    bossBar.progress(time / mainTime)
                }
            }

        }.runTaskTimer(instance, 0, 2)
    }


    @EventHandler
    fun interact(e: PlayerInteractEvent) {
        currentRule?.let {
            it.interact?.let { interact ->
                val data = InteractData(
                    e.action,
                    e.clickedBlock,
                    e.item,
                    e.player
                )
                interact(data)
                e.isCancelled = data.isCancelled
            }
        }
    }

}