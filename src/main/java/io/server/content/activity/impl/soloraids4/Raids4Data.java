package io.server.content.activity.impl.soloraids4;

public final class Raids4Data {

	/**
	 * 
	 * @author Adam_#6723
	 * Stores the NPC Data for All Vs One
	 */
	public enum WaveData {
		WAVE_1(7405, 7405),
		WAVE_2(7398, 7398),
		WAVE_3(6492, 6492),
		WAVE_4(6494, 6494),
		WAVE_5(6493, 6492),
		WAVE_6(8095);
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
