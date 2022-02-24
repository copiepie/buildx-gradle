package co.pie.pie.util

import org.apache.commons.lang3.StringUtils

import java.nio.charset.StandardCharsets

class DockerCommandUtil {
    static final ProcessBuilder processBuilder = new ProcessBuilder()

    static void removeImage(String tag) {
        if (imageExists(tag)) {
            executeCommand('sh', '-c', "docker image rm $tag")
        }
    }

    static String getImageSize(String tag) {
        Process process = executeCommand('sh', '-c', "docker image ls ${tag} --format {{.Size}}")
        return getProcessOutput(process)
    }

    static boolean imageExists(String tag) {
        Process process = executeCommand('sh', '-c', "docker image ls $tag | wc -l")
        int lines = Integer.valueOf(getProcessOutput(process))
        lines > 1
    }

    private static String getProcessOutput(Process process) {
        String procOutput = new String(process.inputStream.readAllBytes(), StandardCharsets.UTF_8)
        if (StringUtils.isBlank(procOutput)) {
            procOutput = new String(process.errorStream.readAllBytes(), StandardCharsets.UTF_8)
        }
        return procOutput.trim()
    }

    private static Process executeCommand(String... commandVector) {
        processBuilder.command(commandVector)
        Process process = processBuilder.start()
        process.waitFor()
        return process
    }
}
