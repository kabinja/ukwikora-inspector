package lu.uni.serval.ikora.inspector.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Gitlab {
    private String url;
    private String token;
    private String group;
    @JsonProperty("default branch")
    private String defaultBranch;
    @JsonProperty("branch exceptions")
    private Map<String, String> branchExceptions;
    @JsonProperty("local folder")
    private String localFolder;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDefaultBranch() {
        return defaultBranch;
    }

    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    public Map<String, String> getBranchExceptions() {
        return branchExceptions;
    }

    public void setBranchExceptions(Map<String, String> branchExceptions) {
        this.branchExceptions = branchExceptions;
    }

    public String getLocalFolder() {
        return localFolder;
    }

    public void setLocalFolder(String localFolder) {
        this.localFolder = localFolder;
    }
}
