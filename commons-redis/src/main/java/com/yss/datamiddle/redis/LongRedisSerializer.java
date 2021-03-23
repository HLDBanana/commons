package com.yss.datamiddle.redis;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Assert;

import java.nio.charset.Charset;

public class LongRedisSerializer implements RedisSerializer<Long>{

    private final Charset charset;

    public LongRedisSerializer() {
        this(Charset.forName("UTF8"));
    }


    public LongRedisSerializer(Charset charset) {
        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }

    @Override
    public byte[] serialize(Long aLong) throws SerializationException {
        return aLong==null?null:aLong.toString().getBytes(charset);
    }

    @Override
    public Long deserialize(byte[] bytes) throws SerializationException {
        return (bytes == null ? null : Long.parseLong(new String(bytes, charset)));
    }
}
