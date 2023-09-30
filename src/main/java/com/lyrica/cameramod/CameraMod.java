package com.lyrica.cameramod;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = CameraMod.MODID, version = CameraMod.VERSION)
public class CameraMod
{
    public static final String MODID = "CameraAndPlayerSizeMod";
    public static final String VERSION = "1.0.0";

    public static final boolean DEBUG = false;

    private static final FileManager fileManager = new FileManager();
    public static final PlayerData playerData = new PlayerData(fileManager);
	public static final CameraData cameraData = new CameraData(fileManager);

    // キーボード
 	public static final KeyBinding keyLeft =      new KeyBinding("01 Left",      Keyboard.KEY_NUMPAD4, "Camera Position");
 	public static final KeyBinding keyRight =     new KeyBinding("02 Right",     Keyboard.KEY_NUMPAD6, "Camera Position");
 	public static final KeyBinding keyUp =        new KeyBinding("03 Up",        Keyboard.KEY_NUMPAD8, "Camera Position");
 	public static final KeyBinding keyDown =      new KeyBinding("04 Down",      Keyboard.KEY_NUMPAD5, "Camera Position");
 	public static final KeyBinding keyCloser =    new KeyBinding("05 Closer",    Keyboard.KEY_NUMPAD9, "Camera Position");
 	public static final KeyBinding keyFurther =   new KeyBinding("06 Further",   Keyboard.KEY_NUMPAD7, "Camera Position");

 	public static final KeyBinding keyZoom =      new KeyBinding("07 Zoom",      Keyboard.KEY_LCONTROL, "Camera Position");
 	public static final KeyBinding keyZoomInc =   new KeyBinding("08 ZoomInc",   Keyboard.KEY_NUMPAD3, "Camera Position");
 	public static final KeyBinding keyZoomDec =   new KeyBinding("09 ZoomDec",   Keyboard.KEY_NUMPAD1, "Camera Position");
 	public static final KeyBinding keyZoomReset = new KeyBinding("10 ZoomReset", Keyboard.KEY_NUMPAD2, "Camera Position");

 	// Renderer
 	public static boolean enableRenderer = true;

 	// カメラ
 	public static String[] cameraWhiteList = new String[]{};
 	public static String[] cameraBlackList = new String[]{};
 	public static float cameraZoom = 6.5f;
 	public static float cameraSensitivityMag = 3.0f;
 	public static float cameraFactor = 10.0f;

 	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
		try
		{
			cfg.load();// コンフィグをロード

			// ここでConfigの値の取得
			//cfgは特殊でgetを入れれば、自動でファイルの内容も変えられる。

			// Renderer
			enableRenderer = cfg.getBoolean("01 Custom Renderer", "01 Renderer", enableRenderer,
					"Custom Renderer(視点の変更やズームなど)を有効にするか" + cfg.NEW_LINE);

			// カメラ
			cameraWhiteList = cfg.getStringList("01 Camera Through Block Whitelist", "02 カメラ", cameraWhiteList,
					"三人称視点の時に視点を通すブロック名" + cfg.NEW_LINE +
					"記述した文字列がブロックの modid:name の一部と一致する全てのブロックを対象にします" + cfg.NEW_LINE +
					"デフォルトでは Material が plants, leaves, circuits, vine, dragonEgg, web" + cfg.NEW_LINE +
					"のものが登録されています" + cfg.NEW_LINE);
			cameraBlackList = cfg.getStringList("02 Camera Through Block Blacklist", "02 カメラ", cameraBlackList,
					"三人称視点の時に視点を通さないブロック名" + cfg.NEW_LINE +
					"記述の仕方はWhitelistと同じです" + cfg.NEW_LINE +
					"Whitelist と Blacklist 両方に属するブロックは Blacklist が優先されます" + cfg.NEW_LINE);
			cameraZoom = cfg.getFloat("03 Camera Zoom", "02 カメラ", cameraZoom, 0.0f, 1000.0f,
					"カメラのズーム機能を何倍にするか" + cfg.NEW_LINE);
			cameraFactor = cfg.getFloat("04 Camera Zoom Factor", "02 カメラ", cameraFactor, 0.0f, 1000.0f,
					"カメラのズーム機能を何％ずつ変更するか" + cfg.NEW_LINE);
			cameraSensitivityMag = cfg.getFloat("05 Camera Sensitivity", "02 カメラ", cameraSensitivityMag, 1.0f / Float.MAX_VALUE, Float.MAX_VALUE,
					"カメラをズームした時に視点の動きを小さくする係数" + cfg.NEW_LINE +
					"この値が大きいほど視点の動きは大きくなります" + cfg.NEW_LINE);
		}
		finally
		{
			cfg.save();// セーブ
		}
	}

 	@EventHandler
	public void init(FMLInitializationEvent event)
    {
 		CameraEventHandler EventHandler = new CameraEventHandler();
 		SaveAndLoadDataEventHandler sandlEventHandler = new SaveAndLoadDataEventHandler(fileManager);

 		MinecraftForge.EVENT_BUS.register(EventHandler);
 		MinecraftForge.EVENT_BUS.register(sandlEventHandler);

 		FMLCommonHandler.instance().bus().register(EventHandler);

 		// キーバインドの登録
 		ClientRegistry.registerKeyBinding(keyLeft);
		ClientRegistry.registerKeyBinding(keyUp);
		ClientRegistry.registerKeyBinding(keyRight);
		ClientRegistry.registerKeyBinding(keyDown);
		ClientRegistry.registerKeyBinding(keyCloser);
		ClientRegistry.registerKeyBinding(keyFurther);

		ClientRegistry.registerKeyBinding(keyZoom);
		ClientRegistry.registerKeyBinding(keyZoomInc);
		ClientRegistry.registerKeyBinding(keyZoomDec);
		ClientRegistry.registerKeyBinding(keyZoomReset);

		// レンダラーの初期化
		EntityRendererAlt.Init(Minecraft.getMinecraft());
    }

 	@EventHandler
	public void serverLoad(FMLServerStartingEvent event)
	{
		// コマンドの登録
		event.registerServerCommand(new CameraCommand());
	}
}
