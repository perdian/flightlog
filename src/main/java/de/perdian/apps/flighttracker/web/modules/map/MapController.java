package de.perdian.apps.flighttracker.web.modules.map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.perdian.apps.flighttracker.business.modules.map.MapQuery;
import de.perdian.apps.flighttracker.business.modules.map.MapService;
import de.perdian.apps.flighttracker.business.modules.map.model.MapModel;
import de.perdian.apps.flighttracker.web.security.FlighttrackerUser;

@Controller
public class MapController {

    private MapService mapService = null;

    @RequestMapping(value = "/map/data", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public MapModel mapData(@AuthenticationPrincipal FlighttrackerUser user, @RequestParam(name = "year", required = false) Integer year) {
        MapQuery mapQuery = new MapQuery();
        mapQuery.setRestrictUsers(user == null ? null : user.toUserEntities());
        mapQuery.setYear(year);
        return this.getMapService().loadMap(mapQuery);
    }

    MapService getMapService() {
        return this.mapService;
    }
    @Autowired
    void setMapService(MapService mapService) {
        this.mapService = mapService;
    }

}
