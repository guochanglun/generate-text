package 中文语句生成;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 14557 on 2017/4/26.
 */
public class WordNode {

    private String body;
    private WordNode maxSonNode;
    private int maxSonNum;
    private Map<WordNode, Integer> map = new HashMap<>();

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getMaxSonNum() {
        return maxSonNum;
    }

    public void setMaxSonNum(int maxSonNum) {
        this.maxSonNum = maxSonNum;
    }

    public Map<WordNode, Integer> getMap() {
        return map;
    }

    public void setMap(Map<WordNode, Integer> map) {
        this.map = map;
    }

    public WordNode getMaxSonNode() {
        return maxSonNode;
    }

    public void setMaxSonNode(WordNode maxSonNode) {
        this.maxSonNode = maxSonNode;
    }

    public WordNode getSon(double epsilon) {
        if (map.isEmpty()) {
            return null;
        }

        if (Math.random() > epsilon) {
            return maxSonNode;
        } else {
            int pos = (int) (Math.random() * 1000 % map.size());
            return (WordNode) map.keySet().toArray()[pos];
        }
    }
}
