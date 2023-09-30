package com.lyrica.cameramod.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import net.minecraft.launchwrapper.IClassTransformer;
import scala.tools.asm.Opcodes;

public class Transformer_Renderer implements IClassTransformer, Opcodes {
	public static final String TARGET = "net.minecraft.client.renderer.EntityRenderer";

	/**
     クラスが最初に読み込まれた時に呼ばれる。

     @param  name クラスの難読化名
     @param  transformedName クラスの易読化名
     @param  bytes オリジナルのクラス
	 */
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes){
		if(!accept(transformedName))
			return bytes;

		//byte配列を読み込み、利用しやすい形にする。
		ClassReader cr = new ClassReader(bytes);
		//これのvisitを呼ぶことによって情報が溜まっていく。
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
/*
		// 空のメソッドを追加する
		MethodVisitor mv;

		mv = cw.visitMethod(ACC_PUBLIC , "setCameraHeight", "(F)V", null, null);
		mv.visitCode();
		mv.visitInsn(RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cw.visitMethod(ACC_PUBLIC , "_orientCamera", "(F)V", null, null);
		mv.visitCode();
		mv.visitInsn(RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		mv = cw.visitMethod(ACC_PUBLIC , "_updateFovModifierHand", "()V", null, null);
		mv.visitCode();
		mv.visitInsn(RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();*/

		//Adapterを通して書き換え出来るようにする。
		ClassVisitor cv = new ClassAdapter_Renderer(cw);
		//元のクラスと同様の順番でvisitメソッドを呼んでくれる
		cr.accept(cv,ClassReader.EXPAND_FRAMES);

		//Writer内の情報をbyte配列にして返す。
		return cw.toByteArray();
    }

    /**
     書き換え対象かどうかを判定する。今回はClass名のみで。
     */
    private boolean accept(String className){
        return TARGET.equals(className);
    }
}
