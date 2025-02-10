package committee.nova.encounter.mixin;

import com.google.common.collect.Lists;
import committee.nova.encounter.api.IHasEncounterGoals;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.Objects;

@Mixin(EntityType.class)
public abstract class MixinEntityType implements IHasEncounterGoals {
    @Unique
    private final List<String> _encounter$toTarget = Lists.newArrayList();
    @Unique
    private final List<String> _encounter$toAvoid = Lists.newArrayList();

    @Unique
    private List<? extends EntityType<?>> encounter$toTarget = null;

    @Unique
    private List<? extends EntityType<?>> encounter$toAvoid = null;

    @Override
    public List<? extends EntityType<?>> encounter$entitiesToTarget() {
        if (encounter$toTarget == null) {
            encounter$toTarget = _encounter$toTarget.stream()
                    .map(ResourceLocation::tryParse)
                    .filter(Objects::nonNull)
                    .map(BuiltInRegistries.ENTITY_TYPE::get)
                    .toList();
        }
        return encounter$toTarget;
    }

    @Override
    public List<? extends EntityType<?>> encounter$entitiesToAvoid() {
        if (encounter$toAvoid == null) {
            encounter$toAvoid = _encounter$toAvoid.stream()
                    .map(ResourceLocation::tryParse)
                    .filter(Objects::nonNull)
                    .map(BuiltInRegistries.ENTITY_TYPE::get)
                    .toList();
        }
        return encounter$toAvoid;
    }

    @Override
    public void encounter$markEntitiesToTarget(List<String> toTarget) {
        this.encounter$toTarget = null;
        this._encounter$toTarget.clear();
        this._encounter$toTarget.addAll(toTarget);
    }

    @Override
    public void encounter$markEntitiesToAvoid(List<String> toAvoid) {
        this.encounter$toAvoid = null;
        this._encounter$toAvoid.clear();
        this._encounter$toAvoid.addAll(toAvoid);
    }
}
