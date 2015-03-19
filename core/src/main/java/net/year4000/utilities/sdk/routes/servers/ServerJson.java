package net.year4000.utilities.sdk.routes.servers;

import lombok.Value;
import net.year4000.utilities.Pinger;

@Value
public class ServerJson {
    private String name;
    private Group group;
    private Pinger.StatusResponse status;

    /** Is this server hidden */
    public boolean isHidden() {
        return name.startsWith(".") || getGroup().isHidden();
    }

    @Value
    public static class Group {
        private String name;
        private String display;

        /** Is this server hidden */
        public boolean isHidden() {
            return name.startsWith(".");
        }
    }
}
