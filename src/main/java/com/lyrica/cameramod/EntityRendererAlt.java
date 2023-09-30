package com.lyrica.cameramod;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityRendererAlt {
	private static Minecraft mc;
	private static double cameraDistance;
	private static Vec3 cameraDifVec;
	private static double cameraDefaultDis;

	// リフレクション
	private static Field cameraZoom;
	private static Field prevCamRoll;
	private static Field camRoll;
	private static Field thirdPersonDistanceTemp;
	private static Field thirdPersonDistance;
	private static Field prevDebugCamYaw;
	private static Field debugCamYaw;
	private static Field prevDebugCamPitch;
	private static Field debugCamPitch;
	private static Field cloudFog;
	private static Field pointedEntity;
	private static Field fovModifierHand;
	private static Field fovModifierHandPrev;
	private static Field fovMultiplierTemp;

	private static Method updateFovModifierHand;
	private static Method orientCamera;

	public static void Init(Minecraft minecraft) {
		mc = minecraft;

		// EntityRenderer のField and Method を取得
		Class<EntityRenderer> c = EntityRenderer.class;

		if (CameraMod.DEBUG)
		{
			cameraZoom = 				ReflectionHelper.getField(c, "cameraZoom");
			prevCamRoll = 				ReflectionHelper.getField(c, "prevCamRoll");
			camRoll = 					ReflectionHelper.getField(c, "camRoll");
			thirdPersonDistanceTemp = 	ReflectionHelper.getField(c, "thirdPersonDistanceTemp");
			thirdPersonDistance = 		ReflectionHelper.getField(c, "thirdPersonDistance");
			prevDebugCamYaw = 			ReflectionHelper.getField(c, "prevDebugCamYaw");
			debugCamYaw = 				ReflectionHelper.getField(c, "debugCamYaw");
			prevDebugCamPitch = 		ReflectionHelper.getField(c, "prevDebugCamPitch");
			debugCamPitch = 			ReflectionHelper.getField(c, "debugCamPitch");
			cloudFog = 					ReflectionHelper.getField(c, "cloudFog");
			pointedEntity = 			ReflectionHelper.getField(c, "pointedEntity");
			fovModifierHand =			ReflectionHelper.getField(c, "fovModifierHand");
			fovModifierHandPrev =		ReflectionHelper.getField(c, "fovModifierHandPrev");
			fovMultiplierTemp =			ReflectionHelper.getField(c, "fovMultiplierTemp");

			updateFovModifierHand = 	ReflectionHelper.getMethod(c, "updateFovModifierHand");
			orientCamera = 				ReflectionHelper.getMethod(c, "orientCamera", float.class);
		}
		else
		{
			cameraZoom = 				ReflectionHelper.getField(c, "field_78503_V");
			prevCamRoll = 				ReflectionHelper.getField(c, "field_78505_P");
			camRoll = 					ReflectionHelper.getField(c, "field_78495_O");
			thirdPersonDistanceTemp = 	ReflectionHelper.getField(c, "field_78491_C");
			thirdPersonDistance = 		ReflectionHelper.getField(c, "field_78490_B");
			prevDebugCamYaw = 			ReflectionHelper.getField(c, "field_78486_E");
			debugCamYaw = 				ReflectionHelper.getField(c, "field_78485_D");
			prevDebugCamPitch = 		ReflectionHelper.getField(c, "field_78488_G");
			debugCamPitch = 			ReflectionHelper.getField(c, "field_78487_F");
			cloudFog = 					ReflectionHelper.getField(c, "field_78500_U");
			pointedEntity = 			ReflectionHelper.getField(c, "field_78528_u");
			fovModifierHand = 			ReflectionHelper.getField(c, "field_78507_R");
			fovModifierHandPrev = 		ReflectionHelper.getField(c, "field_78506_S");
			fovMultiplierTemp = 		ReflectionHelper.getField(c, "field_78501_T");

			updateFovModifierHand = 	ReflectionHelper.getMethod(c, "func_78477_e");
			orientCamera = 				ReflectionHelper.getMethod(c, "func_78467_g", float.class);
		}
	}

	public static float getOffsetY()
	{
		return CameraMod.playerData.eyeHeight - 1.62f;
	}

	/*
	@Override
	public void getMouseOver(float partialTick) {
		if (mc.thePlayer == null || mc.thePlayer.isPlayerSleeping()) {
			super.getMouseOver(partialTick);
			return;
		}
		// adjust the y position to get a mouseover at eye-level
		// not perfect, as the server posY does not match, meaning
		// that some block clicks do not process correctly
		// (distance check or something like that)
		EntityPlayer player = mc.thePlayer;
		player.eyeHeight = player.getDefaultEyeHeight();
		float offsetY = getOffsetY();

		player.posY += offsetY;
		player.prevPosY += offsetY;
		player.lastTickPosY += offsetY;
		//getMouseOver2(partialTick);
		super.getMouseOver(partialTick);
		player.posY -= offsetY;
		player.prevPosY -= offsetY;
		player.lastTickPosY -= offsetY;

		MyModEventHandler.setPlayerEyeHeight(player);
	}//*/

	public static void _getMouseOver1() {
		if (!CameraMod.enableRenderer || mc == null || mc.thePlayer == null || mc.thePlayer.isPlayerSleeping()) {
			return;
		}

		EntityPlayer player = mc.thePlayer;
		float offsetY = getOffsetY();

		player.posY += offsetY;
		player.prevPosY += offsetY;
		player.lastTickPosY += offsetY;
		player.eyeHeight = player.getDefaultEyeHeight();
	}

	public static void _getMouseOver2() {
		if (!CameraMod.enableRenderer || mc == null || mc.thePlayer == null || mc.thePlayer.isPlayerSleeping()) {
			return;
		}

		EntityPlayer player = mc.thePlayer;
		float offsetY = getOffsetY();

		player.posY -= offsetY;
		player.prevPosY -= offsetY;
		player.lastTickPosY -= offsetY;

		CameraEventHandler.setPlayerEyeHeight(player);
	}

	// ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆
	//                                                             FOVの調節
	// ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆

	/**
	 * Update FOV modifier hand
	 */
	public static void _updateFovModifierHand(EntityRenderer renderer) {
		if (!CameraMod.enableRenderer) {
			ReflectionHelper.invokeMethod(renderer, updateFovModifierHand);
			return;
		}

		if (mc.renderViewEntity instanceof EntityPlayerSP) {
			EntityPlayerSP player = (EntityPlayerSP) mc.renderViewEntity;
			ReflectionHelper.setField(renderer, fovMultiplierTemp, getFOVMultiplier(player, renderer));
		} else {
			ReflectionHelper.setField(renderer, fovMultiplierTemp, getFOVMultiplier(mc.thePlayer, renderer));
		}
		ReflectionHelper.setField(renderer, fovModifierHandPrev, ReflectionHelper.getFloatField(renderer, fovModifierHand));
		ReflectionHelper.setField(renderer, fovModifierHand, ReflectionHelper.getFloatField(renderer, fovModifierHand) + (ReflectionHelper.getFloatField(renderer, fovMultiplierTemp) - ReflectionHelper.getFloatField(renderer, fovModifierHand)) * 0.5f);
		//this.fovModifierHand += (this.fovMultiplierTemp - this.fovModifierHand) * 0.5F;

		if (ReflectionHelper.getFloatField(renderer, fovModifierHand) > 1.5F) {
			ReflectionHelper.setField(renderer, fovModifierHand, 1.5f);
			//this.fovModifierHand = 1.5F;
		}

		if (ReflectionHelper.getFloatField(renderer, fovModifierHand) < 0.1F) {
			ReflectionHelper.setField(renderer, fovModifierHand, 0.1f);
			//this.fovModifierHand = 0.1F;
		}
    }

	static LinkedList<Float> prevWalkSpeed = new LinkedList<Float>();
	static float prevFOV = 1.0f;
	static int slashBladeCount = 0;

	private static float getFOVMultiplier(EntityPlayer player, EntityRenderer renderer) {
		// 抜刀剣を持ってスニークをしているときはFOVを変えない
		--slashBladeCount;
		if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem().delegate.name().contains("slashblade:slashblade") && player.isSneaking()) {
			slashBladeCount = 5;
			return prevFOV;
		}
		if (slashBladeCount >= 0)
			return prevFOV;

		float fov = 1.0F;

		if (player.capabilities.isFlying) {
			fov *= 1.1F;
		}

		ReflectionHelper.setField(renderer, cameraZoom, CameraEventHandler.cameraZoom);

		float walkSpeed = 1.0f;

		// 現在のスピードからFOVを計算する
		float newFOV;
		newFOV = fov * getFOV(player, walkSpeed);

		// 前回のFOVと同じ場合 prevWalkSpeed をリセットする
		if (newFOV == prevFOV) {
			prevWalkSpeed = new LinkedList<Float>();
			fov = newFOV;
		} else {
			// 違っていた場合 prevWalkSpeed から計算し直して同じになるか試す
			tryFOV: {
				float newFOV2;
				for (float walkSpeed2 : prevWalkSpeed) {
					if (walkSpeed == walkSpeed2)
						continue;
					newFOV2 = fov * getFOV(player, walkSpeed2);
					// 同じになった場合これを採用する
					if (newFOV2 == prevFOV) {
						fov = newFOV2;
						break tryFOV;
					}
				}
				// 同じにならなかった場合 prevFOV を更新して prevWalkSpeed をリセットする
				prevFOV = newFOV;
				prevWalkSpeed = new LinkedList<Float>();
				fov = newFOV;
			}

		}
		// prevWalkSpeed に現在の値が入っていなければ追加する
		if (!prevWalkSpeed.contains(walkSpeed))
			prevWalkSpeed.add(walkSpeed);

		if (player.capabilities.getWalkSpeed() == 0.0F || Float.isNaN(fov) || Float.isInfinite(fov)) {
			fov = 1.0F;
		}

		if (player.isUsingItem() && player.getItemInUse().getItem() == Items.bow) {
			int i = player.getItemInUseDuration();
			float f1 = (float) i / 20.0F;

			if (f1 > 1.0F) {
				f1 = 1.0F;
			} else {
				f1 *= f1;
			}

			fov *= 1.0F - f1 * 0.15F;
		}

		return fov;
	}

	private static float getFOV(EntityPlayer player, float speed) {
		IAttributeInstance iattributeinstance = player.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
		return (float) ((iattributeinstance.getAttributeValue() / (double) player.capabilities.getWalkSpeed() / speed + 1.0D) / 2.0D);
	}

	// ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆
	//                                                      3人称視点のカメラを調節
	// ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆

	/**
     * sets up player's eye (or camera in third person mode)
     */
    public static void _orientCamera(EntityRenderer renderer, float p_78467_1_) {
    	if (!CameraMod.enableRenderer) {
    		ReflectionHelper.invokeMethod(renderer, orientCamera, p_78467_1_);
    		return;
    	}

    	ReflectionHelper.invokeMethod(renderer, orientCamera, p_78467_1_);

    	EntityLivingBase entitylivingbase = mc.renderViewEntity;
    	float f1 = entitylivingbase.yOffset - 1.62F;
    	double d0 = entitylivingbase.prevPosX + (entitylivingbase.posX - entitylivingbase.prevPosX) * (double)p_78467_1_;
    	double d1 = entitylivingbase.prevPosY + (entitylivingbase.posY - entitylivingbase.prevPosY) * (double)p_78467_1_ - (double)f1;
    	double d2 = entitylivingbase.prevPosZ + (entitylivingbase.posZ - entitylivingbase.prevPosZ) * (double)p_78467_1_;
    	GL11.glRotatef(ReflectionHelper.getFloatField(renderer, prevCamRoll) + (ReflectionHelper.getFloatField(renderer, camRoll) - ReflectionHelper.getFloatField(renderer, prevCamRoll)) * p_78467_1_, 0.0F, 0.0F, 1.0F);

    	if (entitylivingbase.isPlayerSleeping()) {
    		f1 = (float)((double)f1 + 1.0D);
    		GL11.glTranslatef(0.0F, 0.3F, 0.0F);

    		if (!mc.gameSettings.debugCamEnable) {
    			//ForgeHooksClient.orientBedCamera(mc, entitylivingbase);
    			GL11.glRotatef(entitylivingbase.prevRotationYaw + (entitylivingbase.rotationYaw - entitylivingbase.prevRotationYaw) * p_78467_1_ + 180.0F, 0.0F, -1.0F, 0.0F);
    			GL11.glRotatef(entitylivingbase.prevRotationPitch + (entitylivingbase.rotationPitch - entitylivingbase.prevRotationPitch) * p_78467_1_, -1.0F, 0.0F, 0.0F);
    		}
    	}
    	else if (mc.gameSettings.thirdPersonView > 0) {
    		double d7 = (double)(ReflectionHelper.getFloatField(renderer, thirdPersonDistanceTemp) + (ReflectionHelper.getFloatField(renderer, thirdPersonDistance) - ReflectionHelper.getFloatField(renderer, thirdPersonDistanceTemp)) * p_78467_1_);
    		// ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆
    		//                                                       カメラの位置を変える
    		// ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆
    		d7 += CameraMod.cameraData.cameraDis;

    		float f2;
    		float f6;

    		if (mc.gameSettings.debugCamEnable) {
    			f6 = ReflectionHelper.getFloatField(renderer, prevDebugCamYaw) + (ReflectionHelper.getFloatField(renderer, debugCamYaw) - ReflectionHelper.getFloatField(renderer, prevDebugCamYaw)) * p_78467_1_;
    			f2 = ReflectionHelper.getFloatField(renderer, prevDebugCamPitch) + (ReflectionHelper.getFloatField(renderer, debugCamPitch) - ReflectionHelper.getFloatField(renderer, prevDebugCamPitch)) * p_78467_1_;
    			GL11.glTranslatef(0.0F, 0.0F, (float)(-d7));
    			GL11.glRotatef(f2, 1.0F, 0.0F, 0.0F);
    			GL11.glRotatef(f6, 0.0F, 1.0F, 0.0F);
    		}
    		else
    		{
    			f6 = entitylivingbase.rotationYaw;
    			f2 = entitylivingbase.rotationPitch;

    			if (mc.gameSettings.thirdPersonView == 2)
				{
					f2 += 180.0F;
				}

    			CameraData data = CameraMod.cameraData;
    			double cameraX = data.cameraX;
    			double temp0D7 = d7;
    			double temp1D7 = cameraDefaultDis;

    			// 視点を阻まれるか確認
    			d7 = cameraDistance;//getCameraDistance(p_78467_1_);

    			if (mc.gameSettings.thirdPersonView == 2)
    			{
    				GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
    			}

    			GL11.glRotatef(entitylivingbase.rotationPitch - f2, 1.0F, 0.0F, 0.0F);
    			GL11.glRotatef(entitylivingbase.rotationYaw - f6, 0.0F, 1.0F, 0.0F);
    			if (temp1D7 != 0)
    				GL11.glTranslatef((float)(cameraX * d7 / temp1D7), 0.0f, (float)(-temp0D7 * d7 / temp1D7));
    			GL11.glRotatef(f6 - entitylivingbase.rotationYaw, 0.0F, 1.0F, 0.0F);
    			GL11.glRotatef(f2 - entitylivingbase.rotationPitch, 1.0F, 0.0F, 0.0F);
    		}
    	}
    	else
    	{
    		GL11.glTranslatef(0.0F, 0.0F, -0.1F);
    	}

    	if (!mc.gameSettings.debugCamEnable)
    	{
    		GL11.glRotatef(entitylivingbase.prevRotationPitch + (entitylivingbase.rotationPitch - entitylivingbase.prevRotationPitch) * p_78467_1_, 1.0F, 0.0F, 0.0F);
    		GL11.glRotatef(entitylivingbase.prevRotationYaw + (entitylivingbase.rotationYaw - entitylivingbase.prevRotationYaw) * p_78467_1_ + 180.0F, 0.0F, 1.0F, 0.0F);
    	}

    	GL11.glTranslatef(0.0F, f1, 0.0F);
    	//d0 = entitylivingbase.prevPosX + (entitylivingbase.posX - entitylivingbase.prevPosX) * (double)p_78467_1_;
    	//d1 = entitylivingbase.prevPosY + (entitylivingbase.posY - entitylivingbase.prevPosY) * (double)p_78467_1_ - (double)f1;
    	//d2 = entitylivingbase.prevPosZ + (entitylivingbase.posZ - entitylivingbase.prevPosZ) * (double)p_78467_1_;

    	//ReflectionHelper.setField(renderer, cloudFog, mc.renderGlobal.hasCloudFog(d0, d1, d2, p_78467_1_));
    }

    /** 必ず getCameraDefaultDis を先に呼ぶこと **/
    private static void getCameraDistance(float p_78467_1_, EntityRenderer renderer) {
    	EntityLivingBase entitylivingbase = mc.renderViewEntity;
    	float f1 = entitylivingbase.yOffset - 1.62F;
    	double d0 = entitylivingbase.prevPosX + (entitylivingbase.posX - entitylivingbase.prevPosX) * (double)p_78467_1_;
    	double d1 = entitylivingbase.prevPosY + (entitylivingbase.posY - entitylivingbase.prevPosY) * (double)p_78467_1_ - (double)f1;
    	double d2 = entitylivingbase.prevPosZ + (entitylivingbase.posZ - entitylivingbase.prevPosZ) * (double)p_78467_1_;

    	double d7 = (double)(ReflectionHelper.getFloatField(renderer, thirdPersonDistanceTemp) + (ReflectionHelper.getFloatField(renderer, thirdPersonDistance) - ReflectionHelper.getFloatField(renderer, thirdPersonDistanceTemp)) * p_78467_1_);
		d7 += CameraMod.cameraData.cameraDis;

		float f6 = entitylivingbase.rotationYaw;
		float f2 = entitylivingbase.rotationPitch;

		if (mc.gameSettings.thirdPersonView == 2)
			f2 += 180.0F;

		double d3 = (double)(-MathHelper.sin(f6 / 180.0F * (float)Math.PI) * MathHelper.cos(f2 / 180.0F * (float)Math.PI)) * d7;
		double d4 = (double)(MathHelper.cos(f6 / 180.0F * (float)Math.PI) * MathHelper.cos(f2 / 180.0F * (float)Math.PI)) * d7;
		double d5 = (double)(-MathHelper.sin(f2 / 180.0F * (float)Math.PI)) * d7;

		d7 = cameraDefaultDis;

		for (int k = 0; k < 8; ++k)
		{
			float f3 = (float)((k & 1) * 2 - 1);
			float f4 = (float)((k >> 1 & 1) * 2 - 1);
			float f5 = (float)((k >> 2 & 1) * 2 - 1);
			float f = 0.1f;
			f3 *= f;
			f4 *= f;
			f5 *= f;

			// ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆
			//                                         登録されたブロックでは視界を阻まれないようにする
			// ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆
			Vec3 vec30 = Vec3.createVectorHelper(d0 + (double)f3, d1 + (double)f4, d2 + (double)f5);
			Vec3 vec31 = Vec3.createVectorHelper(d0 - d3 + (double)f3 + (double)f5, d1 - d5 + (double)f4, d2 - d4 + (double)f5);
			Vec3 vec3 = getMODistance(vec30, vec31);
			if (vec3 != null) {
				double d6 = vec3.distanceTo(Vec3.createVectorHelper(d0, d1, d2));
				if (d6 < d7)
					d7 = d6;
			}
		}

		double d = mc.gameSettings.viewBobbing ? 0.05 : 0;

		cameraDistance = Math.max(0, d7 - Math.abs(CameraMod.cameraData.cameraX) * 0.01 - d);

		return;
	}

    private static void getCameraDefaultDis(float p_78467_1_, EntityRenderer renderer) {
    	EntityLivingBase entitylivingbase = mc.renderViewEntity;
    	float f1 = entitylivingbase.yOffset - 1.62F;
    	double d0 = entitylivingbase.prevPosX + (entitylivingbase.posX - entitylivingbase.prevPosX) * (double)p_78467_1_;
    	double d1 = entitylivingbase.prevPosY + (entitylivingbase.posY - entitylivingbase.prevPosY) * (double)p_78467_1_ - (double)f1;
    	double d2 = entitylivingbase.prevPosZ + (entitylivingbase.posZ - entitylivingbase.prevPosZ) * (double)p_78467_1_;

    	double d7 = (double)(ReflectionHelper.getFloatField(renderer, thirdPersonDistanceTemp) + (ReflectionHelper.getFloatField(renderer, thirdPersonDistance) - ReflectionHelper.getFloatField(renderer, thirdPersonDistanceTemp)) * p_78467_1_);
		d7 += CameraMod.cameraData.cameraDis;

		float f6 = entitylivingbase.rotationYaw;
		float f2 = entitylivingbase.rotationPitch;

		if (mc.gameSettings.thirdPersonView == 2)
			f2 += 180.0F;

		double d3 = (double)(-MathHelper.sin(f6 / 180.0F * (float)Math.PI) * MathHelper.cos(f2 / 180.0F * (float)Math.PI)) * d7;
		double d4 = (double)(MathHelper.cos(f6 / 180.0F * (float)Math.PI) * MathHelper.cos(f2 / 180.0F * (float)Math.PI)) * d7;
		double d5 = (double)(-MathHelper.sin(f2 / 180.0F * (float)Math.PI)) * d7;

		getCmeraDif();
		Vec3 vec31 = Vec3.createVectorHelper(d0 - d3, d1 - d5, d2 - d4).addVector(cameraDifVec.xCoord, cameraDifVec.yCoord, cameraDifVec.zCoord);
		cameraDefaultDis = Vec3.createVectorHelper(d0, d1, d2).distanceTo(vec31);
    }

    private static void getCmeraDif() {
    	// カメラを元の位置から動かした差分の座標
    	CameraData data = CameraMod.cameraData;
    	EntityLivingBase entitylivingbase = mc.renderViewEntity;
		float f6 = entitylivingbase.rotationYaw;
		float x = MathHelper.cos(f6 / 180.0F * (float)Math.PI);
		float z = MathHelper.sin(f6 / 180.0F * (float)Math.PI);
		cameraDifVec = Vec3.createVectorHelper(x, 0, z).normalize();
		cameraDifVec = Vec3.createVectorHelper(cameraDifVec.xCoord * data.cameraX, data.cameraY, cameraDifVec.zCoord * data.cameraX);
    }

	private static Vec3 getMODistance(Vec3 vec30, Vec3 vec31) {
		{
			// lookVec に垂直で posVec を通る平面で y = 0 に平行なベクトル
			//Vec3 newVec = Vec3.createVectorHelper(-vec3.zCoord, 0, vec3.xCoord).normalize();
			// カメラを移動させた分座標も移動させる
			vec31 = vec31.addVector(cameraDifVec.xCoord, cameraDifVec.yCoord, cameraDifVec.zCoord);
    	}

		Vec3 vec3 = vec31.addVector(-vec30.xCoord, -vec30.yCoord, -vec30.zCoord);
		vec3 = vec3.normalize();

		// 長さを短くしてブロックを飛び越えにくくする
		double d = 0.0001d;
		vec3.xCoord *= d;
		vec3.yCoord *= d;
		vec3.zCoord *= d;
		//Vec3 tempVec3 = null;
		World world = mc.theWorld;
		Vec3 pos = Vec3.createVectorHelper(vec30.xCoord, vec30.yCoord, vec30.zCoord);
		double distance = pos.squareDistanceTo(vec31);

		int debugCount = 0;

		while(true) {
			MovingObjectPosition moPos = mc.theWorld.rayTraceBlocks(vec30, vec31);

			if (moPos != null) {
				LinkedList<BlockMetaPair> blocks = new LinkedList<BlockMetaPair>();
				int x, y, z;
				x = moPos.blockX; y = moPos.blockY; z = moPos.blockZ;
				blocks.add(new BlockMetaPair(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z)));

				if (moPos.blockX == moPos.hitVec.xCoord && vec3.xCoord < 0) {
					x = moPos.blockX - 1; y = moPos.blockY; z = moPos.blockZ;
					blocks.add(new BlockMetaPair(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z)));
				}
				if (moPos.blockX == moPos.hitVec.xCoord - 1 && vec3.xCoord > 0) {
					x = moPos.blockX + 1; y = moPos.blockY; z = moPos.blockZ;
					blocks.add(new BlockMetaPair(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z)));
				}
				if (moPos.blockY == moPos.hitVec.yCoord && vec3.yCoord < 0) {
					x = moPos.blockX; y = moPos.blockY - 1; z = moPos.blockZ;
					blocks.add(new BlockMetaPair(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z)));
				}
				if (moPos.blockY == moPos.hitVec.yCoord - 1 && vec3.yCoord > 0) {
					x = moPos.blockX; y = moPos.blockY + 1; z = moPos.blockZ;
					blocks.add(new BlockMetaPair(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z)));
				}
				if (moPos.blockZ == moPos.hitVec.zCoord && vec3.zCoord < 0) {
					x = moPos.blockX; y = moPos.blockY; z = moPos.blockZ - 1;
					blocks.add(new BlockMetaPair(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z)));
				}
				if (moPos.blockZ == moPos.hitVec.zCoord - 1 && vec3.zCoord > 0) {
					x = moPos.blockX; y = moPos.blockY; z = moPos.blockZ + 1;
					blocks.add(new BlockMetaPair(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z)));
				}

				Block block;
				int meta;
				for(BlockMetaPair pair : blocks) {
					block = pair.block;
					meta = pair.meta;
					Material blockMaterial = block.getMaterial();

					boolean throughFlag =
							blockMaterial == Material.plants || blockMaterial == Material.leaves || blockMaterial == Material.circuits ||
							blockMaterial == Material.vine || blockMaterial == Material.dragonEgg || blockMaterial == Material.web;

					throughFlag = throughFlag || !block.canCollideCheck(meta, false);

					// whitelist の確認
					for (String name : CameraMod.cameraWhiteList) {
						if (block.delegate.name().contains(name)) {
							throughFlag = true;
							break;
						}
					}
					// blacklist の確認
					for (String name : CameraMod.cameraBlackList) {
						if (block.delegate.name().contains(name)) {
							throughFlag = false;
							break;
						}
					}

					// 視界を阻むブロックならそこまでの距離を返す
					if (!throughFlag) {
						return moPos.hitVec;
					}
				}
				// 視界を阻まれないブロックなら

				// 少しだけベクトルを足す
				vec30 = moPos.hitVec.addVector(vec3.xCoord, vec3.yCoord, vec3.zCoord);

				// 目的座標を通り越してないか
				if (pos.squareDistanceTo(vec30) > distance) {
					return null;
				}

				// 同じブロックを判定していないか
				//if (tempVec3 != null && tempVec3.xCoord == vec30.xCoord && tempVec3.yCoord == vec30.yCoord && tempVec3.zCoord == vec30.zCoord) {
				//	chat("" + debugCount);
				//	return null;
				//}
				//tempVec3 = Vec3.createVectorHelper(vec30.xCoord, vec30.yCoord, vec30.zCoord);

				// 判定をやり直す
				continue;
			} else {
				return null;
			}
		}
	}

	//private void chat(String text) {
	//	Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(text));
	//}

	// ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆
	//                                                       プレイヤーの高さ変更
	// ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆

	public static void setCameraHeight(EntityRenderer renderer, float p_78471_1_) {
		if (!CameraMod.enableRenderer)
			return;

		EntityPlayer player = mc.thePlayer;
		player.yOffset -= getOffsetY();
		if (mc.gameSettings.thirdPersonView > 0) {
			getCameraDefaultDis(p_78471_1_, renderer);
			getCameraDistance(p_78471_1_, renderer);
			if (cameraDefaultDis != 0) {
				CameraData data = CameraMod.cameraData;
				player.yOffset -= data.cameraY * cameraDistance / cameraDefaultDis;
			}
		}
	}

	public static float getMouseSensitivity(float f) {
		if (!CameraMod.enableRenderer)
			return f;
		return f * CameraEventHandler.sensitivity;
	}
