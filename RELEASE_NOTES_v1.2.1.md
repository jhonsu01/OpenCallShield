# OpenCallShield v1.2.1

Corrección del build en CI.

## Correcciones

- 🛠️ **CI deja de fallar por memoria:** el workflow ahora asigna 4 GB de heap a Gradle
  en el runner (`org.gradle.jvmargs`), evitando el error
  *"Gradle build daemon has been stopped: since the JVM garbage collector is thrashing"*
  al compilar Compose + KSP. El `gradle.properties` del repo no cambia.

## Sin cambios funcionales

Mismas funciones que v1.2.0 (historial accionable, aporte vía Issue, login GitHub).
versionCode 5.
