package cn.eeo.debugtool.plugin;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class DebugToolCollectAnnoMethodVisitor extends MethodVisitor implements Opcodes {


    public DebugToolCollectAnnoMethodVisitor(MethodVisitor methodVisitor) {
        super(Opcodes.ASM7, methodVisitor);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        AnnotationVisitor annotationVisitor = super.visitAnnotation(descriptor, visible);

        if ("".equals(descriptor)){

        }
        return annotationVisitor;
    }
}
