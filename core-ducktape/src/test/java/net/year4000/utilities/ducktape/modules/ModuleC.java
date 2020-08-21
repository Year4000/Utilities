/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape.modules;

import com.google.inject.Inject;
import net.year4000.utilities.ducktape.module.Load;
import net.year4000.utilities.ducktape.module.Module;
import net.year4000.utilities.ducktape.settings.Comment;
import net.year4000.utilities.ducktape.settings.Settings;
import net.year4000.utilities.ducktape.settings.SettingsBase;

@Module(id = "c")
public class ModuleC {
    @Inject private Settings<ModuleCSettings> settings;

    public ModuleC() {
        System.out.println("ModuleC Constructor");
    }

    @Load
    public void loasdas() {
        System.out.println("ModuleC loader");
        System.out.println(settings.instance().setting);
    }

    @SettingsBase("c")
    public static class ModuleCSettings {
        public ModuleCSettings() {
            System.out.println("ModuleCSettings Constructor");
        }

        @Comment("This is a comment")
        private String setting = "default value";
    }
}
