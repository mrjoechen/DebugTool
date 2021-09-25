package cn.eeo.debugtool.plugin;

import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.gradle.internal.pipeline.TransformManager;

import org.gradle.api.Project;
import org.gradle.api.logging.Logger;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by chenqiao on 2021/9/24.
 * e-mail : mrjctech@gmail.com
 */
public class BaseTransform extends Transform {
  private static final String TAG = "BaseTransform";
  private static final Set<QualifiedContent.Scope> SCOPES = new HashSet<>();

  static {
    SCOPES.add(QualifiedContent.Scope.PROJECT);
    SCOPES.add(QualifiedContent.Scope.SUB_PROJECTS);
    SCOPES.add(QualifiedContent.Scope.EXTERNAL_LIBRARIES);
  }

  private final Logger logger;
  private final Project project;
  private boolean weaveTrigger = false;

  public BaseTransform(Project project) {
    this.logger = project.getLogger();
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

  @Override
  public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
    super.transform(transformInvocation);
    BuildType buildType = getBuildType();
    String variantName = transformInvocation.getContext().getVariantName();
    logger.info(TAG, "variantName: " + variantName);
    if ("debug".equals(variantName)){
      weaveTrigger = buildType == BuildType.DEBUG || buildType == BuildType.ALWAYS;
    }

    if ("release".equals(variantName)){
      weaveTrigger = buildType == BuildType.RELEASE || buildType == BuildType.ALWAYS;
    }

    if (buildType == BuildType.NEVER){
      weaveTrigger = false;
    }

    long startTime = System.currentTimeMillis();

    boolean incremental = transformInvocation.isIncremental();

    if (!incremental){
      transformInvocation.getOutputProvider().deleteAll();
    }


    long cost = System.currentTimeMillis() - startTime;
    logger.info(getClass().getSimpleName(), "transform cost: " + cost +" ms");


  }

  protected BuildType getBuildType(){
    return BuildType.NEVER;
  }

}
