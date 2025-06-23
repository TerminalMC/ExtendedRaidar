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

package dev.terminalmc.extendedraidar.gui.screen;

import dev.terminalmc.extendedraidar.config.Config;
import dev.terminalmc.extendedraidar.config.Config.Options;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;

import static dev.terminalmc.extendedraidar.util.Localization.localized;

public class ClothScreenProvider {

    /**
     * Builds and returns a Cloth Config options screen.
     *
     * @param parent the current screen.
     * @return a new options {@link Screen}.
     * @throws NoClassDefFoundError if the Cloth Config API mod is not available.
     */
    static Screen getConfigScreen(Screen parent) {
        Config.Options options = Config.options();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(localized("name"))
                .setSavingRunnable(Config::save);
        ConfigEntryBuilder eb = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(localized("option", "general"));

        general.addEntry(eb.startBooleanToggle(
                        localized("option", "general.modEnabled"),
                        options.modEnabled
                )
                .setDefaultValue(Options.modEnabledDefault)
                .setSaveConsumer(val -> options.modEnabled = val)
                .build());

        general.addEntry(eb.startIntSlider(
                        localized("option", "general.highlightDistance"),
                        options.highlightDistance,
                        48,
                        256
                )
                .setDefaultValue(Options.highlightDistanceDefault)
                .setSaveConsumer(val -> options.highlightDistance = val)
                .setTextGetter(val -> localized("option", "general.highlightDistance.value", val))
                .build());

        general.addEntry(eb.startIntSlider(
                        localized("option", "general.highlightDuration"),
                        options.highlightDuration,
                        3,
                        60
                )
                .setDefaultValue(Options.highlightDurationDefault)
                .setSaveConsumer(val -> options.highlightDuration = val)
                .setTextGetter(val -> localized("option", "general.highlightDuration.value", val))
                .build());

        return builder.build();
    }
}
