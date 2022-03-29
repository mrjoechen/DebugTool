# DebugTool

> DebugTool is a gradle plugin based on ASM, you can collect method time-consuming with annotation just like this.


```
class MainActivity : AppCompatActivity(){

    @DebugProbe
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
```

then the log will print:

```
cn.eeo.debugtool D/DebugTool: cn.eeo.debugtool.MainActivity:  [onCreate] cost 98 ms
```



## How to use

1. add classpath to root project level build.gradle

```
    classpath 'cn.eeo.android.business:debugtool-plugin:0.1.1'
```

2. add dependencies to project module build.gradle

```
    implementation 'cn.eeo.android.business:debugtool-lib:0.1.1'
```

3. apply plugin

```
    apply plugin: 'cn.eeo.debugtool'
    debugToolExt{
        buildType = 'DEBUG' // RELEASE, ALWAYS, NEVER
    }
```


## For example

Add `@DebugProbe` to your method will print costed time.

```
class MainActivity : AppCompatActivity(){

    @DebugProbe
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
```

You can also add `@DebugProbe` to Class decleration, and debugTool will collect time-consuming of all methods in this Class.

```
@DebugProbe
class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.tv).setOnClickListener(this)

    }
    
    override fun onClick(p0: View?) {
        
    }
    
}
```

you will see

```
cn.eeo.debugtool D/DebugTool: cn.eeo.debugtool.MainActivity:  [onCreate] cost 98 ms
cn.eeo.debugtool D/DebugTool: cn.eeo.debugtool.MainActivity:  [onClick] cost 300 ms
```

Meanwhile, if you don't want to see the time-consuming of some methods，use `@DebugSkip` to skip the method.

```
@DebugSkip
override fun sleep() {
    Thread.sleep(300);
}
```


TODO

- Print method parameter, and return value
- replace custom print logger
- Support for incremental compilation
...

updateLog
- add Thread Name to log 22.03.29 