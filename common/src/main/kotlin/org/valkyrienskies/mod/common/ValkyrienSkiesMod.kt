package org.valkyrienskies.mod.common

import net.minecraft.server.MinecraftServer
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import org.valkyrienskies.core.apigame.VSCore
import org.valkyrienskies.core.apigame.VSCoreClient
import org.valkyrienskies.core.impl.hooks.VSEvents
import org.valkyrienskies.mod.api_impl.events.VsApiImpl
import org.valkyrienskies.mod.common.blockentity.TestHingeBlockEntity
import org.valkyrienskies.mod.common.config.VSGameConfig
import org.valkyrienskies.mod.common.entity.ShipMountingEntity
import org.valkyrienskies.mod.common.entity.VSPhysicsEntity
import org.valkyrienskies.mod.common.networking.VSGamePackets
import org.valkyrienskies.mod.common.util.GameTickForceApplier
import org.valkyrienskies.mod.common.util.ShipSettings
import org.valkyrienskies.mod.common.util.SplitHandler
import org.valkyrienskies.mod.common.util.SplittingDisablerAttachment

object ValkyrienSkiesMod {
    const val MOD_ID = "valkyrienskies"

    lateinit var TEST_CHAIR: Block
    lateinit var TEST_HINGE: Block
    lateinit var TEST_FLAP: Block
    lateinit var TEST_WING: Block
    lateinit var TEST_SPHERE: Block
    lateinit var CONNECTION_CHECKER_ITEM: Item
    lateinit var SHIP_CREATOR_ITEM: Item
    lateinit var SHIP_ASSEMBLER_ITEM: Item
    lateinit var SHIP_CREATOR_ITEM_SMALLER: Item
    lateinit var AREA_ASSEMBLER_ITEM: Item
    lateinit var PHYSICS_ENTITY_CREATOR_ITEM: Item
    lateinit var SHIP_MOUNTING_ENTITY_TYPE: EntityType<ShipMountingEntity>
    lateinit var PHYSICS_ENTITY_TYPE: EntityType<VSPhysicsEntity>
    lateinit var TEST_HINGE_BLOCK_ENTITY_TYPE: BlockEntityType<TestHingeBlockEntity>

    /**
     * Keeps track of the MinecraftServers which have been created and run. Hopefully this contains at most one
     * server...
     */
    private val currentServers = mutableListOf<MinecraftServer>()


    @JvmStatic
    var currentServer: MinecraftServer?
        get() {
            return currentServers.lastOrNull()
        }
        set(value) { currentServer = value }

    @JvmStatic
    lateinit var vsCore: VSCore

    @JvmStatic
    val vsCoreClient get() = vsCore as VSCoreClient

    @JvmStatic
    val api by lazy {
        VsApiImpl(vsCore)
    }

    @JvmStatic
    lateinit var splitHandler: SplitHandler

    @JvmStatic
    fun addServer(server: MinecraftServer?) {
        if (server != null) {
            currentServers.add(server)
        }
    }
    @JvmStatic
    fun removeServer(server: MinecraftServer?) {
        if (server != null) {
            currentServers.remove(server)
        }
    }


    fun init(core: VSCore) {
        this.vsCore = core

        BlockStateInfo.init()
        VSGamePackets.register()
        VSGamePackets.registerHandlers()

        core.registerConfigLegacy("vs", VSGameConfig::class.java)

        splitHandler = SplitHandler(this.vsCore.hooks.enableBlockEdgeConnectivity, this.vsCore.hooks.enableBlockCornerConnectivity)

        core.registerAttachment(ShipSettings::class.java)
        core.registerAttachment(GameTickForceApplier::class.java) {
            useLegacySerializer()
        }
        core.registerAttachment(SplittingDisablerAttachment::class.java) {
            useLegacySerializer()
        }

        VSEvents.ShipLoadEvent.on { event ->
            event.ship.setAttachment(GameTickForceApplier())
            event.ship.setAttachment(SplittingDisablerAttachment(true))
        }
    }
}
