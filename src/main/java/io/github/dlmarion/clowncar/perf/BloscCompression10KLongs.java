package io.github.dlmarion.clowncar.perf;

import java.nio.ByteBuffer;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.AuxCounters;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

@State(Scope.Thread)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@Threads(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class BloscCompression10KLongs {

	@Param({"blosclz","lz4","lz4hc","snappy","zlib","zstd"})
	public String compressor;

	@Param({"6"})
	public int level;
	
	@Param({"0","1","2"})
	public int shuffle;
	
	@Param({"4"})
	public int threads;
	
	@State(Scope.Thread)
	@AuxCounters
	public static class CompressedSize {
		private long size;
		private long executions;
		
		public long getAvgSize() {
			return (size/executions);
		}
		
		@Setup(Level.Iteration)
		public void clean() {
			size = 0;
			executions = 0;
		}		
	}
	
	private static final long srcLength = Long.BYTES * 10000;
	private static final long dstLength = srcLength + 16;
	private static final ByteBuffer srcLongs = ByteBuffer.allocateDirect((int)srcLength);
	private static final ByteBuffer dst = ByteBuffer.allocateDirect((int)dstLength);
	private static final Random r = new Random(1352351385L);

	static {
		for (int i = 0; i < 10000; i++) {
			srcLongs.putLong(r.nextLong());
		}
	}
	
    @Benchmark
    public void testJnrCompression(CompressedSize result) {
    	result.size += io.github.dlmarion.clowncar.jnr.BloscLibrary.compress(this.level, this.shuffle, Long.BYTES, srcLongs, srcLength, dst, dstLength, this.compressor, (int)srcLength, this.threads);
    	result.executions++;
    }

    @Benchmark
    public void testJnaCompression(CompressedSize result) {
    	result.size += io.github.dlmarion.clowncar.jna.BloscLibrary.compress(this.level, this.shuffle, Long.BYTES, srcLongs, srcLength, dst, dstLength, this.compressor, (int)srcLength, this.threads);
    	result.executions++;
    }

}
