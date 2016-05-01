/*
 * Copyright 2015 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.router.pipline;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpServerCodec;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.router.Router;

public class HttpInitializer extends ChannelInitializer<Channel> {
    private final Router router;

    public HttpInitializer(Router router) {
        this.router = Conditions.nonNull(router, "router");
    }

    /** Add the handlers to the channel pipeline */
    public void addHandlers(ChannelPipeline pipeline) {
        pipeline.addLast("http_server_codec", new HttpServerCodec())
            .addLast(RouterDecoder.NAME, new RouterDecoder())
            .addLast(MessageHandler.NAME, new MessageHandler());
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        System.err.println("init");
        channel.pipeline().firstContext().attr(Router.ATTRIBUTE_KEY).set(router);
        addHandlers(channel.pipeline());
    }
}
