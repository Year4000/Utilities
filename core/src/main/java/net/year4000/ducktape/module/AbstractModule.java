package net.year4000.ducktape.module;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

@ModuleInfo(
    name = "AbstractModule",
    version = "internal",
    description = "Used for toString(), equals(), and hashCode()",
    authors = {"Year4000"}
)
@Getter
@Setter
public abstract class AbstractModule {
    /** Is the module enabled */
    public boolean enabled;

    /** Run when the Module is loaded */
    public void load() {}

    /** Run when the Module is enabled */
    public void enable() {}

    /** Run when the Module is disabled */
    public void disable() {}

    /** The ModuleInfo that this class is for */
    public ModuleInfo getModuleInfo() {
        return checkNotNull(getClass().getAnnotation(ModuleInfo.class));
    }

    /** Get the folder that will hold this Module's data */
    public abstract File getDataFolder();

    /** Register a command class */
    public abstract void registerCommand(Class<?> clazz);

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        AbstractModule that = (AbstractModule) object;
        ModuleInfo thisModule = getModuleInfo();
        ModuleInfo thatModule = that.getModuleInfo();

        return Objects.equal(thisModule.name(), thatModule.name()) && Objects.equal(thisModule.version(), thatModule.version());
    }

    @Override
    public int hashCode() {
        ModuleInfo module = getModuleInfo();
        return Objects.hashCode(module.name(), module.version());
    }

    @Override
    public String toString() {
        ModuleInfo module = getModuleInfo();
        return Objects.toStringHelper(this)
            .add("name", module.name())
            .add("version", module.version())
            .add("enabled", enabled)
            .toString();
    }
}
