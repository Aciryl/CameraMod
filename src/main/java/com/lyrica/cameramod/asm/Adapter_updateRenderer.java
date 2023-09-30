package com.lyrica.cameramod.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.LocalVariablesSorter;

import com.lyrica.cameramod.CameraMod;

public class Adapter_updateRenderer extends LocalVariablesSorter implements Opcodes {
	public Adapter_updateRenderer(int access, String desc, MethodVisitor mv){
		super(ASM5, access, desc, mv);
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		String methodName;

		// updateFovModifierHand(...) を _updateFovModifierHand(...) に置き換える
		methodName = CameraMod.DEBUG ? "updateFovModifierHand" : "func_78477_e";
		//if (opcode == INVOKESPECIAL && owner.equals(class_Renderer) && name.equals("updateFovModifierHand") && desc.equals(ASMHelper.toDesc(void.class))) {
		if (opcode == INVOKESPECIAL && methodName.equals(ASMHelper.mapMethodName(owner, name, desc)) && desc.equals(ASMHelper.toDesc(void.class))) {
			String class_RendererAlt = "com/lyrica/cameramod/EntityRendererAlt";

			// _updateFovModifierHand(...);
			String s = CameraMod.DEBUG ? "net.minecraft.client.renderer.EntityRenderer" :
				 ASMHelper.unmapClassName("net.minecraft.client.renderer.EntityRenderer");
			super.visitMethodInsn(INVOKESTATIC, class_RendererAlt, "_updateFovModifierHand", ASMHelper.toDesc(void.class, s), itf);
			return;
		}

		super.visitMethodInsn(opcode, owner, name, desc, itf);
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		String fieldName;

		super.visitFieldInsn(opcode, owner, name, desc);

		// mouseSensitivity を getMouseSensitivity(mouseSensitivity) に置き換える
		fieldName = CameraMod.DEBUG ? "mouseSensitivity" : "field_74341_c";
		if (opcode == GETFIELD && fieldName.equals(ASMHelper.mapFieldName(owner, name, desc)) && desc.equals(ASMHelper.getDescriptor(float.class))) {
			String class_RendererAlt = "com/lyrica/cameramod/EntityRendererAlt";

			// getMouseSensitivity(...);
			super.visitMethodInsn(INVOKESTATIC, class_RendererAlt, "getMouseSensitivity", "(F)F", false);
			return;
		}
	}
}