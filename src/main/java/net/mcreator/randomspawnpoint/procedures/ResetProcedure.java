package net.mcreator.randomspawnpoint.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.CommandSourceStack;

import net.mcreator.randomspawnpoint.network.RandomSpawnPointModVariables;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;

public class ResetProcedure {
	public static void execute(CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		try {
			for (Entity entityiterator : EntityArgument.getEntities(arguments, "target")) {
				{
					RandomSpawnPointModVariables.PlayerVariables _vars = entityiterator.getData(RandomSpawnPointModVariables.PLAYER_VARIABLES);
					_vars.firstJoin = true;
					_vars.markSyncDirty();
				}
				if (entity instanceof Player _player && !_player.level().isClientSide())
					_player.displayClientMessage(Component.literal(("Successfully reset " + entityiterator.getDisplayName().getString() + "'s first time spawn. Please have them reconnect.")), false);
			}
		} catch (CommandSyntaxException e) {
			e.printStackTrace();
		}
	}
}