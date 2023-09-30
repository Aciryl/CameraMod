package com.lyrica.cameramod.asm;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.LocalVariablesSorter;

import com.lyrica.cameramod.CameraMod;

public class Adapter_renderWorld extends LocalVariablesSorter implements Opcodes {
	public Adapter_renderWorld(int access, String desc, MethodVisitor mv){
		super(ASM5, access, desc, mv);
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		String methodName;

		//String class_Profiler = "net/minecraft/profiler/Profiler";
		String class_Renderer = "net/minecraft/client/renderer/EntityRenderer";
		String class_RendererAlt = "com/lyrica/cameramod/EntityRendererAlt";
		//String class_Impl = "net/minecraft/client/renderer/culling/ClippingHelperImpl";

		// this.mc.mcProfiler.endStartSection("clear");
		// の前に setCameraHeight 入れる
		methodName = CameraMod.DEBUG ? "endStartSection" : "func_76318_c";
		//if (opcode == INVOKEVIRTUAL && owner.equals(class_Profiler) && name.equals("endStartSection") && desc.equals(ASMHelper.toDesc(void.class, String.class))) {
		if (opcode == INVOKEVIRTUAL && methodName.equals(ASMHelper.mapMethodName(owner, name, desc)) && desc.equals(ASMHelper.toDesc(void.class, String.class))) {
			Label l_super = new Label();

			super.visitInsn(DUP);
			super.visitLdcInsn("clear");
			super.visitJumpInsn(IF_ACMPNE, l_super);

			// setCameraHeight(p_78471_1_);
			super.visitVarInsn(ALOAD, 0);
			super.visitVarInsn(FLOAD, 1);
			String s = CameraMod.DEBUG ? "net.minecraft.client.renderer.EntityRenderer" :
										 ASMHelper.unmapClassName("net.minecraft.client.renderer.EntityRenderer");
			super.visitMethodInsn(INVOKESTATIC, class_RendererAlt, "setCameraHeight", ASMHelper.toDesc(void.class, s, float.class), false);

			super.visitLabel(l_super);
		}

		super.visitMethodInsn(opcode, owner, name, desc, itf);

		// ClippingHelperImpl.getInstance();
		// の後に player.yOffset = player.getDefaultEyeHeight(); を入れる
		methodName = CameraMod.DEBUG ? "getInstance" : "func_78558_a";
		//if (opcode == INVOKESTATIC && owner.equals(class_Impl) && name.equals("getInstance") && desc.equals(ASMHelper.toDesc(ClippingHelper.class))) {
		if (opcode == INVOKESTATIC && methodName.equals(ASMHelper.mapMethodName(owner, name, desc))) {
			String fieldName;

			String class_Minecraft = "net/minecraft/client/Minecraft";
			String class_Player = "net/minecraft/client/entity/EntityClientPlayerMP";

			// mc.thePlayer.yOffset = 1.62f;
			int player = newLocal(Type.getType(ASMHelper.getDescriptor(class_Player)));// EntityPlayer player;
			super.visitVarInsn(ALOAD, 0);// this.

			fieldName = CameraMod.DEBUG ? "mc" : "field_78531_r";
			//super.visitFieldInsn(GETFIELD, class_Renderer, "mc", ASMHelper.getDescriptor(class_Minecraft)); // mc
			super.visitFieldInsn(GETFIELD, class_Renderer, fieldName, ASMHelper.getDescriptor(class_Minecraft)); // mc

			fieldName = CameraMod.DEBUG ? "thePlayer" : "field_71439_g";
			//super.visitFieldInsn(GETFIELD, class_Minecraft, "thePlayer", ASMHelper.getDescriptor(class_Player));// mc.thePlayer
			super.visitFieldInsn(GETFIELD, class_Minecraft, fieldName, ASMHelper.getDescriptor(class_Player));// mc.thePlayer

			super.visitLdcInsn(1.62f);

			fieldName = CameraMod.DEBUG ? "yOffset" : "field_70129_M";
			//super.visitFieldInsn(PUTFIELD, class_Player, "yOffset", "F");// mc.thePlayer.yOffset = ～
			super.visitFieldInsn(PUTFIELD, class_Player, fieldName, "F");// mc.thePlayer.yOffset = ～
		}
	}
}