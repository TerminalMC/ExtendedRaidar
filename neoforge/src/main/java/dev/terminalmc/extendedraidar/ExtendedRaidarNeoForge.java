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

import dev.terminalmc.extendedraidar.gui.screen.ConfigScreenProvider;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(
        value = ExtendedRaidar.MOD_ID,
        dist = Dist.CLIENT
)
@EventBusSubscriber(
        modid = ExtendedRaidar.MOD_ID,
        bus = EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public class ExtendedRaidarNeoForge {

    public ExtendedRaidarNeoForge() {
        // Register config screen
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (mc, parent) -> ConfigScreenProvider.getConfigScreen(parent)
        );

        // Initialize client
        ExtendedRaidar.init();
    }

    /**
     * Registers all keybinds.
     */
    @SubscribeEvent
    static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        ExtendedRaidar.KEYBINDS.forEach(event::register);
    }

    @EventBusSubscriber(
            modid = ExtendedRaidar.MOD_ID,
            value = Dist.CLIENT
    )
    static class ClientEventHandler {

        /**
         * Registers client after-tick event.
         */
        @SubscribeEvent
        public static void registerAfterClientTick(ClientTickEvent.Post event) {
            ExtendedRaidar.afterClientTick(Minecraft.getInstance());
        }
    }
}
