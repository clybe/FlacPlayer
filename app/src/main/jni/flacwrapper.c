#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_clybe_flacplayer_MainActivity_stringFromJNI(JNIEnv *env, jobject instance) {
    return (*env)->NewStringUTF(env, "hello flac");
}
