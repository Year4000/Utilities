/*
 * Copyright 2015 Year4000.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

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
