//
// Created by iMorning on 2021-04-26.
//

#include "CodeUtils.h"
#include "JniUtils.h"
#include<iostream>
#include<ctime>

using namespace std;


//单个字符异或运算
char MakecodeChar(char c, int key) {
    return c = c ^ key;
}

//单个字符解密
char CutcodeChar(char c, int key) {
    return c ^ key;
}


/**
 * 加密
 * @param pstr 待加密的文本
 * @param pkey 密钥
 */
void CodeUtils::encode(char *pstr, int *pkey) {
    int len = strlen(pstr);//获取长度
    for (int i = 0; i < len; i++)
        *(pstr + i) = MakecodeChar(*(pstr + i), pkey[i % 5]);
}

/**
 * 解密
 * @param pstr 待解密的文本
 * @param pkey 密钥
 */
void CodeUtils::decode(char *pstr, int *pkey) {
    int len = strlen(pstr);
    for (int i = 0; i < len; i++)
        *(pstr + i) = CutcodeChar(*(pstr + i), pkey[i % 5]);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_imorning_senseinfohelper_utils_JniUtils_getApiId(JNIEnv *env, jclass thiz, jstring nouse_key) {
    std::string encryptApiId = "72:376";
    int key[] = {1, 2, 3, 4, 5};
    CodeUtils::decode(encryptApiId.data(), key);
    return env->NewStringUTF(encryptApiId.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_imorning_senseinfohelper_utils_JniUtils_getKey(JNIEnv *env, jclass thiz, jstring api_id) {
    std::string encryptKey = "7db1<35g3`e`72g1c0677`ab63f11065";
    int key[] = {1, 2, 3, 4, 5};
    CodeUtils::decode(encryptKey.data(), key);
    return env->NewStringUTF(encryptKey.c_str());
}