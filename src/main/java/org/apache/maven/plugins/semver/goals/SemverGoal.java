package org.apache.maven.plugins.semver.goals;

/**
 *
 * <h1>SemverGoal</h1>
 *
 * <p></p>
 *
 * @author sido
 */
public class SemverGoal {

    public enum SEMVER_GOAL {
        MAJOR("MAJOR"),
        MINOR("MINOR"),
        PATCH("PATCH"),
        ROLLBACK("ROLLBACK");

        private String description = "";

        SEMVER_GOAL(String description) {
            this.description = description;
        }

        public String getDescription() {
            return this.description;
        }
    }

    private SemverGoal() {}


}
