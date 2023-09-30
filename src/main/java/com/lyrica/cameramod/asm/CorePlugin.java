package com.lyrica.cameramod.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class CorePlugin implements IFMLLoadingPlugin
{
	//書き換え機能を実装したクラス一覧を渡す関数。書き方はパッケージ名+クラス名。
    @Override
    public String[] getASMTransformerClass() {
        return new String[]{"com.lyrica.cameramod.asm.Transformer_Renderer",
        					"com.lyrica.cameramod.asm.Transformer_EyeHeight",
        					"com.lyrica.cameramod.asm.Transformer_EntityLivingBase",
        					"com.lyrica.cameramod.asm.Transformer_TPV"};
    }

    //あとは今回は使わない為適当に。
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public String getModContainerClass() {
        return null;
    }
}