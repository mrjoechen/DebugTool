package cn.eeo.debugtool.plugin;
import com.android.build.gradle.BaseExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class DebugToolPlugin implements Plugin<Project> {

  public static final String DEBUG_TOOL_EXT_NAME = "debugToolExt";

  @Override
  public void apply(Project project) {
    System.out.println("DebugTool Plugin apply ...");
    project.getExtensions().create(DEBUG_TOOL_EXT_NAME, DebugToolExtension.class);
    BaseExtension extension = project.getExtensions().getByType(BaseExtension.class);
    extension.registerTransform(new DebugToolTransform(project));
  }
}
