package com.lyrica.cameramod.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.LocalVariablesSorter;

public class Adapter_isEntityInsideOpaqueBlock extends LocalVariablesSorter implements Opcodes {
	public Adapter_isEntityInsideOpaqueBlock(int access, String desc, MethodVisitor mv){
		super(ASM5, access, desc, mv);
	}

	// なんでうまくいくのか分からない
	@Override
	public void visitCode()
	{
		super.visitLdcInsn(true);
		//super.visitCode();

		//String class_RendererAlt = "com/lyrica/cameramod/EntityRendererAlt";

		//String s = CameraMod.DEBUG ? "net.minecraft.client.renderer.EntityRenderer" :
		//	 ASMHelper.unmapClassName("net.minecraft.client.renderer.EntityRenderer");
		//super.visitMethodInsn(INVOKESTATIC, class_RendererAlt, "setCameraHeight", ASMHelper.toDesc(boolean.class, s), false);
	}

	//public static boolean
}
