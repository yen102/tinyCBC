LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := tinyCBC
LOCAL_SRC_FILES := aes.cpp
LOCAL_CPP_EXTENSION := .cpp
include $(BUILD_SHARED_LIBRARY)