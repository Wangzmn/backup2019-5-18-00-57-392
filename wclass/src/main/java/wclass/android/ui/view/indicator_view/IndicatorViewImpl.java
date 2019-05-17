package wclass.android.ui.view.indicator_view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者 做就行了！
 * @时间 2019/4/8 0008
 * @使用说明：
 */
public class IndicatorViewImpl extends IndicatorView {
    private static final boolean DEBUG = false;
    private static final boolean RECT_DEBUG = true;
    /**
     * 指示器数量。
     */
    private int maxIndiCount;

    /**
     * 常态的指示器 的宽高。
     * <p>
     * normer的宽高。
     */
    private float normerSize;

//    /**
//     * 被选中的指示器 的宽。
//     * <p>
//     * stander的宽。
//     */
//    private int standerWidth;

    /**
     * 横向分成的份数。
     */
    private float sectionWidth;

    /**
     * stander的rect。
     */
    Rect standerRect = new Rect();

    /**
     * 所有normer的rect。
     */
    List<Rect> rects;

    public IndicatorViewImpl(Context context, int maxIndiCount) {
        super(context, maxIndiCount);
        init(maxIndiCount);
    }

    private void init(int maxIndiCount) {
        this.maxIndiCount = maxIndiCount;
        rects = new ArrayList<>(maxIndiCount);
        for (int i = 0; i < maxIndiCount; i++) {
            rects.add(new Rect());
        }
    }

    public IndicatorViewImpl(Context context, int maxIndiCount,
                             Drawable standerDrawable, NormerDrawableMaker m) {
        super(context, maxIndiCount, standerDrawable, m);
        init(maxIndiCount);
    }

    public IndicatorViewImpl(Context context, int maxIndiCount, int normerColor, int standerColor) {
        super(context, maxIndiCount, normerColor, standerColor);
        init(maxIndiCount);
    }

    @Override
    protected void onCalculateAllDrawing(boolean changed) {
        /**
         * 1、按每个都是小正方形设计。
         * 2、
         */
        float width = getWidth();
        float height = getHeight();
        normerSize= height;
        sectionWidth = (width-normerSize) / (maxIndiCount-1);
        if (DEBUG) {
            Log.e("TAG", getClass().getSimpleName() + "#onCalculateAllDrawing:" +
                    " sectionWidth = " + sectionWidth + " ," +
                    " normerSize = " + normerSize);
        }
        super.onCalculateAllDrawing(changed);
    }

    @Override
    protected void onCalculateNormersRect(boolean changed, Rect usableRect) {
        if (DEBUG) {
            Log.e("TAG", getClass().getSimpleName() + "#onCalculateNormersRect：" +
                    "changed = " + changed);
        }
        if (!changed) {
            return;
        }
        int top = usableRect.top;
        int bottom = usableRect.bottom;
        for (int i = 0; i < maxIndiCount; i++) {
            Rect rect = rects.get(i);
            int left = (int) (usableRect.left + i * sectionWidth);
            int right = (int) (left + normerSize);
            rect.set(left, top, right, bottom);
            if (RECT_DEBUG) {
                Log.e("TAG", getClass().getSimpleName() + "#onCalculateNormersRect：" +
                        "rect" + i +
                        " = " + rect);
            }
        }
        if (RECT_DEBUG) {
            Log.e("TAG", getClass().getSimpleName() + "#onCalculateNormersRect：" +
                    "rectCount = " + rects.size() + " 。");
        }

    }

    @Override
    protected void onCalculateStanderRect(boolean changed, Rect usableRect, int selectDex) {
        if (DEBUG) {
            Log.e("TAG", getClass().getSimpleName() + "#onCalculateStanderRect：" +
                    "changed = " + changed);
        }
    }

    @Override
    protected void onApplyNormerRect(boolean changed) {
        if (DEBUG) {
            Log.e("TAG", getClass().getSimpleName() + "#onApplyNormerRect：" +
                    "changed = " + changed);
        }
        if (!changed) {
            return;
        }
        for (int i = 0; i < maxIndiCount; i++) {
            Drawable normerDrawable = getNormerDrawable(i);
            Rect normerRect = getNormerRect(i);
            normerDrawable.setBounds(normerRect);
            if (DEBUG) {
                Log.e("TAG", getClass().getSimpleName() + "#onApplyNormerRect：" +
                        "normerRect" + i + " = " + normerRect);
            }
        }
    }

    @Override
    protected void onApplyStanderRect(boolean changed, int dex) {
        if (DEBUG) {
            Log.e("TAG", getClass().getSimpleName() + "#onApplyStanderRect：" +
                    "changed = " + changed);
        }
        if (!changed) {
            return;
        }
        Rect standerRect = getStanderRect(dex, getNormerRect(dex));
        if (DEBUG) {
            Log.e("TAG", getClass().getSimpleName() + "#onApplyStanderRect：" +
                    "standerRect = " + standerRect);
        }
        setStanderRect(standerRect);
    }

    @Override
    protected Rect getNormerRect(int dex) {
        return rects.get(dex);
    }

    @Override
    protected Rect getStanderRect(int dex, Rect normerRect) {
        int i = maxIndiCount - 1;
        if (dex == i) {
            dex = i - 1;
            normerRect = getNormerRect(dex);
        }

        Rect normerRect1 = getNormerRect(dex + 1);

        standerRect.left = normerRect.left;
        standerRect.top = normerRect.top;
        standerRect.right = normerRect1.right;
        standerRect.bottom = normerRect.bottom;

        return standerRect;
    }
}
