package com.lyrica.cameramod.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.LocalVariablesSorter;

public class Adapter_getMouseOver  extends LocalVariablesSorter implements Opcodes {
	//int player;
	//int offsetY;

	public Adapter_getMouseOver(int access, String desc, MethodVisitor mv)
	{
		super(ASM5, access, desc, mv);
	}

	@Override
	public void visitCode()
	{
		super.visitCode();

		String class_RendererAlt = "com/lyrica/cameramod/EntityRendererAlt";

		super.visitMethodInsn(INVOKESTATIC, class_RendererAlt, "_getMouseOver1", "()V", false);

		/*
		String class_Minecraft = "net/minecraft/client/Minecraft";
		String class_Player = "net/minecraft/client/entity/EntityClientPlayerMP";
		String class_Renderer = "net/minecraft/client/renderer/EntityRenderer";
		String class_RendererAlt = "com/lyrica/cameramod/EntityRendererAlt";

		Label l_super = new Label();

		// EntityPlayer player = mc.thePlayer;
		player = newLocal(Type.getType(ASMHelper.getDescriptor(class_Player)));// EntityPlayer player;
		super.visitVarInsn(ALOAD, 0);// this.
		super.visitFieldInsn(GETFIELD, class_Renderer, "field_78531_r", ASMHelper.getDescriptor(class_Minecraft)); // mc
		super.visitFieldInsn(GETFIELD, class_Minecraft, "field_71439_g", ASMHelper.getDescriptor(class_Player));// mc.thePlayer
		super.visitVarInsn(ASTORE, player);// player = mc.thePlayer;

		// if (!(mc.thePlayer == null || mc.thePlayer.isPlayerSleeping())) {
		//     -----
		//     -----
		// }
		super.visitVarInsn(ALOAD, player);
		super.visitJumpInsn(IFNULL, l_super); // !(mc.thePlayer == null) then
		super.visitVarInsn(ALOAD, player);
		super.visitMethodInsn(INVOKEVIRTUAL, class_Player, "func_70608_bn", "()Z", false);// mc.thePlayer.isPlayerSleeping()
		super.visitJumpInsn(IFNE, l_super);// !(mc.thePlayer.isPlayerSleeping()) then

		// float offsetY = getOffsetY();
		offsetY = newLocal(Type.FLOAT_TYPE);// float offsetY
		super.visitMethodInsn(INVOKESTATIC, class_RendererAlt, "getOffsetY", "()F", false);// getOffsetY()
		super.visitVarInsn(FSTORE, offsetY);// offsetY = getOffsetY();

		// player.eyeHeight = player.getDefaultEyeHeight();
		super.visitVarInsn(ALOAD, player);// player.
		super.visitVarInsn(ALOAD, player);// player.
		super.visitMethodInsn(INVOKEVIRTUAL, class_Player, "getDefaultEyeHeight", "()F", false);// player.getDefaultEyeHeight()
		super.visitFieldInsn(PUTFIELD, class_Player, "eyeHeight", "F");// player.eyeHeight = ～

		// player.posY += offsetY;
		super.visitVarInsn(ALOAD, player);// player.
		super.visitVarInsn(ALOAD, player);// player.
		super.visitFieldInsn(GETFIELD, class_Player, "posY", "D");// player.posY
		super.visitVarInsn(FLOAD, offsetY);// offsetY
		super.visitInsn(F2D);// (double)offsetY
		super.visitInsn(DADD);// posY + offsetY
		super.visitFieldInsn(PUTFIELD, class_Player, "posY", "D");// player.posY = ～

		// player.prevPosY += offsetY;
		super.visitVarInsn(ALOAD, player);// player.
		super.visitVarInsn(ALOAD, player);// player.
		super.visitFieldInsn(GETFIELD, class_Player, "prevPosY", "D");// player.prevPosY
		super.visitVarInsn(FLOAD, offsetY);// offsetY
		super.visitInsn(F2D);// (double)offsetY
		super.visitInsn(DADD);// posY + offsetY
		super.visitFieldInsn(PUTFIELD, class_Player, "prevPosY", "D");// player.prevPosY = ～

		// player.lastTickPosY += offsetY;
		super.visitVarInsn(ALOAD, player);// player.
		super.visitVarInsn(ALOAD, player);// player.
		super.visitFieldInsn(GETFIELD, class_Player, "lastTickPosY", "D");// player.prevPosY
		super.visitVarInsn(FLOAD, offsetY);// offsetY
		super.visitInsn(F2D);// (double)offsetY
		super.visitInsn(DADD);// posY + offsetY
		super.visitFieldInsn(PUTFIELD, class_Player, "lastTickPosY", "D");// player.prevPosY = ～

		super.visitLabel(l_super);*/
	}

