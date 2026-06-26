# OpenCallShield v1.0.0

Primera versión pública (MVP) de OpenCallShield, app Android open source (MIT)
para bloqueo de llamadas SPAM.

## Novedades

- 🛡️ Bloqueo de llamadas vía `CallScreeningService` (Android 10+): rechazar o silenciar.
- 🧠 Motor de reglas: contacto conocido → permitir; número reportado o prefijo en
  lista negra → bloquear; número fuera de contactos → opcional.
- 🗃️ Base local Room (números reportados + historial de llamadas filtradas).
- 🌐 Sincronización con base colaborativa pública en GitHub (manual + diaria con WorkManager).
- 🎨 UI en Jetpack Compose (Material 3) con pestañas Protección / Lista SPAM / Historial.

## Instalación

1. Descarga `OpenCallShield-v1.0.0-debug.apk` (más abajo en *Assets*).
2. En el teléfono, permite instalar de "fuentes desconocidas".
3. Instala, concede permisos de contactos/teléfono y pulsa
   **"Activar como app de filtrado de llamadas"**.

## Detalles técnicos

- `applicationId`: `io.github.jhonsu01.opencallshield`
- minSdk 29 (Android 10) · targetSdk 34
- APK firmado con **clave de depuración** (instalable; no apto para Play Store sin
  firma de release).

## Limitaciones conocidas

- El bloqueo real solo se prueba en un dispositivo físico (un emulador no recibe llamadas).
- La base colaborativa arranca con números de ejemplo ficticios; se poblará con reportes/PRs.
