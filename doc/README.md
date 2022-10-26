[![Build](https://github.com/promcteam/proskillapiparties/actions/workflows/publish.yml/badge.svg?branch=master)](https://s01.oss.sonatype.org/content/repositories/snapshots/com/promcteam/proskillapiparties/${project.version})

# ${project.name}

If you wish to use ${project.name} as a dependency in your projects, ${project.name} is available through Maven Central
or snapshots through Sonatype.

```xml
<repository>
    <id>sonatype</id>
    <url>https://s01.oss.sonatype.org/content/repositories/snapshots</url>
</repository>
...
<dependency>
    <groupId>${project.groupId}</groupId>
    <artifactId>${project.artifactId}</artifactId>
    <version>${project.version}</version>
</dependency>
```