package de.perdian.flightlog.modules.common;

import org.apache.commons.lang3.StringUtils;

public class BuildInfo {

    private String version = null;
    private String revision = null;

    @Override
    public String toString() {
        if (StringUtils.isEmpty(this.getVersion()) && StringUtils.isEmpty(this.getRevision())) {
            return "Unknown version";
        } else {
            StringBuilder resultString = new StringBuilder();
            resultString.append(StringUtils.defaultString(this.getVersion(), "UNKNOWN"));
            if (StringUtils.isNotEmpty(this.getRevision())) {
                resultString.append(" (").append(this.getRevision()).append(")");
            }
            return resultString.toString();
        }
    }

    public String getVersion() {
        return this.version;
    }
    public void setVersion(String version) {
        this.version = version;
    }

    public String getRevision() {
        return this.revision;
    }
    public void setRevision(String revision) {
        this.revision = revision;
    }

}
