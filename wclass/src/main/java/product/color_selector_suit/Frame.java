package product.color_selector_suit;

/**
 * @作者 做就行了！
 * @时间 2019/3/29 0029
 * @使用说明：
 */
public class Frame {

    /**
     * 一个正方形容器中：
     * A在上方，B和C在下方。
     *
     * @param baseSize
     */
    public void resize(int baseSize) {
        float aes = baseSize*4/5;
        float aes_another = baseSize - aes;

        float A_width = baseSize;
        float A_height = aes;
        float B_width = aes;
        float B_height = aes_another;
        float C_width = aes_another;
        float C_height = aes_another;
        float A_x = 0;
        float A_y = 0;

        float B_x = 0;
        float B_y = aes;

        float C_x = aes;
        float C_y = aes;
        if (cb != null) {
            cb.on(baseSize, aes, aes_another);
            cb.onA(A_width, A_height, A_x, A_y);
            cb.onB(B_width, B_height, B_x, B_y);
            cb.onC(C_width, C_height, C_x, C_y);
        }
    }

    public void setCb(Callback cb) {
        this.cb = cb;
    }

    Callback cb;

    public interface Callback {
        void on(int baseSize, float aes, float aes_another);
        void onA(float width, float height, float x, float y);

        void onB(float width, float height, float x, float y);

        void onC(float width, float height, float x, float y);
    }
}
