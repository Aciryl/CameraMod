package com.lyrica.cameramod.asm;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import com.lyrica.cameramod.CameraMod;

public class ClassAdapter_EntityLivingBase extends ClassVisitor{
	public ClassAdapter_EntityLivingBase(ClassVisitor cv){
		super(ASM5, cv);
    }

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions){
		String methodName;

		methodName = CameraMod.DEBUG ? "rayTrace" : "func_70614_a";
		// "rayTrace"
		if(methodName.equals(ASMHelper.mapMethodName(Transformer_EntityLivingBase.TARGET, name, desc))) {
			return new Adapter_rayTrace(access, desc, super.visitMethod(access, name, desc, signature, exceptions));
		}

		return super.visitMethod(access, name, desc, signature, exceptions);
	}
}