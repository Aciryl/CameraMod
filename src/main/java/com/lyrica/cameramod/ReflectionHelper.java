package com.lyrica.cameramod;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionHelper {
	// フィールド
	public static Field getField(Class c, String s) {
		try
		{
			Field f = c.getDeclaredField(s);
			f.setAccessible(true);
			return f;
		}
		catch (NoSuchFieldException e)
		{
			e.printStackTrace();
			throw new RuntimeException(String.format("フィールドへのアクセスに失敗しました。"));
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
			throw new RuntimeException(String.format("フィールドへのアクセスに失敗しました。"));
		}
	}

	public static void setField(Object o, Field f, Object value) {
		try
		{
			f.set(o, value);
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
			throw new RuntimeException(String.format("フィールドの代入に失敗しました。"));
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
			throw new RuntimeException(String.format("フィールドの代入に失敗しました。"));
		}
	}

	public static Object getFieldValue(Object o, Field f) {
		try
		{
			return f.get(o);
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
			throw new RuntimeException(String.format("フィールドの取得に失敗しました。"));
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
			throw new RuntimeException(String.format("フィールドの取得に失敗しました。"));
		}
	}

	public static int getIntField(Object o, Field f) {
		return (Integer)getFieldValue(o, f);
	}

	public static long getLongField(Object o, Field f) {
		return (Long)getFieldValue(o, f);
	}

	public static float getFloatField(Object o, Field f) {
		return (Float)getFieldValue(o, f);
	}

	public static double getDoubleField(Object o, Field f) {
		return (Double)getFieldValue(o, f);
	}

	public static boolean getBooleanField(Object o, Field f) {
		return (Boolean)getFieldValue(o, f);
	}

	public static String getStringField(Object o, Field f) {
		return (String)getFieldValue(o, f);
	}

	// メソッド
	public static Method getMethod(Class c, String s, Class... parm) {
		try
		{
			Method m = c.getDeclaredMethod(s, parm);
			m.setAccessible(true);
			return m;
		}
		catch (NoSuchMethodException e)
		{
			e.printStackTrace();
			throw new RuntimeException(String.format("メソッドへのアクセスに失敗しました。"));
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
			throw new RuntimeException(String.format("メソッドへのアクセスに失敗しました。"));
		}
	}

	public static Object invokeMethod(Object o, Method m, Object... parm) {
		try
		{
			return m.invoke(o, parm);
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
			throw new RuntimeException(String.format("メソッドの実行に失敗しました。"));
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
			throw new RuntimeException(String.format("メソッドの実行に失敗しました。"));
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
			throw new RuntimeException(String.format("メソッドの実行に失敗しました。"));
		}
	}
}