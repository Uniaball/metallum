package com.metallum.client.metal.render.mtl;

import com.metallum.client.metal.render.MetalProbe;
import com.metallum.client.metal.render.bridge.MetalNativeBridge;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.lang.foreign.MemorySegment;

@Environment(EnvType.CLIENT)
public final class MTLRenderCommandEncoder {
    private MemorySegment handle;

    MTLRenderCommandEncoder(final MemorySegment handle) {
        this.handle = handle;
    }

    MemorySegment handle() {
        if (MetalProbe.isNullHandle(this.handle)) {
            throw new IllegalStateException("MTLRenderCommandEncoder is closed");
        }
        return this.handle;
    }

    public void endEncoding() {
        if (MetalProbe.isNullHandle(this.handle)) {
            return;
        }
        MetalNativeBridge.INSTANCE.MTLCommandEncoder_endEncoding(this.handle);
        MetalNativeBridge.INSTANCE.metallum_release_object(this.handle);
        this.handle = MemorySegment.NULL;
    }

    public void setRenderPipelineState(final MemorySegment pipeline) {
        MetalNativeBridge.INSTANCE.MTLRenderCommandEncoder_setRenderPipelineState(this.handle(), pipeline);
    }

    public void setDepthStencilState(final MemorySegment depthStencilState) {
        MetalNativeBridge.INSTANCE.MTLRenderCommandEncoder_setDepthStencilState(this.handle(), depthStencilState);
    }

    public void setDepthBias(final double depthBias, final double slopeScale, final double clamp) {
        MetalNativeBridge.INSTANCE.MTLRenderCommandEncoder_setDepthBias(this.handle(), depthBias, slopeScale, clamp);
    }

    public void setFrontFacingWinding(final int clockwise) {
        MetalNativeBridge.INSTANCE.MTLRenderCommandEncoder_setFrontFacingWinding(this.handle(), clockwise);
    }

    public void setCullMode(final long cullMode) {
        MetalNativeBridge.INSTANCE.MTLRenderCommandEncoder_setCullMode(this.handle(), cullMode);
    }

    public void setTriangleFillMode(final int lines) {
        MetalNativeBridge.INSTANCE.MTLRenderCommandEncoder_setTriangleFillMode(this.handle(), lines);
    }

    public void setVertexBuffer(final MemorySegment buffer, final long offset, final long index) {
        MetalNativeBridge.INSTANCE.MTLRenderCommandEncoder_setVertexBuffer(this.handle(), buffer, offset, index);
    }

    public void setFragmentBuffer(final MemorySegment buffer, final long offset, final long index) {
        MetalNativeBridge.INSTANCE.MTLRenderCommandEncoder_setFragmentBuffer(this.handle(), buffer, offset, index);
    }

    public void setVertexTexture(final MemorySegment texture, final long index) {
        MetalNativeBridge.INSTANCE.MTLRenderCommandEncoder_setVertexTexture(this.handle(), texture, index);
    }

    public void setFragmentTexture(final MemorySegment texture, final long index) {
        MetalNativeBridge.INSTANCE.MTLRenderCommandEncoder_setFragmentTexture(this.handle(), texture, index);
    }

    public void setVertexSamplerState(final MemorySegment sampler, final long index) {
        MetalNativeBridge.INSTANCE.MTLRenderCommandEncoder_setVertexSamplerState(this.handle(), sampler, index);
    }

    public void setFragmentSamplerState(final MemorySegment sampler, final long index) {
        MetalNativeBridge.INSTANCE.MTLRenderCommandEncoder_setFragmentSamplerState(this.handle(), sampler, index);
    }

    public void setScissorRect(final long x, final long y, final long width, final long height) {
        MetalNativeBridge.INSTANCE.MTLRenderCommandEncoder_setScissorRect(this.handle(), x, y, width, height);
    }

    public void drawPrimitives(final long primitiveType, final int firstVertex, final int vertexCount, final int instanceCount) {
        MetalNativeBridge.INSTANCE.MTLRenderCommandEncoder_drawPrimitives(this.handle(), primitiveType, firstVertex, vertexCount, instanceCount);
    }

    public void drawIndexedPrimitives(final long primitiveType, final int indexCount, final long indexType, final MemorySegment indexBuffer, final long offset, final int instanceCount, final int baseVertex) {
        MetalNativeBridge.INSTANCE.MTLRenderCommandEncoder_drawIndexedPrimitives(this.handle(), primitiveType, indexCount, indexType, indexBuffer, offset, instanceCount, baseVertex);
    }

    public void drawPrimitivesTriangleFan(final MemorySegment fanIndexBuffer, final int firstVertex, final int vertexCount, final int instanceCount) {
        MetalNativeBridge.INSTANCE.MTLRenderCommandEncoder_drawPrimitivesTriangleFan(this.handle(), fanIndexBuffer, firstVertex, vertexCount, instanceCount);
    }

    public void drawIndexedPrimitivesTriangleFan(final MemorySegment indexBuffer, final MemorySegment fanIndexBuffer, final long indexType, final long offset, final int indexCount, final int baseVertex, final int instanceCount) {
        MetalNativeBridge.INSTANCE.MTLRenderCommandEncoder_drawIndexedPrimitivesTriangleFan(this.handle(), indexBuffer, fanIndexBuffer, indexType, offset, indexCount, baseVertex, instanceCount);
    }
}
