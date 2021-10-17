package cn.eeo.debug.lib;

import android.util.Log;

/**
 * Created by chenqiao on 2021/10/17.
 * e-mail : mrjctech@gmail.com
 */
public class DebugLogger {


  public static void main(String[] args) {
    enterMethod("className1", "method1");
    enterMethod("className2", "method2");
    enterMethod("className3", "method3");

    debug("className1", "method1", 100);
    debug("className2", "method2", 100);
    debug("className3", "method3", 100);


    exitMethod("className1", "method1");
    exitMethod("className2", "method2");
    exitMethod("className3", "method3");


  }

  public static void debug(String tag, String msg){
//    Log.d(tag, msg);
    System.out.println(tag + ":" + msg);
  }

  public static void debug(String className, String methodName, long cost){
    debug("DebugTool: "+className.replace("/", "."), "--- [" + methodName + "] cost " + cost + " ms");
  }

  public static void enterMethod(String className, String methodName){
    debug("DebugTool: "+className.replace("/", "."), "--> [" + methodName + "] ");
  }

  public static void exitMethod(String className, String methodName){
    debug("DebugTool: "+className.replace("/", "."), "<-- [" + methodName + "] ");
  }

}
