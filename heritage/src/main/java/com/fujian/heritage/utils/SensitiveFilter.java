package com.fujian.heritage.utils;

import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class SensitiveFilter {

    // 敏感词库 (Trie树结构)
    private TrieNode rootNode = new TrieNode();

    // 假设这是从数据库或文件加载的敏感词列表
    private static final String[] SENSITIVE_WORDS = {"暴力", "赌博", "诈骗", "笨蛋"};

    @PostConstruct
    public void init() {
        for (String word : SENSITIVE_WORDS) {
            addKeyword(word);
        }
    }

    // 1. 定义前缀树节点
    private class TrieNode {
        // 关键词结束标识
        private boolean isKeywordEnd = false;
        // 子节点 (key是下级字符, value是下级节点)
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }
    }

    // 2. 将敏感词添加到前缀树
    private void addKeyword(String keyword) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            if (subNode == null) {
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }
            tempNode = subNode;
            if (i == keyword.length() - 1) {
                tempNode.setKeywordEnd(true);
            }
        }
    }

    // 3. 过滤文本算法 (核心算法)
    public String filter(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        TrieNode tempNode = rootNode;
        int begin = 0; // 指针1：指向文本中敏感词的起始位置
        int position = 0; // 指针2：指向文本中正在检查的字符

        while (position < text.length()) {
            char c = text.charAt(position);

            // 跳过特殊符号 (如 "赌-博")
            if (isSymbol(c)) {
                if (tempNode == rootNode) {
                    result.append(c);
                    begin++;
                }
                position++;
                continue;
            }

            // 检查下级节点
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                // 以begin开头的字符串不是敏感词
                result.append(text.charAt(begin));
                position = ++begin;
                tempNode = rootNode;
            } else if (tempNode.isKeywordEnd()) {
                // 发现敏感词，将 begin~position 字符串替换为 ***
                result.append("***");
                begin = ++position;
                tempNode = rootNode;
            } else {
                // 检查下一个字符
                position++;
            }
        }
        // 将最后一批字符计入结果
        result.append(text.substring(begin));
        return result.toString();
    }

    // 判断是否为符号
    private boolean isSymbol(Character c) {
        // 0x2E80~0x9FFF 是东亚文字范围
        return !((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
                && (c < 0x2E80 || c > 0x9FFF);
    }
}