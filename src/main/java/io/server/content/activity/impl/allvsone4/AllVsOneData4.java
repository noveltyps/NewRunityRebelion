package io.server.content.activity.impl.allvsone4;

public final class AllVsOneData4 {

	/**
	 * @author Adam_#6723
	 * Stores the NPC Data for All Vs One
	 */
	
	public static enum WaveData {
		WAVE_1(6593, 6593, 6593, 6593, 6615, 6615),
		WAVE_2(6493, 6493, 6493, 6495, 6495, 6495),
		WAVE_3(6504, 6504, 6504, 6504, 6609, 6609),
		WAVE_4(6505, 6505, 6505, 5862, 5862, 5862),
		WAVE_5(6384, 6384, 319, 319, 7585, 7585),
		WAVE_6(6619, 6619, 6619, 6619, 494, 494),
		WAVE_7(7860, 7860, 7860, 7585, 7585, 7585),
		WAVE_8(6505, 6505, 6505, 6384, 6384, 6384),
		WAVE_9(6593, 6593, 6593, 6593, 6502, 6502),
		WAVE_10(319, 319, 319, 319, 5862, 5862),
		WAVE_11(6615, 6615, 7585, 7585, 7585, 7585),
		WAVE_12(6492, 6492, 6492, 6494, 6494, 6494),
		WAVE_13(6609, 6609, 6500, 6500, 6500, 6500),
		WAVE_14(6609, 6500, 6609, 494, 494, 494),
		WAVE_15(6384, 6384, 6766, 6766, 6766, 6766),
		WAVE_16(494, 6505, 6505, 5779, 7585, 494),
		WAVE_17(5862, 5862, 5862, 6615, 6615, 6615),
		WAVE_18(6503, 6503, 6503, 5779, 5779, 5779),
		WAVE_19(6502, 6502, 6503, 6503, 6503, 6503),
		WAVE_20(5862, 5862, 8060, 8060, 8060, 8060)

		
		
		
		;
		/* WAVE_2(6593, 6499), WAVE_3(2265, 2267, 2266, 5947), WAVE_4(6766, 6766),
		WAVE_5(4005, 7940, 7939), WAVE_6(6619, 6618, 5874, 5874), WAVE_7(2215, 2217, 2218, 2216),
		WAVE_8(2205, 2207), WAVE_9(3162, 3165, 3164, 3163), WAVE_10(3129, 3132, 3130, 3131),
		WAVE_11(6505, 7148, 7149, 7148), WAVE_12(6609, 1160, 1157, 1158), WAVE_13(3024, 6615, 6616, 6616),
		WAVE_14(5862, 7935), WAVE_15(5129), WAVE_16(8095);*/

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
