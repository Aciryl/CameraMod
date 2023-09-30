package com.lyrica.cameramod;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class CameraCommand extends CommandBase{
	private final List aliases;
	private int confFlag = 0;
	private String confKind = "";
	private String confString = "";
	private String confPlayer = "";

	private ICommandSender sender;

	public CameraCommand() {
		aliases = new ArrayList();
        aliases.add("cameramod");
        aliases.add("cm");
	}

	@Override
	public String getCommandName() {
		return "CameraMod";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] var2) {
		--confFlag;
		this.sender = sender;

		if (var2.length < 1) {
			error(0);
			return;
		}

		String param0 = var2[0].toLowerCase();

		if (param0.equals("help")) {
			command_help(var2);
			return;
		}

		if (param0.equals("preset")) {
			command_Preset(var2);
			return;
		}

		if (param0.equals("presetlist") || param0.equals("list")) {
			command_PresetList(var2);
			return;
		}

		if (param0.equals("camerax")) {
			command_CameraX(var2);
			return;
		}

		if (param0.equals("cameray")) {
			command_CameraY(var2);
			return;
		}

		if (param0.equals("cameradistance") || param0.equals("cameradist") || param0.equals("cameradis") || param0.equals("cameraz")) {
			command_CameraZ(var2);
			return;
		}

		if (param0.equals("cameraspeed") || param0.equals("cameras")) {
			command_CameraSpeed(var2);
			return;
		}

		if (param0.equals("height")) {
			command_Height(var2);
			return;
		}

		if (param0.equals("width")) {
			command_Width(var2);
			return;
		}

		if (param0.equals("eyeheight")) {
			command_EyeHeight(var2);
			return;
		}

		error(2);
	}

	private void command_help(String[] var2) {
		// ChatFormatting.BLACK + "" で色変更
		chat("§eCameraMod は cameramod または cm に置き換えることができます");
		chat("§e第2引数に help を入力するとより詳しい情報が表示されます");
		chat("§e ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆ - + - ☆");

		chat("/CameraMod <§6Preset§r> <§6Help§r | §6Add§r | §6Set§r | §6Get§r | §6change§r |");
		chat("                    §6delete§r | §6copy§r | §6MoveTo§r> [(String)§bPresetName§r]");
		chat("--> カメラのプリセットの設定や削除などができます。");

		chat("/CameraMod <§6PresetList§r | §6List§r>");
		chat("--> カメラのプリセットリストを表示します");

		chat("/CameraMod <§6CameraX§r> <§6Help§r | §6Set§r | §6Get§r | §6Add§r> [(Float)§bCameraX§r]");
		chat("--> 三人称視点時のカメラのX座標を調節します");

		chat("/CameraMod <§6CameraY§r> <§6Help§r | §6Set§r | §6Get§r | §6Add§r> [(Float)§bCameraY§r]");
		chat("--> 三人称視点時のカメラのY座標を調節します");

		chat("/CameraMod <§6CameraDistance§r | §6CameraDist§r | §6CameraDis§r | §6CameraZ§r>");
		chat("           <§6Help§r | §6Set§r | §6Get§r | §6Add§r> [(Float)§bCameraDistance§r]");
		chat("--> 三人称視点時のカメラとプレイヤーの距離を調節します");

		chat("/CameraMod <§6CameraSpeed§r | §6CameraS§r> <§6Help§r | §6Set§r | §6Get§r | §6Add§r>");
		chat("           [(Float)§bCameraSpeed§r]");
		chat("--> キーを使ってカメラを動かすときのスピードを調節します");

		chat("/CameraMod <§6Height§r> <§6Help§r | §6Set§r | §6Get§r | §6Add§r> [(Float)§bHeight§r]");
		chat("           [§6True§r | §6False§r = True]");
		chat("-> プレイヤーの高さを設定または取得します。");

		chat("/CameraMod <§6Width§r> <§6Help§r | §6Set§r | §6Get§r | §6Add§r> [(Float)§bWidth§r]");
		chat("--> プレイヤーの横幅を設定または取得します。");

		chat("/CameraMod <§6EyeHeight§r> <§6Help§r | §6Set§r | §6Get§r> [(Float) §bEyeHeight§r]");
		chat("--> プレイヤーの視点の高さを設定または取得します。");
	}

	// カメラのプリセット
	private void command_Preset(String[] var2) {
		if (var2.length < 2 || var2[1] == null) {
			error(0);
			return;
		}

		String param1 = var2[1].toLowerCase();

		// Help
		if (param1.equals("help")) {
			chat("/CameraMod <§6Preset§r> <§6Add§r> <(String)§bPresetName§r>");
			chat("--> 新しくプリセットを登録します");
			chat("    既に登録されている名前は使用できません");
			chat("/CameraMod <§6Preset§r> <§6Set§r> <(String)§bPresetName§r>");
			chat("--> 新しくプリセットを登録し、現在のプリセットを登録したプリセットに");
			chat("    変更します");
			chat("    既にプリセットが登録されている場合は、プリセットの変更だけ行います");
			chat("/CameraMod <§6Preset§r> <§6Get§r>");
			chat("--> 現在のプリセット名を表示します");
			chat("/CameraMod <§6Preset§r> <§6Change§r> <(String)§bPresetName§r>");
			chat("--> 現在のプリセットを指定されたプリセットに変更します");
			chat("/CameraMod <§6Preset§r> <§6Delete§r> [(String)§bPresetName§r]");
			chat("--> 指定されたプリセットを削除します");
			chat("    プリセット名を指定しなかった場合は、現在のプリセットを削除します");
			chat("    現在のプリセットが削除された場合は、プリセットを『§bdefault§r』に変更");
			chat("    します");
			chat("/CameraMod <§6Preset§r> <§6CopyTo§r> <(String)§bPresetName§r>");
			chat("--> 指定されたプリセットのデータを、現在のデータで上書きします");
			chat("    指定されたプリセットが登録されていない場合は、新しくプリセットを");
			chat("    登録します");
			chat("/CameraMod <§6Preset§r> <§6MoveTo§r> <(String)§bPresetName§r>");
			chat("--> 指定されたプリセットのデータを、現在のデータで上書きします");
			chat("    指定されたプリセットが登録されていない場合は、新しくプリセットを");
			chat("    登録します。");
			chat("    その後、現在のプリセットを指定されたプリセットに変更します");
			return;
		}

		CameraData data = CameraMod.cameraData;

		// Set
		boolean playerAddFlag = true;
		if (param1.equals("set")) {
			playerAddFlag = !data.cameraPresetList.contains(var2[2]);
		}

		// Add, Set
		if (param1.equals("add") || (param1.equals("set") && playerAddFlag)) {
			// 引数の数を確認
			if (var2.length != 3)
			{
				error(1);
				return;
			}

			// default は使用できない
			if (var2[2].toLowerCase().equals("default")) {
				error(4);
				return;
			}

			// プリセットが既に登録されている時
			if (!data.addCamera(var2[2])) {
				error(5);
				return;
			}

			chat("プリセットリストに『§b" + var2[2] + "§r』を追加しました");
			if (!param1.equals("set"))
				return;
		}

		// Change, Set
		if (param1.equals("change") || param1.equals("set")) {
			// 引数の数を確認
			if (var2.length != 3)
			{
				error(1);
				return;
			}

			// プリセットが登録されていない時
			if (!data.changeCamera(var2[2])) {
				error(6);
				return;
			}

			chat("プリセットを『§b" + var2[2] + "§r』に変更しました");
			return;
		}

		// CopyTo, MoveTo
		if (param1.equals("copyto") || param1.equals("moveto")) {
			// 引数の数を確認
			if (var2.length != 3)
			{
				error(1);
				return;
			}

			playerAddFlag = false;
			// プリセットが登録されていない時
			if (!data.cameraPresetList.contains(var2[2])) {
				// プリセットを登録する
				// (プリセットが既に登録されている時)
				if (!data.addCamera(var2[2])) {
					error(5);
					return;
				}

				chat("プリセットリストに『§b" + var2[2] + "§r』を追加しました");
				playerAddFlag = true;
			}

			// 同じプリセットにコピーしようとしている時
			if (data.cameraPresetName.equals(var2[2])) {
				error(7);
				return;
			}

			if (!playerAddFlag &&
			   (confFlag <= 0 || !"CopyTo".equals(confKind) || !confString.equals(var2[2]) || !confPlayer.equals(data.cameraPresetName))) {
				chat("§cプリセット『§b" + var2[2] + "§c』のデータを『§b" + data.cameraPresetName + "§c』のデータで上書きします");
				chat("§cよろしければもう一度同じコマンドを実行してください");
				confFlag = 2;
				confKind = "CopyTo";
				confString = var2[2];
				confPlayer = data.cameraPresetName;
				return;
			}

			//String oldName = data.playerName;

			// (プリセットが登録されていない時)
			if (!data.copyToCamera(var2[2])) {
				error(6);
				return;
			}

			if (playerAddFlag)
				chat("プリセット『§b" + var2[2] + "§r』に『§b" + data.cameraPresetName + "§r』のデータをにコピーしました");
			else
				chat("プリセット『§b" + var2[2] + "§r』のデータを『§b" + data.cameraPresetName + "§r』のデータで上書きしました");

			// プリセットを変更
			if (param1.equals("moveto")) {
				// (プリセットが登録されていない時)
				if (!data.changeCamera(var2[2])) {
					error(6);
					return;
				}

				chat("プレイセットを『§b" + var2[2] + "§r』に変更しました");
			}
			return;
		}

		if (param1.equals("get")) {
			// 引数の数を確認
			if (var2.length != 2)
			{
				error(1);
				return;
			}

			chat("現在のプリセットの名前は『§b" + data.cameraPresetName + "§r』です");
			return;
		}

		if (param1.equals("delete")) {

			// 引数の数を確認
			if (var2.length != 2 && var2.length != 3)
			{
				error(1);
				return;
			}

			String presetName;
			// 引数が2つの時は現在のプリセット名を削除する
			if (var2.length == 2)
				presetName = data.cameraPresetName;
			else
				presetName = var2[2];

			// プリセットが登録されていない時
			if (!data.cameraPresetList.contains(presetName)) {
				error(6);
				return;
			}

			if (confFlag <= 0 || !"Delete".equals(confKind) || !confString.equals(presetName) || !confPlayer.equals(data.cameraPresetName)) {
				chat("§cプリセット『§b" + presetName + "§c』を削除します");
				chat("§cよろしければもう一度同じコマンドを実行してください");
				confFlag = 2;
				confKind = "Delete";
				confString = presetName;
				confPlayer = data.cameraPresetName;
				return;
			}

			// (プリセットが登録されていない時)
			if (!data.deleteCamera(presetName)) {
				error(6);
				return;
			}
			chat("プリセット『§b" + presetName + "§r』を削除しました");
			return;
		}

		error(3, 2);
	}

	// プレイヤーリスト
	private void command_PresetList(String[] var2) {
		// 引数の数を確認
		if (var2.length != 1)
		{
			error(1);
			return;
		}

		CameraData data = CameraMod.cameraData;
		chat("プリセットリストを表示します");
		for (String name : data.cameraPresetList) {
			chat("§b" + name);
		}
	}

	// カメラ
	private void command_CameraX(String[] var2) {
		if ((var2.length < 2)) {
			error(0);
			return;
		}

		String param1 = var2[1].toLowerCase();

		// 引数の数をチェック
		if ((param1.equals("set") && var2.length != 3) ||
		   (param1.equals("add") && var2.length != 3) ||
		   (param1.equals("get") && var2.length != 2) ||
		   (param1.equals("help") && var2.length != 2))
		{
			error(1);
			return;
		}

		if (param1.equals("help")) {
			chat("/CameraMod <§6CameraX§r> <§6Set§r> <(Float)§bCameraX§r>");
			chat("--> カメラのX座標を設定します");
			chat("/CameraMod <§6CameraX§r> <§6Get§r>");
			chat("--> カメラのX座標を表示します");
			chat("/CameraMod <§6CameraX§r> <§6Add§r> <(Float)§bCameraX§r>");
			chat("--> カメラのX座標を加算します");
			return;
		}

		// Set Get Add のみ
		if (!canParseSGA(param1)) {
			error(8, 2);
			return;
		}

		CameraData data = CameraMod.cameraData;

		if (param1.equals("set") || param1.equals("add")) {
			String param2 = var2[2].toLowerCase();

			if (!canParseFloat(param2))
			{
				error(9, 3);
				return;
			}

			float cameraX;
			if (param1.equals("set"))
				cameraX = Float.parseFloat(param2);
			else
				cameraX = data.cameraX + Float.parseFloat(param2);

			data.cameraX = cameraX;
			chat("カメラのX座標を§b" + cameraX + "§rに設定しました");

			return;
		}

		if (param1.equals("get")) {
			chat("カメラのX座標は§b" + data.cameraX + "§rです");
			return;
		}
	}

	private void command_CameraY(String[] var2) {
		if ((var2.length < 2)) {
			error(0);
			return;
		}

		String param1 = var2[1].toLowerCase();

		// 引数の数をチェック
		if ((param1.equals("set") && var2.length != 3) ||
		   (param1.equals("add") && var2.length != 3) ||
		   (param1.equals("get") && var2.length != 2) ||
		   (param1.equals("help") && var2.length != 2))
		{
			error(1);
			return;
		}

		if (param1.equals("help")) {
			chat("/CameraMod <§6CameraY§r> <§6Set§r> <(Float)§bCameraY§r>");
			chat("--> カメラのY座標を設定します");
			chat("/CameraMod <§6CameraY§r> <§6Get§r>");
			chat("--> カメラのY座標を表示します");
			chat("/CameraMod <§6CameraY§r> <§6Add§r> <(Float)§bCameraY§r>");
			chat("--> カメラのY座標を加算します");
			return;
		}

		// Set Get Add のみ
		if (!canParseSGA(param1)) {
			error(8, 2);
			return;
		}

		CameraData data = CameraMod.cameraData;

		if (param1.equals("set") || param1.equals("add")) {
			String param2 = var2[2].toLowerCase();

			if (!canParseFloat(param2))
			{
				error(9, 3);
				return;
			}

			float cameraY;
			if (param1.equals("set"))
				cameraY = Float.parseFloat(param2);
			else
				cameraY = data.cameraY + Float.parseFloat(param2);

			data.cameraY = cameraY;
			chat("カメラのY座標を§b" + cameraY + "§rに設定しました");

			return;
		}

		if (param1.equals("get")) {
			chat("カメラのY座標は§b" + data.cameraY + "§rです");
			return;
		}
	}

	private void command_CameraZ(String[] var2) {
		if ((var2.length < 2)) {
			error(0);
			return;
		}

		String param1 = var2[1].toLowerCase();

		// 引数の数をチェック
		if ((param1.equals("set") && var2.length != 3) ||
		   (param1.equals("add") && var2.length != 3) ||
		   (param1.equals("get") && var2.length != 2) ||
		   (param1.equals("help") && var2.length != 2))
		{
			error(1);
			return;
		}

		if (param1.equals("help")) {
			chat("§6CameraDistance§r は §6CameraDist CameraDis CameraZ§r に置き換えられます");
			chat("/CameraMod <§6CameraDistance§r> <§6Set§r> <(Float)§bCameraDistance§r>");
			chat("--> カメラの距離を設定します");
			chat("/CameraMod <§6CameraDistance§r> <§6Get§r>");
			chat("--> カメラの距離を表示します");
			chat("/CameraMod <§6CameraDistance§r> <§6Add§r> <(Float)§bCameraDistance§r>");
			chat("--> カメラの距離を加算します");
			return;
		}

		// Set Get Add のみ
		if (!canParseSGA(param1)) {
			error(8, 2);
			return;
		}

		CameraData data = CameraMod.cameraData;

		if (param1.equals("set") || param1.equals("add")) {
			String param2 = var2[2].toLowerCase();

			if (!canParseFloat(param2))
			{
				error(9, 3);
				return;
			}

			float cameraZ;
			if (param1.equals("set"))
				cameraZ = Float.parseFloat(param2);
			else
				cameraZ = data.cameraDis + Float.parseFloat(param2);

			data.cameraDis = cameraZ;
			chat("カメラの距離を§b" + cameraZ + "§rに設定しました");

			return;
		}

		if (param1.equals("get")) {
			chat("カメラの距離は§b" + data.cameraDis + "§rです");
			return;
		}
	}

	private void command_CameraSpeed(String[] var2) {
		if ((var2.length < 2)) {
			error(0);
			return;
		}

		String param1 = var2[1].toLowerCase();

		// 引数の数をチェック
		if ((param1.equals("set") && var2.length != 3) ||
		   (param1.equals("add") && var2.length != 3) ||
		   (param1.equals("get") && var2.length != 2) ||
		   (param1.equals("help") && var2.length != 2))
		{
			error(1);
			return;
		}

		if (param1.equals("help")) {
			chat("§6CameraSpeed§r は §6CameraS§r に置き換えられます");
			chat("/CameraMod <§6CameraSpeed§r> <§6Set§r> <(Float)§bCameraSpeed§r>");
			chat("--> カメラのスピードを設定します");
			chat("    CameraSpeed に『§bDefault§r』と入力した場合は、デフォルトの値を設定できます");
			chat("/CameraMod <§6CameraSpeed§r> <§6Get§r>");
			chat("--> カメラのスピードを表示します");
			chat("/CameraMod <§6CameraSpeed§r> <§6Add§r> <(Float)§bCameraSpeed§r>");
			chat("--> カメラのスピードを加算します");
			return;
		}

		// Set Get Add のみ
		if (!canParseSGA(param1)) {
			error(8, 2);
			return;
		}

		CameraData data = CameraMod.cameraData;

		if (param1.equals("set") || param1.equals("add")) {
			String param2 = var2[2].toLowerCase();

			// デフォルト
			if (param1.equals("set") && param2.equals("default"))
				param2 = new Float(CameraMod.cameraData.getDefaultCameraSpeed()).toString();

			if (!canParseFloat(param2))
			{
				error(9, 3);
				return;
			}

			float cameraS;
			if (param1.equals("set"))
				cameraS = Float.parseFloat(param2);
			else
				cameraS = data.cameraSpeed + Float.parseFloat(param2);

			data.cameraSpeed = cameraS;
			chat("カメラのスピードを§b" + cameraS + "§rに設定しました");

			return;
		}

		if (param1.equals("get")) {
			chat("カメラのスピードは§b" + data.cameraSpeed + "§rです");
			return;
		}
	}

	// プレイヤーの高さの変更
	private void command_Height(String[] var2) {
		if ((var2.length < 2)) {
			error(0);
			return;
		}

		String param1 = var2[1].toLowerCase();

		// 引数の数をチェック
		if ((param1.equals("set") && var2.length != 3 && var2.length != 4) ||
		   (param1.equals("add") && var2.length != 3 && var2.length != 4) ||
		   (param1.equals("get") && var2.length != 2) ||
		   (param1.equals("help") && var2.length != 2))
		{
			error(1);
			return;
		}

		if (param1.equals("help")) {
			chat("/CameraMod <§6Height§r> <§6Set§r> <(Float)§bHeight§r> [§6True§r | §6False§r = True]");
			chat("--> プレイヤーの高さを設定します");
			chat("    Height に『§bDefault§r』と入力した場合は、デフォルトの値を設定できます");
			chat("    第4引数に『§bTrue§r』を入力すると、自動で横幅と視点の高さが設定されます。省略すると『§bTrue§r』です");
			chat("/CameraMod <§6Height§r> <§6Get§r>");
			chat("--> 現在のプレイヤーの高さを表示します");
			chat("/CameraMod <§6Height§r> <§6Add§r> <(Float)§bHeight§r> [§6True§r | §6False§r = True]");
			chat("--> プレイヤーの高さを加算します");
			chat("    第4引数に『§bTrue§r』を入力すると、自動で横幅と視点の高さが設定されます。省略すると『§bTrue§r』です");
			return;
		}

		// Set Get Add のみ
		if (!canParseSGA(param1)) {
			error(8, 2);
			return;
		}

		PlayerData data = CameraMod.playerData;

		if (param1.equals("set") || param1.equals("add")) {
			String param2 = var2[2].toLowerCase();
			if (param1.equals("set") && param2.equals("default"))
				param2 = new Float(1.8f).toString();

			if (!canParseFloat(param2)) {
				error(9, 3);
				return;
			}

			boolean bool;
			if (var2.length < 4)
				bool = true;
			else
			{
				String param3 = var2[3].toLowerCase();
				if (!canParseBoolean(param3))
				{
					error(10, 4);
					return;
				}
				bool = Boolean.parseBoolean(param3);
			}

			float height;
			if (param1.equals("set"))
				height = Float.parseFloat(param2);
			else
				height = data.height + Float.parseFloat(param2);

			if (height <= 0) {
				chat("プレイヤーの高さは§b0§rより大きい値を指定してください");
				return;
			}

			data.height = height;
			chat("プレイヤーの高さを§b" + height + "§rに設定しました");

			if (height <= 0.5f) {
				chat("§c[警告] プレイヤーの高さが§b0.5§c以下の場合は、");
				chat("§c       浮いている半ブロックの上でスニークしたときに動けなくなります");
			}

			if (bool) {
				float eyeHeight = height * 0.9f;
				data.eyeHeight = eyeHeight;
				setPlayerEyeHeight(Minecraft.getMinecraft().thePlayer, eyeHeight);
				float width = height / 1.8f * 0.6f;
				data.width = width;

				chat("プレイヤーの横幅を§b" + width + "§rに設定しました");
				chat("視点の高さを§b" + eyeHeight + "§rに設定しました");
			}
			return;
		}

		if (param1.equals("get")) {
			chat("現在のプレイヤーの高さは§b" + data.height + "§rです");
			return;
		}

		return;
	}

	// プレイヤーの横幅の変更
	private void command_Width(String[] var2) {
		if ((var2.length < 2)) {
			error(0);
			return;
		}

		String param1 = var2[1].toLowerCase();

		// 引数の数をチェック
		if ((param1.equals("set") && var2.length != 3) ||
		   (param1.equals("add") && var2.length != 3) ||
		   (param1.equals("get") && var2.length != 2) ||
		   (param1.equals("help") && var2.length != 2))
		{
			error(1);
			return;
		}

		if (param1.equals("help")) {
			chat("/CameraMod <§6Width§r> <§6Set§r> <(Float)§bWidth§r>");
			chat("--> プレイヤーの横幅を設定します");
			chat("    Width に『§bDefault§r』と入力した場合は、デフォルトの値を設定できます");
			chat("/CameraMod <§6Width§r> <§6Get§r>");
			chat("--> 現在のプレイヤーの横幅を表示します");
			chat("/CameraMod <§6Width§r> <§6Add§r> <(Float)§bWidth§r>");
			chat("--> プレイヤーの横幅を加算します");
			return;
		}

		// Set Get Add のみ
		if (!canParseSGA(param1)) {
			error(8, 2);
			return;
		}

		PlayerData data = CameraMod.playerData;

		if (param1.equals("set") || param1.equals("add")) {
			String param2 = var2[2].toLowerCase();
			if (param1.equals("set") && param2.equals("default"))
				param2 = new Float(0.6f).toString();

			if (!canParseFloat(param2))
			{
				error(9, 3);
				return;
			}

			float width;
			if (param1.equals("set"))
				width = Float.parseFloat(param2);
			else
				width = data.width + Float.parseFloat(param2);

			if (width <= 0) {
				chat("プレイヤーの横幅は§b0§rより大きい値を指定してください");
				return;
			}

			data.width = width;
			chat("プレイヤーの横幅を§b" + width + "§rに設定しました");

			return;
		}

		if (param1.equals("get")) {
			chat("現在のプレイヤーの横幅は§b" + data.width + "§rです");
			return;
		}

		return;
	}