/*
	//@Override
	public void _renderWorld(float p_78471_1_, long p_78471_2_) {
		//Class<EntityRenderer> c = EntityRenderer.class;
		//try {// - - - - - - - - - - - - - -  - - - - - - - - - - -  --  - - - - - - -

		this.mc.mcProfiler.startSection("lightTex");

		if (this.lightmapUpdateNeeded) {
			this.updateLightmap(p_78471_1_);
		}

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.5F);

		if (this.mc.renderViewEntity == null) {
			this.mc.renderViewEntity = this.mc.thePlayer;
		}

		this.mc.mcProfiler.endStartSection("pick");
		this.getMouseOver(p_78471_1_);
		EntityLivingBase entitylivingbase = this.mc.renderViewEntity;
		RenderGlobal renderglobal = this.mc.renderGlobal;
		EffectRenderer effectrenderer = this.mc.effectRenderer;
		double d0 = entitylivingbase.lastTickPosX + (entitylivingbase.posX - entitylivingbase.lastTickPosX) * (double)p_78471_1_;
		double d1 = entitylivingbase.lastTickPosY + (entitylivingbase.posY - entitylivingbase.lastTickPosY) * (double)p_78471_1_;
		double d2 = entitylivingbase.lastTickPosZ + (entitylivingbase.posZ - entitylivingbase.lastTickPosZ) * (double)p_78471_1_;
		this.mc.mcProfiler.endStartSection("center");

		for (int j = 0; j < 2; ++j) {
			if (this.mc.gameSettings.anaglyph) {
				anaglyphField = j;

				if (anaglyphField == 0) {
					GL11.glColorMask(false, true, true, false);
				} else {
					GL11.glColorMask(true, false, false, false);
				}
			}
			//super.renderWorld(p_78471_1_, p_78471_2_);
			// ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆
			//                                                           ここから編集
			// ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆

			EntityPlayer player = mc.thePlayer;
			player.yOffset -= getOffsetY();
			if (this.mc.gameSettings.thirdPersonView > 0) {
				getCameraDefaultDis(p_78471_1_);
				getCameraDistance(p_78471_1_);
				if (cameraDefaultDis != 0) {
					OtherData data = MyMod_v2.otherData;
					player.yOffset -= data.cameraY * cameraDistance / cameraDefaultDis;
				}
			}

			this.mc.mcProfiler.endStartSection("clear");
			GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
			this.updateFogColor(p_78471_1_);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glEnable(GL11.GL_CULL_FACE);
			this.mc.mcProfiler.endStartSection("camera");


			//Method setupCameraTransform = c.getDeclaredMethod("setupCameraTransform", float.class, int.class);
			//setupCameraTransform.setAccessible(true);
			//setupCameraTransform.invoke(this, p_78471_1_, j);
			this._setupCameraTransform(p_78471_1_, j);

			ActiveRenderInfo.updateRenderInfo(this.mc.thePlayer, this.mc.gameSettings.thirdPersonView == 2);
			this.mc.mcProfiler.endStartSection("frustrum");
			ClippingHelperImpl.getInstance();

			player.yOffset = 1.62f;
			// ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆
			//                                                             ここまで
			// ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆
			if (this.mc.gameSettings.renderDistanceChunks >= 4) {
				this.setupFog(-1, p_78471_1_);
				this.mc.mcProfiler.endStartSection("sky");
				renderglobal.renderSky(p_78471_1_);
			}

			GL11.glEnable(GL11.GL_FOG);
			this.setupFog(1, p_78471_1_);

			if (this.mc.gameSettings.ambientOcclusion != 0) {
				GL11.glShadeModel(GL11.GL_SMOOTH);
			}

			this.mc.mcProfiler.endStartSection("culling");
			Frustrum frustrum = new Frustrum();
			frustrum.setPosition(d0, d1, d2);
			this.mc.renderGlobal.clipRenderersByFrustum(frustrum, p_78471_1_);

			if (j == 0) {
				this.mc.mcProfiler.endStartSection("updatechunks");

				while (!this.mc.renderGlobal.updateRenderers(entitylivingbase, false) && p_78471_2_ != 0L) {
					long k = p_78471_2_ - System.nanoTime();

					if (k < 0L || k > 1000000000L) {
						break;
					}
				}
			}
			if (entitylivingbase.posY < 128.0D) {
				this.renderCloudsCheck(renderglobal, p_78471_1_);
			}

			this.mc.mcProfiler.endStartSection("prepareterrain");
			this.setupFog(0, p_78471_1_);
			GL11.glEnable(GL11.GL_FOG);
			this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
			RenderHelper.disableStandardItemLighting();
			this.mc.mcProfiler.endStartSection("terrain");
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glPushMatrix();
			renderglobal.sortAndRender(entitylivingbase, 0, (double)p_78471_1_);
			GL11.glShadeModel(GL11.GL_FLAT);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			EntityPlayer entityplayer;

			if (this.debugViewDirection == 0) {
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				RenderHelper.enableStandardItemLighting();
				this.mc.mcProfiler.endStartSection("entities");
				net.minecraftforge.client.ForgeHooksClient.setRenderPass(0);
				renderglobal.renderEntities(entitylivingbase, frustrum, p_78471_1_);
				net.minecraftforge.client.ForgeHooksClient.setRenderPass(0);
				//ToDo: Try and figure out how to make particles render sorted correctly.. {They render behind water}
				RenderHelper.disableStandardItemLighting();
				this.disableLightmap((double)p_78471_1_);
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glPopMatrix();
				GL11.glPushMatrix();

				if (this.mc.objectMouseOver != null && entitylivingbase.isInsideOfMaterial(Material.water) && entitylivingbase instanceof EntityPlayer && !this.mc.gameSettings.hideGUI)
				{
					entityplayer = (EntityPlayer)entitylivingbase;
					GL11.glDisable(GL11.GL_ALPHA_TEST);
					this.mc.mcProfiler.endStartSection("outline");
					if (!ForgeHooksClient.onDrawBlockHighlight(renderglobal, entityplayer, mc.objectMouseOver, 0, entityplayer.inventory.getCurrentItem(), p_78471_1_))
					{
						renderglobal.drawSelectionBox(entityplayer, this.mc.objectMouseOver, 0, p_78471_1_);
					}
					GL11.glEnable(GL11.GL_ALPHA_TEST);
				}
			}

			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glPopMatrix();

			if (this.cameraZoom == 1.0D && entitylivingbase instanceof EntityPlayer && !this.mc.gameSettings.hideGUI && this.mc.objectMouseOver != null && !entitylivingbase.isInsideOfMaterial(Material.water)) {
				entityplayer = (EntityPlayer)entitylivingbase;
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				this.mc.mcProfiler.endStartSection("outline");
				if (!ForgeHooksClient.onDrawBlockHighlight(renderglobal, entityplayer, mc.objectMouseOver, 0, entityplayer.inventory.getCurrentItem(), p_78471_1_)) {
					renderglobal.drawSelectionBox(entityplayer, this.mc.objectMouseOver, 0, p_78471_1_);
				}
				GL11.glEnable(GL11.GL_ALPHA_TEST);
			}

			this.mc.mcProfiler.endStartSection("destroyProgress");
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 1, 1, 0);
			renderglobal.drawBlockDamageTexture(Tessellator.instance, entitylivingbase, p_78471_1_);
			GL11.glDisable(GL11.GL_BLEND);

			if (this.debugViewDirection == 0) {
				this.enableLightmap((double)p_78471_1_);
				this.mc.mcProfiler.endStartSection("litParticles");
				effectrenderer.renderLitParticles(entitylivingbase, p_78471_1_);
				RenderHelper.disableStandardItemLighting();
				this.setupFog(0, p_78471_1_);
				this.mc.mcProfiler.endStartSection("particles");
				effectrenderer.renderParticles(entitylivingbase, p_78471_1_);
				this.disableLightmap((double)p_78471_1_);
			}

			GL11.glDepthMask(false);
			GL11.glEnable(GL11.GL_CULL_FACE);
			this.mc.mcProfiler.endStartSection("weather");
			this.renderRainSnow(p_78471_1_);
			GL11.glDepthMask(true);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_CULL_FACE);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			this.setupFog(0, p_78471_1_);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDepthMask(false);
			this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

			if (this.mc.gameSettings.fancyGraphics) {
				this.mc.mcProfiler.endStartSection("water");

				if (this.mc.gameSettings.ambientOcclusion != 0) {
					GL11.glShadeModel(GL11.GL_SMOOTH);
				}

				GL11.glEnable(GL11.GL_BLEND);
				OpenGlHelper.glBlendFunc(770, 771, 1, 0);

				if (this.mc.gameSettings.anaglyph) {
					if (anaglyphField == 0) {
						GL11.glColorMask(false, true, true, true);
					} else {
						GL11.glColorMask(true, false, false, true);
					}

					renderglobal.sortAndRender(entitylivingbase, 1, (double)p_78471_1_);
				} else {
					renderglobal.sortAndRender(entitylivingbase, 1, (double)p_78471_1_);
				}

				GL11.glDisable(GL11.GL_BLEND);
				GL11.glShadeModel(GL11.GL_FLAT);
			} else {
				this.mc.mcProfiler.endStartSection("water");
				renderglobal.sortAndRender(entitylivingbase, 1, (double) p_78471_1_);
			}

			if (this.debugViewDirection == 0) // Only render if render pass 0 happens as well.
			{
				RenderHelper.enableStandardItemLighting();
				this.mc.mcProfiler.endStartSection("entities");
				ForgeHooksClient.setRenderPass(1);
				renderglobal.renderEntities(entitylivingbase, frustrum, p_78471_1_);
				ForgeHooksClient.setRenderPass(-1);
				RenderHelper.disableStandardItemLighting();
			}

			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_FOG);

			if (entitylivingbase.posY >= 128.0D) {
				this.mc.mcProfiler.endStartSection("aboveClouds");
				this.renderCloudsCheck(renderglobal, p_78471_1_);
			}

			this.mc.mcProfiler.endStartSection("FRenderLast");
			ForgeHooksClient.dispatchRenderLast(renderglobal, p_78471_1_);

			this.mc.mcProfiler.endStartSection("hand");

			if (!ForgeHooksClient.renderFirstPersonHand(renderglobal, p_78471_1_, j) && this.cameraZoom == 1.0D) {
				GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
				this.renderHand(p_78471_1_, j);
			}

			if (!this.mc.gameSettings.anaglyph) {
				this.mc.mcProfiler.endSection();
				return;
			}
		}

		GL11.glColorMask(true, true, true, false);
		this.mc.mcProfiler.endSection();

		//} //catch (ReflectiveOperationException e) {
			//e.printStackTrace();
		//}
	}*/

	// ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆
	//                                                            Mouse Over
	// ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆

	/**
     * Finds what block or object the mouse is over at the specified partial tick time. Args: partialTickTime
     */
    /*private void getMouseOver2(float p_78473_1_)
    {
        if (this.mc.renderViewEntity != null)
        {
            if (this.mc.theWorld != null)
            {
            	//Vec3 lookVec = this.mc.renderViewEntity.getLook(p_78473_1_).normalize();
            	Vec3 posVec = this.mc.renderViewEntity.getPosition(p_78473_1_);
            		// lookVec に垂直で posVec を通る平面で y = 0 に平行なベクトル？
            		//EntityLivingBase entitylivingbase = this.mc.renderViewEntity;
        			//float f6 = entitylivingbase.rotationYaw;
        			//float x = MathHelper.cos(f6 / 180.0F * (float)Math.PI);
        			//float z = MathHelper.sin(f6 / 180.0F * (float)Math.PI);
        			//Vec3 newVec = Vec3.createVectorHelper(x, 0, z).normalize();
                	// //Vec3 newVec = Vec3.createVectorHelper(lookVec.zCoord, 0, -lookVec.xCoord).normalize();
                	// Position をカメラと同じだけ横に移動させる
                	//double d = MyMod_v2.otherData.cameraX;
                	//posVec = posVec.addVector(newVec.xCoord * d, newVec.yCoord * d, newVec.zCoord * d);

                this.mc.pointedEntity = null;
                double d0 = (double)this.mc.playerController.getBlockReachDistance();
                //Vec3 endVec = posVec.addVector(lookVec.xCoord * d0, lookVec.yCoord * d0, lookVec.zCoord * d0);
                this.mc.objectMouseOver = this.mc.renderViewEntity.rayTrace(d0, p_78473_1_);

                double d1 = d0;

                if (this.mc.playerController.extendedReach())
                {
                    d0 = 6.0D;
                    d1 = 6.0D;
                }
                else
                {
                    if (d0 > 3.0D)
                    {
                        d1 = 3.0D;
                    }

                    d0 = d1;
                }

                if (this.mc.objectMouseOver != null)
                {
                    d1 = this.mc.objectMouseOver.hitVec.distanceTo(posVec);
                }

                Vec3 vec31 = this.mc.renderViewEntity.getLook(p_78473_1_);
                Vec3 vec32 = posVec.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
                this.pointedEntity = null;
                Vec3 vec33 = null;
                float f1 = 1.0F;
                List list = this.mc.theWorld.getEntitiesWithinAABBExcludingEntity(this.mc.renderViewEntity, this.mc.renderViewEntity.boundingBox.addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand((double)f1, (double)f1, (double)f1));
                double d2 = d1;

                for (int i = 0; i < list.size(); ++i)
                {
                    Entity entity = (Entity)list.get(i);

                    if (entity.canBeCollidedWith())
                    {
                        float f2 = entity.getCollisionBorderSize();
                        AxisAlignedBB axisalignedbb = entity.boundingBox.expand((double)f2, (double)f2, (double)f2);
                        MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(posVec, vec32);

                        if (axisalignedbb.isVecInside(posVec))
                        {
                            if (0.0D < d2 || d2 == 0.0D)
                            {
                            	ReflectionHelper.setField(this, pointedEntity, entity);
                                //this.pointedEntity = entity;
                                vec33 = movingobjectposition == null ? posVec : movingobjectposition.hitVec;
                                d2 = 0.0D;
                            }
                        }
                        else if (movingobjectposition != null)
                        {
                            double d3 = posVec.distanceTo(movingobjectposition.hitVec);

                            if (d3 < d2 || d2 == 0.0D)
                            {
                                if (entity == this.mc.renderViewEntity.ridingEntity && !entity.canRiderInteract())
                                {
                                    if (d2 == 0.0D)
                                    {
                                    	ReflectionHelper.setField(this, pointedEntity, entity);
                                        //this.pointedEntity = entity;
                                        vec33 = movingobjectposition.hitVec;
                                    }
                                }
                                else
                                {
                                	ReflectionHelper.setField(this, pointedEntity, entity);
                                    //this.pointedEntity = entity;
                                    vec33 = movingobjectposition.hitVec;
                                    d2 = d3;
                                }
                            }
                        }
                    }
                }

                if (this.pointedEntity != null && (d2 < d1 || this.mc.objectMouseOver == null))
                {
                    this.mc.objectMouseOver = new MovingObjectPosition((Entity)ReflectionHelper.getFieldValue(this, pointedEntity), vec33);

                    if (ReflectionHelper.getFieldValue(this, pointedEntity) instanceof EntityLivingBase || ReflectionHelper.getFieldValue(this, pointedEntity) instanceof EntityItemFrame)
                    {
                        this.mc.pointedEntity = (Entity)ReflectionHelper.getFieldValue(this, pointedEntity);
                    }
                }
            }
        }
    }*/

    // ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆
 	//                                                          最初に呼ばれる
 	// ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆

    /**
     * Will update any inputs that effect the camera angle (mouse) and then render the world and GUI
     */
    /*public void updateCameraAndRender(float p_78480_1_)
    {
        this.mc.mcProfiler.startSection("lightTex");

        if (this.lightmapUpdateNeeded)
        {
            this.updateLightmap(p_78480_1_);
        }

        this.mc.mcProfiler.endSection();
        boolean flag = Display.isActive();

        if (!flag && this.mc.gameSettings.pauseOnLostFocus && (!this.mc.gameSettings.touchscreen || !Mouse.isButtonDown(1)))
        {
            if (Minecraft.getSystemTime() - this.prevFrameTime > 500L)
            {
                this.mc.displayInGameMenu();
            }
        }
        else
        {
            this.prevFrameTime = Minecraft.getSystemTime();
        }

        this.mc.mcProfiler.startSection("mouse");

        if (this.mc.inGameHasFocus && flag)
        {
            this.mc.mouseHelper.mouseXYChange();
            float f1 = this.mc.gameSettings.mouseSensitivity * MyModEventHandler.sensitivity * 0.6F + 0.2F;// < -----------------------------------------------------------------
            float f2 = f1 * f1 * f1 * 8.0F;
            float f3 = (float)this.mc.mouseHelper.deltaX * f2;
            float f4 = (float)this.mc.mouseHelper.deltaY * f2;
            byte b0 = 1;

            if (this.mc.gameSettings.invertMouse)
            {
                b0 = -1;
            }

            if (this.mc.gameSettings.smoothCamera)
            {
                this.smoothCamYaw += f3;
                this.smoothCamPitch += f4;
                float f5 = p_78480_1_ - this.smoothCamPartialTicks;
                this.smoothCamPartialTicks = p_78480_1_;
                f3 = this.smoothCamFilterX * f5;
                f4 = this.smoothCamFilterY * f5;
                this.mc.thePlayer.setAngles(f3, f4 * (float)b0);
            }
            else
            {
                this.mc.thePlayer.setAngles(f3, f4 * (float)b0);
            }
        }

        this.mc.mcProfiler.endSection();

        if (!this.mc.skipRenderWorld)
        {
            anaglyphEnable = this.mc.gameSettings.anaglyph;
            final ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
            int i = scaledresolution.getScaledWidth();
            int j = scaledresolution.getScaledHeight();
            final int k = Mouse.getX() * i / this.mc.displayWidth;
            final int l = j - Mouse.getY() * j / this.mc.displayHeight - 1;
            int i1 = this.mc.gameSettings.limitFramerate;

            if (this.mc.theWorld != null)
            {
                this.mc.mcProfiler.startSection("level");

                if (this.mc.isFramerateLimitBelowMax())
                {
                    this.renderWorld(p_78480_1_, this.renderEndNanoTime + (long)(1000000000 / i1));
                }
                else
                {
                    this.renderWorld(p_78480_1_, 0L);
                }

                if (OpenGlHelper.shadersSupported)
                {
                    if (this.theShaderGroup != null)
                    {
                        GL11.glMatrixMode(GL11.GL_TEXTURE);
                        GL11.glPushMatrix();
                        GL11.glLoadIdentity();
                        this.theShaderGroup.loadShaderGroup(p_78480_1_);
                        GL11.glPopMatrix();
                    }

                    this.mc.getFramebuffer().bindFramebuffer(true);
                }

                this.renderEndNanoTime = System.nanoTime();
                this.mc.mcProfiler.endStartSection("gui");

                if (!this.mc.gameSettings.hideGUI || this.mc.currentScreen != null)
                {
                    GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
                    this.mc.ingameGUI.renderGameOverlay(p_78480_1_, this.mc.currentScreen != null, k, l);
                }

                this.mc.mcProfiler.endSection();
            }
            else
            {
                GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
                GL11.glMatrixMode(GL11.GL_PROJECTION);
                GL11.glLoadIdentity();
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glLoadIdentity();
                this.setupOverlayRendering();
                this.renderEndNanoTime = System.nanoTime();
            }

            if (this.mc.currentScreen != null)
            {
                GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

                try
                {
                    if (!MinecraftForge.EVENT_BUS.post(new DrawScreenEvent.Pre(this.mc.currentScreen, k, l, p_78480_1_)))
                        this.mc.currentScreen.drawScreen(k, l, p_78480_1_);
                    MinecraftForge.EVENT_BUS.post(new DrawScreenEvent.Post(this.mc.currentScreen, k, l, p_78480_1_));
                }
                catch (Throwable throwable)
                {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering screen");
                    CrashReportCategory crashreportcategory = crashreport.makeCategory("Screen render details");
                    crashreportcategory.addCrashSectionCallable("Screen name", new Callable()
                    {
                        private static final String __OBFID = "CL_00000948";
                        public String call()
                        {
                            return EntityRendererAlt.this.mc.currentScreen.getClass().getCanonicalName();
                        }
                    });
                    crashreportcategory.addCrashSectionCallable("Mouse location", new Callable()
                    {
                        private static final String __OBFID = "CL_00000950";
                        public String call()
                        {
                            return String.format("Scaled: (%d, %d). Absolute: (%d, %d)", new Object[] {Integer.valueOf(k), Integer.valueOf(l), Integer.valueOf(Mouse.getX()), Integer.valueOf(Mouse.getY())});
                        }
                    });
                    crashreportcategory.addCrashSectionCallable("Screen size", new Callable()
                    {
                        private static final String __OBFID = "CL_00000951";
                        public String call()
                        {
                            return String.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d", new Object[] {Integer.valueOf(scaledresolution.getScaledWidth()), Integer.valueOf(scaledresolution.getScaledHeight()), Integer.valueOf(EntityRendererAlt.this.mc.displayWidth), Integer.valueOf(EntityRendererAlt.this.mc.displayHeight), Integer.valueOf(scaledresolution.getScaleFactor())});
                        }
                    });
                    throw new ReportedException(crashreport);
                }
            }
        }
        //super.updateCameraAndRender(p_78480_1_);//
    }*/

	/*
	public void updateRenderer()
    {
    	super.updateRenderer();//
        if (OpenGlHelper.shadersSupported && ShaderLinkHelper.getStaticShaderLinkHelper() == null)
        {
            ShaderLinkHelper.setNewStaticShaderLinkHelper();
        }

        this.updateFovModifierHand();
        this.updateTorchFlicker();
        this.fogColor2 = this.fogColor1;
        this.thirdPersonDistanceTemp = this.thirdPersonDistance;
        this.prevDebugCamYaw = this.debugCamYaw;
        this.prevDebugCamPitch = this.debugCamPitch;
        this.prevDebugCamFOV = this.debugCamFOV;
        this.prevCamRoll = this.camRoll;
        float f;
        float f1;

        if (this.mc.gameSettings.smoothCamera)
        {
            f = this.mc.gameSettings.mouseSensitivity * MyModEventHandler.sensitivity * 0.6F + 0.2F;// <------------------------------------------------------------------------------
            f1 = f * f * f * 8.0F;
            this.smoothCamFilterX = this.mouseFilterXAxis.smooth(this.smoothCamYaw, 0.05F * f1);
            this.smoothCamFilterY = this.mouseFilterYAxis.smooth(this.smoothCamPitch, 0.05F * f1);
            this.smoothCamPartialTicks = 0.0F;
            this.smoothCamYaw = 0.0F;
            this.smoothCamPitch = 0.0F;
        }

        if (this.mc.renderViewEntity == null)
        {
            this.mc.renderViewEntity = this.mc.thePlayer;
        }

        f = this.mc.theWorld.getLightBrightness(MathHelper.floor_double(this.mc.renderViewEntity.posX), MathHelper.floor_double(this.mc.renderViewEntity.posY), MathHelper.floor_double(this.mc.renderViewEntity.posZ));
        f1 = (float)this.mc.gameSettings.renderDistanceChunks / 16.0F;
        float f2 = f * (1.0F - f1) + f1;
        this.fogColor1 += (f2 - this.fogColor1) * 0.1F;
        ++this.rendererUpdateCount;
        this.itemRenderer.updateEquippedItem();
        this.addRainParticles();
        this.bossColorModifierPrev = this.bossColorModifier;

        if (BossStatus.hasColorModifier)
        {
            this.bossColorModifier += 0.05F;

            if (this.bossColorModifier > 1.0F)
            {
                this.bossColorModifier = 1.0F;
            }

            BossStatus.hasColorModifier = false;
        }
        else if (this.bossColorModifier > 0.0F)
        {
            this.bossColorModifier -= 0.0125F;
        }

        //super.updateRenderer();//
    }
	 */

    /**
     * sets up projection, view effects, camera position/rotation
     */
    /*public void _setupCameraTransform(float p_78479_1_, int p_78479_2_)
    {
        this.farPlaneDistance = (float)(this.mc.gameSettings.renderDistanceChunks * 16);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        float f1 = 0.07F;

        if (this.mc.gameSettings.anaglyph)
        {
            GL11.glTranslatef((float)(-(p_78479_2_ * 2 - 1)) * f1, 0.0F, 0.0F);
        }

        if (this.cameraZoom != 1.0D)
        {
            GL11.glTranslatef((float)this.cameraYaw, (float)(-this.cameraPitch), 0.0F);
            GL11.glScaled(this.cameraZoom, this.cameraZoom, 1.0D);
        }

        Project.gluPerspective(this.getFOVModifier(p_78479_1_, true), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
        float f2;

        if (this.mc.playerController.enableEverythingIsScrewedUpMode())
        {
            f2 = 0.6666667F;
            GL11.glScalef(1.0F, f2, 1.0F);
        }

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        if (this.mc.gameSettings.anaglyph)
        {
            GL11.glTranslatef((float)(p_78479_2_ * 2 - 1) * 0.1F, 0.0F, 0.0F);
        }

        this.hurtCameraEffect(p_78479_1_);

        if (this.mc.gameSettings.viewBobbing)
        {
            this.setupViewBobbing(p_78479_1_);
        }

        f2 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * p_78479_1_;

        if (f2 > 0.0F)
        {
            byte b0 = 20;

            if (this.mc.thePlayer.isPotionActive(Potion.confusion))
            {
                b0 = 7;
            }

            float f3 = 5.0F / (f2 * f2 + 5.0F) - f2 * 0.04F;
            f3 *= f3;
            GL11.glRotatef(((float)this.rendererUpdateCount + p_78479_1_) * (float)b0, 0.0F, 1.0F, 1.0F);
            GL11.glScalef(1.0F / f3, 1.0F, 1.0F);
            GL11.glRotatef(-((float)this.rendererUpdateCount + p_78479_1_) * (float)b0, 0.0F, 1.0F, 1.0F);
        }*/

        /*
        Class<EntityRenderer> c = EntityRenderer.class;
		try {
			//Method orientCamera = c.getDeclaredMethod("orientCamera", float.class);
			Method orientCamera = c.getDeclaredMethod("func_78467_g", float.class);
			orientCamera.setAccessible(true);
			orientCamera.invoke(this, p_78479_1_);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}*/
        /*this.orientCamera(p_78479_1_);

        if (this.debugViewDirection > 0)
        {
            int j = this.debugViewDirection - 1;

            if (j == 1)
            {
                GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            }

            if (j == 2)
            {
                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            }

            if (j == 3)
            {
                GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            }

            if (j == 4)
            {
                GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            }

            if (j == 5)
            {
                GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
            }
        }
    }*/
}