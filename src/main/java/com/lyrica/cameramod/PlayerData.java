package com.lyrica.cameramod;

public class PlayerData {
	// ファイル
	protected final FileManager file;

	// 新しい Data か区別するためのID
	protected int loadID = 0;

	// データ
	// プレイヤーのステータス
	public float height;
	public float width;
	public float eyeHeight;

	public PlayerData(FileManager file) {
		this.file = file;
	}

	// ロードID
	public int getLoadID() {
		return loadID;
	}

	// ----------------- Default -----------------
	// ステータス
	private float getDefaultHeight() {
		return 1.8f;
	}

	private float getDefaultWidth() {
		return 0.6f;
	}

	private float getDefaultEyeHeight() {
		return 1.62f;
	}

	// ----------------- データのロード＆セーブ -----------------

	public void loadData()
	{
		// ロードIDを変える
		++loadID;

		// ステータスを読み込む
		height =    getData("Height", getDefaultHeight());
		width =     getData("Width", getDefaultWidth());
		eyeHeight = getData("Eye Height", getDefaultEyeHeight());
	}

	public void saveData() {
		// ステータスを書き込む
		setData("Height", height);
		setData("Width", width);
		setData("Eye Height", eyeHeight);
	}

	private float getData(String key, float defaultValue) {
		return file.hasKey(key) ? file.getFloatData(key) : defaultValue;
	}

	private void setData(String key, float data) {
		file.setFloatData(key, (Float)data);
	}
}
