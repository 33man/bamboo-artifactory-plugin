package org.jfrog.bamboo.configuration;

import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.TaskDefinition;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jfrog.bamboo.admin.ServerConfig;
import org.jfrog.bamboo.context.IvyBuildContext;

import java.util.Map;
import java.util.Set;

/**
 * Configuration for {@link org.jfrog.bamboo.task.ivy.ArtifactoryIvyTask}
 *
 * @author Tomer Cohen
 */
public class ArtifactoryIvyConfiguration extends AbstractArtifactoryConfiguration {
    private static final Logger log = Logger.getLogger(ArtifactoryIvyConfiguration.class);
    protected static final String DEFAULT_TEST_REPORTS_XML = "**/test-reports/*.xml";
    private static final Set<String> FIELDS_TO_COPY = IvyBuildContext.getFieldsToCopy();

    @Override
    protected String getKey() {
        return "artifactoryIvyBuilder";
    }

    @Override
    protected String getDeployableRepoKey() {
        return IvyBuildContext.DEPLOYABLE_REPO_KEY;
    }

    @Override
    protected String getDefaultTestDirectory() {
        return DEFAULT_TEST_REPORTS_XML;
    }

    @Override
    public void populateContextForCreate(@NotNull Map<String, Object> context) {
        super.populateContextForCreate(context);
        context.put("artifactoryIvyTask", this);
        context.put("builderType", this);
        context.put("builder", this);
        //context.put("adminConfig", administrationConfiguration);
        context.put("baseUrl", administrationConfiguration.getBaseUrl());
        context.put("build", context.get("plan"));
        context.put("dummyList", Lists.newArrayList());
        context.put("serverConfigManager", serverConfigManager);
        context.put("selectedServerId", -1);
        context.put("selectedRepoKey", "");
    }

    @Override
    public void populateContextForEdit(@NotNull Map<String, Object> context, @NotNull TaskDefinition taskDefinition) {
        super.populateContextForEdit(context, taskDefinition);
        taskConfiguratorHelper.populateContextWithConfiguration(context, taskDefinition, FIELDS_TO_COPY);
        String publishingKey = IvyBuildContext.PREFIX + IvyBuildContext.DEPLOYABLE_REPO_KEY;
        String selectedPublishingRepoKey = context.get(publishingKey) != null ? context.get(publishingKey).toString() :
                IvyBuildContext.NO_PUBLISHING_REPO_KEY_CONFIGURED;
        context.put("selectedRepoKey", selectedPublishingRepoKey);
        context.put("selectedServerId", context.get(IvyBuildContext.PREFIX + IvyBuildContext.SERVER_ID_PARAM));
        context.put("serverConfigManager", serverConfigManager);
    }

    @Override
    public void populateContextForView(@NotNull Map<String, Object> context, @NotNull TaskDefinition taskDefinition) {
        super.populateContextForView(context, taskDefinition);
        taskConfiguratorHelper.populateContextWithConfiguration(context, taskDefinition, FIELDS_TO_COPY);
        context.put("serverConfigManager", serverConfigManager);
        IvyBuildContext buildContext = IvyBuildContext.createContextFromMap(context);
        long serverId = buildContext.getArtifactoryServerId();
        context.put("selectedServerId", serverId);
        ServerConfig serverConfig = serverConfigManager.getServerConfigById(serverId);
        context.put("selectedServerUrl", serverConfig.getUrl());
        context.put("isUseM2CompatiblePatterns", buildContext.isMaven2Compatible());
        context.put("isRunLicenseChecks", buildContext.isRunLicenseChecks());
        context.put("isPublishArtifacts", buildContext.isPublishArtifacts());
        context.put("hasTests", buildContext.isTestChecked());
    }

    @NotNull
    @Override
    public Map<String, String> generateTaskConfigMap(@NotNull ActionParametersMap params,
            @Nullable TaskDefinition previousTaskDefinition) {
        Map<String, String> configMap = super.generateTaskConfigMap(params, previousTaskDefinition);
        taskConfiguratorHelper.populateTaskConfigMapWithActionParameters(configMap, params, FIELDS_TO_COPY);
        IvyBuildContext buildContext = new IvyBuildContext(configMap);
        resetConfigIfNeeded(buildContext);
        configMap.put(IvyBuildContext.PREFIX + IvyBuildContext.TEST_RESULT_DIRECTORY, getTestDirectory(buildContext));
        return configMap;
    }

    public boolean taskProducesTestResults(@NotNull TaskDefinition definition) {
        return new IvyBuildContext(definition.getConfiguration()).isTestChecked();
    }
}
