package io.gwynt.codec;

public class DecoderException extends RuntimeException {

    public DecoderException(Throwable cause) {
        super(cause);
    }

    public DecoderException(String message) {
        super(message);
    }
}
