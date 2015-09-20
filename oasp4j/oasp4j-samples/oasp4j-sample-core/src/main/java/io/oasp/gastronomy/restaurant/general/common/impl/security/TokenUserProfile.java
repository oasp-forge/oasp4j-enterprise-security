package io.oasp.gastronomy.restaurant.general.common.impl.security;

import java.util.Collection;

import org.neo4j.cypher.InvalidArgumentException;

import io.oasp.gastronomy.restaurant.general.common.api.UserProfile;
import io.oasp.gastronomy.restaurant.general.common.api.datatype.Role;
import io.oasp.gastronomy.restaurant.general.common.api.security.UserData;

public class TokenUserProfile implements UserProfile{
	
	private Long userId = 1L;
	private String name;
	private String firstName;
	private String lastName;
	private Role role;
	
	public TokenUserProfile (String name, String firstName, String lastName, Collection<String> groups){
		this.name = name;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = parseRole(groups);
		if (this.role == null)
			new IllegalArgumentException("no valide role found");
		
	}

	@Override
	public Long getId() {
		return this.userId;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getFirstName() {
		return this.firstName;
	}

	@Override
	public String getLastName() {
		return this.lastName;
	}

	@Override
	public Role getRole() {
		return this.role;
	}
	
	private Role parseRole(Collection<String> groups){
		for(String group: groups){
			for (Role role: Role.values()){
				if (group.equalsIgnoreCase(role.getName()))
					return role;
			}
		}
		return null;
	}

	
	
}
