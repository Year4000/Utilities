/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.net.router.pipline;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpServerCodec;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.net.router.Router;

import java.util.function.Consumer;

public class HttpInitializer extends ChannelInitializer<Channel> {
    private final Router router;
    private Consumer<ChannelPipeline> handler;

    public HttpInitializer(Router router) {
        this(router, null);
    }

    public HttpInitializer(Router router, Consumer<ChannelPipeline> handler) {
        this.router = Conditions.nonNull(router, "router");
        this.handler = handler;
    }

    /** Add the handlers to the channel pipeline */
    public void addHandlers(ChannelPipeline pipeline) {
        pipeline.addLast("http_server_codec", new HttpServerCodec())
            .addLast(RouterDecoder.NAME, RouterDecoder.INSTANCE)
            .addLast(MessageHandler.NAME, MessageHandler.INSTANCE)
            .addLast(ExceptionHandler.NAME, ExceptionHandler.INSTANCE);
        if (handler != null) {
            handler.accept(pipeline);
        }
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.attr(Router.ATTRIBUTE_KEY).set(router);
        addHandlers(channel.pipeline());
    }
}
