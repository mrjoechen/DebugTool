package cn.eeo.debug.lib;

public class Test {

  void test(){
    long pre = System.currentTimeMillis();
    long duration = System.currentTimeMillis() - pre;
    String threadName = Thread.currentThread().getName();
    DebugLogger.debugWithThread(threadName, "className", "methodName", duration);

  }
}
