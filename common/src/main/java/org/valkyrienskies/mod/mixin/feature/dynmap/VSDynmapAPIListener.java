package org.valkyrienskies.mod.mixin.feature.dynmap;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.server.level.ServerLevel;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.DynmapCommonAPIListener;
import org.dynmap.markers.GenericMarker;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerIcon;
import org.dynmap.markers.MarkerSet;
import org.joml.Vector3dc;
import org.valkyrienskies.core.api.ships.QueryableShipData;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.mod.common.ValkyrienSkiesMod;

public abstract class VSDynmapAPIListener extends DynmapCommonAPIListener {
    public DynmapCommonAPI api = null;

    @Override
    public void apiEnabled(DynmapCommonAPI dynmapCommonAPI) {
        this.api = dynmapCommonAPI;
    }

    public void register() {
        DynmapCommonAPIListener.register(this);
    }

    /**
     * This method is used to either geto r create the VS2 MarkerSet
     *
     * @return the VS2 MarkerSet
     */
    public MarkerSet getOrCreateMarkerSet() {
        if (this.api == null) return null;

        MarkerAPI api = this.api.getMarkerAPI();
        if (api == null) return null;

        MarkerSet set = api.getMarkerSet(ValkyrienSkiesMod.MOD_ID);
        if (set == null)
            set = api.createMarkerSet(ValkyrienSkiesMod.MOD_ID, "Ship Markers", new HashSet<>(), true);
        return set;
    }

    /**
     * This method is used to add or update a Ship's Icon-based Marker based on world and if the Ship is loaded.
     *
     * @param ship the provided Ship
     * @param set the provided MarkerSet
     * @param world the provided Dynmap world name
     * @param isLoaded whether the Ship is loaded
     */
    public void addOrUpdateShipIconMarker(ServerShip ship, MarkerSet set, String world, boolean isLoaded) {
        String markerId = "ship" + ship.getId();
        Vector3dc com = ship.getTransform().getPositionInWorld();
        String label = createShipLabel(ship);
        MarkerIcon icon = isLoaded ? getOrCreateLoadedIcon() : getOrCreateUnloadedIcon();

        Marker marker = set.findMarker(markerId);
        if (marker == null) {
            set.createMarker(markerId, label, true, world, com.x(), com.y(), com.z(), icon, true);
            return;
        }
        marker.setDescription(label);
        marker.setLocation(world, com.x(), com.y(), com.z());
        marker.setMarkerIcon(icon);
    }

    /**
     * This method loops over every Icon-based Marker in the given MarkerSet and attempts to remove unused them.
     *
     * @param set the provided MarkerSet
     * @param data the queryable list of Ships
     */
    public void clearUnusedIconMarkers(MarkerSet set, QueryableShipData<ServerShip> data) {
        Set<Marker> markers = set.getMarkers();
        if (markers == null) return;

        // TODO: Replace true with config option
        markers.forEach(marker -> clearUnusedMarker(marker, data, true));
    }

    //public void clearUnusedPolylineMarkers(MarkerSet set, QueryableShipData<ServerShip> data) {
    //    Set<PolyLineMarker> markers = set.getPolyLineMarkers();
    //    if (markers == null) return;

        // TODO: Replace true with config option
    //    markers.forEach(marker -> clearUnusedMarker(marker, data, true));
    //}

    /**
     * This method is used to remove unused markers such as deleted Ships or if the config was changed to disallow this type of marker
     *
     * @param marker the potentially unused marker
     * @param data the queryable list of Ships
     * @param enabled whether the config allows this type of marker
     */
    public void clearUnusedMarker(GenericMarker marker, QueryableShipData<ServerShip> data, boolean enabled) {
        long id = Long.getLong(marker.getMarkerID().replace("ship", ""));
        if (!enabled || data.getById(id) == null)
            marker.deleteMarker();
    }

    /**
     * Created the description of the Marker in HTML
     *
     * @param ship the ship being described
     * @return the HTML formatted description
     */
    public static String createShipLabel(ServerShip ship) {
        String label = "<h1>" + ship.getSlug() + "</h1>";

        // TODO: Config options for ShipID and Mass

        return label;
    }

    /**
     * Call this method on end of Server Tick to update the markers
     *
     * @param level the provided ServerLevel
     */
    public abstract void updateShipMarkers(ServerLevel level);

    /**
     * Use either FabricServer or ForgeServer to grab the VS2 Logo
     *
     * @return the normal version of the VS2 Logo
     */
    public abstract MarkerIcon getOrCreateLoadedIcon();

    /**
     * Use either FabricServer or ForgeServer to grab the grey-scaled VS2 Logo
     *
     * @return the gray-scaled version of the VS2 Logo
     */
    public abstract MarkerIcon getOrCreateUnloadedIcon();
}
