package design.aeonic.nifty.api.network.container;

import design.aeonic.nifty.api.util.DataUtils;
import net.minecraft.world.inventory.ContainerData;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public abstract class DataField<T> {

    private short[] shortCache;
    private final @Nullable Supplier<T> getter;

    public DataField() {
        this(null);
    }

    public DataField(@Nullable Supplier<T> getter) {
        shortCache = encode(defaultValue());
        this.getter = getter;
    }

    /**
     * The field's initial value.
     */
    protected abstract T defaultValue();

    /**
     * @param value the input value
     * @return a short array of length equal to {@link #slots()}
     */
    protected abstract short[] encode(T value);

    /**
     * @param data the input short array of length equal to {@link #slots()}
     * @return the decoded value
     */
    protected abstract T decode(short[] data);

    /**
     * The number of short slots needed to represent this field in a {@link ContainerData} object.
     */
    public abstract int slots();

    /**
     * Gets the value of the contained field.
     */
    public T getValue() {
        return decode(shortCache);
    }

    /**
     * Sets the value of the contained field.
     */
    public void setValue(T value) {
        shortCache = encode(value);
    }

    /**
     * Writes a single short value to the field's cached data.
     * @param data the data slot's value
     * @param segment the index of the data slot, must be less than {@link #slots()}
     */
    public void write(int segment, short data) {
        shortCache[segment] = data;
    }

    /**
     * Reads a single short value from the field's cached data. If the field has a getter (usually on a server instance), it first updates the cache from it.
     * @param segment the index of the data slot, must be less than {@link #slots()}
     * @return the value of the data slot at the given index
     */
    public short read(int segment) {
        if (getter != null) {
            shortCache = encode(getter.get());
        }
        return shortCache[segment];
    }

    public static class ShortField extends DataField<Short> {

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
            return new short[] {value};
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

    public static class IntField extends DataField<Integer> {

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

    public static class LongField extends DataField<Long> {

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

}
