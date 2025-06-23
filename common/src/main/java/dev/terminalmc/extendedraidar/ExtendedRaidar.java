/*
 * Copyright 2025 TerminalMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.terminalmc.extendedraidar;

import dev.terminalmc.extendedraidar.config.Config;
import dev.terminalmc.extendedraidar.util.ModLogger;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static dev.terminalmc.extendedraidar.config.Config.options;

public class ExtendedRaidar {

    public static final String MOD_ID = "extendedraidar";
    public static final String MOD_NAME = "ExtendedRaidar";
    public static final ModLogger LOG = new ModLogger(MOD_NAME);
    public static final Component PREFIX = Component.empty()
            .append(Component.literal("[").withStyle(ChatFormatting.DARK_GRAY))
            .append(Component.literal(MOD_NAME).withStyle(ChatFormatting.GOLD))
            .append(Component.literal("] ").withStyle(ChatFormatting.DARK_GRAY))
            .withStyle(ChatFormatting.GRAY);
    public static final List<KeyMapping> KEYBINDS = List.of();

    private static @Nullable BlockPos bellPos;
    private static @Nullable Timer timer;

    public static void init() {
        Config.getAndSave();
    }

    public static void afterClientTick(Minecraft mc) {
        if (timer != null)
            timer.tick();
    }

    public static void onConfigSaved(Config config) {
        if (!config.options.modEnabled)
            clearData();
    }

    // Actual logic

    public static void clearData() {
        bellPos = null;
        timer = null;
    }

    public static void onBellRing(BlockPos bellPos) {
        if (!options().modEnabled)
            return;
        ExtendedRaidar.bellPos = bellPos;
        ExtendedRaidar.timer = new Timer(options().highlightDuration * 20);
    }

    public static boolean shouldGlow(Entity entity) {
        return bellPos != null
                && timer != null
                && timer.get() > 0
                && entity instanceof LivingEntity livingEntity
                && livingEntity.getType().is(EntityTypeTags.RAIDERS)
                && isRaiderWithinRange(livingEntity);
    }

    private static boolean isRaiderWithinRange(LivingEntity raider) {
        return raider.isAlive()
                && !raider.isRemoved()
                && bellPos != null
                && bellPos.closerToCenterThan(raider.position(), options().highlightDistance)
                && raider.getType().is(EntityTypeTags.RAIDERS);
    }

    public static class Timer {

        private int ticks;

        public Timer(int ticks) {
            this.ticks = ticks;
        }

        public int tick() {
            return ticks--;
        }

        public int get() {
            return ticks;
        }
    }
}
