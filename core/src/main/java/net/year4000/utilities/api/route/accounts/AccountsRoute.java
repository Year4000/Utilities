package net.year4000.utilities.api.route.accounts;

import net.year4000.utilities.api.APIManager;
import net.year4000.utilities.api.Route;
import net.year4000.utilities.api.routedata.Accounts;

public class AccountsRoute extends Route<Accounts> {

    int page;

    public AccountsRoute(int page) {
        super(APIManager.getRouteUrl("accounts"), Accounts.class);
        this.page = page;
    }

    public AccountsRoute() {
        this(1);
    }

    @Override
    public String getExtraData() {
        return "?page=" + page;
    }
}
