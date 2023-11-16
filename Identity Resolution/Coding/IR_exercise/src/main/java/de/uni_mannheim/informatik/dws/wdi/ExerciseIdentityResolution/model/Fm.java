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
public class Fm implements Matchable {

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
	private int age;
	private String nationality; 
	private List<String> positions;
	private String league;
	private int weight;
	private String club;
	private int height;
	private String preferred_foot;
	private int wage;
	

	public Fm(String identifier, String provenance) {
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
		if(obj instanceof Movie){
			return this.getIdentifier().equals(((Movie) obj).getIdentifier());
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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getLeague() {
		return league;
	}

	public void setLeague(String league) {
		this.league = league;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getPreferred_foot() {
		return preferred_foot;
	}

	public void setPreferred_foot(String preferred_foot) {
		this.preferred_foot = preferred_foot;
	}

	public int getWage() {
		return wage;
	}

	public void setWage(int wage) {
		this.wage = wage;
	}
	
	@Override
	public String toString() {
	    return "Tm [attribute1=" + name;
	}
	
	
	
}
