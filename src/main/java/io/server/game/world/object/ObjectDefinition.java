package io.server.game.world.object;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.server.fs.cache.archive.Archive;
import io.server.util.Buffer;

public final class ObjectDefinition {

	private static final Logger logger = LogManager.getLogger();

	public static void init(Archive archive) {
		Buffer dataBuf = new Buffer(archive.getData("loc.dat").array());
		final int count = dataBuf.readUShort();

		ObjectDefinition[] definitions = new ObjectDefinition[count];

		logger.info(String.format("Loaded: %d objects", count));

		for (int i = 0; i < count; i++) {
			ObjectDefinition def = new ObjectDefinition(i);
			def.setDefaults();
			def.decode(dataBuf);
			definitions[i] = def;
		}

		ObjectDefinition.definitions = definitions;
	}

	public void setDefaults() {
		modelIds = null;
		modelTypes = null;
		description = null;
		modifiedModelColors = null;
		originalModelColors = null;
		originalTexture = null;
		modifiedTexture = null;
		width = 1;
		length = 1;
		solid = true;
		impenetrable = true;
		interactive = false;
		contouredGround = false;
		nonFlatShading = false;
		modelClipped = false;
		animation = -1;
		decorDisplacement = 16;
		ambientLighting = 0;
		contrast = 0;
		actions = null;
		mapIcon = -1;
		mapscene = -1;
		inverted = false;
		castsShadow = true;
		scaleX = 128;
		scaleY = 128;
		scaleZ = 128;
		surroundings = 0;
		translateX = 0;
		translateY = 0;
		translateZ = 0;
		obstructsGround = false;
		isHollow = false;
		supportItems = -1;
		varbit = -1;
		varp = -1;
		morphisms = null;
	}

