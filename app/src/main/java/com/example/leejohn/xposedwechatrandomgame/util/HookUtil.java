package com.example.leejohn.xposedwechatrandomgame.util;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class HookUtil implements IXposedHookLoadPackage{
	private int diceCount = 0;
//	private int morraNum = 0; // 0-剪刀 1-石头 2-布

	@Override
	public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable{

		if (!lpparam.packageName.equals("com.tencent.mm")){
			return;
		}
		XposedBridge.log("-----com.tencent.mm---------");
//		findAndHookMethod("com.tencent.mm.sdk.platformtools.bb", lpparam, "pu"); //6.3.9
		findAndHookMethod("com.tencent.mm.sdk.platformtools.bg", lpparam,"dK"); //6.5.10

	}


	private void findAndHookMethod(String className, final LoadPackageParam lpparam, String methodName) throws Exception {
		final Class clazz = Class.forName(className, false, lpparam.classLoader);
		XposedBridge.hookAllMethods(clazz,  methodName,  new XC_MethodReplacement(){
			@Override
			protected Object replaceHookedMethod(MethodHookParam param) throws Throwable{
				int gameType = (int) param.args[0];
				XposedBridge.log("-----replaceHookedMethod----------- param :"+ gameType);
				XSharedPreferences pre = new XSharedPreferences("com.example.leejohn.xposedwechatrandomgame");
				switch (gameType){
					case 5: // 摇骰子
						pre.reload();
						diceCount = pre.getInt("dice_num", 0);
						XposedBridge.log("查询获取骰子数为:" + diceCount);
						break;

					case 2: // 猜拳
						pre.reload();
						diceCount = pre.getInt("morra_num", 0);
						XposedBridge.log("查询猜拳数为:" + diceCount);
						break;
				}
				XposedBridge.log("replaceHookedMethod--函数返回值:" + diceCount);
				return diceCount;
			}
		});
	}

}
