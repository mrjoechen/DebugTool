package cn.eeo.debugtool.plugin;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class DebugToolCollectAnnoClassVisitor extends ClassVisitor implements Opcodes {

    private String mClassName;

    public DebugToolCollectAnnoClassVisitor(int api, ClassVisitor classVisitor) {
        super(Opcodes.ASM7, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        System.out.println("DebugToolCollectAnnoClassVisitor : visit -----> started ：" + name);
        this.mClassName = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        System.out.println("DebugToolCollectAnnoClassVisitor : visitMethod : " + name);
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if ("".equals(this.mClassName)) {
            if ("".equals(name) ) {
                System.out.println("DebugToolCollectAnnoClassVisitor : change method ----> " + name);
                return new DebugToolMethodVisitor(mv);
            }
        }
        return mv;
    }

    @Override
    public void visitEnd() {
        System.out.println("DebugToolCollectAnnoClassVisitor : visit -----> end");
        super.visitEnd();
    }
}
