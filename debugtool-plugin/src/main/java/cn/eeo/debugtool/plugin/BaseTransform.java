package cn.eeo.debugtool.plugin;

import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.gradle.internal.pipeline.TransformManager;

import org.gradle.api.Project;
import org.gradle.api.logging.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by chenqiao on 2021/9/24.
 * e-mail : mrjctech@gmail.com
 */
public class BaseTransform extends Transform {



  private static final Set<QualifiedContent.Scope> SCOPES = new HashSet<>();

  static {
    SCOPES.add(QualifiedContent.Scope.PROJECT);
    SCOPES.add(QualifiedContent.Scope.SUB_PROJECTS);
    SCOPES.add(QualifiedContent.Scope.EXTERNAL_LIBRARIES);
  }

  private final Logger logger;
  private final Project project;
  private boolean emptyRun = false;

  public BaseTransform(Logger logger, Project project) {
    this.logger = logger;
    this.project = project;
  }

  @Override
  public String getName() {
    return this.getClass().getSimpleName();
  }

  @Override
  public Set<QualifiedContent.ContentType> getInputTypes() {
    return TransformManager.CONTENT_CLASS;
  }

  @Override
  public Set<QualifiedContent.Scope> getScopes() {
    return SCOPES;
  }

  @Override
  public boolean isIncremental() {
    return true;
  }

}
