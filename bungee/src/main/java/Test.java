import net.year4000.ducktape.bungee.module.BungeeModule;
import net.year4000.ducktape.core.module.ModuleInfo;

@ModuleInfo(
    name = "Test",
    version = "0.0.1",
    description = "test",
    authors = {"Year4000"}
)
public class Test extends BungeeModule {
    public Test() {
        System.out.println("init");
        System.out.println(getModuleInfo().name());
    }

    public void load() {
        System.out.println("loaded");
    }

    public void enable() {
        System.out.println("enabled");
    }

    public void disable() {
        System.out.println("disable");
    }
}
