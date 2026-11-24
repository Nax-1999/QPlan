# QPlan

## 构建说明

本仓库的代码和构建配置位于根目录下的 `MyQPlan/` 子目录，不在仓库根目录；构建前请先进入该目录（使用 Android Studio 时也请直接打开 `MyQPlan/`）。

### 环境要求

- **JDK 11**：Android Gradle Plugin 7.0.0 需要 JDK 11 来运行构建（应用源码与字节码兼容级别为 Java 8，见 `MyQPlan/app/build.gradle`）。
- **Gradle 7.0.2**：项目自带 Gradle Wrapper（`MyQPlan/gradle/wrapper/gradle-wrapper.properties`），首次构建会自动下载对应版本，无需手动安装。
- **Android SDK**：`compileSdk`/`targetSdk` 为 31，`minSdk` 为 21。

### 构建

```bash
cd MyQPlan
./gradlew assembleDebug        # Windows: gradlew.bat assembleDebug
```

构建生成的 debug APK 位于 `MyQPlan/app/build/outputs/apk/debug/app-debug.apk`。
