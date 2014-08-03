package net.year4000.ducktape;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.cubespace.Yamler.Config.Comment;
import net.cubespace.Yamler.Config.Config;

@Data
@EqualsAndHashCode(callSuper = false)
public class GlobalSettings extends Config {
    public GlobalSettings() {
        CONFIG_HEADER = new String[] {"DuckTape Settings"};
    }

    @Comment("The URL to get the locale files from.")
    private String url = "https://git.year4000.net/year4000/locales/raw/master/net/year4000/ducktape/locales/";

    @Comment("The path were the modules are stored.")
    private String modulesPath = "modules";

    @Comment("The path were the modules data folder is stored.")
    private String modulesData = "data";
}
