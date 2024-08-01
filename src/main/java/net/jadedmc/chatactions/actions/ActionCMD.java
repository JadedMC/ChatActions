/*
 * This file is part of ChatActions, licensed under the MIT License.
 *
 *  Copyright (c) JadedMC
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package net.jadedmc.chatactions.actions;

import net.jadedmc.chatactions.ChatActionsPlugin;
import net.jadedmc.chatactions.utils.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Represents the command that a player is to run when they want to use an action.
 */
public class ActionCMD extends BukkitCommand {
    private final ChatActionsPlugin plugin;
    private final Action action;

    /**
     * Creates the action command.
     * @param plugin Instance of the plugin.
     * @param commandName Name of the command.
     * @param action Associated action.
     */
    public ActionCMD(@NotNull final ChatActionsPlugin plugin, @NotNull final String commandName, @NotNull final Action action) {
        super(commandName, "", "", new ArrayList<>(action.getAliases()));
        this.plugin = plugin;
        this.action = action;
    }

    /**
     * Runs when the command is executed.
     * @param commandSender Sender of the command.
     * @param label Command label.
     * @param args Arguments of the command.
     * @return true.
     */
    @Override
    public boolean execute(@NotNull final CommandSender commandSender, @NotNull final String label, @NotNull final String[] args) {
        // Make sure the sender is a player.
        if(!(commandSender instanceof Player player)) {
            ChatUtils.chat(commandSender, "<red>Only players can use that command!");
            return true;
        }

        // Make sure the player has permission to use the command.
        if(!action.canUse(player)) {
            ChatUtils.chat(player, action.getPermissionMessage());
            return true;
        }

        // Make sure the sender is using the command properly.
        if(args.length == 0) {
            ChatUtils.chat(player, action.getUsageMessage());
            return true;
        }


        // Check if the target is everyone.
        if(!action.requiresTarget() && args[0].equalsIgnoreCase("all")) {
            final String globalMessage = this.action.getNoTargetMessage(player);

            // Display the global message to all.
            for(final Player viewer : plugin.getServer().getOnlinePlayers()) {
                ChatUtils.chat(viewer, globalMessage);
            }
        }
        else {
            final Player target = plugin.getServer().getPlayer(args[0]);

            // Make sure the target player is online.
            if(target == null) {
                ChatUtils.chat(player, "<red><bold>Error</bold> <dark_gray>» <red>That player is not online!");
                return true;
            }

            // Makes sure the target and sender aren't the same player.
            if(target.equals(player)) {
                ChatUtils.chat(player, "<red><bold>Error</bold> <dark_gray>» <red>You cannot send that to yourself!");
                return true;
            }

            // Sends the proper messages to the sender and target.
            ChatUtils.chat(player, this.action.getSenderMessage(player, target));
            ChatUtils.chat(target, this.action.getTargetMessage(player, target));

            // If a global message is configured, sends it.
            if(!this.action.getGlobalMessage().isEmpty()) {
                final String globalMessage = this.action.getGlobalMessage(player, target);
                for(final Player viewer : plugin.getServer().getOnlinePlayers()) {
                    // Skip if the player already received a message.
                    if(viewer.equals(player) || viewer.equals(target)) {
                        continue;
                    }

                    ChatUtils.chat(viewer, globalMessage);
                }
            }
        }

        return true;
    }
}