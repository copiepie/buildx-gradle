package co.pie.pie;

import co.pie.pie.build.ImageBuilder;
import org.apache.commons.lang3.StringUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.util.List;

public abstract class BuildxImageTask extends DefaultTask {
  @Input String imageTag;

  @InputFile
  File dockerfilePath;

  @Input Boolean pushImageToRemote;

  @Input
  List<String> targetPlatforms;

  @Input
  @Optional
  String buildArgs;

  @TaskAction
  public void buildxImage() {
    ImageBuilder imageBuilder =
        new ImageBuilder(
            StringUtils.join(targetPlatforms, ","),
            dockerfilePath,
            imageTag,
            pushImageToRemote,
            buildArgs);
    imageBuilder.buildImage();
  }

  public String getImageTag() {
    return imageTag;
  }

  public File getDockerfilePath() {
    return dockerfilePath;
  }

  public Boolean getPushImageToRemote() {
    return pushImageToRemote;
  }

  public List<String> getTargetPlatforms() {
    return targetPlatforms;
  }

  public String getBuildArgs() { return buildArgs; }
}
