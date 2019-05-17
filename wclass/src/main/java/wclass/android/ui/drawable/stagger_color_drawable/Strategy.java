package wclass.android.ui.drawable.stagger_color_drawable;

public enum Strategy {
        NORMAL,//不改变大小。最后一个不能完全显示。
        FULL_TO_BIG,//数量少一个，每个大一点。
        FULL_TO_SMALL,//数量多一个，每个小一点。
        /**
         * 最后一个位置剩余空间较大时，该属性为{@link #FULL_TO_SMALL}。
         * 最后一个位置剩余空间较小时，该属性为{@link #FULL_TO_BIG}。
         */
        FULL_AUTO
    }