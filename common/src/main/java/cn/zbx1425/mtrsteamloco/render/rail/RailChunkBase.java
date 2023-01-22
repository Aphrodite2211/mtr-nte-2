package cn.zbx1425.mtrsteamloco.render.rail;

import cn.zbx1425.mtrsteamloco.data.RailModelRegistry;
import cn.zbx1425.sowcer.batch.BatchManager;
import cn.zbx1425.sowcer.batch.ShaderProp;
import cn.zbx1425.sowcer.math.Matrix4f;
import mtr.data.Rail;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public abstract class RailChunkBase implements Closeable {

    public Long chunkId;
    public AABB boundingBox;
    public HashMap<BakedRail, ArrayList<Matrix4f>> containingRails = new HashMap<>();

    protected float modelYMin;
    protected float modelYMax;

    public boolean isDirty = false;

    public RailChunkBase(long chunkId, String modelKey) {
        this.chunkId = chunkId;
        long boundary = RailModelRegistry.boundingBoxes.get(modelKey);
        modelYMin = Float.intBitsToFloat((int)(boundary >> 32));
        modelYMax = Float.intBitsToFloat((int)(boundary & 0xFFFFFFFFL));
        setBoundingBox(0, 0);
    }

    protected void setBoundingBox(float yMin, float yMax) {
        int posXMin = (int)(chunkId >> 32) << (4 + 1);
        int posZMin = (int)(chunkId & 0xFFFFFFFFL) << (4 + 1);
        int span = 1 << (4 + 1);
        boundingBox = new AABB(posXMin, yMin + modelYMin - 1, posZMin,
                posXMin + span, yMax + modelYMax + 1, posZMin + span);
    }

    public void addRail(BakedRail rail) {
        containingRails.put(rail, rail.coveredChunks.get(chunkId));
        isDirty = true;
    }

    public void removeRail(BakedRail rail) {
        containingRails.remove(rail);
        isDirty = true;
    }

    public void rebuildBuffer(Level world) {
        isDirty = false;
    }
    public abstract void enqueue(BatchManager batchManager, ShaderProp shaderProp);

    @Override
    public void close() {

    }
}