package com.lyrica.cameramod;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class CameraEventHandler {
	// ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆
	//                                                     プレイヤーの大きさを変更
	// ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆

	public void setPlayerSize(EntityPlayer player, float height, float width) {
		player.height = height;// height > 0.5f にしないと上付き半ブロックでスニークしたときに動けなくなる(default = 1.8f)
		player.width = width;// (default = 0.6f)
		player.boundingBox.maxX = player.boundingBox.minX + (double) player.width;
		player.boundingBox.maxZ = player.boundingBox.minZ + (double) player.width;
		player.boundingBox.maxY = player.boundingBox.minY + (double) player.height;

		float f2 = player.width % 2.0F;

		if ((double) f2 < 0.375D) {
			player.myEntitySize = Entity.EnumEntitySize.SIZE_1;
		} else if ((double) f2 < 0.75D) {
			player.myEntitySize = Entity.EnumEntitySize.SIZE_2;
		} else if ((double) f2 < 1.0D) {
			player.myEntitySize = Entity.EnumEntitySize.SIZE_3;
		} else if ((double) f2 < 1.375D) {
			player.myEntitySize = Entity.EnumEntitySize.SIZE_4;
		} else if ((double) f2 < 1.75D) {
			player.myEntitySize = Entity.EnumEntitySize.SIZE_5;
		} else {
			player.myEntitySize = Entity.EnumEntitySize.SIZE_6;
		}
	}

	@SubscribeEvent
	public void changePlayerSizeEvent(PlayerTickEvent event) {
		if (event.phase == Phase.END) return;

		EntityPlayer player = event.player;
		float height = CameraMod.playerData.height;
		float width = CameraMod.playerData.width;

		if (player.height != height || player.width != width) {
			this.setPlayerSize(player, height, width);
		}
	}

	// ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆
	//                                                         視点の高さの変更
	// ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆

	public static double cameraZoom = 1.00;
	private float defaultZoom = CameraMod.cameraZoom;
	public static float sensitivity = 1.0f;
	//private EntityRendererAlt renderer;

	// レンダラー
	@SubscribeEvent
	public void renderTick(RenderTickEvent event) {
		if (event.phase != Phase.START) return;

		if (CameraMod.keyZoomInc.isPressed())
			CameraMod.cameraZoom *= CameraMod.cameraFactor / 100.0f + 1.0f;
		if (CameraMod.keyZoomDec.isPressed())
			CameraMod.cameraZoom /= CameraMod.cameraFactor / 100.0f + 1.0f;
		if (CameraMod.keyZoomReset.isPressed())
			CameraMod.cameraZoom = defaultZoom;

		// ズーム
		if (CameraMod.keyZoom.getIsKeyPressed())
			cameraZoom = CameraMod.cameraZoom;
		else
			cameraZoom = 1.0;
		sensitivity = 1.0f / (float)((cameraZoom - 1.0) / CameraMod.cameraSensitivityMag + 1.0);

		//if (mc.theWorld == null || mc.entityRenderer == null)
		//	return;

		//if (CameraMod.enableRenderer) {
			//if (renderer == null)
				//renderer = new EntityRendererAlt(mc);
		//}
	}

	// 視点の変更
	private int prevLoadID = -1;

	@SubscribeEvent
	public void changeEyeHeightEvent(PlayerTickEvent event) {
		if (event.phase == Phase.END) return;

		EntityPlayer player = event.player;
		World world = player.worldObj;
		PlayerData data = CameraMod.playerData;

		// データがロードされた時に視点を更新
		if (prevLoadID != data.getLoadID() && !world.isRemote) {
			setPlayerEyeHeight(player);
			prevLoadID = data.getLoadID();
		}
	}

	public static void setPlayerEyeHeight(EntityPlayer player) {
		if (CameraMod.enableRenderer)
			player.eyeHeight = CameraMod.playerData.eyeHeight - (player.worldObj.isRemote ? 1.5f : 0.0f);
		else
			player.eyeHeight = player.getDefaultEyeHeight();
	}

	// ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆
	//                                                            キーボード
	// ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆

	@SubscribeEvent
	public void keyboardEvent(RenderTickEvent event) {
		if (event.phase == Phase.END)
			return;

		// 一人称視点のときは視点を動かさない
		if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0 || !CameraMod.enableRenderer)
			return;

		// カメラの位置を変える
		CameraData data = CameraMod.cameraData;
		double speed = data.cameraSpeed / 1000.0;
		if (CameraMod.keyLeft.getIsKeyPressed())
			data.cameraX += speed;
		if (CameraMod.keyUp.getIsKeyPressed())
			data.cameraY += speed;
		if (CameraMod.keyRight.getIsKeyPressed())
			data.cameraX -= speed;
		if (CameraMod.keyDown.getIsKeyPressed())
			data.cameraY -= speed;
		if (CameraMod.keyCloser.getIsKeyPressed())
			data.cameraDis -= speed;
		if (CameraMod.keyFurther.getIsKeyPressed())
			data.cameraDis += speed;
	}
}
