package cn.eeo.debugtool.plugin;

import com.android.build.gradle.AppExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class DebugToolPlugin implements Plugin<Project> {
  @Override
  public void apply(Project project) {
    System.out.println("DebugTool Plugin apply ...");
    AppExtension appExtension = (AppExtension)project.getProperties().get("android");
    appExtension.registerTransform(new DebugToolTransform(project));
  }
}
