# How to Install

## Table of Contents
- [How to Install](#how-to-install)
  * [Intellij](#Intellij)
  * [Eclipse](#eclipse)
  * [Maven](#maven)
  * [Gradle](#gradle)

## Intellij

Go to [releases](https://github.com/Krassnig/CodeDraw/releases) and download the newest CodeDraw.jar.

Open Intellij with the project where you would like to add CodeDraw. Click on **File > Project Structure...**.
Under **Project Settings**, select **Libraries**.
At the top left, click on the small **plus icon** and select the **Java** option.
Go to the downloaded CodeDraw.jar, and select it and then press **OK**.
Now you can import CodeDraw with `import codedraw.*;` at the top of your Java files.

## Eclipse

Go to [releases](https://github.com/Krassnig/CodeDraw/releases) and download the newest CodeDraw.jar.

Open Eclipse with the project where you would like to add CodeDraw. Right-click on your Project > **Properties > Java Build Path > Add External JARs...**.
Go to the downloaded CodeDraw.jar, and select it and then press **OK**.
Now you can import CodeDraw with `import codedraw.*;` at the top of your Java files.

## Maven

To use CodeDraw as a Maven dependency, add the following lines to your `pom.xml`.

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

```xml
<dependency>
    <groupId>com.github.Krassnig</groupId>
    <artifactId>CodeDraw</artifactId>
    <version>4.0.2</version>
</dependency>
```

After adding CodeDraw as a dependency, your `pom.xml` might look like the example below.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project>

   <modelVersion>4.0.0</modelVersion>
   <groupId>com.mycompany.app</groupId>
   <artifactId>my-app</artifactId>
   <version>1</version>

   <repositories>
      <repository>
         <id>jitpack.io</id>
         <url>https://jitpack.io</url>
      </repository>
   </repositories>

   <dependencies>
      <dependency>
         <groupId>com.github.Krassnig</groupId>
         <artifactId>CodeDraw</artifactId>
         <version>4.0.2</version>
      </dependency>
   </dependencies>

</project>
```

For guidance on configuring a `pom.xml`, please refer to the
[official Maven documentation](https://maven.apache.org/guides/introduction/introduction-to-the-pom.html). 


## Gradle

To use CodeDraw as a Gradle dependency, add the following lines to your `build.gradle`.

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

```groovy
dependencies {
    implementation 'com.github.Krassnig:CodeDraw:4.0.2'
}
```

After adding CodeDraw as a dependency, your `build.gradle` might look like the example below.

```groovy
plugins {
    id 'application'
}

repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.Krassnig:CodeDraw:4.0.2'
}

application {
    mainClass = 'demo.App' //Enter path to your Main class
}
```

For guidance on configuring a `build.gradle`, please refer to the
[official Gradle documentation](https://docs.gradle.org/current/samples/sample_building_java_applications.html#review_the_project_files).