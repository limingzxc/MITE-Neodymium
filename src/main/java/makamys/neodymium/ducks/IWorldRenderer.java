package makamys.neodymium.ducks;

import java.util.List;

import makamys.neodymium.renderer.ChunkMesh;

public interface IWorldRenderer {
    public List<ChunkMesh> neodymium$getChunkMeshes();
    public boolean neodymium$isDrawn();
    public void neodymium$updateRendererSort();
}
