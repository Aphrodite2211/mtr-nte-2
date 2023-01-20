package cn.zbx1425.mtrsteamloco.render.rail;

import cn.zbx1425.sowcer.batch.BatchManager;
import cn.zbx1425.sowcer.batch.ShaderProp;
import cn.zbx1425.sowcer.math.Matrix4f;
import mtr.data.Rail;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

import java.io.Closeable;

public abstract class BakedRailBase implements Closeable {

    public Rail rail;

    public boolean bufferBuilt = false;

    public static final int MAX_RAIL_LENGTH_ACCEPTABLE = 4000;

    protected BakedRailBase(Rail rail) {
        this.rail = rail;
    }

    public void rebuildBuffer(Level world) {
        bufferBuilt = true;
    }
    public abstract void enqueue(BatchManager batchManager, ShaderProp shaderProp);

    @Override
    public void close() {

    }

    protected Matrix4f lookAt(float posX, float posY, float posZ, float tgX, float tgY, float tgZ, float len) {
        Matrix4f matrix4f = Matrix4f.translation(posX, posY, posZ);

        final float yaw = (float) Mth.atan2(tgX - posX, tgZ - posZ);
        final float pitch = (float) Math.asin((tgY - posY) * (1f / len));

        matrix4f.rotateY((float) Math.PI + yaw);
        matrix4f.rotateX(pitch);

        return matrix4f;
    }
}