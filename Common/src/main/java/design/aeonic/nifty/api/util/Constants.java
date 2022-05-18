package design.aeonic.nifty.api.util;

import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Constants {

    public static final String NIFTY_ID = "nifty";
    public static final String NIFTY_NAME = "Nifty";

    public static final Logger LOGGER = LogManager.getLogger(NIFTY_NAME);

    public static final ResourceLocation EMPTY = new ResourceLocation(NIFTY_ID, "empty");

}