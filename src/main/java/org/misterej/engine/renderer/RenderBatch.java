package org.misterej.engine.renderer;

public class RenderBatch {
    // VERTEX
    // ======
    //  Position            Color                           UV
    //  float, float        float, float, float, float      float, float
    private final int POSITION_SIZE = 2;
    private final int COLOR_SIZE = 4;

    private final int POSITION_OFFSET = 0;
    private final int COLOR_OFFSET = (POSITION_SIZE) * Float.BYTES;
    private final int VERTEX_SIZE = (POSITION_SIZE + COLOR_SIZE) * Float.BYTES;
}
