package cn.eeo.debugtool.plugin;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Arrays;

public class DebugToolClassVisitor extends ClassVisitor implements Opcodes {

    private String mClassName;
    private boolean injectAllMethods;

    public DebugToolClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM7, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
//        System.out.println("DebugToolClassVisitor : visit -----> start ：" + name);
        this.mClassName = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        AnnotationVisitor annotationVisitor = super.visitAnnotation(descriptor, visible);

        if ("Lcn/eeo/debug/lib/DebugProbe;".equals(descriptor)){
            injectAllMethods = true;
            System.out.println("DebugToolClassVisitor : inject class -----> ：" + mClassName);

        }
        return annotationVisitor;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
//        if ("cn/eeo/debugtool/MainActivity".equals(mClassName)) {
//            System.out.println("DebugToolClassVisitor : visitMethod :name " + name + " desc:" + desc);

            return new DebugToolMethodVisitor(mClassName, name, mv, access, desc, injectAllMethods);
//        }
//        return mv;
    }

    @Override
    public void visitEnd() {
//        System.out.println("DebugToolClassVisitor : visit ----->" + mClassName + " end");
        super.visitEnd();
    }

}
