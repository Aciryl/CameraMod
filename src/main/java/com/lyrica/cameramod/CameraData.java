package com.lyrica.cameramod;

import java.util.LinkedList;

public class CameraData {
	// ファイル
	protected final FileManager file;

	// 新しい Data か区別するためのID
	protected int loadID = 0;

	// データ
	// カメラのプリセット
	public LinkedList<String> cameraPresetList;
	public String cameraPresetName;
	// カメラ
	public float cameraDis;
	public float cameraX;
	public float cameraY;
	public float cameraSpeed;

	public CameraData(FileManager file) {
		this.file = file;
	}

	// ロードID
	public int getLoadID() {
		return loadID;
	}

	// プリセットの変更
	public boolean changeCamera(String name) {
		if (!cameraPresetList.contains(name))
			return false;
		saveData();

		cameraPresetName = name;
		loadData(false);
		return true;
	}

	// プリセットの追加
	public boolean addCamera(String name) {
		if (cameraPresetList.contains(name))
			return false;
		cameraPresetList.add(name);
		return true;
	}

	// プリセットの削除
	public boolean deleteCamera(String name) {
		if (!cameraPresetList.contains(name))
			return false;

		String oldName = cameraPresetName.equals(name) ? "default" : cameraPresetName;
		saveData();

		cameraPresetName = name;
		deleteCameraData();

		cameraPresetName = oldName;
		loadData(false);
		return true;
	}

	// プリセットのコピー
	public boolean copyToCamera(String name) {
		if (!cameraPresetList.contains(name))
			return false;

		String oldName = cameraPresetName;

		cameraPresetName = name;
		saveData();

		cameraPresetName = oldName;
		//loadData(false);
		return true;
	}

	// ----------------- Default -----------------
	// プリセット
	private LinkedList getDefaultCameraPresetList() {
		LinkedList<String> list = new LinkedList<String>();
		list.add("default");
		return list;
	}

	private String getDefaultCameraPresetName() {
		return "default";
	}

	// カメラ位置
	private float getDefaultCameraDis() {
		return 0.0f;
	}

	private float getDefaultCameraX() {
		return 0.0f;
	}

	private float getDefaultCameraY() {
		return 0.0f;
	}

	// カメラスピード
	public float getDefaultCameraSpeed() {
		return 20.0f;
	}

	// ----------------- データのロード＆セーブ -----------------
	public void loadData() {
		loadData(true);
	}

	public void loadData(boolean flag) {
		// ロードIDを変える
		++loadID;

		// カメラ
		// プリセット
		if (flag) {
			cameraPresetList = getCameraData("List", getDefaultCameraPresetList());
			cameraPresetName = getCameraData("Name", getDefaultCameraPresetName());
		} else {
			saveCameraPreset();
		}

		cameraDis =   getCameraData("Distance",     getDefaultCameraDis());
		cameraX =     getCameraData("CameraX",      getDefaultCameraX());
		cameraY =     getCameraData("CameraY",      getDefaultCameraY());
		cameraSpeed = getCameraData("CameraSpeed",  getDefaultCameraSpeed());
	}

	public void saveData() {
		// カメラ
		// プリセット
		saveCameraPreset();

		setCameraData("Distance", cameraDis);
		setCameraData("CameraX", cameraX);
		setCameraData("CameraY", cameraY);
		setCameraData("CameraSpeed", cameraSpeed);
	}

	private void saveCameraPreset() {
		if (!cameraPresetList.contains(cameraPresetName))
			cameraPresetList.add(cameraPresetName);

		setCameraData("List", cameraPresetList, false);
		setCameraData("Name", cameraPresetName, false);
	}

	private void deleteCameraData() {
		removeCameraData("Distance");
		removeCameraData("CameraX");
		removeCameraData("CameraY");
		removeCameraData("CameraSpeed");

		// リストから削除
		cameraPresetList.remove(cameraPresetName);
	}

	private int getData(String key, int defaultValue) {
		return file.hasKey(key) ? file.getIntData(key) : defaultValue;
	}

	private long getData(String key, long defaultValue) {
		return file.hasKey(key) ? file.getLongData(key) : defaultValue;
	}

	private float getData(String key, float defaultValue) {
		return file.hasKey(key) ? file.getFloatData(key) : defaultValue;
	}

	private double getData(String key, double defaultValue) {
		return file.hasKey(key) ? file.getDoubleData(key) : defaultValue;
	}

	private boolean getData(String key, boolean defaultValue) {
		return file.hasKey(key) ? file.getBooleanData(key) : defaultValue;
	}

	private String getData(String key, String defaultValue) {
		return file.hasKey(key) ? file.getStringData(key) : defaultValue;
	}

	private LinkedList<String> getData(String key, LinkedList<String> defaultValue) {
		return file.hasKey(key) ? file.getStringListData(key) : defaultValue;
	}

	private void setData(String key, Object data) {
		if (data instanceof Integer)
			file.setIntData(key, (Integer)data);
		else if (data instanceof Long)
			file.setLongData(key, (Long)data);
		else if (data instanceof Float)
			file.setFloatData(key, (Float)data);
		else if (data instanceof Double)
			file.setDoubleData(key, (Double)data);
		else if (data instanceof Boolean)
			file.setBooleanData(key, (Boolean)data);
		else if (data instanceof String)
			file.setStringData(key, (String)data);
		else if (data instanceof LinkedList<?>) {
			if (data == null || ((LinkedList) data).size() == 0)
				file.setStringData(key, "");
			else if (((LinkedList) data).getFirst() instanceof String)
				file.setStringListData(key, (LinkedList<String>)data);
		}
		return;
	}

	private void removeData(String key) {
		file.delete(key);
	}

	// カメラ用
	private float getCameraData(String key, float defaultValue) {
		key = "Camera " + cameraPresetName + " " + key;
		return getData(key, defaultValue);
	}

	private String getCameraData(String key, String defaultValue) {
		key = "Camera " + key;
		return getData(key, defaultValue);
	}

	private LinkedList<String> getCameraData(String key, LinkedList<String> defaultValue) {
		key = "Camera " + key;
		return getData(key, defaultValue);
	}

	private void setCameraData(String key, Object data) {
		setCameraData(key, data, true);
	}

	private void setCameraData(String key, Object data, boolean flag) {
		if (flag)
			key = "Camera " + cameraPresetName + " " + key;
		else
			key = "Camera " + key;

		setData(key, data);
		return;
	}

	private void removeCameraData(String key) {
		key = "Camera " + cameraPresetName + " " + key;
		removeData(key);
	}
}
