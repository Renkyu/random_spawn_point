package net.mcreator.randomspawnpoint.procedures;

import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;

import net.mcreator.randomspawnpoint.network.RandomSpawnPointModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class RandomSpawnLogicProcedure {
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		execute(event, event.getEntity().level(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		double local_x = 0;
		double local_z = 0;
		double local_y = 0;
		if (entity.getData(RandomSpawnPointModVariables.PLAYER_VARIABLES).firstJoin == true) {
			local_x = Mth.nextDouble(RandomSource.create(), -5000, 5000);
			local_z = Mth.nextDouble(RandomSource.create(), -5000, 5000);
			local_y = 320;
			while (world.isEmptyBlock(BlockPos.containing(local_x, local_y, local_z))) {
				local_y = local_y - 1;
			}
			if (world.getBlockState(BlockPos.containing(local_x, local_y, local_z)).canOcclude()) {
				local_y = local_y + 1;
				{
					Entity _ent = entity;
					_ent.teleportTo(local_x, local_y, local_z);
					if (_ent instanceof ServerPlayer _serverPlayer)
						_serverPlayer.connection.teleport(local_x, local_y, local_z, _ent.getYRot(), _ent.getXRot());
				}
				if (entity instanceof ServerPlayer _serverPlayer)
					_serverPlayer.setRespawnPosition(_serverPlayer.level().dimension(), BlockPos.containing(local_x, local_y, local_z), _serverPlayer.getYRot(), true, false);
				{
					RandomSpawnPointModVariables.PlayerVariables _vars = entity.getData(RandomSpawnPointModVariables.PLAYER_VARIABLES);
					_vars.firstJoin = false;
					_vars.markSyncDirty();
				}
			} else {
				RandomSpawnLogicProcedure.execute(world, entity);
			}
		}
	}
}