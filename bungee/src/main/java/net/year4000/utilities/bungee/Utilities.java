/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.bungee;

public class Utilities extends BungeePlugin {
    private static Utilities inst;

    public static Utilities getInst() {
        return Utilities.inst;
    }

    @Override
    public void onLoad() {
        inst = this;
        Messages.Factory.inst.get(); // Trigger a download from server now so it can cache it for later
    }
}
