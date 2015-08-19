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

package net.year4000.ducktape.modules;

import lombok.extern.java.Log;
import net.year4000.ducktape.TesterModule;
import net.year4000.ducktape.module.ModuleInfo;

@ModuleInfo(
    name = "Test Module",
    version = "1.0.0",
    description = "A test module to test the module loading system.",
    authors = {"Year4000"}
)
@Log
public class TestModule extends TesterModule {
    @Override
    public void load() {
        log.info("Loaded");
    }

    @Override
    public void enable() {
        log.info("Enabled");
    }

    @Override
    public void disable() {
        log.info("Disabled");
    }
}
