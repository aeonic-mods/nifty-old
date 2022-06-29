package design.aeonic.nifty.api.network.container.field;

import design.aeonic.nifty.api.network.container.DataField;
import design.aeonic.nifty.api.util.DataUtils;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class IntField extends DataField<Integer> {

    public IntField() {
        super();
    }

    public IntField(@Nullable Supplier<Integer> getter) {
        super(getter);
    }

    @Override
    protected Integer defaultValue() {
        return 0;
    }

    @Override
    protected short[] encode(Integer value) {
        return DataUtils.intToTwoShorts(value);
    }

    @Override
    protected Integer decode(short[] data) {
        return DataUtils.intFromTwoShorts(data);
    }

    @Override
    public int slots() {
        return 2;
    }

}
