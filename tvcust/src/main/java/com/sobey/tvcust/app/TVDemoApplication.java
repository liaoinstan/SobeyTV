/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sobey.tvcust.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.sobey.common.utils.ApplicationHelp;
import com.sobey.tvcust.BuildConfig;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

public class TVDemoApplication extends Application {


	/**
	 * 当前用户nickname,为了苹果推送不是userid而是昵称
	 */
	public static String currentUserNick = "";

	@Override
	public void onCreate() {
		MultiDex.install(this);
		super.onCreate();
		ApplicationHelp.getApplicationContext(this);

		initJpush();
		initXutils();
//		initHuanxin();
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	private void initJpush() {
		JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
		JPushInterface.init(this);     		// 初始化 JPush
	}

	private void initXutils(){
		x.Ext.init(this);
		x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
	}
	private void initHuanxin(){
		TVHelper.getInstance().init(this);
	}
}
