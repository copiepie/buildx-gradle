package co.pie.pie;

import co.pie.pie.build.ImageBuilder;
import org.apache.commons.lang3.StringUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import java.util.List;

public abstract class BuildxImageTask extends DefaultTask {
  @Input Property<String> imageTag;

  @Input
  RegularFileProperty dockerfilePath;

  @Input Property<Boolean> pushImageToRemote;

  @Input
  ListProperty<String> targetPlatforms;

  @TaskAction
  public void buildxImage() {
    ImageBuilder imageBuilder =
        new ImageBuilder(
            StringUtils.join(targetPlatforms.get(), ","),
            dockerfilePath.get().getAsFile(),
            imageTag.get(),
            pushImageToRemote.get());
    imageBuilder.buildImage();
  }
}
