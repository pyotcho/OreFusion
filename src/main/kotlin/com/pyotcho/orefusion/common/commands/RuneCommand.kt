package com.pyotcho.orefusion.common.commands

import com.pyotcho.orefusion.OreFusion
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class RuneCommand : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>?
    ): Boolean {
        if (sender !is Player)
            return false

        val player = sender as Player

        if (args!!.isEmpty()) {
            player.sendMessage("§cUsage: /rune give <player> <rune>")
            OreFusion.logger.info("Player ${player.name} executed command: without arguments")
            return true
        }

        if (args.size != 3) {
            player.sendMessage("§cUsage: /rune give <player> <rune>")
            OreFusion.logger.info("Player ${player.name} executed command: not enough arguments")
            return true
        }

        if (!player.hasPermission("rune.give")) {
            player.sendMessage("§cYou don't have permission to do this")
            OreFusion.logger.info("Player ${player.name} executed command: don't have permission")
            return true
        }

        if (args[0] != "give") {
            player.sendMessage("§cUsage: /rune give <player> <rune>")
            OreFusion.logger.info("Player ${player.name} executed command: without give")
            return true
        }

        val target = player.server.getPlayer(args[1])
        if (target == null) {
            player.sendMessage("§cPlayer ${args[1]} not found")
            OreFusion.logger.info("Player ${player.name} executed command: target player not found")
            return true
        }

        val rune = args[2] == OreFusion.runeBook.getKeyName()
        if (rune) {
            target.inventory.addItem(OreFusion.runeBook.getItem()!!)
            player.sendMessage("§aPlayer ${target.name} got the 'Rune: VeinSmelt'")
        } else {
            player.sendMessage("§cThis rune doesn't exist")
            OreFusion.logger.info("Player ${player.name} executed command: rune not found")
        }

        return true
    }
}