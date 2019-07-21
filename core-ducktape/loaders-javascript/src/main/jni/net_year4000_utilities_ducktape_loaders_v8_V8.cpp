/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
#include <iostream>
#include <jni.h>
#include "net_year4000_utilities_ducktape_loaders_v8_V8.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <libplatform/libplatform.h>
#include <v8.h>

JNIEXPORT void JNICALL Java_net_year4000_utilities_ducktape_loaders_v8_V8_test(JNIEnv *, jobject) {
    std::cout << "hello world, i'm running from c++" << std::endl;
}
