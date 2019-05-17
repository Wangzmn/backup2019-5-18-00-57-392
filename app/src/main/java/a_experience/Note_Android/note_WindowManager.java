package a_experience.Note_Android;

import android.view.WindowManager;

/**
 * @作者 做就行了！
 * @时间 2019/1/26 0026
 * @使用说明：
 */
@SuppressWarnings("DanglingJavadoc")
public class note_WindowManager {
    /**
     * step 结论：
     * 1、窗口能点击，view不能点击时，不能继续往下层传递事件。
     * 2、后添加的窗口，在最上层。
     * 3、窗口中的rootView为INVISIBLE时，窗口不拦截点击事件。
     * 4、当params.width/height=WRAP_CONTENT 时，窗口以左上角为原点。
     * 5、窗体大小改变影响坐标时，里边的控件会延迟改变，大约延迟200毫秒。
     *    每次窗体改变大小时，这个200毫秒会重新计时。
     */
    /**
     * 安卓7.1.1及以下最好使用：TYPE_PHONE。
     */
    /**
     * step 详细说明：
     * 1、{@link WindowManager.LayoutParams#FLAG_NOT_TOUCH_MODAL}
     * ①能获取焦点时，无法触摸。
     * ②名取的不好，准确的说应该是：FLAG_NOT_TOUCHABLE_WHEN_FOCUSING。
     * 友情提示：FLAG_NOT_FOCUSABLE时，该属性无效。
     * 2、{@link WindowManager.LayoutParams#FLAG_NOT_FOCUSABLE}
     * ①不能获得焦点。
     * 3、{@link WindowManager.LayoutParams#FLAG_NOT_TOUCHABLE}
     * ①不能触摸。
     * 4、{@link WindowManager.LayoutParams#FLAG_ALT_FOCUSABLE_IM}
     * ①输入法高于自己。
     */
}
