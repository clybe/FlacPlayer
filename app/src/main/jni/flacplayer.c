#include <jni.h>
#include <FLAC/stream_decoder.h>


#include "flacplayer.h"


JNIEXPORT jstring JNICALL
Java_com_clybe_flacplayer_jni_LibFlac_stringFromJNI(JNIEnv *env, jobject instance) {
    return (*env)->NewStringUTF(env, "hello flac");
}


static FLAC__StreamDecoderWriteStatus
write_callback(const FLAC__StreamDecoder *decoder, const FLAC__Frame *frame,
               const FLAC__int32 *const buffer[], void *client_data);

static void
metadata_callback(const FLAC__StreamDecoder *decoder, const FLAC__StreamMetadata *metadata,
                  void *client_data);

static void
error_callback(const FLAC__StreamDecoder *decoder, FLAC__StreamDecoderErrorStatus status,
               void *client_data);

static FLAC__uint64 total_samples = 0;
static unsigned sample_rate = 0;
static unsigned channels = 0;
static unsigned bps = 0;

JNIEXPORT jstring JNICALL
Java_com_clybe_flacplayer_jni_LibFlac_decode(JNIEnv *env, jobject instance) {
    FLAC__bool ok = true;
    FLAC__StreamDecoder *decoder = 0;
    FLAC__StreamDecoderInitStatus init_status;

    FILE *fout;

    if ((decoder = FLAC__stream_decoder_new()) == NULL) {
        fprintf(stderr, "ERROR: allocating decoder\n");
        LOGE("allocating decoder error");
    } else {
        LOGE("allocating decoder success");
    }


    if ((fout = fopen("sdcard/test.flac", "wb")) == NULL) {
        LOGE("ERROR: opening %s for output\n", "sdcard/test.fla");
        return 1;
    }


    init_status = FLAC__stream_decoder_init_file(decoder, "sdcard/test.flac", write_callback,
                                                 metadata_callback, error_callback, /*client_data=*/
                                                 fout);
    if (init_status != FLAC__STREAM_DECODER_INIT_STATUS_OK) {
        fprintf(stderr, "ERROR: initializing decoder: %s\n",
                FLAC__StreamDecoderInitStatusString[init_status]);
        ok = false;
    }

    if (ok) {
        ok = FLAC__stream_decoder_process_until_end_of_stream(decoder);
        fprintf(stderr, "decoding: %s\n", ok ? "succeeded" : "FAILED");
        fprintf(stderr, "   state: %s\n",
                FLAC__StreamDecoderStateString[FLAC__stream_decoder_get_state(decoder)]);
    }

    FLAC__stream_decoder_delete(decoder);
    fclose(fout);


    return (*env)->NewStringUTF(env, "hello flac");
}


FLAC__StreamDecoderWriteStatus
write_callback(const FLAC__StreamDecoder *decoder, const FLAC__Frame *frame,
               const FLAC__int32 *const buffer[], void *client_data) {
    return FLAC__STREAM_DECODER_WRITE_STATUS_CONTINUE;
}

void metadata_callback(const FLAC__StreamDecoder *decoder, const FLAC__StreamMetadata *metadata,
                       void *client_data) {
    (void) decoder, (void) client_data;

    /* print some stats */
    if (metadata->type == FLAC__METADATA_TYPE_STREAMINFO) {
        /* save for later */
        total_samples = metadata->data.stream_info.total_samples;
        sample_rate = metadata->data.stream_info.sample_rate;
        channels = metadata->data.stream_info.channels;
        bps = metadata->data.stream_info.bits_per_sample;

        fprintf(stderr, "sample rate    : %u Hz\n", sample_rate);
        fprintf(stderr, "channels       : %u\n", channels);
        fprintf(stderr, "bits per sample: %u\n", bps);
#ifdef _MSC_VER
        fprintf(stderr, "total samples  : %I64u\n", total_samples);
#else
        fprintf(stderr, "total samples  : %llu\n", total_samples);
#endif
    }
}

void error_callback(const FLAC__StreamDecoder *decoder, FLAC__StreamDecoderErrorStatus status,
                    void *client_data) {
    (void) decoder, (void) client_data;

    fprintf(stderr, "Got error callback: %s\n", FLAC__StreamDecoderErrorStatusString[status]);
}