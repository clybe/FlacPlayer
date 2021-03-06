//
// Created by CaiYu on 2017/6/24.
//

#ifndef FLACPLAYER_FLACPLAYER_H
#define FLACPLAYER_FLACPLAYER_H

#include <android/log.h>

#define LOG_TAG    "FlacPalyer_JNI"
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__) // 定义LOGD类型
#define LOGW(...)  __android_log_print(ANDROID_LOG_WARN,LOG_TAG,__VA_ARGS__) // 定义LOGW类型
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__) // 定义LOGE类型
#define LOGF(...)  __android_log_print(ANDROID_LOG_FATAL,LOG_TAG,__VA_ARGS__) // 定义LOGF类型

#endif //FLACPLAYER_FLACPLAYER_H
