package prepreocess;

import org.apache.commons.io.IOUtils;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by 14557 on 2017/4/26.
 */
public class Test {

    public static void main(String[] args) throws IOException {

        String text = getNoval();
        StringReader sr = new StringReader(text);
        IKSegmenter ik = new IKSegmenter(sr, true);
        Lexeme lex = null;

        int i = 0;
        while ((lex = ik.next()) != null) {

            if (i > 50) {
                break;
            }
            i++;

            System.out.print(lex.getLexemeText() + "|");
        }
    }

    public static String getNoval() throws IOException {
        StringBuilder builder = new StringBuilder();

        String path = Test.class.getResource("/7609.txt").getPath();
        FileInputStream fis = new FileInputStream(path);
        IOUtils.readLines(fis).forEach(line -> builder.append(line));
        return builder.toString();
    }
}
