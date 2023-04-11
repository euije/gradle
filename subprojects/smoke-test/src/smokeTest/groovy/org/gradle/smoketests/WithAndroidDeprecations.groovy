/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.smoketests

import groovy.transform.SelfType
import org.gradle.util.GradleVersion
import org.gradle.util.internal.VersionNumber

@SelfType(BaseDeprecations)
trait WithAndroidDeprecations implements WithReportDeprecations {
    private static final VersionNumber AGP_VERSION_WITH_FIXED_NEW_WORKERS_API = VersionNumber.parse('4.2')
    private static final VersionNumber AGP_VERSION_WITHOUT_CONVENTION_USAGES = VersionNumber.parse('7.4')
    private static final VersionNumber AGP_VERSION_WITHOUT_CONFIG_UTIL = VersionNumber.parse('8.0.0-rc01')

    boolean androidPluginUsesOldWorkerApi(String agpVersion) {
        versionIsLower(agpVersion, AGP_VERSION_WITH_FIXED_NEW_WORKERS_API)
    }

    boolean androidPluginUsesConventions(String agpVersion) {
        versionIsLower(agpVersion, AGP_VERSION_WITHOUT_CONVENTION_USAGES)
    }

    private boolean versionIsLower(String version, VersionNumber threshold) {
        VersionNumber versionNumber = VersionNumber.parse(version)
        versionNumber < threshold
    }

    void expectAndroidWorkerExecutionSubmitDeprecationWarning(String agpVersion) {
        runner.expectLegacyDeprecationWarningIf(androidPluginUsesOldWorkerApi(agpVersion), WORKER_SUBMIT_DEPRECATION)
    }

    void expectProjectConventionDeprecationWarning(String agpVersion) {
        runner.expectLegacyDeprecationWarningIf(androidPluginUsesConventions(agpVersion), PROJECT_CONVENTION_DEPRECATION)
    }

    void expectAndroidConventionTypeDeprecationWarning(String agpVersion) {
        runner.expectLegacyDeprecationWarningIf(androidPluginUsesConventions(agpVersion), CONVENTION_TYPE_DEPRECATION)
    }

    void expectConfigUtilDeprecationWarning(String agpVersion) {
        runner.expectLegacyDeprecationWarningIf(
            versionIsLower(agpVersion, AGP_VERSION_WITHOUT_CONFIG_UTIL),
            "The org.gradle.util.ConfigureUtil type has been deprecated. " +
                "This is scheduled to be removed in Gradle 9.0. " +
                "Consult the upgrading guide for further information: " +
                "https://docs.gradle.org/${GradleVersion.current().version}/userguide/upgrading_version_8.html#org_gradle_util_reports_deprecations",
        )
    }
}
