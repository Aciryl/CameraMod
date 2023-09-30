package com.lyrica.cameramod.asm;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import com.lyrica.cameramod.CameraMod;

public class ClassAdapter_TPV extends ClassVisitor{
	public ClassAdapter_TPV(ClassVisitor cv){
		super(ASM5, cv);
    }

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions){
		String methodName;

		methodName = CameraMod.DEBUG ? "runGameLoop" : "func_71411_J";
		// "runGameLoop"
		if(methodName.equals(ASMHelper.mapMethodName(Transformer_TPV.TARGET, name, desc))) {
			return new Adapter_isInsideBlock(access, desc, super.visitMethod(access, name, desc, signature, exceptions));
		}

		return super.visitMethod(access, name, desc, signature, exceptions);
	}
}