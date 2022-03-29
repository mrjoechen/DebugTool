package cn.eeo.debugtool.plugin;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.LocalVariablesSorter;

public class DebugToolMethodVisitor extends LocalVariablesSorter implements Opcodes {

  private boolean debugMethod;
  private String methodName;
  private String className;
  private boolean haveClassAnno;
  private boolean skipMethod;

  public DebugToolMethodVisitor(String className, String methodName, MethodVisitor methodVisitor, int access, String desc, boolean haveClassAnno) {
    super(Opcodes.ASM7, access, desc, methodVisitor);
    this.className = className;
    this.methodName = methodName;
    this.haveClassAnno = haveClassAnno;
    if (haveClassAnno) debugMethod = true;
  }


  @Override
  public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
    AnnotationVisitor defaultAv = super.visitAnnotation(desc, visible);
//    System.out.println("DebugToolMethodVisitor : visitAnnotation : " + desc);
    if ("Lcn/eeo/debug/lib/DebugProbe;".equals(desc)) {
      debugMethod = true;
      System.out.println("DebugToolMethodVisitor : inject : " + methodName);
    }

    if ("Lcn/eeo/debug/lib/DebugSkip;".equals(desc)) {
      skipMethod = true;
      System.out.println("DebugToolMethodVisitor : skip : " + methodName);
    }


    if (haveClassAnno){
      if (skipMethod) {
        debugMethod = false;
      }else {
        debugMethod = true;
      }
    }


    return defaultAv;
  }


  private int startTimeId;


  @Override
  public void visitCode() {
    super.visitCode();

    if (!debugMethod) return;
    System.out.println("DebugToolMethodVisitor : visitCode : " + methodName);

    startTimeId = newLocal(Type.LONG_TYPE);
    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
    mv.visitVarInsn(Opcodes.LSTORE, startTimeId);

  }


  @Override
  public void visitInsn(int opcode) {

    if (debugMethod && ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW)) {

      int durationId = newLocal(Type.LONG_TYPE);
      mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
      mv.visitVarInsn(LLOAD, startTimeId);
      mv.visitInsn(LSUB);
      mv.visitVarInsn(LSTORE, durationId);

      mv.visitTypeInsn(NEW, "java/lang/String");
      mv.visitInsn(DUP);
      mv.visitLdcInsn("Hello");
      mv.visitMethodInsn(INVOKESPECIAL, "java/lang/String", "<init>", "(Ljava/lang/String;)V", false);
      mv.visitVarInsn(ASTORE, 0);

      Type threadType = Type.getObjectType("java/lang/String");
      int threadVarId = newLocal(threadType);
      mv.visitMethodInsn(INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false);
      mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Thread", "getName", "()Ljava/lang/String;", false);
      mv.visitVarInsn(ASTORE, threadVarId);
      mv.visitVarInsn(ALOAD, threadVarId);

      mv.visitLdcInsn(className);
      mv.visitLdcInsn(methodName);
      mv.visitVarInsn(LLOAD, durationId);
      mv.visitMethodInsn(INVOKESTATIC, "cn/eeo/debug/lib/DebugLogger", "debugWithThread", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;J)V", false);

    }

    super.visitInsn(opcode);

  }
}
