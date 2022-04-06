package io.github.perpk.buildx.build;

import io.github.perpk.buildx.cli.CommandLineWrapper;
import io.github.perpk.buildx.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.gradle.api.GradleException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ImageBuilder {
  private final List<String> targetPlatforms;
  private final File dockerfilePath;
  private final String imageTag;
  private final boolean pushImage;
  private final Map<String, String> buildArgs;

  private static final String TAR_FILENAME = "out.tar";

  public ImageBuilder(
      List<String> targetPlatforms,
      File dockerfilePath,
      String imageTag,
      boolean pushImage,
      Map<String, String> buildArgs) {
    this.targetPlatforms = targetPlatforms;
    this.dockerfilePath = dockerfilePath;
    this.imageTag = imageTag;
    this.pushImage = pushImage;
    this.buildArgs = buildArgs;
  }

  public void buildImage() {
    try {
      String dockerBuildCommand = createDockerBuildCommand();

      CommandLineWrapper.executeCommand(dockerfilePath.getParent(), "sh", "-c", dockerBuildCommand);

      if (!pushImage) {
        registerImageLocally();
      }
    } catch (IOException | InterruptedException e) {
      throw new GradleException("Build via buildx failed", e);
    }
  }

  public String createDockerBuildCommand() {
    StringBuilder sb = new StringBuilder("docker buildx build");
    if (targetPlatforms != null && !targetPlatforms.isEmpty()) {
      sb.append(" --platform=").append(concatTargetPlatforms());
    }
    if (buildArgs != null && !buildArgs.isEmpty()) {
      sb.append(" ").append(concatBuildArgs());
    }
    sb.append(" -t ")
        .append(imageTag)
        .append(" ")
        .append(Utils.pushOrKeepLocally(pushImage, TAR_FILENAME))
        .append(" .");

    return sb.toString();
  }

  private String concatBuildArgs() {
    return buildArgs.entrySet().stream()
        .map(e -> String.format("--build-arg %s=%s", e.getKey(), e.getValue()))
        .collect(Collectors.joining(" "));
  }

  private String concatTargetPlatforms() {
    return StringUtils.join(targetPlatforms, ",");
  }

  public void registerImageLocally() {
    try {
      CommandLineWrapper.executeCommand(
          dockerfilePath.getParent(),
          "sh",
          "-c",
          String.format(
              "cat %s | docker import - %s",
              Utils.prependTmpDirToFilename(TAR_FILENAME), imageTag));
    } catch (IOException | InterruptedException e) {
      throw new GradleException("Image import to local registry failed", e);
    }
  }
}
