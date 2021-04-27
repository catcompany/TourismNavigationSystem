#include <jni.h>
/* Header for class com_imorning_senseinfohelper_utils_JniUtils */

#ifndef _Included_com_imorning_senseinfohelper_utils_JniUtils
#define _Included_com_imorning_senseinfohelper_utils_JniUtils
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_imorning_senseinfohelper_utils_JniUtils
 * Method:    getApiId
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_imorning_senseinfohelper_utils_JniUtils_getApiId
  (JNIEnv *, jclass clazz, jstring);

/*
 * Class:     com_imorning_senseinfohelper_utils_JniUtils
 * Method:    getKey
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_imorning_senseinfohelper_utils_JniUtils_getKey
  (JNIEnv *, jclass clazz, jstring);

#ifdef __cplusplus
}
#endif
#endif