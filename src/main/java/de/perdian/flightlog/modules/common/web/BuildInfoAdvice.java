package de.perdian.flightlog.modules.common.web;

import de.perdian.flightlog.modules.common.BuildInfo;
import de.perdian.flightlog.modules.common.service.BuildInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class BuildInfoAdvice {

    private BuildInfoService buildInfoService = null;

    @ModelAttribute("buildInfo")
    public BuildInfo buildInfo() {
        return this.getBuildInfoService().getBuildInfo();
    }

    BuildInfoService getBuildInfoService() {
        return this.buildInfoService;
    }
    @Autowired
    void setBuildInfoService(BuildInfoService buildInfoService) {
        this.buildInfoService = buildInfoService;
    }

}
