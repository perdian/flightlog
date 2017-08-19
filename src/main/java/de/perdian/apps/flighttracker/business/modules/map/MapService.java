package de.perdian.apps.flighttracker.business.modules.map;

import de.perdian.apps.flighttracker.business.modules.map.model.MapModel;

public interface MapService {

    MapModel loadMap(MapQuery query);

}