	@Override
	public void visitInsn(int opcode)
	{
		if (opcode == RETURN) {
			String class_RendererAlt = "com/lyrica/cameramod/EntityRendererAlt";

			super.visitMethodInsn(INVOKESTATIC, class_RendererAlt, "_getMouseOver2", "()V", false);
			/*
			String class_Minecraft = "net/minecraft/client/Minecraft";
			String class_Player = "net/minecraft/client/entity/EntityClientPlayerMP";
			String class_Renderer = "net/minecraft/client/renderer/EntityRenderer";
			String class_RendererAlt = "com/lyrica/cameramod/EntityRendererAlt";
			String class_EHandler = "myMod_v2/MyModEventHandler";

			Label l_return = new Label();

			// EntityPlayer player = mc.thePlayer;
			player = newLocal(Type.getType(ASMHelper.getDescriptor(class_Player)));// EntityPlayer player;
			super.visitVarInsn(ALOAD, 0);// this.
			super.visitFieldInsn(GETFIELD, class_Renderer, "mc", ASMHelper.getDescriptor(class_Minecraft)); // mc
			super.visitFieldInsn(GETFIELD, class_Minecraft, "thePlayer", ASMHelper.getDescriptor(class_Player));// mc.thePlayer
			super.visitVarInsn(ASTORE, player);// player = mc.thePlayer;

			// if (!(mc.thePlayer == null || mc.thePlayer.isPlayerSleeping())) {
			//     -----
			//     -----
			// }
			super.visitVarInsn(ALOAD, player);
			super.visitJumpInsn(IFNULL, l_return); // !(mc.thePlayer == null) then
			super.visitVarInsn(ALOAD, player);
			super.visitMethodInsn(INVOKEVIRTUAL, class_Player, "isPlayerSleeping", "()Z", false);// mc.thePlayer.isPlayerSleeping()
			super.visitJumpInsn(IFNE, l_return);// !(mc.thePlayer.isPlayerSleeping()) then

			// float offsetY = getOffsetY();
			offsetY = newLocal(Type.FLOAT_TYPE);// float offsetY
			super.visitMethodInsn(INVOKESTATIC, class_RendererAlt, "getOffsetY", "()F", false);// getOffsetY()
			super.visitVarInsn(FSTORE, offsetY);// offsetY = getOffsetY();

			//player.posY -= offsetY;
			super.visitVarInsn(ALOAD, player);// player.
			super.visitVarInsn(ALOAD, player);// player.
			super.visitFieldInsn(GETFIELD, class_Player, "posY", "D");// player.posY
			super.visitVarInsn(FLOAD, offsetY);// offsetY
			super.visitInsn(F2D);// (double)offsetY
			super.visitInsn(DSUB);// posY - offsetY
			super.visitFieldInsn(PUTFIELD, class_Player, "posY", "D");// player.posY = ～

			//player.prevPosY -= offsetY;
			super.visitVarInsn(ALOAD, player);// player.
			super.visitVarInsn(ALOAD, player);// player.
			super.visitFieldInsn(GETFIELD, class_Player, "prevPosY", "D");// player.prevPosY
			super.visitVarInsn(FLOAD, offsetY);// offsetY
			super.visitInsn(F2D);// (double)offsetY
			super.visitInsn(DSUB);// posY - offsetY
			super.visitFieldInsn(PUTFIELD, class_Player, "prevPosY", "D");// player.prevPosY = ～

			//player.lastTickPosY -= offsetY;
			super.visitVarInsn(ALOAD, player);// player.
			super.visitVarInsn(ALOAD, player);// player.
			super.visitFieldInsn(GETFIELD, class_Player, "lastTickPosY", "D");// player.prevPosY
			super.visitVarInsn(FLOAD, offsetY);// offsetY
			super.visitInsn(F2D);// (double)offsetY
			super.visitInsn(DSUB);// posY - offsetY
			super.visitFieldInsn(PUTFIELD, class_Player, "lastTickPosY", "D");// player.prevPosY = ～

			//MyModEventHandler.setPlayerEyeHeight(player);
			super.visitVarInsn(ALOAD, player);// player
			super.visitMethodInsn(INVOKESTATIC, class_EHandler, "setPlayerEyeHeight", ASMHelper.toDesc(void.class, EntityPlayer.class), false);

			super.visitLabel(l_return);*/
		}
		super.visitInsn(opcode);
	}
}