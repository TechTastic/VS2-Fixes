package org.valkyrienskies.mod.common.hooks

import net.minecraft.client.Minecraft
import org.valkyrienskies.core.api.world.ShipWorld
import org.valkyrienskies.core.apigame.hooks.CoreHooksOut
import org.valkyrienskies.core.apigame.hooks.PlayState
import org.valkyrienskies.core.apigame.hooks.PlayState.CLIENT_MULTIPLAYER
import org.valkyrienskies.core.apigame.hooks.PlayState.CLIENT_SINGLEPLAYER
import org.valkyrienskies.core.apigame.hooks.PlayState.CLIENT_TITLESCREEN
import org.valkyrienskies.core.apigame.hooks.PlayState.SERVERSIDE
import org.valkyrienskies.mod.common.ValkyrienSkiesMod
import org.valkyrienskies.mod.common.shipObjectWorld
import org.valkyrienskies.mod.common.vsCore

abstract class CommonHooksImpl : CoreHooksOut {

    override var enableBlockEdgeConnectivity: Boolean
        get() = vsCore.hooks.enableBlockEdgeConnectivity
        set(value) {}

    override var enableBlockCornerConnectivity: Boolean
        get() = vsCore.hooks.enableBlockCornerConnectivity
        set(value) {}

    override var enableConnectivity: Boolean
        get() = vsCore.hooks.enableConnectivity
        set(value) {}

    override var enableWorldConnectivity: Boolean
        get() = vsCore.hooks.enableWorldConnectivity
        set(value) {}

    override var enableSplitting: Boolean
        get() = vsCore.hooks.enableSplitting
        set(value) {}

    override val playState: PlayState
        get() {
            if (!isPhysicalClient) {
                return SERVERSIDE
            }

            // Client is not connected to any game
            if (Minecraft.getInstance().connection?.connection?.isConnected != true) {
                return CLIENT_TITLESCREEN
            }

            // Client is in Singleplayer (or has their singleplayer world open to LAN)
            if (Minecraft.getInstance().singleplayerServer != null) {
                return CLIENT_SINGLEPLAYER
            }

            return CLIENT_MULTIPLAYER
        }

    override val currentShipServerWorld: ShipWorld?
        get() = ValkyrienSkiesMod.currentServer?.shipObjectWorld

    override val currentShipClientWorld: ShipWorld
        get() = Minecraft.getInstance().shipObjectWorld!!
}
