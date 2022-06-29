package design.aeonic.nifty.api.network.container.field;

import design.aeonic.nifty.api.network.container.DataField;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class BooleanField extends DataField<Boolean> {

    public BooleanField() {
        super();
    }

    public BooleanField(@Nullable Supplier<Boolean> getter) {
        super(getter);
    }

    @Override
    protected Boolean defaultValue() {
        return false;
    }

    @Override
    protected short[] encode(Boolean value) {
        return new short[] { (short) (value ? 1 : 0) };
    }

    @Override
    protected Boolean decode(short[] data) {
        return data[0] == (short) 1;
    }

    @Override
    public int slots() {
        return 1;
    }

}
