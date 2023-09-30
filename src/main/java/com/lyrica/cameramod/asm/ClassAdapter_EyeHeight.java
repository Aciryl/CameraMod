package com.lyrica.cameramod.asm;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import com.lyrica.cameramod.CameraMod;

public class ClassAdapter_EyeHeight extends ClassVisitor{
	public ClassAdapter_EyeHeight(ClassVisitor cv){
		super(ASM5, cv);
    }

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions){
		String methodName;

		methodName = CameraMod.DEBUG ? "isEntityInsideOpaqueBlock" : "func_70094_T";
		// "isEntityInsideOpaqueBlock"
		if(methodName.equals(ASMHelper.mapMethodName(Transformer_EyeHeight.TARGET, name, desc))) {
			return new Adapter_isEntityInsideOpaqueBlock(access, desc, super.visitMethod(access, name, desc, signature, exceptions));
		}

		return super.visitMethod(access, name, desc, signature, exceptions);
	}
}