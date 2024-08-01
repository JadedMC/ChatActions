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
package net.jadedmc.chatactions.utils;

import net.jadedmc.chatactions.ChatActionsPlugin;
import net.jadedmc.chatactions.actions.Action;
import net.jadedmc.chatactions.actions.ActionCMD;
import org.bukkit.command.CommandMap;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A collection of methods useful for Dynamically registering commands.
 */
public class CommandUtils {
    private static ChatActionsPlugin plugin = null;
    private static Field bukkitCommandMap = null;
    private static CommandMap commandMap = null;

    /**
     * Registers the utility.
     * @param pl Instance of the plugin.
     */
    public CommandUtils(@NotNull final ChatActionsPlugin pl) {
        plugin = pl;

        // Allow editing bukkit's command map dynamically using Reflection.
        try {
            bukkitCommandMap = pl.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            commandMap = (CommandMap) bukkitCommandMap.get(pl.getServer());
        }
        catch (IllegalAccessException | NoSuchFieldException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Registers the command for a given action.
     * @param action Action to create the command for.
     */
    public static void registerCommand(@NotNull final Action action) {
        commandMap.register(action.getName(), new ActionCMD(plugin, action.getName(), action));
    }

    /**
     * Update the command map for all online players, allowing players to see commands added in the "SHOW" Rule Type.
     */
    public static void syncCommands() {
        try {
            final Method syncCommandsMethod = plugin.getServer().getClass().getDeclaredMethod("syncCommands");
            syncCommandsMethod.invoke(plugin.getServer());
        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
            exception.printStackTrace();
        }
    }
}