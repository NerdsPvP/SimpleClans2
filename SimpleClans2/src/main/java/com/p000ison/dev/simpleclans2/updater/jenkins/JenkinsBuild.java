/*
 * This file is part of SimpleClans2 (2012).
 *
 *     SimpleClans2 is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     SimpleClans2 is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with SimpleClans2.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     Last modified: 03.11.12 17:32
 */

package com.p000ison.dev.simpleclans2.updater.jenkins;

import com.p000ison.dev.simpleclans2.api.logging.Logging;
import com.p000ison.dev.simpleclans2.updater.Build;
import com.p000ison.dev.simpleclans2.updater.UpdateType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

/**
 * Represents a JenkinsBuild
 */
public class JenkinsBuild implements Build {

    private static final String PROJECT_GITHUB_URL = "https://github.com/p000ison/SimpleClans2/commit/";
    private static final String JENKINS_HOST = "jenkins.greatmancode.com";
    private static final String API_FILE = "api/json";
    // --> http://
    private static final byte httpLength = 7;

    private int buildNumber;
    private List<JenkinsArtifact> artifacts;

    private String job;
    private UpdateType updateType;

    private long started, duration;

    private String pusher;
    private String commitId, comment;
    private Set<String> modifiedFiles = new HashSet<String>();
    private Set<String> createdFiles = new HashSet<String>();
    private Set<String> deletedFiles = new HashSet<String>();

    public JenkinsBuild(String job, UpdateType updateType, JenkinsArtifact... artifacts) {
        this.job = job;
        this.artifacts = Arrays.asList(artifacts);
        this.updateType = updateType;
    }

    @Override
    public void fetchInformation() throws IOException {
        URL buildAPIURL = new URL("http", JENKINS_HOST, 80, "/job/" + job + "/" + updateType + "/" + API_FILE);

        URLConnection connection = buildAPIURL.openConnection();
        Reader reader = new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8"));

        JSONObject content = parseJSON(reader);

        this.buildNumber = content.get("number").hashCode();
        this.started = (Long) content.get("timestamp");
        this.duration = (Long) content.get("duration");

        try {
            JSONArray artifactsInfo = (JSONArray) content.get("artifacts");
            if (artifactsInfo == null || artifactsInfo.isEmpty()) {
                return;
            }

            final String artifactURL = content.get("url").toString().substring(httpLength + JENKINS_HOST.length()) + "artifact/";

            for (Object rawArtifact : artifactsInfo) {
                JSONObject artifactInfo = (JSONObject) rawArtifact;
                String path = (String) artifactInfo.get("relativePath");

                for (JenkinsArtifact artifact : artifacts) {
                    if (path.contains(artifact.getName())) {
                        artifact.setURL(new URL("http", JENKINS_HOST, 80, artifactURL + path));
                    }
                }
            }

            JSONObject changes = (JSONObject) content.get("changeSet");
            JSONArray items = (JSONArray) changes.get("items");
            if (!items.isEmpty()) {
                JSONObject buildInfo = (JSONObject) items.get(0);

                this.commitId = buildInfo.get("commitId").toString();
                this.pusher = ((JSONObject) buildInfo.get("author")).get("fullName").toString();
                this.comment = buildInfo.get("msg").toString();

                for (Object element : (JSONArray) buildInfo.get("paths")) {
                    JSONObject entry = (JSONObject) element;

                    if (entry.get("editType").equals("edit")) {
                        modifiedFiles.add(entry.get("file").toString());
                    } else if (entry.get("editType").equals("delete")) {
                        deletedFiles.add(entry.get("file").toString());
                    } else if (entry.get("editType").equals("add")) {
                        createdFiles.add(entry.get("file").toString());
                    }
                }
            }
        } catch (ClassCastException e) {
            Logging.debug(e, true, "The format of the api changed! Could not fetch the cause!");
        }
    }

    private static JSONObject parseJSON(Reader reader) {
        Object parse = JSONValue.parse(reader);

        if (!(parse instanceof JSONObject)) {
            Logging.debug(Level.SEVERE, "Failed at reading the update info! Please contact the developers!");
        }

        return (JSONObject) parse;
    }

    @Override
    public List<JenkinsArtifact> getDownloadURLs() throws IOException {
        return artifacts;
    }

    @Override
    public UpdateType getUpdateType() {
        return updateType;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public long getStarted() {
        return started;
    }

    @Override
    public int getBuildNumber() {
        return buildNumber;
    }

    @Override
    public Set<String> getDeletedFiles() {
        return deletedFiles;
    }

    @Override
    public String getCommitURL() {
        return commitId == null ? "None" : PROJECT_GITHUB_URL + commitId;
    }

    @Override
    public String getComment() {
        return comment == null ? "None" : comment;
    }

    @Override
    public Set<String> getModifiedFiles() {
        return modifiedFiles;
    }

    @Override
    public Set<String> getCreatedFiles() {
        return createdFiles;
    }

    @Override
    public String getAuthor() {
        return pusher == null ? "None" : pusher;
    }

    @Override
    public String getDownloadLink() {
        return null;
    }
}
