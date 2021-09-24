package cn.eeo.debugtool.plugin;

import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;

import java.util.Set;

/**
 * Created by chenqiao on 2021/9/24.
 * e-mail : mrjctech@gmail.com
 */
public class DebugToolTransform extends Transform {


  @Override
  public String getName() {
    return null;
  }

  @Override
  public Set<QualifiedContent.ContentType> getInputTypes() {
    return null;
  }

  @Override
  public Set<? super QualifiedContent.Scope> getScopes() {
    return null;
  }

  @Override
  public boolean isIncremental() {
    return false;
  }
}
