package com.lyrica.cameramod.asm;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import scala.tools.asm.Type;

public class ASMHelper {
	//super.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
	//super.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

	//super.visitLdcInsn(5);
	//super.visitMethodInsn(INVOKESTATIC, "com/lyrica/cameramod/asm/TestHook", "testHook", "(I)V", false);

	/**
    下の{@link #toDesc(Object)}をMethodのDescriptor用に使えるようにしたもの。
    thisを指定するときは{@link Class}型ではなく{@link String}型を使用してください。

    @param returnType {@link String}型か、{@link Class}型で目的のMethodの返り値の型を指定する。
    @param rawDesc {@link String}型か、{@link Class}型でMethodの引数たちの型を指定する。
    @throws IllegalArgumentException 引数に{@link String}型か、{@link Class}型以外が入ったら投げられる。
    @return Javaバイトコードで扱われる形の文字列に変換されたDescriptor。
    */
   public static String toDesc(Object returnType,Object... rawDesc){
       StringBuilder sb = new StringBuilder("(");
       for(Object o : rawDesc){
           sb.append(getDescriptor(o));
       }
       sb.append(')');
       sb.append(getDescriptor(returnType));
       return sb.toString();
   }

   public static String toDesc(Object returnType){
       StringBuilder sb = new StringBuilder("(");
       sb.append(')');
       sb.append(getDescriptor(returnType));
       return sb.toString();
   }

   /**
    {@link Class#forName}とか{@link Class#getCanonicalName()}したりするとまだ読み込まれてなかったりしてまずいので安全策。

    @param raw {@link String}型か、{@link Class}型でASM用の文字列に変換したいクラスを指定する。
    @throws IllegalArgumentException {@param raw}に{@link String}型か、{@link Class}型以外が入ったら投げられる。
    @return Javaバイトコードで扱われる形の文字列に変換されたクラス。
    */
   public static String getDescriptor(Object raw){
       if(raw instanceof Class){
           Class<?> clazz = (Class<?>) raw;
           return Type.getDescriptor(clazz);
       }else if(raw instanceof String){
           String desc = (String) raw;
           desc = desc.replace('.','/');
           desc = desc.matches("L.+;")?desc:"L"+desc+";";
           return desc;
       }else {
           throw new IllegalArgumentException();
       }
   }

   /**
   クラスの名前を難読化(obfuscation)する。
   */
  public static String unmapClassName(String name){
      return FMLDeobfuscatingRemapper.INSTANCE.unmap(name.replace('.','/')).replace('/', '.');
  }

  /**
   メソッドの名前を易読化(deobfuscation)する。
   */
  public static String mapMethodName(String owner,String methodName,String desc){
      return FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(unmapClassName(owner), methodName, desc);
  }

  /**
   フィールドの名前を易読化(deobfuscation)する。
   */
  public static String mapFieldName(String owner,String methodName,String desc){
      return FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(unmapClassName(owner), methodName, desc);
  }
}