package com.lyrica.cameramod.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.LocalVariablesSorter;

import com.lyrica.cameramod.CameraMod;

public class Adapter_setupCameraTransform extends LocalVariablesSorter implements Opcodes {
	public Adapter_setupCameraTransform(int access, String desc, MethodVisitor mv){
		super(ASM5, access, desc, mv);
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		String methodName;

		//String class_Renderer = "net/minecraft/client/renderer/EntityRenderer";

		// orientCamera(...) を _orientCamera(...) に置き換える
		methodName = CameraMod.DEBUG ? "orientCamera" : "func_78467_g";
		//if (opcode == INVOKESPECIAL && owner.equals(class_Renderer) && name.equals("orientCamera") && desc.equals(ASMHelper.toDesc(void.class, float.class))) {
		if (opcode == INVOKESPECIAL && methodName.equals(ASMHelper.mapMethodName(owner, name, desc)) && desc.equals(ASMHelper.toDesc(void.class, float.class))) {
			String class_RendererAlt = "com/lyrica/cameramod/EntityRendererAlt";

			// _orientCamera(...);
			String s = CameraMod.DEBUG ? "net.minecraft.client.renderer.EntityRenderer" :
				 ASMHelper.unmapClassName("net.minecraft.client.renderer.EntityRenderer");
			super.visitMethodInsn(INVOKESTATIC, class_RendererAlt, "_orientCamera", ASMHelper.toDesc(void.class, s, float.class), itf);
			return;
		}

		super.visitMethodInsn(opcode, owner, name, desc, itf);
	}
}
