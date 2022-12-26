package cn.zbx1425.sowcer.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.ShaderInstance;
import org.lwjgl.opengl.GL33;

public class GlStateTracker {

    private static int vertArrayBinding;
    private static int arrayBufBinding;
    private static int elementBufBinding;

    private static ShaderInstance currentShaderInstance;
    private static int currentProgram;

    public static boolean isStateProtected;

    public static void capture() {
        if (isStateProtected) throw new IllegalStateException("GlStateTracker: Nesting");
        vertArrayBinding = GL33.glGetInteger(GL33.GL_VERTEX_ARRAY_BINDING);
        arrayBufBinding = GL33.glGetInteger(GL33.GL_ARRAY_BUFFER_BINDING);
        elementBufBinding = GL33.glGetInteger(GL33.GL_ELEMENT_ARRAY_BUFFER_BINDING);

        currentShaderInstance = ShaderInstance.lastAppliedShader;
        currentProgram = GL33.glGetInteger(GL33.GL_CURRENT_PROGRAM);

        isStateProtected = true;
    }

    public static void restore() {
        if (!isStateProtected) throw new IllegalStateException("GlStateTracker: Not captured");
        GL33.glBindVertexArray(vertArrayBinding);
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, arrayBufBinding);
        GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, elementBufBinding);
        GL33.glUseProgram(currentProgram);
        ShaderInstance.lastAppliedShader = currentShaderInstance;
        ShaderInstance.lastProgramId = currentProgram;

        // TODO obtain original state from RenderSystem?
        RenderSystem.enableCull();
        RenderSystem.depthMask(true);

        isStateProtected = false;
    }

    public static void assertProtected() {
        if (!isStateProtected) throw new IllegalStateException("GlStateTracker: Not protected");
    }

}