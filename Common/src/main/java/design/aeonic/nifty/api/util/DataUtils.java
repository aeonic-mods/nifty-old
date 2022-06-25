package design.aeonic.nifty.api.util;

import net.minecraft.world.inventory.ContainerData;
import org.apache.commons.lang3.Conversion;

/**
 * Utility methods largely for use with {@link ContainerData}.
 */
public class DataUtils {

    public static short[] intToTwoShorts(int s) {
        return Conversion.intToShortArray(s, 0, new short[2], 0, 2);
    }

    public static int intFromTwoShorts(short[] s) {
        assert s.length == 2;
        return Conversion.shortArrayToInt(s, 0, 0, 0, 2);
    }

    public static short[] longToFourShorts(long s) {
        return Conversion.longToShortArray(s, 0, new short[4], 0, 4);
    }

    public static long longFromFourShorts(short[] s) {
        assert s.length == 4;
        return Conversion.shortArrayToLong(s, 0, 0, 0, 4);
    }

}
