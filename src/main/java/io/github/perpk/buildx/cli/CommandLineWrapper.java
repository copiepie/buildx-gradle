package io.github.perpk.buildx.cli;

import org.apache.commons.lang3.StringUtils;
import org.gradle.api.GradleException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class CommandLineWrapper {
  public static void executeCommand(String workingDir, String... commandVector)
      throws IOException, InterruptedException {
    ProcessBuilder processBuilder = new ProcessBuilder();
    processBuilder.directory(new File(workingDir));
    processBuilder.command(commandVector);
    Process process = processBuilder.start();
    process.waitFor();
    printResults(process);
  }

  private static void printResults(Process process) throws IOException {
    String procOutput = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    if (StringUtils.isBlank(procOutput)) {
      procOutput = new String(process.getErrorStream().readAllBytes(), StandardCharsets.UTF_8);
      System.out.println(procOutput);
      if (process.exitValue() != 0) {
        throw new GradleException("Command execution failed");
      }
    }
    System.out.println(procOutput);
  }
}
