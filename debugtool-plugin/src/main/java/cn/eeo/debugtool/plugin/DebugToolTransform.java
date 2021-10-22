package cn.eeo.debugtool.plugin;

import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInvocation;

import org.gradle.api.Project;

import java.io.IOException;

/**
 * Created by chenqiao on 2021/9/24.
 * e-mail : mrjctech@gmail.com
 */
public class DebugToolTransform extends BaseTransform {


  private Project project;
  private DebugToolExtension debugToolExtension;

  public DebugToolTransform(Project project) {
    super(project);
    this.project = project;
  }


  @Override
  public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
    debugToolExtension = DebugToolExtension.getConfig(project);
    super.transform(transformInvocation);

  }

  @Override
  protected BuildType getBuildType() {
    return debugToolExtension.buildType;
  }

  @Override
  BaseInjection getBaseInjection() {
    return new DebugInjection();
  }

  public boolean isTimingEnable() {
    return debugToolExtension.timingEnable;
  }

  public boolean isParameterEnableEnable() {
    return debugToolExtension.parameterEnable;
  }

}
