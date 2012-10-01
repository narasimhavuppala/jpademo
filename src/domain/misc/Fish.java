package domain.misc;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Fish {

	private FishPK id;
	private String fishName;
	
	@EmbeddedId
	public FishPK getId() {
		return id;
	}
	public void setId(FishPK id) {
		this.id = id;
	}
	public String getFishName() {
		return fishName;
	}
	public void setFishName(String fishName) {
		this.fishName = fishName;
	}
	
	@Override
	public String toString() {
		return String.format("Fish '%s', species %d, id %d", getFishName(), getId().getSpeciesId(), getId().getIndividualId());
	}
}
