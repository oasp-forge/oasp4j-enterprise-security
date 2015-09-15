package org.sixgems.model.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Julian on 19.07.2015.
 */
public interface SsoAccessToken {

    String getTokenId();

    String getUsername(String propName);

    Collection<String> getUserGroups();

    Map<String, Object> getUserAttributes(List<String> propNames);

}
