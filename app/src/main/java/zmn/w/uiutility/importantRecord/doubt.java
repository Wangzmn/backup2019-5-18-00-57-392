package zmn.w.uiutility.importantRecord;

import android.view.WindowManager;


/**
 * @作者 做就行了！
 * @时间 2019/1/25 0025
 * @使用说明：
 */
public class doubt {
    /**
     * fix 2019年2月24日17:59:03
     * 1、移动窗口时，长按/点击 app 还会触发功能，想办法取消这些操作。
     */
    /**
     * fix 2019年2月15日12:27:31
     * 1、有时不启动activity。
     *    解决：使用application的context就行了
     * fix 2019年2月15日17:28:24
     * 1、menuPlugin进入编辑状态，再打开输入法并关闭，再退出编辑状态，之后icon图片放大不正常了。
     * 2、{@link Adapter}中的item大小跟随prefer，需要更改。
     */
    /**
     * step 运行后产生的：
     * 1、{@link WindowManager.LayoutParams#TYPE_PHONE}不显示悬浮窗口。
     *    还有时导致整个屏幕不能点击
     */
}
