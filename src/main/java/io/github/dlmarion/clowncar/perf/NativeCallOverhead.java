package io.github.dlmarion.clowncar.perf;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

@State(Scope.Thread)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@Threads(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class NativeCallOverhead {
	
	private static final long srcLength = Long.BYTES * 10000;
	private static final long dstLength = srcLength + 16;
	private static final ByteBuffer srcLongs = ByteBuffer.allocateDirect((int)srcLength);
	private static final ByteBuffer dst = ByteBuffer.allocateDirect((int)dstLength);

    @Benchmark
    public int testJnaOverhead() {
    	return io.github.dlmarion.clowncar.jna.BloscLibrary.compress(6, 0, Long.BYTES, srcLongs, srcLength, dst, dstLength, "snappy", (int)srcLength, 1);
    }

    @Benchmark
    public int testJnrOverhead() {
    	return io.github.dlmarion.clowncar.jnr.BloscLibrary.compress(6, 0, Long.BYTES, srcLongs, srcLength, dst, dstLength, "snappy", (int)srcLength, 1);
    }

}
