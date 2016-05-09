package net.year4000.utilities.net.router.pipline;

import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.MessageToMessageDecoder;

/** Allow creating static decoders */
@ChannelHandler.Sharable
public abstract class StaticDecoder<T> extends MessageToMessageDecoder<T> {}
