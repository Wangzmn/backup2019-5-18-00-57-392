package wclass.android.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;

/**
 * @作者 做就行了！
 * @时间 2019-03-18下午 11:06
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class PhoneUTUT_misc {

    private static final int REQUEST_OVERLAY = 1;
    private static final int REQUEST_PIC_CLIP = 2;
    private static final int REQUEST_CHOICE_PIC = 3;
    private static final int REQUEST_USER_INFO = 4;
    private static final int REQUEST_ALL_DEVICE = 5;
    private static final int REQUEST_MY_DEVICE = 6;

    /**
     * 启动至 自己的设备管理器界面
     */
//    static void toMyDevice(Activity activity) {
//        ComponentName deviceAdmin = new ComponentName
//                (activity.getApplicationContext(), H02_DeviceReceiver.class);
//        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdmin);
//        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"请开启权限");
//        try{
//            activity.startActivityForResult(intent, REQUEST_MY_DEVICE);
//        }catch(Exception ignored){}
//    }
    /**
     * 启动至 总的设备管理器界面
     */
    static void toAllDevice(Activity activity) {
        Intent intent = new Intent();
        ComponentName cm = new ComponentName("com.android.settings",
                "com.android.settings.DeviceAdminSettings");
        intent.setComponent(cm);
        try{
            activity.startActivityForResult(intent,REQUEST_ALL_DEVICE);
        }catch(Exception ignored){}
    }

    /**
     * 启动至 “用户信息查看权” 界面
     */
    public static  void jumpToUserInfo(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//Ver 21
            try{
                context.startActivity
                        (new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }

    /**
     * 启动至 “用户信息查看权” 界面
     */
    public static void jumpToUserInfo(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//Ver 21
            try {
                activity.startActivityForResult
                        (new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), requestCode);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    /**
     * 启动 相册界面
     */
    static void toPhotoPage(Activity activity) {//todo 极少情况出现 activity not found
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            activity.startActivityForResult(intent, REQUEST_CHOICE_PIC); //启动带有返回值的页面
        }catch(Exception ignored){}
    }
    /**
     * 启动 剪裁界面
     */
    private static void toClipPage(Activity activity, Intent data) {
        Intent intent = new Intent("com.android.camera.action.CROP");//剪裁工具
        intent.setDataAndType(data.getData(), "image/*");//放入数据，调出符合type的程序
        intent.putExtra("crop", "true");//加不加无所谓
        intent.putExtra("aspectX", 1);//剪裁时的选框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);//同时固定选框比例
        intent.putExtra("outputY", 200);
        // 切图大小不足输出，无黑框
        intent.putExtra("scale", true);//不固定比例时，才生效
        intent.putExtra("scaleUpIfNeeded", true);//同上
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());//输出格式

        try {
            activity.startActivityForResult(intent, REQUEST_PIC_CLIP);//启动裁剪程序
        }catch(Exception ignored){}
    }
}
