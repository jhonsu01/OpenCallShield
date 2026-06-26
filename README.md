# 🛡️ OpenCallShield

[![Android CI](https://github.com/jhonsu01/OpenCallShield/actions/workflows/android.yml/badge.svg)](https://github.com/jhonsu01/OpenCallShield/actions/workflows/android.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

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

Kotlin · Jetpack Compose (Material 3) · Room · WorkManager · MVVM · `CallScreeningService`

## 📦 Compilar

```bash
# Requiere JDK 17 y Android SDK (compileSdk 34)
./gradlew assembleDebug      # APK de depuración instalable
./gradlew assembleRelease    # APK de release (requiere firma)
```

El APK queda en `app/build/outputs/apk/`. La CI (GitHub Actions) también lo compila
en cada push y lo adjunta como artefacto; al crear un tag `vX.Y.Z` publica un Release
con el APK.

## 📲 Uso

1. Instala el APK.
2. Concede permisos de contactos / teléfono.
3. Pulsa **"Activar como app de filtrado de llamadas"** y acepta el rol en Android.
4. Ajusta las reglas y/o sincroniza la base colaborativa.

## 🌐 Base colaborativa

La app sincroniza desde [`spam_numbers.json`](spam_numbers.json) de este mismo repo:

```
https://raw.githubusercontent.com/jhonsu01/OpenCallShield/main/spam_numbers.json
```

Formato:

```json
{
  "version": "1.0",
  "updated_at": "2026-01-01",
  "numbers": [
    { "number": "+1234567890", "reports": 120, "tag": "scam" }
  ]
}
```

Para ampliar la base, edita `spam_numbers.json` (o acepta aportes de la comunidad).
La URL es configurable desde la propia app.

## 🔑 Aportar desde la app (login con GitHub)

Desde la pestaña **Cuenta** el usuario puede iniciar sesión con GitHub y, desde
**Lista SPAM**, pulsar **"Aportar a la base pública"**: la app abre un **Issue** en el
repo con los números reportados en formato JSON, listos para integrar en `spam_numbers.json`.
Cualquiera que clone o haga fork puede aportar a su propio repo cambiando el destino.

Dos formas de iniciar sesión (sin servidor/backend):

### Opción A — OAuth Device Flow (recomendado)

1. En GitHub: **Settings → Developer settings → OAuth Apps → New OAuth App**.
   - *Application name:* OpenCallShield
   - *Homepage URL:* `https://github.com/jhonsu01/OpenCallShield`
   - *Authorization callback URL:* `https://github.com/jhonsu01/OpenCallShield` (no se usa en Device Flow)
2. Tras crearla, en la página de la app marca **Enable Device Flow** y guarda.
3. Copia el **Client ID** (es público, no es secreto).
4. En la app → pestaña **Cuenta** → pega el Client ID y pulsa **Iniciar sesión con GitHub**.
   Sigue el código en `github.com/login/device`.

> El Client ID puede fijarse por defecto en `SettingsStore.DEFAULT_CLIENT_ID`
> (`app/src/main/java/com/opencallshield/data/SettingsStore.kt`) para que venga precargado.

### Opción B — Token personal (PAT)

1. GitHub → **Settings → Developer settings → Personal access tokens** → crea uno con
   scope **`public_repo`**.
2. En la app → pestaña **Cuenta** → pégalo en *"Entrar con token"*.

El token se guarda **cifrado** en el dispositivo (`EncryptedSharedPreferences`); nunca
se sube a ningún sitio salvo a la propia API de GitHub.

## 🛠️ Aprobar aportes (mantenedor)

Los aportes llegan como **Issues** con un bloque JSON. Para integrarlos a la base con
**un clic**, el repo incluye la Action [`integrate-spam.yml`](.github/workflows/integrate-spam.yml):

1. Crea una etiqueta llamada **`aprobado`** en el repo (una sola vez):
   *Issues → Labels → New label → `aprobado`*.
2. Revisa el Issue de aporte y, si los números son correctos, **aplícale la etiqueta `aprobado`**.
3. La Action lee el JSON, lo **fusiona en `spam_numbers.json`** (sin duplicados, sumando
   reportes), hace commit en `main` y **cierra el Issue** con un comentario de confirmación.

Si prefieres hacerlo a mano: copia el bloque JSON del Issue, pégalo dentro de `"numbers"`
en `spam_numbers.json`, actualiza `"updated_at"` y haz commit con `Closes #N`.

## 🔓 Licencia

MIT — ver [LICENSE](LICENSE).
