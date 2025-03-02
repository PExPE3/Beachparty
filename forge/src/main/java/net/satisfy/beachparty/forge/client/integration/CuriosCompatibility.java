package net.satisfy.beachparty.forge.client.integration;

import net.satisfy.beachparty.core.registry.ObjectRegistry;
import top.theillusivec4.curios.api.CuriosApi;

public class CuriosCompatibility {
    public static void load() {
        CuriosApi.registerCurio(ObjectRegistry.BEACH_HAT.get(), new CuriosWearableTrinket.BeachhatCurio());
        CuriosApi.registerCurio(ObjectRegistry.CROCS.get(), new CuriosWearableTrinket.CrocsCurio());
        CuriosApi.registerCurio(ObjectRegistry.SUNGLASSES.get(), new CuriosWearableTrinket.SunglassesCurio());
        CuriosApi.registerCurio(ObjectRegistry.SWIM_WINGS.get(), new CuriosWearableTrinket.SwimWingsCurio());
        CuriosApi.registerCurio(ObjectRegistry.TRUNKS.get(), new CuriosWearableTrinket.SwimSuitCurio());
        CuriosApi.registerCurio(ObjectRegistry.BIKINI.get(), new CuriosWearableTrinket.SwimSuitCurio());
        CuriosApi.registerCurio(ObjectRegistry.RUBBER_RING_STRIPPED.get(), new CuriosWearableTrinket.RubberRingCurio());
        CuriosApi.registerCurio(ObjectRegistry.RUBBER_RING_PINK.get(), new CuriosWearableTrinket.RubberRingCurio());
        CuriosApi.registerCurio(ObjectRegistry.RUBBER_RING_BLUE.get(), new CuriosWearableTrinket.RubberRingCurio());
        CuriosApi.registerCurio(ObjectRegistry.RUBBER_RING_PELICAN.get(), new CuriosWearableTrinket.RubberRingCurio());
        CuriosApi.registerCurio(ObjectRegistry.RUBBER_RING_AXOLOTL.get(), new CuriosWearableTrinket.RubberRingCurio());
    }
}
