package com.cap.testingplugins.StorageVolumes;

import android.os.Environment;
import java.io.Serializable;
public class StorageVolumeInfo implements Serializable {

  public String mPath;

  public String mState;

  public boolean mRemovable;

  public String mName;

  public Number mAvaliableSpace;
  public Number mTotalBytes;
  public Number mFreeBytes;
  public Number mUsedBytes;

  public String mType;

  public StorageVolumeInfo(String path) {
    mPath = path;
  }

  public boolean isMounted() {
    return Environment.MEDIA_MOUNTED.equals(mState);
  }

  @Override
  public String toString() {
    return "StorageVolumeInfo{" +
      "path='" + mPath + '\'' +
      ", state='" + mState + '\'' +
      ", removable=" + mRemovable +
      '}';
  }
}
