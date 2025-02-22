package de.perdian.flightlog.modules.common.service;

import de.perdian.flightlog.modules.common.BuildInfo;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Properties;

@Component
public class BuildInfoService {

    private static final Logger log = LoggerFactory.getLogger(BuildInfoService.class);

    private ResourceLoader resourceLoader = null;
    private BuildInfo buildInfo = null;

    @PostConstruct
    void loadBuildInfo() {
        BuildInfo buildInfo = new BuildInfo();
        Resource buildPropertiesResource = this.getResourceLoader().getResource("classpath:/build.properties");
        if (!buildPropertiesResource.exists()) {
            log.debug("Cannot find build.properties file on classpath. Skipping build information");
        } else {
            try (InputStream buildPropertiesStream = buildPropertiesResource.getInputStream()) {
                Properties buildProperties = new Properties();
                buildProperties.load(buildPropertiesStream);
                buildInfo.setVersion(buildProperties.getProperty("version"));
                buildInfo.setRevision(buildProperties.getProperty("revision"));
            } catch (Exception e) {
                log.warn("Cannot load build.properties file from classpath", e);
            }
        }
        this.setBuildInfo(buildInfo);
    }

    ResourceLoader getResourceLoader() {
        return this.resourceLoader;
    }
    @Autowired
    void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public BuildInfo getBuildInfo() {
        return this.buildInfo;
    }
    private void setBuildInfo(BuildInfo buildInfo) {
        this.buildInfo = buildInfo;
    }

}
