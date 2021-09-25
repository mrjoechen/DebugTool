package cn.eeo.debugtool.plugin;


import org.gradle.api.Project;

import java.util.List;

/**
 * Created by chenqiao on 2021/9/23.
 * e-mail : mrjctech@gmail.com
 */
class DebugToolExtension {

  boolean timingEnable = true;

  boolean parameterEnable = true;

  public BuildType buildType = BuildType.NEVER;

  List<String> excludeFiles;

  static DebugToolExtension getConfig(Project project) {
    DebugToolExtension extension = project.getExtensions().findByType(DebugToolExtension.class);
    if (extension == null) {
      extension = new DebugToolExtension();
    }
    return extension;
  }

}