/*
	// プレイヤーの高さと幅の変更
	private void command_HandW(String[] var2) {
		if ((var2.length < 2)) {
			error(0);
			return;
		}

		String param1 = var2[1].toLowerCase();

		// 引数の数をチェック
		if ((param1.equals("set") && var2.length != 3) ||
		   (param1.equals("get") && var2.length != 2) ||
		   (param1.equals("help") && var2.length != 2))
		{
			error(1);
			return;
		}

		if (param1.equals("help")) {
			chat("/CameraMod <§6Width§r> <§6Set§r> <(Float)§bWidth§r>");
			chat("--> プレイヤーの横幅を設定します");
			chat("    Width に『§bDefault§r』と入力した場合は、デフォルトの値を設定できます");
			chat("/CameraMod <§6Width§r> <§6Get§r>");
			chat("--> 現在のプレイヤーの横幅を表示します");
			return;
		}

		// Set または Get のみ
		if (!canParseSG(param1)) {
			error(8, 2);
			return;
		}

		if (var2[1].equals("set")) {
			String[] var0;
			String[] var1 = new String[]{"", var2[1], var2[3]};
			if (var2.length <= 4)
				var0 = new String[]{"", var2[1], var2[2]};
			else
				var0 = new String[]{"", var2[1], var2[2], var2[4]};

			command_Height(var0);
			command_Width(var1);
			return;
		}

		if (var2[1].equals("get")) {
			String[] var0 = new String[]{"", var2[1]};
			if (command_Height(var0))
				command_Width(var0);
			return;
		}
	}*/

	// 視点の高さの変更
	private void command_EyeHeight(String[] var2) {
		if ((var2.length < 2)) {
			error(0);
			return;
		}

		String param1 = var2[1].toLowerCase();

		// 引数の数をチェック
		if ((param1.equals("set") && var2.length != 3) ||
		   (param1.equals("get") && var2.length != 2) ||
		   (param1.equals("help") && var2.length != 2))
		{
			error(1);
			return;
		}

		if (param1.equals("help")) {
			chat("/CameraMod <§6EyeHeight§r> <§6Set§r> <(Float)§bEyeHeight§r>");
			chat("--> プレイヤーの視点の高さを設定します");
			chat("    EyeHeight に『§bDefault§r』と入力した場合は、デフォルトの値を設定できます");
			chat("/CameraMod <§6EyeHeight§r> <§6Get§r>");
			chat("--> 現在のプレイヤーの視点の高さを表示します");
			chat("/CameraMod <§6EyeHeight§r> <§6Add§r> <(Float)§bEyeHeight§r>");
			chat("--> プレイヤーの視点の高さを加算します");
			return;
		}

		// Set Get Add のみ
		if (!canParseSGA(param1)) {
			error(8, 2);
			return;
		}

		PlayerData data = CameraMod.playerData;

		if (param1.equals("set") || param1.equals("add")) {
			String param2 = var2[2].toLowerCase();
			if (param1.equals("set") && param2.equals("default"))
				param2 = new Float(1.62f).toString();

			if (!canParseFloat(param2))
			{
				error(9, 3);
				return;
			}

			float eyeHeight;
			if (param1.equals("set"))
				eyeHeight = Float.parseFloat(param2);
			else
				eyeHeight = data.eyeHeight + Float.parseFloat(param2);

			if (eyeHeight < 0) {
				chat("視点の高さは§b0§r以上を指定してください");
				return;
			}

			setPlayerEyeHeight(Minecraft.getMinecraft().thePlayer, eyeHeight);

			chat("視点の高さを§b" + eyeHeight + "§rに設定しました");
			return;
		}

		if (param1.equals("get")) {
			chat("現在の視点の高さは§b" + data.eyeHeight + "§rです");
			return;
		}
	}

	private void setPlayerEyeHeight(EntityPlayer player, float eyeHeight) {
		CameraMod.playerData.eyeHeight = eyeHeight;
		CameraEventHandler.setPlayerEyeHeight(player);
	}

	private void error(int i) {
		error(i, 0);
	}

	private void error(int i, int j) {
		switch (i)
		{
		case 0 :
			chat("§c引数を指定してください。/HeartMod help でコマンドの詳細を表示します");
			break;
		case 1:
			chat("§c引数の数が違います。/HeartMod help でコマンドの詳細を表示します");
			break;
		case 2 :
			chat("§c登録されていない引数です。/HeartMod help でコマンドの詳細を表示します");
			break;
		case 3 :
			chat("§c引数が違います。第" + j + "引数には Help Add Set Get Change Delete CopyTo MoveTo");
			chat("§cのいずれかを指定してください");
			break;
		case 4 :
			chat("§cdefault は使用できません。他の名前を指定してください");
			break;
		case 5 :
			chat("§c既に使われている名前です。他の名前を指定してください");
			break;
		case 6 :
			chat("§c登録されていない名前です。他の名前を指定してください");
			break;
		case 7 :
			chat("§c同じプリセットにはコピーできません。他の名前を指定してください");
			break;
		case 8 :
			chat("§c引数が違います。第" + j + "引数には Set Get Add のいずれかを指定してください");
			break;
		case 9 :
			chat("§c引数の型が違います。第" + j + "引数には Float(少数) を指定してください");
			break;
		case 10 :
			chat("§c引数の型が違います。第" + j + "引数には Boolean(True False) を指定してください");
			break;
		}
	}

	private boolean canParseFloat(String s) {
		try {
			Float.parseFloat(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private boolean canParseBoolean(String s) {
		if (s.toLowerCase().equals("true") || s.toLowerCase().equals("false"))
			return true;
		return false;
	}

	private boolean canParseSGA(String s) {
		if (s.toLowerCase().equals("set") || s.toLowerCase().equals("get") || s.toLowerCase().equals("add"))
			return true;
		return false;
	}

	@Override
	public List getCommandAliases() {
        return this.aliases;
    }

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "/CameraMod help を確認してください";
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return true;
	}

	private void chat(String s) {
		sender.addChatMessage(new ChatComponentText(s));
	}
}
