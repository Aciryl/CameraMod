package com.lyrica.cameramod.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.LocalVariablesSorter;

public class Adapter_orientCamera extends LocalVariablesSorter implements Opcodes {
	public Adapter_orientCamera(int access, String desc, MethodVisitor mv){
		super(ASM5, access, desc, mv);
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		// GL11.glTranslatef と GL11.glRotatef を消す
		if ("glTranslatef".equals(ASMHelper.mapMethodName(owner, name, desc))) {
			super.visitInsn(POP);
			super.visitInsn(POP);
			super.visitInsn(POP);
			return;
		}

		if ("glRotatef".equals(ASMHelper.mapMethodName(owner, name, desc))) {
			super.visitInsn(POP);
			super.visitInsn(POP);
			super.visitInsn(POP);
			super.visitInsn(POP);
			return;
		}

		super.visitMethodInsn(opcode, owner, name, desc, itf);
	}
}
