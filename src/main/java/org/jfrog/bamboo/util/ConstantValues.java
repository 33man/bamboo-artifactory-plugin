/*
 * Copyright (C) 2010 JFrog Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jfrog.bamboo.util;

import java.io.Serializable;

/**
 * @author Noam Y. Tenne
 */
public interface ConstantValues extends Serializable {

    public static final String BUILD_RESULT_COLLECTION_ACTIVATED_PARAM = "org.jfrog.bamboo.buildInfo.activated";
    public static final String BUILD_RESULT_RELEASE_ACTIVATED_PARAM = "org.jfrog.bamboo.release.activated";
    public static final String BUILD_RESULT_SELECTED_SERVER_PARAM = "org.jfrog.bamboo.buildInfo.serverUrl";

    public static final String BUILD_SERVLET_CONTEXT_NAME = "artifactoryBuildServlet";
    public static final String BUILD_SERVLET_KEY_PARAM = "buildKey";

    public static final String ADMIN_CONFIG_SERVLET_CONTEXT_NAME = "artifactoryAdminConfigServlet";

    public static final String ARTIFACTORY_SERVER_CONFIG_MODULE_KEY = "serverConfigManager";

    public static final String PLAN_KEY_PARAM = "planKey";
}
