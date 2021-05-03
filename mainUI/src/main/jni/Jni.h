//
// Created by iMorning on 2021/5/3.
//

#ifndef _Included_com_imorning_tns_utils_JniUtils

#define _Included_com_imorning_tns_utils_JniUtils

/**
 * 获取App签名的sha1
 */
extern "C"
JNIEXPORT jstring JNICALL
Java_com_imorning_tns_utils_JniUtils_getAppSHA1(JNIEnv *env, jobject thiz, jobject context_obj);


#endif //_Included_com_imorning_tns_utils_JniUtils
