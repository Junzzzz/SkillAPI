package com.github.junzzzz.skillapi.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

import javax.vecmath.Vector2d;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jun
 */
public class YAxisAlignedBoundingBox {
    private final Vector2d[] vertex;

    private final double minX;
    private final double minY;
    private final double minZ;
    private final double maxX;
    private final double maxY;
    private final double maxZ;


    public YAxisAlignedBoundingBox(double minY, double maxY, Vector2d... vertex) {
        if (vertex.length != 4) {
            throw new IllegalArgumentException("The maximum number of vertices is 4");
        }
        this.vertex = vertex;

        double _minX = vertex[0].x, _minZ = vertex[0].y, _maxX = vertex[0].x, _maxZ = vertex[0].y;

        for (int i = 1; i < 3; i++) {
            Vector2d v = vertex[i];
            if (v.x < _minX) {
                _minX = v.x;
            }
            if (v.y < _minZ) {
                _minZ = v.y;
            }
            if (v.x > _maxX) {
                _maxX = v.x;
            }
            if (v.y > _maxZ) {
                _maxZ = v.y;
            }
        }
        this.minY = minY;
        this.maxY = maxY;

        this.minX = _minX;
        this.maxX = _maxX;
        this.minZ = _minZ;
        this.maxZ = _maxZ;
    }

    public boolean intersectsWith(AxisAlignedBB box) {
        if (box.minX > this.maxX || box.maxX < this.minX) {
            return false;
        }
        if (box.minZ > this.maxZ || box.maxZ < this.minZ) {
            return false;
        }
        if (box.minY > this.maxY || box.maxY < this.minY) {
            return false;
        }
        if (checkPointInBox(box.minX, box.minZ)
                || checkPointInBox(box.minX, box.maxZ)
                || checkPointInBox(box.maxX, box.minZ)
                || checkPointInBox(box.maxX, box.maxZ)) {
            return true;
        } else {
            Vector2d[] boxVectors = {new Vector2d(box.minX, box.minZ), new Vector2d(box.minX, box.maxZ), new Vector2d(box.maxX, box.maxZ), new Vector2d(maxX, box.minZ)};
            for (Vector2d vec : this.vertex) {
                if (checkPointInBox(boxVectors, vec.x, vec.y)) {
                    return true;
                }
            }
            return false;
        }
    }

    private boolean checkPointInBox(double x, double y) {
        return checkPointInBox(this.vertex, x, y);
    }

    private boolean checkPointInBox(Vector2d[] vertices, double x, double y) {
        int crossings = 0;
        for (int i = 0, j = vertices.length - 1; i < vertices.length; j = i++) {
            if (((vertices[i].x > x) != (vertices[j].x > x))
                    && (x > (vertices[j].x - vertices[i].x) * (y - vertices[i].y) / (vertices[j].y - vertices[i].y) + vertices[i].x))
                crossings++;
        }
        return (crossings % 2 != 0);
    }

    public List<Entity> searchWorldEntities(World world) {
        List<Entity> result = new ArrayList<>();

        IChunkProvider chunkProvider = world.getChunkProvider();

        int i = MathHelper.floor_double(this.minX / 16.0D);
        int j = MathHelper.floor_double(this.maxX / 16.0D);
        int k = MathHelper.floor_double(this.minZ / 16.0D);
        int l = MathHelper.floor_double(this.maxZ / 16.0D);

        for (int chunkX = i; chunkX <= j; chunkX++) {
            for (int chunkY = k; chunkY <= l; chunkY++) {
                if (chunkProvider.chunkExists(chunkX, chunkY)) {
                    searchChunkEntities(world.getChunkFromChunkCoords(chunkX, chunkY), result);
                }
            }
        }

        return result;
    }


    private void searchChunkEntities(Chunk chunk, List<Entity> result) {
        int i = MathHelper.floor_double(this.minY / 16.0D);
        int j = MathHelper.floor_double(this.maxY / 16.0D);
        i = MathHelper.clamp_int(i, 0, chunk.entityLists.length - 1);
        j = MathHelper.clamp_int(j, 0, chunk.entityLists.length - 1);

        for (int k = i; k <= j; ++k) {
            @SuppressWarnings("rawtypes")
            List entityList = chunk.entityLists[k];

            for (Object o : entityList) {
                Entity entity = (Entity) o;

                if (intersectsWith(entity.boundingBox)) {
                    result.add(entity);
                }
            }
        }
    }
}
