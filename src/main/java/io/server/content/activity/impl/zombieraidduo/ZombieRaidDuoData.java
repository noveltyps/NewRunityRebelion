package io.server.content.activity.impl.zombieraidduo;

public final class ZombieRaidDuoData {

	/**
	 * @author Adam_#6723
	 * Stores the NPC Data for All Vs One
	 */
	
	public static enum WaveData {
	
		WAVE_1(6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460),
		WAVE_2(6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460),
		WAVE_3(6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460),
		WAVE_4(6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460),
		WAVE_5(6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460),
		WAVE_6(6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460),
		WAVE_7(6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460),
		WAVE_8(6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460),
		WAVE_9(6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460),
		WAVE_10(6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460),
		WAVE_11(6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460),
		WAVE_12(6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460),
		WAVE_13(6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460),
		WAVE_14(6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460),
		WAVE_15(6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460),
		WAVE_16(6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460),
		WAVE_17(6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460),
		WAVE_18(6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460),
		WAVE_19(6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460),
		WAVE_20(6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460),
		WAVE_21(6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460),
		WAVE_22(6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460),
		WAVE_23(6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460),
		WAVE_24(6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460),
		WAVE_25(6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460),
		WAVE_26(6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460),
		WAVE_27(6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460),
		WAVE_28(6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460),
		WAVE_29(6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460, 6460),
		
		;
		

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
