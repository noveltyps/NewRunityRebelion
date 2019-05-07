package io.server.content.activity.impl.soloraids5;

public final class Raids5Data {

	/**
	 * 
	 * @author Adam_#6723
	 * Stores the NPC Data for All Vs One
	 */
	public enum WaveData {
		WAVE_1(7570, 7570, 7570, 7570, 7570),
		WAVE_2(7585, 7585, 7585, 7585, 7585),
		WAVE_3(5779, 5779, 7585, 7604, 7404),
		WAVE_4(7542, 7542, 7542, 5779, 7585),
		WAVE_5(7405, 7405, 7405, 7570, 7570),
		WAVE_6(7526, 7526, 7526, 7526, 7526),
		WAVE_7(2205, 2205, 2205, 6500, 6500),
		WAVE_8(6384, 6384, 2215, 2215, 2215),
		WAVE_9(3162, 3162, 6495, 6495, 7570),
		WAVE_10(6310, 6310, 7570, 7570, 2205);
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
