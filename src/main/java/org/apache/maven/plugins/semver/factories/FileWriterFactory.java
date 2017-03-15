package org.apache.maven.plugins.semver.factories;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.semver.providers.VersionProvider;
import org.apache.maven.project.MavenProject;

import java.io.*;
import java.util.Map;

/**
 * @author sido
 */
public class FileWriterFactory {

    private FileWriterFactory() {}

    /**
     * <p>Create the release.properties file</p>
     *
     * @param LOG                @see {@link org.apache.maven.plugin.logging.Log}
     * @param project            @see {@link org.apache.maven.project.MavenProject}
     * @param finalVersions      map with development, release and scm version
     */
    public static void createReleaseProperties(Log LOG, MavenProject project, Map<VersionProvider.FINAL_VERSION, String> finalVersions) {
        String mavenProjectRelease = "project.rel." + project.getGroupId() + "\\\u003A" + project.getArtifactId() + "\u003D" + finalVersions.get(VersionProvider.FINAL_VERSION.RELEASE);
        String mavenProjectDevelopment = "project.dev." + project.getGroupId() + "\\\u003A" + project.getArtifactId() + "\u003D" + finalVersions.get(VersionProvider.FINAL_VERSION.DEVELOPMENT);
        String mavenProjectScm = "scm.tag=" + finalVersions.get(VersionProvider.FINAL_VERSION.SCM);

        try {
            File releaseProperties = new File("release.properties");
            if (releaseProperties.exists()) {
                LOG.info("Old release.properties removed    : " + releaseProperties.getAbsolutePath());
                boolean isDeleted = releaseProperties.delete();
                if (!isDeleted) {
                    LOG.error("File: release.properties.xml is not removed");
                }
            }
            FileWriter fileWriter = new FileWriter(releaseProperties);
            StringBuilder releaseText = new StringBuilder();
            releaseText.append(mavenProjectRelease);
            releaseText.append("\n");
            releaseText.append(mavenProjectDevelopment);
            releaseText.append("\n");
            releaseText.append(mavenProjectScm);

            LOG.info("New release.properties prepared   : " + releaseProperties.getAbsolutePath());

            writeFileToDisk(LOG, fileWriter, releaseText.toString());
            fileWriter.close();
        } catch (IOException err) {
            if(LOG != null) {
                LOG.error("semver-maven-plugin is terminating");
                LOG.error("Error when creating new release.properties", err);
            }
            Runtime.getRuntime().exit(1);
        }
    }

    /**
     * <p>Backup the old pom to make sure when the build fails it can be set back.</p>
     *
     * @param LOG      @see {@link org.apache.maven.plugin.logging.Log}
     * @param project  @see {@link org.apache.maven.project.MavenProject}
     */
    public static void backupSemverPom(Log LOG, MavenProject project) {
        try {
            File pomXmlSemverBackup = new File("pom.xml.semverBackup");
            if (pomXmlSemverBackup.exists()) {
                if(LOG != null) {
                    LOG.info("Old semver backup pom.xml removed    : " + pomXmlSemverBackup.getAbsolutePath());
                }
                boolean isDeleted = pomXmlSemverBackup.delete();
                if (!isDeleted) {
                    if(LOG != null) {
                        LOG.error("File: semver backup pom.xml is not removed");
                    }
                }
            }
            FileWriter fileWriter = new FileWriter(pomXmlSemverBackup);

            if(LOG != null) {
                LOG.info("New semver backup pom.xml prepared   : " + pomXmlSemverBackup.getAbsolutePath());
            }

            writeFileToDisk(LOG, fileWriter, project.toString());
            fileWriter.close();

        } catch (IOException err) {
            if(LOG != null) {
                LOG.error("semver-maven-plugin is terminating");
                LOG.error("Error when creating new semver backup pom.xml", err);
            }
            Runtime.getRuntime().exit(1);
        }
    }

    /**
     * <p>Write actual file to disk</p>
     *
     * @param LOG         @see {@link org.apache.maven.plugin.logging.Log}
     * @param fileWriter  the fileWriter for release.properties
     * @param releaseText the full content for the release.properties
     */
    private static void writeFileToDisk(Log LOG, FileWriter fileWriter, String releaseText) {
        try {
            Writer output = new BufferedWriter(fileWriter);
            output.append(releaseText);
            output.close();
        } catch (IOException err) {
            if(LOG != null) {
                LOG.error("semver-maven-plugin is terminating");
                LOG.error("Error when creating new release.properties", err);
            }
            Runtime.getRuntime().exit(1);
        }
    }

}