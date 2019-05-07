package io.server.content.activity.impl.raids2;

public final class Raids2Data {

	/**
	 * @author Adam_#6723
	 * Stores the NPC Data for All Vs One
	 * force
	 */
	
	public static enum WaveData {
		
		WAVE_1(7585, 7585, 7585, 7585, 7585, 7585),
		WAVE_2(8388, 8388, 8388, 8388, 8388, 8388),
		WAVE_3(8370, 8370, 8370, 8370, 8370, 8370),
		WAVE_4(8388, 8388, 8388, 8388, 8388, 8388),
		WAVE_5(239, 239, 239, 239, 239, 239);
	
		private final int[] monster;

		WaveData(int... monster) {
			this.monster = monster;
		}

		public int[] getMonster() {
			return monster;
		}

		public static WaveData getOrdinal(int ordinal) {
			for (WaveData wave : values()) {
				if (wave.ordinal() == ordinal)
					return wave;
			}
			return null;
		}
		
		public static WaveData getNext(int current) {
			return getOrdinal(current + 1);
		}
		
		public static WaveData getOrdinal1(int ordinal) {
			for (WaveData wave : values()) {
				if (wave.ordinal() == ordinal + 1)
					return wave;
			}
			return null;
		}


	}
}
