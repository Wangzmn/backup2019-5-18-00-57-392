package ex;

/**
 * @作者 做就行了！
 * @时间 2019-05-13下午 11:57
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class Length {
    public int start;
    public int end;

    public Length() {
    }

    public Length(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public void setLength(Length length) {
        start = length.start;
        end = length.end;
    }

    public void setLength(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getValue() {
        return end - start;
    }
}
