package ru.timjosten.hidesecondarysim;

import de.robv.android.xposed.*;
import de.robv.android.xposed.callbacks.*;

public class HideSecondarySIM implements IXposedHookLoadPackage
{
  private static final String TAG = HideSecondarySIM.class.getSimpleName() + ": ";

  public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam)
  throws Throwable
  {
    if(!lpparam.packageName.equals("com.android.systemui"))
      return;

    try
    {
      XposedHelpers.findAndHookMethod("com.android.systemui.statusbar.SignalClusterView.PhoneState", lpparam.classLoader, "apply", boolean.class,
      new XC_MethodHook()
      {
        @Override
        protected void beforeHookedMethod(MethodHookParam param)
        throws Throwable
        {
          try
          {
            if((boolean)param.args[0] == true)
              XposedHelpers.setBooleanField(param.thisObject, "mMobileVisible", false);
          }
          catch(Throwable t)
          {
            XposedBridge.log(TAG + t);
          }
        }
      });
      XposedHelpers.findAndHookMethod("com.android.keyguard.CarrierText", lpparam.classLoader, "concatenate", CharSequence.class, CharSequence.class,
      new XC_MethodHook()
      {
        @Override
        protected void beforeHookedMethod(MethodHookParam param)
        throws Throwable
        {
          try
          {
            if(param.args[0] != null
            && param.args[1] != null)
              param.args[1] = null;
          }
          catch(Throwable t)
          {
            XposedBridge.log(TAG + t);
          }
        }
      });
    }
    catch(Throwable t)
    {
      XposedBridge.log(TAG + t);
    }
  }
}
