package makamys.neodymium.renderer.compat;

import makamys.neodymium.renderer.ChunkMesh;
import makamys.neodymium.renderer.attribs.AttributeSet;
import makamys.neodymium.util.BufferWriter;
import net.minecraft.OpenGlHelper;
import net.minecraft.Tessellator;
import org.lwjgl.opengl.ARBVertexShader;

import static makamys.neodymium.renderer.MeshPolygon.DEFAULT_BRIGHTNESS;
import static makamys.neodymium.renderer.MeshPolygon.DEFAULT_COLOR;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE3;
import static org.lwjgl.opengl.GL13.glClientActiveTexture;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class RenderUtilShaders implements RenderUtil {
    public static final RenderUtilShaders INSTANCE = new RenderUtilShaders();

    public static final int POLYGON_OFFSET_U = 3;
    public static final int POLYGON_OFFSET_V = 4;
    public static final int POLYGON_OFFSET_C = 5;
    public static final int POLYGON_OFFSET_B = 7;
    public static final int POLYGON_OFFSET_E1 = 6;
    public static final int POLYGON_OFFSET_XN = 8;
    public static final int POLYGON_OFFSET_YN = 9;
    public static final int POLYGON_OFFSET_ZN = 10;
    public static final int POLYGON_OFFSET_UM = 11;
    public static final int POLYGON_OFFSET_VM = 12;
    public static final int POLYGON_OFFSET_OPTIFINE_START = POLYGON_OFFSET_XN;
    public static final int POLYGON_OFFSET_OPTIFINE_END = POLYGON_OFFSET_VM;
    public static final int POLYGON_OFFSET_OPTIFINE_COUNT = POLYGON_OFFSET_OPTIFINE_END - POLYGON_OFFSET_OPTIFINE_START + 1;

    @Override
    public void readMeshPolygon(int[] tessBuffer, int tessOffset, int[] polygonBuffer, int polygonOffset, float offsetX, float offsetY, float offsetZ, int vertices, ChunkMesh.Flags flags) {
        final int tessVertexSize = vertexSizeInTessellator();
        final int polygonVertexSize = vertexSizeInPolygonBuffer();

        for(int vi = 0; vi < vertices; vi++) {
            int tI = tessOffset + vi * tessVertexSize;
            int qI = polygonOffset + vi * polygonVertexSize;

            polygonBuffer[qI + POLYGON_OFFSET_XPOS] = Float.floatToRawIntBits(Float.intBitsToFloat(tessBuffer[tI]) + offsetX);
            polygonBuffer[qI + POLYGON_OFFSET_YPOS] = Float.floatToRawIntBits(Float.intBitsToFloat(tessBuffer[tI + 1]) + offsetY);
            polygonBuffer[qI + POLYGON_OFFSET_ZPOS] = Float.floatToRawIntBits(Float.intBitsToFloat(tessBuffer[tI + 2]) + offsetZ);

            polygonBuffer[qI + POLYGON_OFFSET_U] = tessBuffer[tI + 3];
            polygonBuffer[qI + POLYGON_OFFSET_V] = tessBuffer[tI + 4];

            polygonBuffer[qI + POLYGON_OFFSET_C] = flags.hasColor ? tessBuffer[tI + 5] : DEFAULT_COLOR;

            polygonBuffer[qI + POLYGON_OFFSET_E1] = tessBuffer[tI + 6];

            polygonBuffer[qI + POLYGON_OFFSET_B] = flags.hasBrightness ? tessBuffer[tI + 7] : DEFAULT_BRIGHTNESS;

            System.arraycopy(tessBuffer, tI + 8, polygonBuffer, qI + POLYGON_OFFSET_OPTIFINE_START, POLYGON_OFFSET_OPTIFINE_COUNT);
        }
    }

    @Override
    public void writeMeshPolygonToBuffer(int[] meshPolygonBuffer, int polygonOffset, BufferWriter out, int expectedStride, int verticesPerPolygon) {
        final int vertexSize = vertexSizeInPolygonBuffer();
        for(int vi = 0; vi < verticesPerPolygon; vi++) {
            int offset = polygonOffset + vi * vertexSize;
            out.writeFloat(Float.intBitsToFloat(meshPolygonBuffer[offset + POLYGON_OFFSET_XPOS]));
            out.writeFloat(Float.intBitsToFloat(meshPolygonBuffer[offset + POLYGON_OFFSET_YPOS]));
            out.writeFloat(Float.intBitsToFloat(meshPolygonBuffer[offset + POLYGON_OFFSET_ZPOS]));

            float u = Float.intBitsToFloat(meshPolygonBuffer[offset + POLYGON_OFFSET_U]);
            float v = Float.intBitsToFloat(meshPolygonBuffer[offset + POLYGON_OFFSET_V]);

            out.writeFloat(u);
            out.writeFloat(v);

            out.writeInt(meshPolygonBuffer[offset + POLYGON_OFFSET_C]);

            out.writeInt(meshPolygonBuffer[offset + POLYGON_OFFSET_E1]);

            out.writeInt(meshPolygonBuffer[offset + POLYGON_OFFSET_B]);

            out.writeFloat(Float.intBitsToFloat(meshPolygonBuffer[offset + POLYGON_OFFSET_XN]));
            out.writeFloat(Float.intBitsToFloat(meshPolygonBuffer[offset + POLYGON_OFFSET_YN]));
            out.writeFloat(Float.intBitsToFloat(meshPolygonBuffer[offset + POLYGON_OFFSET_ZN]));

            out.writeFloat(Float.intBitsToFloat(meshPolygonBuffer[offset + POLYGON_OFFSET_UM]));
            out.writeFloat(Float.intBitsToFloat(meshPolygonBuffer[offset + POLYGON_OFFSET_VM]));

            assert out.position() % expectedStride == 0;
        }
    }

    @Override
    public int vertexSizeInTessellator() {
        // pos + uv + color + entityData + brightness + normal + midtexture + none
        return 3 + 2 + 1 + 1 + 1 + 3 + 2 + 3;
    }

    @Override
    public int vertexSizeInPolygonBuffer() {
        // pos + uv + color + entityData + brightness + normal + midtexture
        return 3 + 2 + 1 + 1 + 1 + 3 + 2;
    }

    @Override
    public void initVertexAttributes(AttributeSet attributes) {
        attributes.addAttribute("POS", 3, 4, GL_FLOAT);
        attributes.addAttribute("TEXTURE", 2, 4, GL_FLOAT);
        attributes.addAttribute("COLOR", 4, 1, GL_UNSIGNED_BYTE);
        attributes.addAttribute("ENTITY_DATA_1", 1, 4, GL_UNSIGNED_INT);
        attributes.addAttribute("BRIGHTNESS", 2, 2, GL_SHORT);

//        attributes.addAttribute("ENTITY_DATA_2", 1, 4, GL_UNSIGNED_INT);
        attributes.addAttribute("NORMAL", 3, 4, GL_FLOAT);
//        attributes.addAttribute("TANGENT", 4, 4, GL_FLOAT);
        attributes.addAttribute("MIDTEXTURE", 2, 4, GL_FLOAT);
    }


    /**
     * TODO: This format is nice, we should have it in the docs too!
     * position   3 floats 12 bytes offset  0
     * texture    2 floats  8 bytes offset 12
     * color      4 bytes   4 bytes offset 20
     * entitydata 2 shorts  4 bytes offset 24
     * brightness 2 shorts  4 bytes offset 28
     * normal     3 floats 12 bytes offset 32
     * midtexture 2 floats  8 bytes offset 44
     *
     * @param attributes Configured Attributes
     */
    @Override
    public void applyVertexAttributes(AttributeSet attributes) {
        final int stride = attributes.stride();
        final Tessellator tessellator = Tessellator.instance;

        final int entityAttrib = 10;
        final int midTexCoordAttrib = 11;

        // position   3 floats 12 bytes offset 0
        glVertexPointer(3, GL_FLOAT, stride, 0);
        glEnableClientState(GL_VERTEX_ARRAY);

        // texture    2 floats  8 bytes offset 12
        glTexCoordPointer(2, GL_FLOAT, stride, 12);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        // color      4 bytes   4 bytes offset 20
        glColorPointer(4, GL_UNSIGNED_BYTE, stride, 20);
        glEnableClientState(GL_COLOR_ARRAY);

        // entitydata 2 shorts  4 bytes offset 24
        glVertexAttribPointer(entityAttrib, 2, GL_SHORT, false, stride, 24);
        glEnableVertexAttribArray(entityAttrib);

        // brightness 2 shorts  4 bytes offset 28
        OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
        glTexCoordPointer(2, GL_SHORT, stride, 28);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);

        // normal     3 floats 12 bytes offset 32
        glNormalPointer(GL_FLOAT, stride, 32);
        glEnableClientState(GL_NORMAL_ARRAY);

        // tangent    4 floats 16 bytes offset 48
//        glVertexAttribPointer(tangentAttrib, 4, GL_FLOAT, false, stride, 48);
//        glEnableVertexAttribArray(tangentAttrib);

        // midtexture 2 floats  8 bytes offset 44
        glClientActiveTexture(GL_TEXTURE3);
        glTexCoordPointer(2, GL_FLOAT, stride, 44);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);

        ARBVertexShader.glVertexAttribPointerARB(midTexCoordAttrib, 2, GL_FLOAT, false, stride, 44);
        ARBVertexShader.glEnableVertexAttribArrayARB(midTexCoordAttrib);
    }
}
