package 中文语句生成;

import org.apache.commons.io.IOUtils;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 14557 on 2017/4/26.
 */
public class ChinaWordNet {

    private static List<String> keyList;
    private static Map<String, WordNode> container;

    public static void main(String[] args) throws IOException {
        // 调用一次即可
//        try {
//            generateWordsFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // 生成wordnet
        String path = ChinaWordNet.class.getResource("/wordsList.txt").getPath();
        container = createWordsNet(path);
        if (container == null) {
            return;
        }

        keyList = new ArrayList<>(container.keySet());

        // 产生随机文本
        String text = generateText(100, 0.6);
        System.out.println(text);
    }

    private static String generateText(int number, double epsilon) {

        int containerSize = container.size();
        int position = (int) (Math.random() * 1000 % containerSize);
        String randomKey = keyList.get(position);

        WordNode node = container.get(randomKey);
        StringBuilder text = new StringBuilder();
        while (number != 0) {
            text.append(node.getBody());

            WordNode sonNode = node.getSon(epsilon);

            if (sonNode == null) {
                position = (int) (Math.random() * 1000 % containerSize);
                randomKey = keyList.get(position);
                sonNode = container.get(randomKey);
            }

            node = sonNode;
            --number;
        }
        return text.toString();
    }

    private static String preWord = null;

    private static Map<String, WordNode> createWordsNet(String path) throws IOException {
        if (path == null) return null;
        Map<String, WordNode> container = new HashMap<>();
        FileInputStream fis = new FileInputStream(path);
        List<String> lines = IOUtils.readLines(fis);

        lines.forEach(word -> {
            word = (String) word;
            if (word == null || word.trim().length() == 0) {
                return;
            }

            // 获取node
            WordNode node = container.getOrDefault(word, null);
            if (node == null) {
                node = new WordNode();
                node.setBody(word);
                container.put(word, node);
            }

            if (preWord == null) {
                preWord = word;
                return;
            }

            // 获取前一个词的node
            WordNode preNode = container.getOrDefault(preWord, null);
            if (preNode == null) {
                preNode = new WordNode();
                preNode.setBody(preWord);
                container.put(preWord, preNode);
            }

            // 建立关系
            int nodeCount = preNode.getMap().getOrDefault(node, 0);
            nodeCount += 1;
            if (nodeCount > preNode.getMaxSonNum()) {
                preNode.setMaxSonNum(nodeCount);
                preNode.setMaxSonNode(node);
            }
            preNode.getMap().put(node, nodeCount);

            // 更新
            preWord = word;
        });
        return container;
    }

    private static void generateWordsFile() throws IOException {

        String basePath = ChinaWordNet.class.getResource("/").getPath();

        String[] paths = new String[]{
                basePath + "/7609.txt",
                basePath + "/5441.txt",
                basePath + "/18976.txt",
                basePath + "/22945.txt"
        };
        StringBuilder wordsBuilder = new StringBuilder();
        for (String path : paths) {
            System.out.println(path);
            StringBuilder builder = new StringBuilder();
            FileInputStream fis = new FileInputStream(path);
            IOUtils.readLines(fis).forEach(line -> builder.append(line));

            StringReader sr = new StringReader(builder.toString());
            IKSegmenter ik = new IKSegmenter(sr, true);

            Lexeme lex = null;
            while ((lex = ik.next()) != null) {
                wordsBuilder.append(lex.getLexemeText()).append("\n");
            }
            fis.close();
        }

        System.out.println("写入文件");
        String outPath = basePath + "/wordsList.txt";
        FileOutputStream fos = new FileOutputStream(outPath);
        IOUtils.write(wordsBuilder.toString(), fos);
        fos.close();
    }
}
