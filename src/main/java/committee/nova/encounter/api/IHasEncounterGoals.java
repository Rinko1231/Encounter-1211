package committee.nova.encounter.api;

import net.minecraft.world.entity.EntityType;

import java.util.List;

public interface IHasEncounterGoals {
    List<? extends EntityType<?>> encounter$entitiesToTarget();

    List<? extends EntityType<?>> encounter$entitiesToAvoid();

    void encounter$markEntitiesToTarget(List<String> toTarget);

    void encounter$markEntitiesToAvoid(List<String> toAvoid);
}
