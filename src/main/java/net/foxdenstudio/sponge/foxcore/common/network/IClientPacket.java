package net.foxdenstudio.sponge.foxcore.common.network;

import io.netty.buffer.ByteBuf;
import org.spongepowered.api.network.ChannelBuf;

/**
 * Created by Fox on 4/18/2016.
 */
public interface IClientPacket {

    void read(ChannelBuf payload);

    void write(ByteBuf buf);
}
