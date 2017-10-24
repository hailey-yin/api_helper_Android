package com.woders.apiprovider;

/**
 * title:
 * desc:返回对象的类型
 * Created by jiangguangming on 2015/10/30.
 */
public interface APIType {
    int LIST = 0x1;
    int OBJECT = 0x2;
    int PAGE = 0x3;
    int NONE = 0x4;
    int MAP = 0x5;
}
