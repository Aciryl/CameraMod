package com.lyrica.cameramod.asm;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import com.lyrica.cameramod.CameraMod;

public class ClassAdapter_Renderer extends ClassVisitor {
	public ClassAdapter_Renderer(ClassVisitor cv){
		super(ASM5, cv);
    }

	/**
	 メソッドについて呼ばれる。

	 @param access
	 {@link Opcodes}に載ってるやつ。publicとかstaticとかの状態がわかる。
	 @param name
	 メソッドの名前。
	 @param desc
	 メソッドの(引数と返り値を合わせた)型。{@link Type#getArgumentTypes(String)},{@link Type#getClassName()}
	 @param signature
	 ジェネリック部分を含むメソッドの(引数と返り値を合わせた)型。ジェネリック付きでなければおそらくnull。{@link Type#getArgumentTypes(String)},{@link
	 Type#getClassName()}
	 @param exceptions
	 throws句にかかれているクラスが列挙される。Lと;で囲われていないので{@link String#replace(char, char)}で'/'と'.'を置換してやればOK。
	 @return ここで返したMethodVisitorのメソッド群が適応される。ClassWriterがセットされていればMethodWriterがsuperから降りてくる。
	 */
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions){
		String methodName;

		//ターゲット(ここではgetMouseOver)かどうかを判定し、
		methodName = CameraMod.DEBUG ? "getMouseOver" : "func_78473_a";
		if(methodName.equals(ASMHelper.mapMethodName(Transformer_Renderer.TARGET, name, desc)) && ASMHelper.toDesc(void.class, float.class).equals(desc))
			return new Adapter_getMouseOver(access, desc, super.visitMethod(access, name, desc, signature, exceptions));

		methodName = CameraMod.DEBUG ? "renderWorld" : "func_78471_a";
		//if("renderWorld".equals(ASMHelper.mapMethodName(MMRendererTransformer.TARGET, name, desc)) && ASMHelper.toDesc(void.class, float.class, long.class).equals(desc)) {
		if(methodName.equals(ASMHelper.mapMethodName(Transformer_Renderer.TARGET, name, desc)) && ASMHelper.toDesc(void.class, float.class, long.class).equals(desc))
			return new Adapter_renderWorld(access, desc, super.visitMethod(access, name, desc, signature, exceptions));

		methodName = CameraMod.DEBUG ? "setupCameraTransform" : "func_78479_a";
		//if("setupCameraTransform".equals(ASMHelper.mapMethodName(MMRendererTransformer.TARGET, name, desc)) && ASMHelper.toDesc(void.class, float.class, int.class).equals(desc))
		if(methodName.equals(ASMHelper.mapMethodName(Transformer_Renderer.TARGET, name, desc)) && ASMHelper.toDesc(void.class, float.class, int.class).equals(desc))
			return new Adapter_setupCameraTransform(access, desc, super.visitMethod(access, name, desc, signature, exceptions));

		methodName = CameraMod.DEBUG ? "updateRenderer" : "func_78464_a";
		//if("updateRenderer".equals(ASMHelper.mapMethodName(MMRendererTransformer.TARGET, name, desc)) && ASMHelper.toDesc(void.class).equals(desc))
		if(methodName.equals(ASMHelper.mapMethodName(Transformer_Renderer.TARGET, name, desc)) && ASMHelper.toDesc(void.class).equals(desc))
			return new Adapter_updateRenderer(access, desc, super.visitMethod(access, name, desc, signature, exceptions));

		methodName = CameraMod.DEBUG ? "updateCameraAndRender" : "func_78480_b";
		//if("updateCameraAndRender".equals(ASMHelper.mapMethodName(MMRendererTransformer.TARGET, name, desc)) && ASMHelper.toDesc(void.class, float.class).equals(desc))
		if(methodName.equals(ASMHelper.mapMethodName(Transformer_Renderer.TARGET, name, desc)) && ASMHelper.toDesc(void.class, float.class).equals(desc))
			return new Adapter_updateCameraAndRender(access, desc, super.visitMethod(access, name, desc, signature, exceptions));

		methodName = CameraMod.DEBUG ? "orientCamera" : "func_78467_g";
		// "orientCamera"
		if(methodName.equals(ASMHelper.mapMethodName(Transformer_Renderer.TARGET, name, desc)) && ASMHelper.toDesc(void.class, float.class).equals(desc))
			return new Adapter_orientCamera(access, desc, super.visitMethod(access, name, desc, signature, exceptions));

		return super.visitMethod(access, name, desc, signature, exceptions);
	}
}