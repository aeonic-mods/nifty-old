package design.aeonic.nifty.api.client.ui.template;

import com.mojang.blaze3d.vertex.PoseStack;
import design.aeonic.nifty.api.client.ui.UiElementTemplate;

import java.util.function.Supplier;

/**
 * A simple static UI element.
 */
public class BooleanUiElementTemplate<A, B> implements UiElementTemplate<BooleanUiElementTemplate.Context<A, B>> {

    protected final UiElementTemplate<A> first;
    protected final UiElementTemplate<B> second;

    /**
     * A boolean UI element template that can switch between two other elements based on a context boolean.
     * Both elements should have the same dimensions.
     *
     * @param first  the template to draw if the context boolean is true
     * @param second the template to draw if the context boolean is false
     */
    public BooleanUiElementTemplate(UiElementTemplate<A> first, UiElementTemplate<B> second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int getWidth() {
        return first.getWidth();
    }

    @Override
    public int getHeight() {
        return first.getWidth();
    }

    @Override
    public void draw(PoseStack stack, int x, int y, int zOffset, Context<A, B> ctx) {
        if (ctx.useFirst()) first.draw(stack, x, y, zOffset, ctx.getFirst());
        else second.draw(stack, x, y, zOffset, ctx.getSecond());
    }

    public interface Context<A, B> {

        boolean useFirst();

        A getFirst();

        B getSecond();

    }

    public record SimpleContext<A, B>(Supplier<Boolean> enabled, Supplier<A> first, Supplier<B> second) implements Context<A, B> {

        @Override
        public boolean useFirst() {
            return enabled.get();
        }

        @Override
        public A getFirst() {
            return first.get();
        }

        @Override
        public B getSecond() {
            return second.get();
        }

    }

}
