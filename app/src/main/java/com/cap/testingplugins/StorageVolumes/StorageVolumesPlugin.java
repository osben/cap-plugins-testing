package com.cap.testingplugins.StorageVolumes;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

@CapacitorPlugin(
  name = "StorageVolumes",
  permissions = {
    @Permission(
      strings = {
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
      },
      alias = StorageVolumesPlugin.PERMISSION_ALIAS_EXTERNAL_STORAGE
    )
  }
)
public class StorageVolumesPlugin extends Plugin {
  // Permission alias constants
  static final String PERMISSION_ALIAS_EXTERNAL_STORAGE = "EXTERNAL_STORAGE";
  static final String PERMISSION_ALIAS_MEDIA_IMAGES = "MEDIA_IMAGES";
  private static final String METHOD_NAME_GET_PATH = "getPath";
  Context context;

  @Override
  public void load() {
    super.load();
    context = getContext();
    StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);

  }

  @PluginMethod
  public void getVolumes(PluginCall call) {

    JSArray DirectoriesArray = new JSArray();

    List<StorageVolumeInfo> StorageVolumes = getStorageVolumes(context);

    for (StorageVolumeInfo storageVolumeInfo : StorageVolumes) {
      JSObject r = new JSObject();
      r.put("path", storageVolumeInfo.mPath);
      r.put("state", storageVolumeInfo.mState);
      r.put("removable", storageVolumeInfo.mRemovable);
      r.put("name", storageVolumeInfo.mName);
      r.put("avaliableSpace", storageVolumeInfo.mAvaliableSpace);
      r.put("freeBytes", storageVolumeInfo.mFreeBytes);
      r.put("totalBytes", storageVolumeInfo.mTotalBytes);
      r.put("usedBytes", storageVolumeInfo.mUsedBytes);
      r.put("type", storageVolumeInfo.mType);


      DirectoriesArray.put(r);
    }
    JSObject ret = new JSObject();
    ret.put("volumes", DirectoriesArray);
    call.resolve(ret);
  }


  public static List<StorageVolumeInfo> getStorageVolumes(Context context) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      return getAvailableStorageN(context.getApplicationContext());
    } else {
      return getAvailableStoragePreN(context.getApplicationContext());
    }
  }


  @RequiresApi(api = Build.VERSION_CODES.N)
  private static List<StorageVolumeInfo> getAvailableStorageN(Context context) {

    Method getPathMethod = null;
    try {
      getPathMethod = StorageVolume.class.getMethod(METHOD_NAME_GET_PATH);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }

    if (getPathMethod == null) {
      return null;
    }

    ArrayList<StorageVolumeInfo> storageInfoList = new ArrayList<StorageVolumeInfo>();

    StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
    List<StorageVolume> storageVolumes = storageManager.getStorageVolumes();


    if (storageVolumes != null) {
      for (StorageVolume volume : storageVolumes) {
        String path = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
          File f = volume.getDirectory();
          if (f != null)
            path = f.getPath();
        } else {
          try {
            path = (String) getPathMethod.invoke(volume);
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          } catch (InvocationTargetException e) {
            e.printStackTrace();
          }
        }


        if (path != null) {
          StorageVolumeInfo info = new StorageVolumeInfo(path);
          info.mState = volume.getState();
          info.mRemovable = volume.isRemovable();
          info.mName = volume.getDescription(context);

          try {
            StatFs stat = new StatFs(path);

            info.mAvaliableSpace = stat.getAvailableBytes();
            info.mTotalBytes = stat.getTotalBytes();
            info.mFreeBytes = stat.getFreeBytes();
            info.mType = volume.getUuid();
          } catch (Exception e) {
            e.printStackTrace();
          }

          storageInfoList.add(info);
        }
      }
    }

    storageInfoList.trimToSize();

    return storageInfoList;
  }

  private static List<StorageVolumeInfo> getAvailableStoragePreN(Context context) {
    ArrayList<StorageVolumeInfo> storaggeInfoList = new ArrayList<>();
    StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
    try {
      Class<?>[] paramClasses = {};
      Method getVolumeList = StorageManager.class.getMethod("getVolumeList", paramClasses);
      getVolumeList.setAccessible(true);
      Object[] params = {};
      Object[] invokes = (Object[]) getVolumeList.invoke(storageManager, params);
      if (invokes != null) {
        StorageVolumeInfo info = null;
        for (int i = 0; i < invokes.length; i++) {
          Object obj = invokes[i];
          Method getPath = obj.getClass().getMethod(METHOD_NAME_GET_PATH);
          String path = (String) getPath.invoke(obj);
          info = new StorageVolumeInfo(path);
          File file = new File(info.mPath);
          if ((file.exists()) && (file.isDirectory()) && (file.canWrite())) {
            Method isRemovable = obj.getClass().getMethod("isRemovable");
            String state = null;
            try {
              Method getVolumeState = StorageManager.class.getMethod("getVolumeState", String.class);
              state = (String) getVolumeState.invoke(storageManager, info.mPath);
              info.mState = state;
            } catch (Exception e) {
              e.printStackTrace();
            }

            if (info.isMounted()) {
              info.mRemovable = ((Boolean) isRemovable.invoke(obj)).booleanValue();
              storaggeInfoList.add(info);
            }
          }
        }
      }
    } catch (NoSuchMethodException e1) {
      e1.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    storaggeInfoList.trimToSize();

    return storaggeInfoList;
  }

}
