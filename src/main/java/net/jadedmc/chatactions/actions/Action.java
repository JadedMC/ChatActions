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

import net.jadedmc.chatactions.utils.CommandUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;

/**
 * Represents an Action that can be performed by a player.
 */
public class Action {
    private final Collection<String> aliases = new HashSet<>();
    private final String senderMessage;
    private final String targetMessage;
    private final String globalMessage;
    private final String noTargetMessage;
    private final String permissionMessage;
    private final String helpMessage;
    private final String usageMessage;
    private final int range;
    private final boolean requireTarget;
    private final String requiredPermission;
    private final String name;

    /**
     * Creates an action from a given configuration.
     * @param config Configuration Section to load the action from.
     */
    public Action(@NotNull final String name, @NotNull final ConfigurationSection config) {
        this.name = name;

        // Load the command aliases from the config.
        if(config.isSet("aliases")) {
            this.aliases.addAll(config.getStringList("aliases"));
        }

        // Load the action's sender message.
        if(config.isSet("messages.sender-message")) {
            this.senderMessage = config.getString("messages.sender-message");
        }
        else {
            this.senderMessage = "";
        }

        // Load the action's target message.
        if(config.isSet("messages.target-message")) {
            this.targetMessage = config.getString("messages.target-message");
        }
        else {
            this.targetMessage = "";
        }

        // Load the global message of the action.
        if(config.isSet("messages.global-message")) {
            this.globalMessage = config.getString("messages.global-message");
        }
        else {
            this.globalMessage = "";
        }

        // Load the no-target global message of the action.
        if(config.isSet("messages.no-target-message")) {
            this.noTargetMessage = config.getString("messages.no-target-message");
        }
        else {
            this.noTargetMessage = "";
        }

        // Loads the permission message of the action.
        if(config.isSet("messages.permission-message")) {
            this.permissionMessage = config.getString("messages.permission-message");
        }
        else {
            this.permissionMessage = "";
        }

        // Loads the help message of the action.
        if(config.isSet("messages.help-message")) {
            this.helpMessage = config.getString("messages.help-message");
        }
        else {
            this.helpMessage = "";
        }

        // Loads the usage message of the action.
        if(config.isSet("messages.usage-message")) {
            this.usageMessage = config.getString("messages.usage-message");
        }
        else {
            this.usageMessage = "";
        }

        // Load the configured range.
        if(config.isSet("range")) {
            this.range = config.getInt("range");
        }
        else {
            this.range = -1;
        }

        // Get if the action requires a target.
        if(config.isSet("require-target")) {
            this.requireTarget = config.getBoolean("require-target");
        }
        else {
            this.requireTarget = true;
        }

        // Get the required permission of the action.
        if(config.isSet("required-permission")) {
            this.requiredPermission = config.getString("required-permission");
        }
        else {
            this.requiredPermission = "";
        }

        // Registers the action's command.
        CommandUtils.registerCommand(this);
    }

    /**
     * Check if a player has permission to use the action.
     * @param player Player to check.
     * @return true if they can use it, false if they cannot.
     */
    public boolean canUse(@NotNull final Player player) {
        // Makes sure actions with no permission allow everyone to use it.
        if(this.requiredPermission.isEmpty()) {
           return true;
        }

        return player.hasPermission(this.requiredPermission);
    }

    /**
     * Gets all aliases of the action.
     * @return Action aliases.
     */
    @NotNull
    public Collection<String> getAliases() {
        return this.aliases;
    }

    /**
     * Gets the global message.
     * @return Global message.
     */
    @NotNull
    public String getGlobalMessage() {
        return this.globalMessage;
    }

    /**
     * Gets the global message with placeholders parsed.
     * @param player Sender of the action.
     * @param target Target of the action.
     * @return Global message.
     */
    @NotNull
    public String getGlobalMessage(@NotNull final Player player, @NotNull final Player target) {
        return this.globalMessage.replace("%sender%", player.getName()).replace("%target%", target.getName());
    }

    /**
     * Gets the action help message.
     * @return Help message.
     */
    @NotNull
    public String getHelpMessage() {
        return this.helpMessage;
    }

    /**
     * Gets the name of the action.
     * @return Action name.
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Gets the global no-target message with placeholders parsed.
     * @param player Sender of the action.
     * @return no-target message.
     */
    @NotNull
    public String getNoTargetMessage(@NotNull final Player player) {
        return this.noTargetMessage.replace("%sender%", player.getName());
    }

    /**
     * Gets the action's permission message.
     * @return Permission message.
     */
    @NotNull
    public String getPermissionMessage() {
        return permissionMessage;
    }

    /**
     * Gets the range (in blocks) the action should work in.
     * @return Range of the action, -1 if global.
     */
    public int getRange() {
        return this.range;
    }

    /**
     * Gets the message that should be sent to the sender.
     * @return Sender message.
     */
    @NotNull
    public String getSenderMessage() {
        return this.senderMessage;
    }

    /**
     * Gets the message that should be sent to the sender, with placeholders parsed.
     * @param sender Sender of the action.
     * @param target Target of the action.
     * @return Sender message.
     */
    @NotNull
    public String getSenderMessage(@NotNull final Player sender, @NotNull final Player target) {
        return this.senderMessage.replace("%sender%", sender.getName()).replace("%target%", target.getName());
    }

    /**
     * Gets the message that should be sent to the target.
     * @return Target message.
     */
    @NotNull
    public String getTargetMessage() {
        return this.targetMessage;
    }

    /**
     * Gets the message that should be sent to the target, with placeholders parsed.
     * @param sender Sender of the action.
     * @param target Target of the action.
     * @return Target message.
     */
    @NotNull
    public String getTargetMessage(@NotNull final Player sender, @NotNull final Player target) {
        return this.targetMessage.replace("%sender%", sender.getName()).replace("%target%", target.getName());
    }

    /**
     * Gets the usage message of the action.
     * @return Usage message.
     */
    @NotNull
    public String getUsageMessage() {
        return this.usageMessage;
    }

    /**
     * Get if the action requires a specific target.
     * @return true if a target is needed, false if the target can be "all".
     */
    public boolean requiresTarget() {
        return this.requireTarget;
    }
}