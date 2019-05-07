package io.server.content.activity.impl.raids3;

public final class Raids3Data {

	/**
	 * @author Adam_#6723
	 * Stores the NPC Data for All Vs One
	 * force
	 */
	
	public static enum WaveData {
		
		WAVE_1(7407, 7407, 7393, 7393),
		WAVE_2(7409, 7397, 7409, 7397),
		WAVE_3(7410, 7410, 7393, 7397),
		WAVE_4(7411, 7411, 7394, 7394),
		WAVE_5(7416, 7398,  7416, 7398),
		WAVE_6(7405, 7405);
	
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
