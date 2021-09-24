package cn.eeo.debugtool.plugin;


import org.gradle.api.Project;

/**
 * Created by chenqiao on 2021/9/23.
 * e-mail : mrjctech@gmail.com
 */
class DebugToolExtension {

  /**
   * 是否注入
   */
  boolean enable;

  static DebugToolExtension getConfig(Project project) {
    DebugToolExtension extension = project.getExtensions().findByType(DebugToolExtension.class);
    if (extension == null) {
      extension = new DebugToolExtension();
    }
    return extension;
  }

}
