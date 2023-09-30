package com.lyrica.cameramod.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import net.minecraft.launchwrapper.IClassTransformer;
import scala.tools.asm.Opcodes;

public class Transformer_EyeHeight implements IClassTransformer, Opcodes {
	public static final String TARGET = "net.minecraft.entity.Entity";

	/**
    クラスが最初に読み込まれた時に呼ばれる。

    @param  name クラスの難読化名
    @param  transformedName クラスの易読化名
    @param  bytes オリジナルのクラス
	 */
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes){
		if(!accept(transformedName))return bytes;
		//byte配列を読み込み、利用しやすい形にする。
		ClassReader cr = new ClassReader(bytes);
		//これのvisitを呼ぶことによって情報が溜まっていく。
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		//Adapterを通して書き換え出来るようにする。
		ClassVisitor cv = new ClassAdapter_EyeHeight(cw);
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