package a_experience.Note_Java;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @作者 做就行了！
 * @时间 2019/2/21 0021
 * @使用说明：
 */
public class dafsd {
    /*step try-with-resources：try(){}的用法*/
    {
        /**
         * {@link AutoCloseable}
         * 1、放入括号中的对象，使用完后自动关闭。
         */
        try(
                InputStream is = new FileInputStream("...");
                OutputStream os = new FileOutputStream("...");
        ){
            //...
        }catch (IOException e) {
            //...
        }
    }
}
