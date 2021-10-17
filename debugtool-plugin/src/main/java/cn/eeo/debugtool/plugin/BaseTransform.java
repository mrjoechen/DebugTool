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
abstract class BaseTransform extends Transform {
  private static final String TAG = "BaseTransform";
  private static final Set<QualifiedContent.Scope> SCOPES = new HashSet<>();

  static {
    SCOPES.add(QualifiedContent.Scope.PROJECT);
    SCOPES.add(QualifiedContent.Scope.SUB_PROJECTS);
    SCOPES.add(QualifiedContent.Scope.EXTERNAL_LIBRARIES);
  }

  private final Logger logger;
  private final Project project;
  private boolean injectTrigger = false;



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

    long startTime = System.currentTimeMillis();
    System.out.println(getClass().getSimpleName() + "---> start transform: ");

    BuildType buildType = getBuildType();
    String variantName = transformInvocation.getContext().getVariantName();
    logger.info(TAG, "variantName: " + variantName);
    if (variantName.toLowerCase().contains("debug")){
      injectTrigger = buildType == BuildType.DEBUG || buildType == BuildType.ALWAYS;
    }

    if (variantName.toLowerCase().contains("release")){
      injectTrigger = buildType == BuildType.RELEASE || buildType == BuildType.ALWAYS;
    }

    if (buildType == BuildType.NEVER){
      injectTrigger = false;
    }


    boolean incremental = transformInvocation.isIncremental();

    if (!incremental){
      transformInvocation.getOutputProvider().deleteAll();
    }

    if (injectTrigger){
      transformInvocation.getInputs().forEach(transformInput -> {
        transformInput.getDirectoryInputs().forEach(directoryInput ->
            getBaseInjection().transformDirectoryFiles(directoryInput, transformInvocation.getOutputProvider())
        );
        transformInput.getJarInputs().forEach(jarInput ->
            getBaseInjection().transformJarFiles(jarInput, transformInvocation.getOutputProvider())
        );
      });
    }

    long cost = System.currentTimeMillis() - startTime;
    logger.warn(getClass().getSimpleName(), "debug tool transform cost: " + cost +" ms");

    System.out.println(getClass().getSimpleName() + "---> transform cost: " + cost +" ms");
  }

  protected BuildType getBuildType(){
    return BuildType.NEVER;
  }

  abstract BaseInjection getBaseInjection();

}
