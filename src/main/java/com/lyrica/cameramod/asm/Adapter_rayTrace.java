package com.lyrica.cameramod.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.LocalVariablesSorter;

import com.lyrica.cameramod.CameraData;
import com.lyrica.cameramod.CameraMod;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class Adapter_rayTrace extends LocalVariablesSorter implements Opcodes {
	public Adapter_rayTrace(int access, String desc, MethodVisitor mv){
		super(ASM5, access, desc, mv);
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		String methodName;

		methodName = CameraMod.DEBUG ? "getPosition" : "func_70666_h";
		// EntityLivingBase.getPosition を this.getPosition に書き換える
		if (methodName.equals(ASMHelper.mapMethodName(owner, name, desc))) {
			String class_Adapter = "com/lyrica/cameramod/asm/Adapter_rayTrace";

			String s = CameraMod.DEBUG ? "net.minecraft.entity.EntityLivingBase" :
				 ASMHelper.unmapClassName("net.minecraft.entity.EntityLivingBase");

			super.visitMethodInsn(INVOKESTATIC, class_Adapter, "getPosition", ASMHelper.toDesc(Vec3.class, s, float.class), false);
			return;
		}

		super.visitMethodInsn(opcode, owner, name, desc, itf);
	}

	public static Vec3 getPosition(EntityLivingBase entity, float p_70666_1_)
	{
		// プレイヤーのみ
		if (!(entity instanceof EntityPlayer))
			return entity.getPosition(p_70666_1_);

		Minecraft mc = Minecraft.getMinecraft();
		int tpv = mc.gameSettings.thirdPersonView;
		if (tpv == 0)
			return entity.getPosition(p_70666_1_);

		EntityPlayer player = (EntityPlayer)entity;

		CameraData data = CameraMod.cameraData;

		// カメラがデフォルト位置から動いた距離
		float f6 = player.rotationYaw;
		float x = MathHelper.cos(f6 / 180.0F * (float)Math.PI);
		float z = MathHelper.sin(f6 / 180.0F * (float)Math.PI);
		Vec3 cameraDifVec = Vec3.createVectorHelper(x, 0, z).normalize();
		cameraDifVec = Vec3.createVectorHelper(cameraDifVec.xCoord * data.cameraX, data.cameraY, cameraDifVec.zCoord * data.cameraX);

		// 元の getPosition からコピー
		if (p_70666_1_ == 1.0F)
        {
            return Vec3.createVectorHelper(entity.posX + cameraDifVec.xCoord,
            							   entity.posY + cameraDifVec.yCoord,
            							   entity.posZ + cameraDifVec.zCoord);
        }
        else
        {
            double d0 = entity.prevPosX + cameraDifVec.xCoord + (entity.posX - entity.prevPosX) * (double)p_70666_1_;
            double d1 = entity.prevPosY + cameraDifVec.yCoord + (entity.posY - entity.prevPosY) * (double)p_70666_1_;
            double d2 = entity.prevPosZ + cameraDifVec.zCoord + (entity.posZ - entity.prevPosZ) * (double)p_70666_1_;
            return Vec3.createVectorHelper(d0, d1, d2);
        }
	}
}
