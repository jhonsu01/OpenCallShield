# 🛡️ OpenCallShield

App Android open source (MIT) para **bloquear llamadas SPAM** mediante
`CallScreeningService` (Android 10+), con base de datos local Room, motor de
reglas de detección y sincronización con una base colaborativa pública en GitHub.

Alternativa privacidad-first y descentralizada a Truecaller.

## ✨ Funciones

- **Bloqueo automático** de llamadas vía `CallScreeningService` (rechazar o silenciar).
- **Motor de reglas:** contacto conocido → permitir; número reportado → bloquear;
  prefijo en lista negra → bloquear; número fuera de contactos → opcional.
- **Base local** Room con números reportados.
- **Sincronización** con un JSON público en GitHub (descarga diaria vía WorkManager + manual).
- **Reportes** locales del usuario e historial de llamadas filtradas.
- **UI** en Jetpack Compose (Material 3).

## 🏗️ Stack

Kotlin · Jetpack Compose · Room · WorkManager · MVVM · `CallScreeningService`

## 📦 Compilar

```bash
# Requiere JDK 17 y Android SDK (compileSdk 34)
./gradlew assembleDebug      # APK de depuración instalable
./gradlew assembleRelease    # APK de release (requiere firma)
```

El APK queda en `app/build/outputs/apk/`.

## 📲 Uso

1. Instala el APK.
2. Concede permisos de contactos / teléfono.
3. Pulsa **"Activar como app de filtrado de llamadas"** y acepta el rol en Android.
4. Ajusta las reglas y/o sincroniza la base colaborativa.

## 🌐 Formato de la base colaborativa

```json
{
  "version": "1.0",
  "updated_at": "2026-01-01",
  "numbers": [
    { "number": "+1234567890", "reports": 120, "tag": "scam" }
  ]
}
```

## 🔓 Licencia

MIT — ver [LICENSE](LICENSE).
