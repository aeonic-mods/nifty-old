package design.aeonic.nifty.api.network.container.field;

import design.aeonic.nifty.api.network.container.DataField;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ShortField extends DataField<Short> {

    public ShortField() {
        super();
    }

    public ShortField(@Nullable Supplier<Short> getter) {
        super(getter);
    }

    @Override
    protected Short defaultValue() {
        return (short) 0;
    }

    @Override
    protected short[] encode(Short value) {
        return new short[]{value};
    }

    @Override
    protected Short decode(short[] data) {
        return data[0];
    }

    @Override
    public int slots() {
        return 1;
    }

}
