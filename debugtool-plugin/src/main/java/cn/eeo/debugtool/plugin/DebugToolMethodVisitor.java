package cn.eeo.debugtool.plugin;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class DebugToolMethodVisitor extends MethodVisitor implements Opcodes {
    public DebugToolMethodVisitor(MethodVisitor methodVisitor) {
        super(Opcodes.ASM7, methodVisitor);
    }
}
