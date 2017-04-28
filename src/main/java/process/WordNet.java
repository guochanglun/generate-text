package process;

import org.apache.commons.io.IOUtils;
import 中文语句生成.WordNode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 14557 on 2017/4/26.
 */
public class WordNet {

    private String preWord = null;
    private List<String> keyList;
    private Map<String, WordNode> container;

    public static void main(String[] args) {
        WordNet net = new WordNet();
        String basePath = net.getClass().getResource("/").getPath();
        String inPath = basePath + "/data.txt";

        // 创建词网络
        net.container = net.createWordNet(inPath);

        if (net.container == null) {
            return;
        }

        net.keyList = new ArrayList<>(net.container.keySet());

        // 产生随机文本
        String text = net.generateText(100, 0.6);
        System.out.println(text);
    }

    private String generateText(int number, double epsilon) {

        int containerSize = container.size();
        int position = (int) (Math.random() * 1000 % containerSize);
        String randomKey = this.keyList.get(position);

        WordNode node = this.container.get(randomKey);
        StringBuilder text = new StringBuilder();
        while (number != 0) {

            text.append(node.getBody()).append(" ");

            WordNode sonNode = node.getSon(epsilon);

            if (sonNode == null) {
                position = (int) (Math.random() * 1000 % containerSize);
                randomKey = this.keyList.get(position);
                sonNode = this.container.get(randomKey);
            }

            node = sonNode;
            --number;
        }
        return text.toString();
    }

    private Map<String, WordNode> createWordNet(String inPath) {
        Map<String, WordNode> container = new HashMap<>();

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(inPath);
            List<String> lines = IOUtils.readLines(fis);

            lines.forEach(line -> {
                if (line != null && line.length() != 0) {
                    for (String word : line.split(" ")) {

                        if (word == null || word.trim().length() == 0) {
                            continue;
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
                            continue;
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
                    }
                }
            });

            return container;
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
        return null;
    }
}
