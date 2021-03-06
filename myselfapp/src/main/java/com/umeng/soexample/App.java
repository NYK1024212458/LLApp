package com.umeng.soexample;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.StrictMode;
import android.widget.TextView;

import com.android.core.StaticValue;
import com.android.core.control.logcat.*;
import com.android.core.control.logcat.BuildConfig;
import com.android.core.utils.ThemeUtils;
import com.baronzhang.android.router.Router;
import com.example.http.BaseApplication;
import com.umeng.soexample.manager.ConfigManage;
import com.umeng.soexample.music.MusicService;
import com.umeng.soexample.music.Playlist;
import com.heaton.liulei.utils.utils.LLUtils;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.soexample.service.RouterService;

import org.litepal.LitePalApplication;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * 作者： liulei
 * 公司：希顿科技
 */
public class App extends BaseApplication {

    public static final String WX_APPID = "wx387b0430eb11c163";
    public static final String WX_APPSecret = "66a6edf9c1fb372e5d12d2d0f85532f5";

    private static App instance;
    public MusicService mMusicServer;
    public ArrayList<Playlist> musicList;
    public int batteryValue;
    public RouterService routerService;

    @Override
    public void onCreate() {
        super.onCreate();

        //解决7.0  FileUriExposedException
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        instance = this;
        //创建全局的路由
        routerService = new Router(this).create(RouterService.class);
        StaticValue.color = ThemeUtils.getThemeColor(this);
        //开启debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        BuildConfig.DEBUG = false;
        // 初始化友盟组件
        UMShareAPI.get(this);
        //初始化LiuleiUtils
        LLUtils.init(this);
        //初始化加载文章缩略图配置
        ConfigManage.INSTANCE.initConfig(this);
        //litepal的配置
        LitePalApplication.initialize(this);
        //崩溃日志
        //注册crashHandler
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
        //Android crash 上传服务器回掉  暂时注释
//        HttpReportCallback report = new HttpReportCallback() {
//            @Override
//            public void uploadException2remote(File file) {
//                //可以直接上传文件
//            }
//        };
//        AndroidCrash.getInstance().setCrashReporter(report).init(this);
        if (BuildConfig.DEBUG) {
            Logcat.init("com.android.racofix").hideThreadInfo().methodCount(3);
        }
        //检查程序哪里出现ANR异常
//        BlockLooper.initialize(new BlockLooper.Builder(this)
//        .setIgnoreDebugger(true)
//        .setReportAllThreadInfo(true)
//        .setSaveLog(true)
//        .setOnBlockListener(new BlockLooper.OnBlockListener() {
//            @Override
//            public void onBlock(BlockError blockError) {
//                blockError.printStackTrace();
//            }
//        })
//        .build());
//        BlockLooper.getBlockLooper().start();//启动检测
    }

    //各个平台的配置，建议放在全局Application或者程序入口
    {
        //微信    wx12342956d1cab4f9,a5ae111de7d9ea137e88a5e02c07c94d
//        PlatformConfig.setWeixin("wx1b4ebbf52692b609", "c7ac8f697f3c7661a3356d11f99a0434");
        PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0");
        //新浪微博
//        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
//        PlatformConfig.setQQZone("1105331365", "XRvsFLkjIW9cBQ6J");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    }

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }

    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }

    /**
     * 更新电池电量及图标
     *
     * @param value 电量值
     */
    public void updateBatteryAndIcon(int value, TextView view) {
        batteryValue = value;
        Drawable battery = null;
        if (value <= 10) {
            battery = getResources().getDrawable(R.mipmap.power0);
        } else if (value <= 25) {
            battery = getResources().getDrawable(R.mipmap.power1);
        } else if (value <= 50) {
            battery = getResources().getDrawable(R.mipmap.power2);
        } else if (value <= 75) {
            battery = getResources().getDrawable(R.mipmap.power3);
        } else {
            battery = getResources().getDrawable(R.mipmap.power4);
        }
        battery.setBounds(0, 0, battery.getMinimumWidth(), battery.getMinimumHeight());
        view.setCompoundDrawables(battery, null, null, null);
        view.setText(value + "%");
    }

}
