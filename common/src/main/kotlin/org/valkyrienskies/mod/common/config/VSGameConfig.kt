package org.valkyrienskies.mod.common.config

import com.github.imifou.jsonschema.module.addon.annotation.JsonSchema
import org.valkyrienskies.mod.mixinducks.feature.mass_tooltip.MassTooltipVisibility

object VSGameConfig {

    @JvmField
    val CLIENT = Client()

    @JvmField
    val SERVER = Server()

    @JvmField
    val COMMON = Common()

    class Client {
        val Tooltip = TOOLTIP()

        val BlockTinting = BLOCKTINT()

        @JsonSchema(description = "Renders the VS2 debug HUD with TPS")
        var renderDebugText = false

        @JsonSchema(
            description = "Recommend ship slugs in mc commands where player names could be used ex. /tp ship-name wich could pollute user autocomplete"
        )
        var recommendSlugsInMcCommands = true

        class TOOLTIP {
            @JsonSchema(
                description = "Set when the Mass Tooltip is Visible"
            )
            var massTooltipVisibility = MassTooltipVisibility.ADVANCED

            @JsonSchema(
                description = "Use Imperial Units to show Mass"
            )
            var useImperialUnits = false
        }

        class BLOCKTINT {
            @JsonSchema(
                description = "Partly fixes the block tinting issue with blocks on ships"
            )
            var fixBlockTinting = false
        }
    }

    class Server {
        val FTBChunks = FTBCHUNKS()

        class FTBCHUNKS {
            @JsonSchema(
                description = "Are Ships protected by FTB Chunk Claims?"
            )
            var shipsProtectedByClaims = true

            @JsonSchema(
                description = "Are ships protected outside of build height (max and min)?"
            )
            var shipsProtectionOutOfBuildHeight = false
        }

        val ComputerCraft = COMPUTERCRAFT()

        class COMPUTERCRAFT {
            @JsonSchema(
                description = "Turtles leaving scaled up/down ship may cause issues" +
                    "Enable/Disable Turtles Leaving Scaled Ships?"
            )
            var canTurtlesLeaveScaledShips = false
        }

        val Weather2 = WEATHER2()

        class WEATHER2 {
            @JsonSchema(
                description = "How much Weather 2's wind affects VS ships"
            )
            var windMultiplier = 0.0001f

            @JsonSchema(
                description = "The maximum velocity a VS ship can travel because of wind"
            )
            var windMaxVel = 20.0f

            @JsonSchema(
                description = "In what range storms affect VS ships"
            )
            var stormRange = 150.0

            @JsonSchema(
                description = "Storm effect dampening on VS ships"
            )
            var stormDampening = 0.0f
        }

        @JsonSchema(
            description = "By default, the vanilla server prevents block interacts past a certain distance " +
                "to prevent cheat clients from breaking blocks halfway across the map. " +
                "This approach breaks down in the face of extremely large ships, " +
                "where the distance from the block origin to the nearest face is greater " +
                "than the interact distance check allows."
        )
        var enableInteractDistanceChecks = true

        @JsonSchema(
            description = "If true, teleportation into the shipyard is redirected to " +
                "the ship it belongs to instead."
        )
        var transformTeleports = true

        @JsonSchema(
            description = "By default, the server checks that player movement is legal, and if it isn't, " +
                "rubber-bands the player with the infamous \"moved too quickly\" message. Since players on VS ships " +
                "will move illegally, they will be affected by this check frequently. This option disables that " +
                "check. (it doesn't work very well anyway, don't worry)"
        )
        var enableMovementChecks = false

        @JsonSchema(
            description = "If true, prevents water and other fluids from flowing out of the ship's bounding box."
        )
        var preventFluidEscapingShip = true

        @JsonSchema(
            description = "Blast force in newtons of a TNT explosion at the center of the explosion."
        )
        var explosionBlastForce = 500000.0

        @JsonSchema(
            description = "Allow natural mob spawning on ships"
        )
        var allowMobSpawns = true

        @JsonSchema(
            description = "Allow pathfinding on ships"
        )
        var aiOnShips = true

        @JsonSchema(
            description = "Scale of the mini ship creator"
        )
        var miniShipSize = 0.5

        @JsonSchema(
            description = "Minimum scale of ships"
        )
        var minScaling = 0.25

        @JsonSchema(
            description = "Enable splitting in worldspace. (Experimental!)"
        )
        var enableWorldSplitting = false

        @JsonSchema(
            description = "The default grace timer for splitting. A split won't occur after a block break at a position until this many ticks have passed. Note that setting this too high may prevent things like explosions from properly launching split ships. (in ticks)"
        )
        var defaultSplitGraceTimer = 1
    }

    class Common {

        @JvmField
        @JsonSchema(title = "Advanced")
        val ADVANCED = Advanced()

        class Advanced { // Debug configs that may be either side
            @JsonSchema(
                description = "Renders mob pathfinding nodes. Must be set on client and server to work. " +
                    "Requires the system property -Dorg.valkyrienskies.render_pathfinding=true"
            )
            var renderPathfinding = true // Requires ValkyrienCommonMixinConfigPlugin.PATH_FINDING_DEBUG to be true
        }
    }
}
