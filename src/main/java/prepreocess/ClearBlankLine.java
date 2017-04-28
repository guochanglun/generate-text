package prepreocess;

import org.apache.commons.io.IOUtils;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by 14557 on 2017/4/26.
 */
public class ClearBlankLine {

    public static void main(String[] args) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(
                    ClearBlankLine.class.getResource("/data.txt").getPath());

            List<String> lines = IOUtils.readLines(fis);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
