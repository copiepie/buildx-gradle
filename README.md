# A Gradle plugin for buildx

## A few words
This plugin just invokes shell commands to run a build. Having buildx available on your system  
is a prerequisite.  
Also, if you'd like to push your build to a remote registry, you'd have to perform the login before  
you conduct the build itself, since this plugin doesn't support docker login atm.  
Since build doesn't register images to a local registry, this plugin uses a workaround to enable images
to appear in the local registry. This is achieved by exporting the image to a tarball and importing it via
``` bash 
cat out.tar | docker import
```
## Restrictions
Since it currently can't be tested on Windows this plugin does not support it as well. The only supported platforms currently are Linux and MacOS.

## How to conf
```groovy
buildxImage {
    imageTag = 'tag/image:version'
    dockerfilePath = file('Dockerfile')
    pushImageToRemote = false/true
    targetPlatforms = ['linux/arm64', 'linux/arm64']
    buildArgs = [arg1: 'onearg', arg2: 'twoarg']
}
```
