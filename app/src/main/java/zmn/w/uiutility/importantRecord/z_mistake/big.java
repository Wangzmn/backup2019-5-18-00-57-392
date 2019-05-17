package zmn.w.uiutility.importantRecord.z_mistake;

/**
 * @作者 做就行了！
 * @时间 2019/2/15 0015
 * @使用说明：
 */
public class big {
    //step float类型的数值，转int需要注意！！！
    {
        float cornerWidthPer;//角标的宽，占父容器宽的百分比。
        float cornerHeightPer;//角标的高，占父容器高的百分比。
        /**
         * 正确：
         * (int) (cornerWidthPer * width)
         * (int) (cornerHeightPer * height)
         * 错误：
         * (int) cornerWidthPer * width
         * (int) cornerHeightPer * height
         */
    }
}
