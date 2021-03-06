/*
 * This file is part of FoxCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) gravityfox - https://gravityfox.net/
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.foxdenstudio.sponge.foxcore.mod;

import com.flowpowered.math.vector.Vector3i;
import net.foxdenstudio.sponge.foxcore.common.network.client.listener.ServerPositionPacketListener;
import net.foxdenstudio.sponge.foxcore.common.network.client.listener.ServerPrintStringPacketListener;
import net.foxdenstudio.sponge.foxcore.common.network.server.packet.ServerPositionPacket;
import net.foxdenstudio.sponge.foxcore.common.network.server.packet.ServerPrintStringPacket;
import net.foxdenstudio.sponge.foxcore.mod.render.RenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.List;

public class ClientProxy extends CommonProxy {

    private RenderHandler renderHandler;
    private FCClientNetworkManager.ClientChannel foxcoreChannel;

    @Override
    public void registerRenderers() {
        MinecraftForge.EVENT_BUS.register(renderHandler = new RenderHandler(Minecraft.getMinecraft()));
    }

    @Override
    public void initializeNetworkManager() {
        FCClientNetworkManager manager = FCClientNetworkManager.instance();
        foxcoreChannel = manager.getOrCreateClientChannel("foxcore");
        foxcoreChannel.registerListener(ServerPositionPacket.ID, new ServerPositionPacketListener());
        foxcoreChannel.registerListener(ServerPrintStringPacket.ID, new ServerPrintStringPacketListener());
    }

    @Override
    public void registerNetworkChannels() {
        FCClientNetworkManager.instance().registerNetworkingChannels();
    }

    @Override
    public void lockNetworkManager() {
        FCClientNetworkManager.instance().lock();
    }

    @Override
    public void updatePositionsList(List<Vector3i> list) {
        this.renderHandler.updateList(list);
    }

    @SubscribeEvent
    public void onDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event){
        FCClientNetworkManager.instance().hasServer = false;
        FoxCoreCUIMain.logger.info("DISCO FOX");
    }
}
