package com.lyrica.cameramod.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.LocalVariablesSorter;

import com.lyrica.cameramod.CameraMod;

public class Adapter_isInsideBlock extends LocalVariablesSorter implements Opcodes {
	public Adapter_isInsideBlock(int access, String desc, MethodVisitor mv){
		super(ASM5, access, desc, mv);
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		String methodName;

		methodName = CameraMod.DEBUG ? "isEntityInsideOpaqueBlock" : "func_70094_T";
		// player.isEntityInsideOpaqueBlock を false に書き換える
		if (methodName.equals(ASMHelper.mapMethodName(owner, name, desc))) {
			super.visitInsn(POP);
			super.visitLdcInsn(false);
			return;
		}

		super.visitMethodInsn(opcode, owner, name, desc, itf);
	}
}
