package net.year4000.utilities.sponge.hologram;

import com.google.common.collect.ImmutableList;
import net.year4000.utilities.Conditions;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.Iterator;

/** A frame buffer that will buffer the hologram frames */
final class FrameBuffer implements Iterable<Text> {
    private final Text[] lines;

    private FrameBuffer(Text... lines) {
        this.lines = Conditions.nonNull(lines, "lines");
    }

    /** Get the specific line of the buffer */
    public Text get(int index) {
        return lines[Conditions.inRange(index, lines)];
    }

    /** Get the size of the buffer */
    public int size() {
        return lines.length;
    }

    /** Get the iterator of this framebuffer */
    @Override
    public Iterator<Text> iterator() {
        return new TextIterator(this);
    }

    /** Grab the buffer in list form */
    public ImmutableList<Text> toList() {
        return ImmutableList.copyOf(lines);
    }

    /** Create the instance of the frame buffer */
    public static Builder builder() {
        return new Builder();
    }

    /** Create the buffer that will create each frame */
    static class Builder implements net.year4000.utilities.Builder<FrameBuffer> {
        private final ImmutableList.Builder<Text> lines = ImmutableList.builder();

        private Builder() {}

        /** Add the text object to the builder */
        Builder add(Text text) {
            lines.add(Conditions.nonNull(text, "text"));
            return this;
        }

        /** Add the lines to the builder */
        Builder add(Text... texts) {
            for (Text text : Conditions.nonNull(texts, "texts")) {
                lines.add(text);
            }
            return this;
        }

        /** Add the raw string to the builder, string can be empty just not null */
        Builder add(String raw) {
            add(TextSerializers.FORMATTING_CODE.deserialize(Conditions.nonNull(raw, "raw")));
            return this;
        }

        @Override
        public FrameBuffer build() {
            ImmutableList<Text> lines = this.lines.build();
            return new FrameBuffer(lines.toArray(new Text[lines.size()]));
        }
    }

    /** Get the text iterator for the framebuffer */
    private class TextIterator implements Iterator<Text> {
        private final FrameBuffer buffer;
        private int index;

        /** Create the iterator, note framebuffer can never be null */
        private TextIterator(FrameBuffer buffer) {
            this.buffer = Conditions.nonNull(buffer, "buffer");
        }

        @Override
        public boolean hasNext() {
            return index < buffer.size();
        }

        @Override
        public Text next() {
            return buffer.lines[index++]; // Use this as we do not need to check for indies
        }
    }
}
