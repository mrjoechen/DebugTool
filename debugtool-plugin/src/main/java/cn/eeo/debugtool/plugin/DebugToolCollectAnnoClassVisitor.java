package cn.eeo.debugtool.plugin;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DebugToolCollectAnnoClassVisitor extends ClassVisitor implements Opcodes {

    private String mClassName;
    private boolean haveProbe;
    private boolean injectAllMethods;

    private Map<String, List<String>> methodParametersMap = new HashMap<>();

    private List<String> injectMethods = new ArrayList<>();

    public DebugToolCollectAnnoClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM7, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        System.out.println("DebugToolCollectAnnoClassVisitor : visit -----> started ：" + name);
        this.mClassName = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        AnnotationVisitor annotationVisitor = super.visitAnnotation(descriptor, visible);

        if ("Lcn/eeo/debug/lib/DebugProbe;".equals(descriptor)){
            injectAllMethods = true;
        }
        return annotationVisitor;
    }


    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        System.out.println("DebugToolCollectAnnoClassVisitor : visitMethod : " + name);
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        return mv;
    }

    public boolean haveProbe() {
        return haveProbe;
    }
}
