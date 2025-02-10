package committee.nova.encounter.reload;

import committee.nova.encounter.Encounter;
import committee.nova.encounter.api.IHasEncounterGoals;
import committee.nova.encounter.config.EncounterConfig;
import committee.nova.encounter.config.EncounterConfigManager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;


import java.util.List;
import java.util.Objects;

public class EncounterReloadListener implements ResourceManagerReloadListener {
    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        EncounterConfigManager.getEncounterConfig().ifPresent(cfg -> {
            final List<EncounterConfig.EncounterEntry> entries = cfg.encounterEntries;
            Encounter.LOGGER.info("Parsing encounter config...");
            for (final EncounterConfig.EncounterEntry entry : entries) {
                final ResourceLocation rl = ResourceLocation.tryParse(entry.entityId);
                if (rl == null) {
                    Encounter.LOGGER.warn("Invalid entity id string \"{}\"", entry.entityId);
                    continue;
                }
                if (!BuiltInRegistries.ENTITY_TYPE.containsKey(rl)) {
                    Encounter.LOGGER.warn("Invalid entity id \"{}\"", rl);
                }
                final IHasEncounterGoals extended = (IHasEncounterGoals) Objects.requireNonNull(
                        BuiltInRegistries.ENTITY_TYPE.get(rl)
                );
                extended.encounter$markEntitiesToAvoid(entry.avoid);
                extended.encounter$markEntitiesToTarget(entry.hostile);
                Encounter.LOGGER.debug(
                        "Parsed encounter config for entity {} >> avoid: {}; hostile: {}",
                        rl,
                        extended.encounter$entitiesToAvoid().size(),
                        extended.encounter$entitiesToTarget().size()
                );
            }
        });
    }
}
