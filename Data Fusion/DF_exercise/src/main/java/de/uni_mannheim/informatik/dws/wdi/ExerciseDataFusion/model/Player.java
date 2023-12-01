/*
 * Copyright (c) 2017 Data and Web Science Group, University of Mannheim, Germany (http://dws.informatik.uni-mannheim.de/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.github.andrewoma.dexx.collection.ArrayList;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

/**
 * A {@link AbstractRecord} representing a movie.
 * 
 * @author Oliver Lehmberg (oli@dwslab.de)
 * 
 */
public class Player extends AbstractRecord<Attribute> implements Serializable {

	/*
	 * example entry <movie> <id>academy_awards_2</id> <title>True Grit</title>
	 * <director> <name>Joel Coen and Ethan Coen</name> </director> <actors>
	 * <actor> <name>Jeff Bridges</name> </actor> <actor> <name>Hailee
	 * Steinfeld</name> </actor> </actors> <date>2010-01-01</date> </movie>
	 */

	private static final long serialVersionUID = 1L;

	public Player(String identifier, String provenance) {
		super(identifier, provenance);

	}

	protected String id;
	private String name;
	private LocalDateTime birthdate;
	private String nationality; 
	private List<String> positions;
	private String club; 
	private int weight;
	private int height;
	private int jersey_number;
	private String league;
	private int current_market_value;
	private int wage; 
	private String preferred_foot; 
	

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
	

	private Map<Attribute, Collection<String>> provenance = new HashMap<>();
	private Collection<String> recordProvenance;

	public void setRecordProvenance(Collection<String> provenance) {
		recordProvenance = provenance;
	}

	public Collection<String> getRecordProvenance() {
		return recordProvenance;
	}

	public void setAttributeProvenance(Attribute attribute,
			Collection<String> provenance) {
		this.provenance.put(attribute, provenance);
	}

	public Collection<String> getAttributeProvenance(String attribute) {
		return provenance.get(attribute);
	}

	public String getMergedAttributeProvenance(Attribute attribute) {
		Collection<String> prov = provenance.get(attribute);

		if (prov != null) {
			return StringUtils.join(prov, "+");
		} else {
			return "";
		}
	}

	public static final Attribute NAME = new Attribute("name");
	public static final Attribute BIRTHDATE = new Attribute("birthdate");
	public static final Attribute NATIONALITY = new Attribute("nationality");
	public static final Attribute CLUB = new Attribute("club");
	public static final Attribute WEIGHT = new Attribute("weight");
	public static final Attribute HEIGHT = new Attribute("height");
	public static final Attribute JERSEY_NUMBER = new Attribute("jersey_number");
	public static final Attribute LEAGUE = new Attribute("league");
	public static final Attribute CURRENT_MARKET_VALUE = new Attribute("current_market_value");
	public static final Attribute WAGE = new Attribute("wage");
	public static final Attribute PREFERRED_FOOT = new Attribute("preferred_foot");
	public static final Attribute POSITIONS = new Attribute("positions");
	

	
	@Override
	public boolean hasValue(Attribute attribute) {
		if(attribute==NAME)
			return getName() != null && !getName().isEmpty();
		else if(attribute==NATIONALITY)
			return getNationality() != null && !getNationality().isEmpty();
		else if(attribute==BIRTHDATE)
			return getBirthdate() != null;
		else if(attribute==CLUB)
			return getClub() != null && !getClub().isEmpty();
		else if(attribute==WEIGHT)
			return getWeight() !=  0;
		else if(attribute==HEIGHT)
			return getHeight() !=  0;
		else if(attribute==JERSEY_NUMBER)
			return getJersey_number() != 0;
		else if(attribute==LEAGUE)
			return getLeague() != null;
		else if(attribute==CURRENT_MARKET_VALUE)
			return getCurrent_market_value() != 0;
		else if(attribute==WAGE)
			return getWage() != 0;
		else if(attribute==PREFERRED_FOOT)
			return getPreferred_foot() != null;
		else if(attribute==POSITIONS)
			return getPositions() != null;
			
			return false;
	}

	@Override
	public String toString() {
		return String.format("[Players %s: %s / %s / %s / %s]", getIdentifier(), getName(),
				getNationality(), getClub(), getBirthdate());
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

	public int getJersey_number() {
		return jersey_number;
	}

	public void setJersey_number(int jersey_number) {
		this.jersey_number = jersey_number;
	}

	public String getLeague() {
		return league;
	}

	public void setLeague(String league) {
		this.league = league;
	}

	public int getCurrent_market_value() {
		return current_market_value;
	}

	public void setCurrent_market_value(int current_market_value) {
		this.current_market_value = current_market_value;
	}

	public int getWage() {
		return wage;
	}

	public void setWage(int wage) {
		this.wage = wage;
	}

	public String getPreferred_foot() {
		return preferred_foot;
	}

	public void setPreferred_foot(String preferred_foot) {
		this.preferred_foot = preferred_foot;
	}

	public List<String> getPositions() {
		return positions;
	}

	public void setPositions(List<String> list) {
		this.positions = list;
	}
	
	
}
