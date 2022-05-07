package design.aeonic.nifty;

import design.aeonic.nifty.api.core.PlatformInfo;
import design.aeonic.nifty.api.registry.Registrar;
import design.aeonic.nifty.impl.Services;

public class Nifty {

    public static final PlatformInfo PLATFORM = Services.load(PlatformInfo.class);
    public static final Registrar REGISTRY = Services.load(Registrar.class);

    public static void init() {

    }

}
