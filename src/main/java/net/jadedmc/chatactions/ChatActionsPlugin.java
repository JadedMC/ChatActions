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
package net.jadedmc.chatactions;

import net.jadedmc.chatactions.actions.ActionManager;
import net.jadedmc.chatactions.utils.ChatUtils;
import net.jadedmc.chatactions.utils.CommandUtils;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChatActionsPlugin extends JavaPlugin {
    private HookManager hookManager;
    private ConfigManager configManager;
    private ActionManager actionManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        new CommandUtils(this);

        new Metrics(this, 21818);
        configManager = new ConfigManager(this);
        hookManager = new HookManager();
        actionManager = new ActionManager(this);

        // Enables ChatUtils.
        ChatUtils.enable(this);
    }

    @Override
    public void onDisable() {
        ChatUtils.disable();
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public HookManager getHookManager() {
        return hookManager;
    }
}