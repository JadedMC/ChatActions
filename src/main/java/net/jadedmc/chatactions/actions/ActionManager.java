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
import net.jadedmc.chatactions.utils.CommandUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;

/**
 * Manages the loading and existence of configured Actions.
 */
public class ActionManager {
    private final Collection<Action> loadedActions = new HashSet<>();
    private final ChatActionsPlugin plugin;

    /**
     * Creates the Action Manager.
     * @param plugin Instance of the plugin.
     */
    public ActionManager(@NotNull final ChatActionsPlugin plugin) {
        this.plugin = plugin;

        loadActions();
    }

    /**
     * Loads a given action from it's name and Configuration Section.
     * @param actionName
     * @param config
     */
    private void loadAction(@NotNull final String actionName, @NotNull final ConfigurationSection config) {
        @NotNull final Action action = new Action(actionName, config);
        loadedActions.add(action);
    }

    /**
     * Loads all the actions from config.yml.
     */
    private void loadActions() {
        final ConfigurationSection actions = plugin.getConfigManager().getConfig().getConfigurationSection("actions");

        // Make sure actions have been configured.
        if(actions == null) {
            return;
        }

        // Loop through all found actions.
        for(final String actionName : actions.getKeys(false)) {
            final ConfigurationSection actionConfig = actions.getConfigurationSection(actionName);

            // Skip the action if it's misconfigured.
            if(actionConfig == null) {
                continue;
            }

            // Loads the action.
            loadAction(actionName, actionConfig);
        }

        // Allow all the action commands to be shown in tab complete.
        CommandUtils.syncCommands();
    }
}