package io.gwynt.codec;

import io.gwynt.pipeline.HandlerContext;
import io.gwynt.util.Signal;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public abstract class ReplayingDecoder<S> extends ByteToMessageDecoder {

    private static final Signal REPLAY = Signal.valueOf(ReplayingDecoder.class, "REPLAY");

    private S state;
    private int checkpoint = -1;

    protected ReplayingDecoder() {
        this(null);
    }

    protected ReplayingDecoder(S state) {
        this.state = state;
    }

    protected void replay() {
        throw REPLAY;
    }

    protected S state(S state) {
        S oldState = this.state;
        this.state = state;
        return oldState;
    }

    protected S state() {
        return state;
    }

    protected void checkpoint() {
        checkpoint = internalBuffer().position();
    }

    protected void checkpoint(S state) {
        checkpoint();
        state(state);
    }

    @Override
    public void onClose(HandlerContext context) {
        List<Object> out = new ArrayList<>();
        try {
            callDecode(context, internalBuffer(), out);
            decodeLast(context, internalBuffer(), out);
        } catch (Signal s) {
            // ignore
            REPLAY.expect(s);
        } catch (DecoderException e) {
            throw e;
        } catch (Throwable e) {
            throw new DecoderException(e);
        } finally {
            out.forEach(context::fireMessageReceived);
            out.clear();
        }
        context.fireClose();
    }

    @Override
    protected void callDecode(HandlerContext context, ByteBuffer in, List<Object> out) {
        try {
            while (in.hasRemaining()) {
                int oldPosition = checkpoint = in.position();
                int oldSize = out.size();
                S oldState = state;
                try {
                    decode(context, in, out);
                    if (context.isRemoved()) {
                        break;
                    }

                    if (oldSize == out.size()) {
                        if (oldPosition == in.position() && state == oldState) {
                            throw new DecoderException(getClass().getSimpleName() + "#decode did not decode anything");
                        }
                    }
                } catch (Signal s) {
                    REPLAY.expect(s);

                    if (checkpoint >= 0) {
                        in.position(checkpoint);
                    }
                    break;
                }

                if (oldPosition == in.position() && state == oldState) {
                    throw new DecoderException(getClass().getSimpleName() + "#decode did not decode anything");
                }

                if (isSingleDecode()) {
                    break;
                }
            }
        } catch (DecoderException e) {
            throw e;
        } catch (Throwable e) {
            throw new DecoderException(e);
        }
    }
}
