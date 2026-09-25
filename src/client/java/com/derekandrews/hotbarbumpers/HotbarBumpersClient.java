package com.derekandrews.hotbarbumpers;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ScrollWheelHandler;
import net.minecraft.resources.Identifier;

public final class HotbarBumpersClient implements ClientModInitializer {
	private static final KeyMapping.Category KEY_CATEGORY = KeyMapping.Category.register(
			Identifier.fromNamespaceAndPath("hotbarbumpers", "hotbarbumpers")
	);
	private static final KeyMapping NEXT_HOTBAR_SLOT = KeyMappingHelper.registerKeyMapping(
			new KeyMapping(
					"key.hotbarbumpers.next_hotbar_slot",
					InputConstants.Type.KEYBOARD,
					InputConstants.UNKNOWN.getValue(),
					KEY_CATEGORY
			)
	);
	private static final KeyMapping PREVIOUS_HOTBAR_SLOT = KeyMappingHelper.registerKeyMapping(
			new KeyMapping(
					"key.hotbarbumpers.previous_hotbar_slot",
					InputConstants.Type.KEYBOARD,
					InputConstants.UNKNOWN.getValue(),
					KEY_CATEGORY
			)
	);

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (NEXT_HOTBAR_SLOT.consumeClick()) {
				changeHotbarSlot(client, -1.0D);
			}

			while (PREVIOUS_HOTBAR_SLOT.consumeClick()) {
				changeHotbarSlot(client, 1.0D);
			}
		});
	}

	private static void changeHotbarSlot(Minecraft client, double scrollAmount) {
		if (client.player == null || client.gui.screen() != null || client.gui.overlay() != null || client.player.isSpectator()) {
			return;
		}

		var inventory = client.player.getInventory();
		inventory.setSelectedSlot(ScrollWheelHandler.getNextScrollWheelSelection(
				scrollAmount,
				inventory.getSelectedSlot(),
				inventory.getSelectionSize()
		));
	}
}
