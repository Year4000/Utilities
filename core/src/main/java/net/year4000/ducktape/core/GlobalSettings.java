package net.year4000.ducktape.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.cubespace.Yamler.Config.Comment;
import net.cubespace.Yamler.Config.Config;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class GlobalSettings extends Config {

    @Comment("The path were the modules are stored.")
    private String modulesPath = "modules";

    @Comment("The modules that should be enabled by default")
    List<String> enabledModules = new ArrayList<String>() {{

    }};
}
