package design.aeonic.nifty.api.client.ui;

import design.aeonic.nifty.api.client.ui.element.FillingUiElementTemplate;
import design.aeonic.nifty.api.client.ui.element.StaticUiElementTemplate;
import design.aeonic.nifty.api.client.ui.element.TankUiElementTemplate;
import design.aeonic.nifty.api.util.Constants;
import net.minecraft.resources.ResourceLocation;

/**
 * Contains {@link UiElementTemplate}s for ease of use,6 with default assets shipped with Nifty
 */
public final class UiSets {

    /**
     * A UI set with some vanilla elements and vanilla style extras - tanks, etc
     */
    public static final class Vanilla {

        /**
         * A texture containing all elements of the Vanilla UI set.
         */
        public static final Texture TEXTURE_MAP = new Texture(new ResourceLocation(Constants.NIFTY_ID, "textures/gui/template/vanilla.png"));

        /**
         * A Screen background with the same size as most vanilla menu screens, blank except for player inventory slots.
         */
        public static final StaticUiElementTemplate SCREEN_BACKGROUND = new StaticUiElementTemplate(TEXTURE_MAP, 176, 165, 80, 91);

        /**
         * A normal item slot.
         */
        public static StaticUiElementTemplate ITEM_SLOT_NORMAL = new StaticUiElementTemplate(TEXTURE_MAP, 18, 18, 0, 108);

        /**
         * An output slot used in vanilla furnaces etc.
         */
        public static StaticUiElementTemplate ITEM_SLOT_OUTPUT = new StaticUiElementTemplate(TEXTURE_MAP, 26, 26, 18, 108);

        /**
         * A tank the size of a normal inventory slot.
         */
        public static TankUiElementTemplate TANK_ONE_ONE = new TankUiElementTemplate(TEXTURE_MAP,
                18, 18, 126, 0, 63, 72, false);

        /**
         * A tank two slots high and the width of half a slot.
         */
        public static TankUiElementTemplate TANK_TWO_HALF = new TankUiElementTemplate(TEXTURE_MAP,
                9, 36, 126, 0, 0, 72, false);

        /**
         * A tank two slots high and the width of one slot.
         */
        public static TankUiElementTemplate TANK_TWO_ONE = new TankUiElementTemplate(TEXTURE_MAP,
                18, 36, 126, 0, 9, 72, false);

        /**
         * A tank two slots high and wide.
         */
        public static TankUiElementTemplate TANK_TWO_TWO = new TankUiElementTemplate(TEXTURE_MAP,
                36, 36, 126, 0, 27, 72, false);

        /**
         * A tank four slots high and the width of one slot.
         */
        public static TankUiElementTemplate TANK_FOUR_ONE = new TankUiElementTemplate(TEXTURE_MAP,
                18, 72, 126, 0, 0, 0, false);

        /**
         * A tank four slots high and the width of two slots.
         */
        public static TankUiElementTemplate TANK_FOUR_TWO = new TankUiElementTemplate(TEXTURE_MAP,
                36, 72, 126, 0, 27, 0, false);

        /**
         * A tank four slots high and wide.
         */
        public static TankUiElementTemplate TANK_FOUR_FOUR = new TankUiElementTemplate(TEXTURE_MAP,
                72, 72, 126, 0, 27, 0, false);

        /**
         * The static recipe arrow used in vanilla guis such as the anvil.
         */
        public static StaticUiElementTemplate RECIPE_ARROW = new StaticUiElementTemplate(TEXTURE_MAP, 23, 16, 0, 176);

        /**
         * The static recipe arrow with a red X used in vanilla guis such as the anvil.
         */
        public static StaticUiElementTemplate RECIPE_ARROW_DISALLOWED = new StaticUiElementTemplate(TEXTURE_MAP, 23, 16, 0, 192);

        /**
         * The recipe arrow used in vanilla furnaces, filling from left to right.
         */
        public static FillingUiElementTemplate RECIPE_ARROW_FILLING = new FillingUiElementTemplate(TEXTURE_MAP,
                23, 16, 0, 176, 23, 176, FillingUiElementTemplate.FillDirection.LEFT_TO_RIGHT);

        /**
         * The burn time indicator used in vanilla furnaces.
         */
        public static FillingUiElementTemplate BURN_TIME_INDICATOR = new FillingUiElementTemplate(TEXTURE_MAP,
                14, 14, 0, 162, 14, 162, FillingUiElementTemplate.FillDirection.BOTTOM_TO_TOP);

        /**
         * The bubble progress indicator used in vanilla brewing stands.
         */
        public static FillingUiElementTemplate BUBBLE_PROGRESS_BAR = new FillingUiElementTemplate(TEXTURE_MAP,
                10, 28, 0, 134, 10, 134, FillingUiElementTemplate.FillDirection.BOTTOM_TO_TOP);

        /**
         * The downward facing progress arrow used in vanilla brewing stands.
         */
        public static FillingUiElementTemplate DOWNWARD_PROGESS_ARROW = new FillingUiElementTemplate(TEXTURE_MAP,
                8, 27, 21, 134, 29, 134, FillingUiElementTemplate.FillDirection.TOP_TO_BOTTOM);

    }

}
