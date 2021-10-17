package cn.eeo.debug.lib;

public class Test {

  void test(){
    long pre = System.currentTimeMillis();


    DebugLogger.debug("className", "methodName", (System.currentTimeMillis() - pre));

  }
}
