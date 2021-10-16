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

  private static final String DEBUG_TOOL_EXT_NAME = "debugToolExt";

  private Project project;
  private DebugToolExtension debugToolExtension;

  public DebugToolTransform(Project project) {
    super(project);
    this.project = project;
    project.getExtensions().create(DEBUG_TOOL_EXT_NAME, DebugToolExtension.class);
  }


  @Override
  public void transform(TransformInvocation transformInvocation) {
    debugToolExtension = (DebugToolExtension) project.getExtensions().getByName(DEBUG_TOOL_EXT_NAME);

//    super.transform(transformInvocation);
    transformInvocation.getInputs().forEach(transformInput -> {
      transformInput.getDirectoryInputs().forEach(directoryInput ->
          TransformHelper.INSTANCE.transformDirectoryFiles(directoryInput, transformInvocation.getOutputProvider())
      );
      transformInput.getJarInputs().forEach(jarInput ->
          TransformHelper.INSTANCE.transformJarFiles(jarInput, transformInvocation.getOutputProvider())
      );
    });
  }

  @Override
  protected BuildType getBuildType() {
    return debugToolExtension.buildType;
  }

  public boolean isTimingEnable() {
    return debugToolExtension.timingEnable;
  }

  public boolean isParameterEnableEnable() {
    return debugToolExtension.parameterEnable;
  }

}
