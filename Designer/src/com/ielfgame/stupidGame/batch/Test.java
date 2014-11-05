package com.ielfgame.stupidGame.batch;



public class Test {
	
	public static class CryptoRand {
		private int seed;
		public CryptoRand(int seed) {
			this.seed = seed;
		}
		public int seed() {
			return this.seed;
		}
		public int rand() {
			this.seed = this.seed * 0x343fd + 0x269ec3;
			return (this.seed>>0x10) & 0x7fff;
		}
	}
	
	public static void main(final String [] args) {
//		final CryptoRand crr = new CryptoRand(100);
		
		for(int k=0; k<5; k++) {
			System.err.println("-----------------------");
			final CryptoRand cr = new CryptoRand(1230);
			System.err.println("seed="+cr.seed());
			System.err.println("next 10 random values:");
			for(int i=0; i<10; i++) {
				System.err.println(cr.rand());
			}
		}
	}
}
