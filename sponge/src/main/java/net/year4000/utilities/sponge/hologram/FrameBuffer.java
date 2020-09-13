/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sponge.hologram;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import net.year4000.utilities.Conditions;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.util.Color;

import java.awt.image.BufferedImage;
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
        private static final Text.Builder BLOCK = Text.builder('\u2588');
        private static final ImmutableBiMap<Color, TextColor> COLORS = ImmutableBiMap.<Color, TextColor>builder()
            .put(TextColors.AQUA.getColor(), TextColors.AQUA)
            .put(TextColors.BLACK.getColor(), TextColors.BLACK)
            .put(TextColors.BLUE.getColor(), TextColors.BLUE)
            .put(TextColors.DARK_AQUA.getColor(), TextColors.DARK_AQUA)
            .put(TextColors.DARK_BLUE.getColor(), TextColors.DARK_BLUE)
            .put(TextColors.DARK_GRAY.getColor(), TextColors.DARK_GRAY)
            .put(TextColors.DARK_GREEN.getColor(), TextColors.DARK_GREEN)
            .put(TextColors.DARK_PURPLE.getColor(), TextColors.DARK_PURPLE)
            .put(TextColors.DARK_RED.getColor(), TextColors.DARK_RED)
            .put(TextColors.GOLD.getColor(), TextColors.GOLD)
            .put(TextColors.GRAY.getColor(), TextColors.GRAY)
            .put(TextColors.GREEN.getColor(), TextColors.GREEN)
            .put(TextColors.LIGHT_PURPLE.getColor(), TextColors.LIGHT_PURPLE)
            .put(TextColors.RED.getColor(), TextColors.RED)
            .put(TextColors.WHITE.getColor(), TextColors.WHITE)
            .put(TextColors.YELLOW.getColor(), TextColors.YELLOW)
            .build();
        private final ImmutableList.Builder<Text> lines = ImmutableList.builder();

        private Builder() {}

        /** Add the text object to the builder */
        Builder add(Text text) {
            if (text == null) {
                text = Text.EMPTY;
            }
            lines.add(text);
            return this;
        }

        /** Add the lines to the builder */
        Builder add(Text... texts) {
            for (Text text : Conditions.nonNull(texts, "texts")) {
                add(text);
            }
            return this;
        }

        /** Add the raw string to the builder, string can be empty just not null */
        Builder add(String raw) {
            add(TextSerializers.FORMATTING_CODE.deserialize(Conditions.nonNull(raw, "raw")));
            return this;
        }

        /** Adds a BufferedImage into the hologram */
        Builder add(BufferedImage image) {
            Conditions.nonNull(image, "image");
            for (int y = 0; y < image.getHeight(); y++) {
                Text.Builder builder = Text.builder();
                for (int x = 0 ; x < image.getWidth(); x++) {
                    Color color = Color.ofRgb(image.getRGB(x, y));
                    builder.append(BLOCK.color(COLORS.getOrDefault(color, TextColors.RESET)).build());
                }
                lines.add(builder.build());
            }
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
