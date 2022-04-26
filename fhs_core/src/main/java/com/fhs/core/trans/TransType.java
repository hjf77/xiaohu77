package com.fhs.core.trans;

/**
 * 翻译类型
 */
public interface TransType {
    /**
     * 字典
     */
    String DICT = "wordbook";
    /**
     * pagex
     */
    String PAGEX = "pagex";
    /**
     * 自动翻译
     */
    String AUTO = "auto";
    /**
     * 简单翻译
     */
    String SIMPLE = "simple";

    /**
     * 远程翻译
     */
    String RPC = "rpc";
}
