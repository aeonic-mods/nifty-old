package design.aeonic.nifty.api.network.container.field;

import design.aeonic.nifty.api.network.container.DataField;
import design.aeonic.nifty.api.util.DataUtils;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class LongField extends DataField<Long> {

    public LongField() {
        super();
    }

    public LongField(@Nullable Supplier<Long> getter) {
        super(getter);
    }

    @Override
    protected Long defaultValue() {
        return 0L;
    }

    @Override
    protected short[] encode(Long value) {
        return DataUtils.longToFourShorts(value);
    }

    @Override
    protected Long decode(short[] data) {
        return DataUtils.longFromFourShorts(data);
    }

    @Override
    public int slots() {
        return 4;
    }

}
