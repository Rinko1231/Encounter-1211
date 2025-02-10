package committee.nova.encounter.mixin;

import committee.nova.encounter.api.IHasEncounterGoals;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Mob.class)
public abstract class MixinMob extends LivingEntity {

    @Shadow
    @Final
    public GoalSelector goalSelector;

    @Shadow
    @Final
    public GoalSelector targetSelector;

    protected MixinMob(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void inject$init(EntityType<? extends Mob> entityType, Level level, CallbackInfo ci) {
        if (level != null && !level.isClientSide) {
            encounter$registerEncounterGoals();
        }
    }

    @Unique
    private void encounter$registerEncounterGoals() {
        final IHasEncounterGoals extended = (IHasEncounterGoals) this.getType();
        if ((Object) this instanceof PathfinderMob pm) {
            final List<? extends EntityType<?>> avoid = extended.encounter$entitiesToAvoid();
            if (!avoid.isEmpty()) this.goalSelector.addGoal(1, new AvoidEntityGoal<>(pm,
                    LivingEntity.class, 12.0f, 1.6, 1.4, e -> avoid.contains(e.getType())));
        }
        final List<? extends EntityType<?>> target = extended.encounter$entitiesToTarget();
        if (!target.isEmpty()) this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>((Mob) (Object) this,
                LivingEntity.class, true, e -> target.contains(e.getType())));
    }
}
