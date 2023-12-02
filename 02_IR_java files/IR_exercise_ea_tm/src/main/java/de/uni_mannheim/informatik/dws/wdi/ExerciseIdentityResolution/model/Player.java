package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;

/**
 * A {@link AbstractRecord} representing a movie.
 * 
 * @author Oliver Lehmberg (oli@dwslab.de)
 * 
 */
public class Player implements Matchable {

	/*
	 * example entry <movie> <id>academy_awards_2</id> <title>True Grit</title>
	 * <director> <name>Joel Coen and Ethan Coen</name> </director> <actors>
	 * <actor> <name>Jeff Bridges</name> </actor> <actor> <name>Hailee
	 * Steinfeld</name> </actor> </actors> <date>2010-01-01</date> </movie>
	 */

	protected String id;
	protected String provenance;
	private String name;
	private LocalDateTime birthdate;
	private String nationality; 
	private List<String> positions;
	private int jersey_number;
	private String club; 
	private int current_market_value;
	

	public Player(String identifier, String provenance) {
		id = identifier;
		this.provenance = provenance;
		setPositions(new LinkedList<>());
	}

	@Override
	public String getIdentifier() {
		return id;
	}

	@Override
	public String getProvenance() {
		return provenance;
	}


	@Override
	public int hashCode() {
		return getIdentifier().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Player){
			return this.getIdentifier().equals(((Player) obj).getIdentifier());
		}else
			return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public LocalDateTime getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(LocalDateTime birthdate) {
		this.birthdate = birthdate;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getClub() {
		return club;
	}

	public void setClub(String club) {
		this.club = club;
	}


	public List<String> getPositions() {
		return positions;
	}

	public void setPositions(List<String> positions) {
		this.positions = positions;
	}
	
	@Override
	public String toString() {
	    return "Tm attribute1=" + name;
	}

	public int getJersey_number() {
		return jersey_number;
	}

	public void setJersey_number(int jersey_number) {
		this.jersey_number = jersey_number;
	}

	public int getCurrent_market_value() {
		return current_market_value;
	}

	public void setCurrent_market_value(int current_market_value) {
		this.current_market_value = current_market_value;
	}
	
	
	
}
