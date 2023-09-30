package com.lyrica.cameramod.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.LocalVariablesSorter;

import com.lyrica.cameramod.CameraMod;

public class Adapter_updateCameraAndRender extends LocalVariablesSorter implements Opcodes {
	public Adapter_updateCameraAndRender(int access, String desc, MethodVisitor mv){
		super(ASM5, access, desc, mv);
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		String fieldName;

		super.visitFieldInsn(opcode, owner, name, desc);

		String class_RendererAlt = "com/lyrica/cameramod/EntityRendererAlt";

		// mouseSensitivity を getMouseSensitivity(mouseSensitivity) に置き換える
		fieldName = CameraMod.DEBUG ? "mouseSensitivity" : "field_74341_c";
		if (opcode == GETFIELD && fieldName.equals(ASMHelper.mapFieldName(owner, name, desc)) && desc.equals(ASMHelper.getDescriptor(float.class))) {
			// getMouseSensitivity(...);
			super.visitMethodInsn(INVOKESTATIC, class_RendererAlt, "getMouseSensitivity", "(F)F", false);
			return;
		}
	}
}
