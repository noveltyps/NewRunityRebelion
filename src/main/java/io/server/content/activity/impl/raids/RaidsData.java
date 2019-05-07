package io.server.content.activity.impl.raids;

public final class RaidsData {

	/**
	 * @author Adam_#6723
	 * Stores the NPC Data for All Vs One
	 */
	
	public static enum WaveData {
	
		WAVE_1(7570, 7570, 7570, 7548, 7548, 7548, 7604, 7604, 7604, 7562, 7562, 7566, 7566),
		WAVE_2(7527, 7527, 7527, 7527, 7548, 7548, 7548, 7548, 7530, 7530, 7530),
		WAVE_3(7548, 7548, 7548, 7548, 7585, 7585, 7585, 6766, 6766, 6766);
		

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
