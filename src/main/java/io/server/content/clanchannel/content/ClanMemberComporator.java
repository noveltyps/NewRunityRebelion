package io.server.content.clanchannel.content;

import java.util.Comparator;

import io.server.content.clanchannel.ClanMember;

public enum ClanMemberComporator implements Comparator<ClanMember> {
	PRIVILAGE {
		@Override
		public int compare(ClanMember first, ClanMember second) {
			int compare = second.rank.compareTo(first.rank);
			if (compare == 0) {
				return first.name.compareTo(second.name);
			}
			return compare;
		}
	},
	NAME {
		@Override
		public int compare(ClanMember first, ClanMember second) {
			return first.name.compareTo(second.name);
		}
	},
	RANK {
		@Override
		public int compare(ClanMember first, ClanMember second) {
			int compare = second.getValue() - first.getValue();
			if (compare == 0) {
				return first.name.compareTo(second.name);
			}
			return compare;
		}
	}
}
