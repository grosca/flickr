LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := FlickrGallery
LOCAL_SRC_FILES := FlickrGallery.cpp

include $(BUILD_SHARED_LIBRARY)
