package mekanism.common.base;

import com.mojang.authlib.GameProfile;
import java.lang.ref.WeakReference;
import java.util.UUID;
import java.util.function.Function;
import javax.annotation.Nonnull;
import mekanism.common.Mekanism;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;

/**
 * Global, shared FakePlayer for Mekanism-specific uses
 *
 * This was introduced to fix https://github.com/dizzyd/Mekanism/issues/2. In that issue, another mod was trying to apply a potion to the fake player and causing the
 * whole system to crash due to essential potion related structures not being initialized for a fake player.
 *
 * The broader problem is that the FakePlayer in Forge 14.23.5.2768 isn't really complete and short of patching Forge and requiring everyone in the world to upgrade,
 * there's no easy fix -- so we introduce our own FakePlayer that will let us override other methods as necessary.
 *
 * Use of the fake player is via a consumer type lambda, where usage is only valid inside the lambda. Afterwards it may be garbage collected at any point.
 *
 * Supports emulating a specific UUID, for use with TileComponentSecurity
 */
public class MekFakePlayer extends FakePlayer {

    private static WeakReference<MekFakePlayer> INSTANCE;

    /**
     * UUID of a player we are pretending to be, null to use the default Mek one
     */
    private UUID emulatingUUID = null;

    private MekFakePlayer(ServerLevel world) {
        super(world, new FakeGameProfile());
        ((FakeGameProfile) this.getGameProfile()).myFakePlayer = this;
    }

    @Override
    public boolean canBeAffected(@Nonnull MobEffectInstance effect) {
        return false;
    }

    public void setEmulatingUUID(UUID uuid) {
        this.emulatingUUID = uuid;
    }

    @Nonnull
    @Override
    public UUID getUUID() {
        return this.emulatingUUID != null ? this.emulatingUUID : super.getUUID();
    }

    @Override
    public Vec3 position() {
        //Provide the actual position that forge's fake player hides in this method
        return new Vec3(getX(), getY(), getZ());
    }

    @Override
    public BlockPos blockPosition() {
        //Provide the actual block position that forge's fake player hides in this method
        return new BlockPos(getBlockX(), getBlockY(), getBlockZ());
    }

    /**
     * Acquire a Fake Player and call a function which makes use of the player. Afterwards, the Fake Player's world is nulled out to prevent GC issues. Emulated UUID is
     * also reset.
     *
     * Do NOT store a reference to the Fake Player, so that it may be Garbage Collected. A fake player _should_ only need to be short-lived
     *
     * @param world              World to set on the fake player
     * @param fakePlayerConsumer consumer of the fake player
     * @param <R>                Result of a computation, etc
     *
     * @return the return value of fakePlayerConsumer
     */
    @SuppressWarnings("WeakerAccess")
    public static <R> R withFakePlayer(ServerLevel world, Function<MekFakePlayer, R> fakePlayerConsumer) {
        MekFakePlayer actual = INSTANCE != null ? INSTANCE.get() : null;
        if (actual == null) {
            actual = new MekFakePlayer(world);
            INSTANCE = new WeakReference<>(actual);
        }
        MekFakePlayer player = actual;
        player.level = world;
        R result = fakePlayerConsumer.apply(player);
        player.emulatingUUID = null;
        player.level = null;//don't keep reference to the World
        return result;
    }

    /**
     * Same as {@link MekFakePlayer#withFakePlayer(ServerLevel, java.util.function.Function)} but sets the Fake Player's position. Use when you think the entity position
     * is relevant.
     *
     * @param world              World to set on the fake player
     * @param fakePlayerConsumer consumer of the fake player
     * @param x                  X pos to set
     * @param y                  Y pos to set
     * @param z                  Z pos to set
     * @param <R>                Result of a computation, etc
     *
     * @return the return value of fakePlayerConsumer
     */
    public static <R> R withFakePlayer(ServerLevel world, double x, double y, double z, Function<MekFakePlayer, R> fakePlayerConsumer) {
        return withFakePlayer(world, fakePlayer -> {
            fakePlayer.setPosRaw(x, y, z);
            return fakePlayerConsumer.apply(fakePlayer);
        });
    }

    public static void releaseInstance(LevelAccessor world) {
        // If the fake player has a reference to the world getting unloaded,
        // null out the fake player so that the world can unload
        MekFakePlayer actual = INSTANCE != null ? INSTANCE.get() : null;
        if (actual != null && actual.level == world) {
            actual.level = null;
        }
    }

    /**
     * Game profile supporting our UUID emulation
     */
    private static class FakeGameProfile extends GameProfile {

        private MekFakePlayer myFakePlayer = null;

        public FakeGameProfile() {
            super(Mekanism.gameProfile.getId(), Mekanism.gameProfile.getName());
        }

        private UUID getEmulatingUUID() {
            return myFakePlayer != null ? myFakePlayer.emulatingUUID : null;
        }

        @Override
        public UUID getId() {
            UUID emulatingUUID = getEmulatingUUID();
            return emulatingUUID != null ? emulatingUUID : super.getId();
        }

        @Override
        public String getName() {
            UUID emulatingUUID = getEmulatingUUID();
            return emulatingUUID != null ? MekanismUtils.getLastKnownUsername(emulatingUUID) : super.getName();
        }

        //NB: super check they're the same class, we only check that name & id match
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof GameProfile that)) {
                return false;
            }
            if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) {
                return false;
            }
            return getName() != null ? getName().equals(that.getName()) : that.getName() == null;
        }

        @Override
        public int hashCode() {
            int result = getId() != null ? getId().hashCode() : 0;
            result = 31 * result + (getName() != null ? getName().hashCode() : 0);
            return result;
        }
    }
}