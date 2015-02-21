package net.year4000.utilities.api.route.accounts;

import net.year4000.utilities.api.APIManager;
import net.year4000.utilities.api.Route;
import net.year4000.utilities.api.routedata.Account;

import java.util.UUID;

public class AccountRoute extends Route<Account> {

    String id;

    public AccountRoute(String username) {
        super(APIManager.getRouteUrl("accounts"), Account.class);
        id = username;
    }

    public AccountRoute(UUID uuid) {
        this(uuid.toString());
    }

    @Override
    public String getExtraData() {
        return "/" + id;
    }
}
