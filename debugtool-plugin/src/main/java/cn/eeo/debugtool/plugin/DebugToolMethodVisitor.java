package cn.eeo.debugtool.plugin;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.LocalVariablesSorter;

public class DebugToolMethodVisitor extends LocalVariablesSorter implements Opcodes {

    private boolean debugMethod = true;
    private String methodName;
    private String className;

    public DebugToolMethodVisitor(String className, String methodName, MethodVisitor methodVisitor, int access, String desc) {
        super(Opcodes.ASM7, access, desc, methodVisitor);
        this.className = className;
        this.methodName = methodName;
    }


    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        AnnotationVisitor defaultAv = super.visitAnnotation(desc, visible);
        System.out.println("DebugToolMethodVisitor : visitAnnotation : " + desc);
        if ("Lcn/eeo/debug/lib/DebugProbe;".equals(desc)) {
            debugMethod = true;
        }
        return defaultAv;
    }


    private int startTimeId;


    @Override
    public void visitCode() {
        super.visitCode();
        System.out.println("DebugToolMethodVisitor : visitCode : " + methodName);

        if(!debugMethod) return;

        startTimeId = newLocal(Type.LONG_TYPE);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
        mv.visitVarInsn(Opcodes.LSTORE, startTimeId);

    }


    @Override
    public void visitInsn(int opcode) {
        System.out.println("DebugToolMethodVisitor : visitInsn : " + methodName);

        if (debugMethod && ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW)){
            int durationId = newLocal(Type.LONG_TYPE);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            mv.visitVarInsn(LLOAD, startTimeId);
            mv.visitInsn(LSUB);
            mv.visitVarInsn(LSTORE, durationId);
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
            mv.visitLdcInsn("The cost time of " + className + "->" + methodName + " is ");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitVarInsn(LLOAD, durationId);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
            mv.visitLdcInsn(" ms");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }

        super.visitInsn(opcode);

    }
}
