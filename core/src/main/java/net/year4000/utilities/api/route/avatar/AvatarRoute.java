package net.year4000.utilities.api.route.avatar;

import net.year4000.utilities.api.APIManager;
import net.year4000.utilities.api.Route;
import net.year4000.utilities.api.routedata.Avatar;

public class AvatarRoute extends Route<Avatar> {

    private String name;
    private boolean hat;
    private int size;

    public AvatarRoute(String name, boolean hat, int size) {
        super(APIManager.getRouteUrl("avatar"), Avatar.class);
        this.name = name;
        this.hat = hat;
        this.size = size;
    }

    public AvatarRoute(String name) {
        this(name, false, 16);
    }

    public AvatarRoute(String name, boolean hat) {
        this(name, hat, 16);
    }

    public AvatarRoute(String name, int size) {
        this(name, false, size);
    }

    @Override
    public String getExtraData() {
        return name + "/" + size + "?json" + (hat ? "&hat" : "");
    }
}
