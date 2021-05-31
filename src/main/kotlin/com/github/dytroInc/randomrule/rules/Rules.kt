package com.github.dytroInc.randomrule.rules

import org.bukkit.Material
import org.bukkit.event.block.Action
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.random.Random.Default.nextInt

object Rules {
    fun createGood() {
        createRule {
            name("연금술사")
            description("맨손으로 점토를 우클릭해서 금으로 만들 수 있습니다.")
            interact {
                if (action != Action.RIGHT_CLICK_BLOCK) return@interact
                block?.let {
                    if (it.type == Material.CLAY) {
                        if (item == null || item.type == Material.AIR) {
                            it.type = Material.GOLD_BLOCK
                        }

                    }
                }
            }
            helpful(Helpfulness.GOOD)
        }
    }

    fun createNeutral() {
        createRule {
            name("도박")
            description("인벤토리에 흙을 소모해서 우클릭하면 힘 2를 10초 동안 얻거나 위더 2를 10초 동안 얻을 수 있습니다.")
            interact {
                if (action != Action.RIGHT_CLICK_BLOCK && action != Action.RIGHT_CLICK_AIR) return@interact
                item?.let {

                    if (it.type == Material.DIRT) {
                        it.subtract()
                        player.addPotionEffect(
                            PotionEffect(
                                when (nextInt(2)) {
                                    0 -> PotionEffectType.INCREASE_DAMAGE
                                    else -> PotionEffectType.WITHER
                                }, 20 * 10, 1
                            )
                        )

                    }
                }
            }
            helpful(Helpfulness.NEUTRAL)
        }
    }

    fun createBad() {
        createRule {
            name("불면증")
            description("침대를 못 이용합니다")
            interact {
                if (action != Action.RIGHT_CLICK_BLOCK) return@interact
                block?.let {
                    if (it.type.name.contains("bed", true)) {
                        cancel()
                    }
                }
            }
            helpful(Helpfulness.BAD)
        }
    }
}