package cn.eeo.debugtool.plugin;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chenqiao on 2021/10/17.
 * e-mail : mrjctech@gmail.com
 */
class DebugInjection extends BaseInjection{

  @Override
  public boolean checkClass(String className) {
    return super.checkClass(className);
  }

//  @NotNull
//  @Override
//  public byte[] transformClassFile(@@NotNull String fileName, @@NotNull InputStream inputStream) {
//    try {
//      ClassReader classReader = new ClassReader(inputStream);
//      ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
//      DebugToolCollectAnnoClassVisitor debugToolCollectAnnoClassVisitor = new DebugToolCollectAnnoClassVisitor(classWriter);
//      classReader.accept(debugToolCollectAnnoClassVisitor, ClassReader.EXPAND_FRAMES);
//
//      if (debugToolCollectAnnoClassVisitor.haveProbe()){
//        return super.transformClassFile(fileName, inputStream);
//      }else {
//        return classWriter.toByteArray();
//      }
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//    return null;
//
//  }

  @NotNull
  @Override
  public ClassVisitor getClassVisitor(@NotNull ClassWriter classWriter) {
    return new DebugToolClassVisitor(classWriter);
  }
}
