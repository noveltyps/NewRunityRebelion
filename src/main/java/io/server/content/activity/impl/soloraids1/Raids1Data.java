package io.server.content.activity.impl.soloraids1;

public final class Raids1Data {

	/**
	 * 
	 * @author Adam_#6723
	 * Stores the NPC Data for All Vs One
	 */
	public enum WaveData {
		WAVE_1(7570, 7570, 7570),
		WAVE_2(7527, 7527, 7527, 7527),
		WAVE_3(7585, 7585, 7585),
		WAVE_4(7562, 7562, 7604, 7604),
		WAVE_5(7604, 7604, 7604),
		WAVE_6(7530, 7530, 7530),
		WAVE_7(7548, 7548, 7548, 7548),
		WAVE_8(6766, 6766, 6766);

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

	}
}
