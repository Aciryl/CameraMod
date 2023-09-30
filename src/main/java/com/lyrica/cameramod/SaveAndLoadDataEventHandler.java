package com.lyrica.cameramod;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Save;

public class SaveAndLoadDataEventHandler {
	// ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆
	//                                                              データ
	// ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆

	FileManager file;

	public SaveAndLoadDataEventHandler(FileManager file) {
		this.file = file;
	}

	// データのロード
	@SubscribeEvent
	public void loadDataEvent(Load event) {
		// ファイルの読み込み
		file.updateSaveFileName();
		file.loadFile();

		// プレイヤーデータのロード
		CameraMod.cameraData.loadData();
		CameraMod.playerData.loadData();
	}

	// データのセーブ
	@SubscribeEvent
	public void saveDataEvent(Save event) {
		// プレイヤーデータのセーブ
		CameraMod.cameraData.saveData();
		CameraMod.playerData.saveData();

		// ファイルの書き込み
		file.saveFile();
	}
}