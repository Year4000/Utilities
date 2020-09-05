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

@Module(id = "a")
public class ModuleA {
    @Inject private ModuleD moduleD;
    //@Inject private Settings<ModuleASettings> settings;

    public ModuleA() {
        System.out.println("ModuleA Constructor");
    }

    @Load
    public void loasdas() {
        System.out.println("ModuleA loader");
        //System.out.println(settings.instance().setting);
    }

    @SettingsBase("a")
    public static class ModuleASettings {
        public ModuleASettings() {
            System.out.println("ModuleASettings Constructor");
        }

        @Comment("This is a comment")
        private String setting = "default values";
    }
}