	public void decode(Buffer buffer) {
		while (true) {
			int opcode = buffer.readUByte();

			if (opcode == 0) {
				break;
			} else if (opcode == 1) {
				int len = buffer.readUByte();
				if (len > 0) {
					if (modelIds == null) {
						modelTypes = new int[len];
						modelIds = new int[len];

						for (int i = 0; i < len; i++) {
							modelIds[i] = buffer.readUShort();
							modelTypes[i] = buffer.readUByte();
						}
					} else {
						buffer.currentOffset += len * 3;
					}
				}
			} else if (opcode == 2) {
				name = buffer.readString();
			} else if (opcode == 5) {
				int len = buffer.readUByte();
				if (len > 0) {
					if (modelIds == null) {
						modelTypes = null;
						modelIds = new int[len];
						for (int i = 0; i < len; i++) {
							modelIds[i] = buffer.readUShort();
						}
					} else {
						buffer.currentOffset += len * 2;
					}
				}
			} else if (opcode == 14) {
				width = buffer.readUByte();
			} else if (opcode == 15) {
				length = buffer.readUByte();
			} else if (opcode == 17) {
				solid = false;
			} else if (opcode == 18) {
				impenetrable = false;
			} else if (opcode == 19) {
				interactive = (buffer.readUByte() == 1);
			} else if (opcode == 21) {
				contouredGround = true;
			} else if (opcode == 22) {
				nonFlatShading = true;
			} else if (opcode == 23) {
				modelClipped = true;
			} else if (opcode == 24) {
				animation = buffer.readUShort();
				if (animation == 0xFFFF) {
					animation = -1;
				}
			} else if (opcode == 27) {
				clipType = 1;
			} else if (opcode == 28) {
				decorDisplacement = buffer.readUByte();
			} else if (opcode == 29) {
				ambientLighting = buffer.readSignedByte();
			} else if (opcode == 39) {
				contrast = buffer.readSignedByte() * 25;
			} else if (opcode >= 30 && opcode < 35) {
				if (actions == null) {
					actions = new String[5];
				}
				actions[opcode - 30] = buffer.readString();
				if (actions[opcode - 30].equalsIgnoreCase("Hidden")) {
					actions[opcode - 30] = null;
				}
			} else if (opcode == 40) {
				int len = buffer.readUByte();
				modifiedModelColors = new int[len];
				originalModelColors = new int[len];
				for (int i = 0; i < len; i++) {
					modifiedModelColors[i] = buffer.readUShort();
					originalModelColors[i] = buffer.readUShort();
				}
			} else if (opcode == 41) {
				int len = buffer.readUByte();
				modifiedTexture = new short[len];
				originalTexture = new short[len];
				for (int i = 0; i < len; i++) {
					modifiedTexture[i] = (short) buffer.readUShort();
					originalTexture[i] = (short) buffer.readUShort();
				}

			} else if (opcode == 62) {
				inverted = true;
			} else if (opcode == 64) {
				castsShadow = false;
			} else if (opcode == 65) {
				scaleX = buffer.readUShort();
			} else if (opcode == 66) {
				scaleY = buffer.readUShort();
			} else if (opcode == 67) {
				scaleZ = buffer.readUShort();
			} else if (opcode == 68) {
				mapscene = buffer.readUShort();
			} else if (opcode == 69) {
				surroundings = buffer.readUByte();
			} else if (opcode == 70) {
				translateX = buffer.readUShort();
			} else if (opcode == 71) {
				translateY = buffer.readUShort();
			} else if (opcode == 72) {
				translateZ = buffer.readUShort();
			} else if (opcode == 73) {
				obstructsGround = true;
			} else if (opcode == 74) {
				isHollow = true;
			} else if (opcode == 75) {
				supportItems = buffer.readUByte();
			} else if (opcode == 78) {
				buffer.readUShort(); // ambient sound id
				buffer.readUByte();
			} else if (opcode == 79) {
				buffer.readUShort();
				buffer.readUShort();
				buffer.readUByte();
				int len = buffer.readUByte();

				for (int i = 0; i < len; i++) {
					buffer.readUShort();
				}
			} else if (opcode == 81) {
				buffer.readUByte();
			} else if (opcode == 82) {
				mapIcon = buffer.readUShort();

				if (mapIcon == 0xFFFF) {
					mapIcon = -1;
				}
			} else if (opcode == 77 || opcode == 92) {
				varp = buffer.readUShort();

				if (varp == 0xFFFF) {
					varp = -1;
				}

				varbit = buffer.readUShort();

				if (varbit == 0xFFFF) {
					varbit = -1;
				}

				int value = -1;

				if (opcode == 92) {
					value = buffer.readUShort();

					if (value == 0xFFFF) {
						value = -1;
					}
				}

				int len = buffer.readUByte();

				morphisms = new int[len + 2];
				for (int i = 0; i <= len; ++i) {
					morphisms[i] = buffer.readUShort();
					if (morphisms[i] == 0xFFFF) {
						morphisms[i] = -1;
					}
				}
				morphisms[len + 1] = value;
			}

		}

		if (name != null && !name.equals("null")) {
			interactive = modelIds != null && (modelTypes == null || modelTypes[0] == 10);
			if (actions != null)
				interactive = true;
		}

		if (isHollow) {
			solid = false;
			impenetrable = false;
		}

		if (supportItems == -1) {
			supportItems = solid ? 1 : 0;
		}
	}

	public ObjectDefinition(int id) {
		this.id = id;
	}

	public static ObjectDefinition lookup(int id) {
		return definitions == null ? null : definitions[id];
	}

	public static int getCount() {
		return definitions == null ? 0 : definitions.length;
	}

	private short[] originalTexture;
	private short[] modifiedTexture;
	public boolean obstructsGround;
	public byte ambientLighting;
	public int translateX;
	public String name;
	public int scaleZ;
	public int contrast;
	public int width;
	public int translateY;
	public int mapIcon;
	public int[] originalModelColors;
	public int scaleX;
	public int varp;
	public boolean inverted;
	public int type;
	public boolean impenetrable;
	public int mapscene;
	public int morphisms[];
	public int supportItems;
	public int length;
	public boolean contouredGround;
	public boolean modelClipped;
	public boolean isHollow;
	public boolean solid;
	public int surroundings;
	public boolean nonFlatShading;
	public int scaleY;
	public int[] modelIds;
	public int varbit;
	public int decorDisplacement;
	public int[] modelTypes;
	public byte description[];
	public boolean interactive;
	public boolean castsShadow;
	public int animation;
	public static ObjectDefinition[] definitions;
	public int translateZ;
	public int[] modifiedModelColors;
	public String[] actions;
	int clipType = 2;

	private int id;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
