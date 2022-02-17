package co.pie.pie.build;

import co.pie.pie.cli.CommandLineWrapper;
import co.pie.pie.util.Utils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

public class ImageBuilder {
  private final String targetPlatforms;
  private final File dockerfilePath;
  private final String imageTag;
  private final boolean pushImage;

  public ImageBuilder(
      String targetPlatforms, File dockerfilePath, String imageTag, boolean pushImage) {
    this.targetPlatforms = targetPlatforms;
    this.dockerfilePath = dockerfilePath;
    this.imageTag = imageTag;
    this.pushImage = pushImage;
  }

  public void buildImage() {
    try {
      StringBuilder sb = new StringBuilder("docker buildx build");
      if (StringUtils.isNotBlank(targetPlatforms)) {
        sb.append(" --platform=").append(targetPlatforms);
      }
      sb.append(" -t ")
          .append(imageTag)
          .append(" ")
          .append(Utils.pushOrKeepLocally(pushImage))
          .append(" .");

      CommandLineWrapper.executeCommand(
          dockerfilePath.getParent(), "sh", "-c", sb.toString());

      if (!pushImage) {
        registerImageLocally();
      }
    } catch (IOException | InterruptedException e) {
      throw new IllegalStateException("Build via buildx failed", e);
    }
  }

  public void registerImageLocally() {
    try {
      CommandLineWrapper.executeCommand(
          dockerfilePath.getParent(),
          "sh",
          "-c",
          String.format("cat out.tar | docker import - %s", imageTag));
    } catch (IOException | InterruptedException e) {
      throw new IllegalStateException("Image import to local registry failed", e);
    }
  }
}
