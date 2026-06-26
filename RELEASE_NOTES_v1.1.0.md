# OpenCallShield v1.1.0

Aportes colaborativos: inicia sesión con GitHub y alimenta la base pública de SPAM
directamente desde la app.

## Novedades

- 🔐 **Inicio de sesión con GitHub** sin servidor:
  - **OAuth Device Flow** (recomendado): introduce un código en `github.com/login/device`.
  - **Token personal (PAT)** como alternativa.
  - El token se guarda **cifrado** en el dispositivo (`EncryptedSharedPreferences`).
- 🤝 **Aportar a la base pública**: desde *Lista SPAM*, el botón "Aportar" abre un
  **Issue** en el repositorio con tus números reportados en formato JSON, listos para
  integrar en `spam_numbers.json`.
- 🧩 **Configurable para forks**: el Client ID y el repo destino (owner/repo) se pueden
  cambiar desde la pestaña **Cuenta**, así quien haga fork aporta a su propia base.
- 🎨 Nueva pestaña **Cuenta** y limpieza de iconos (build sin warnings).

## Detalles técnicos

- `applicationId`: `io.github.jhonsu01.opencallshield`
- versionName `1.1.0` (versionCode 2) · minSdk 29 · targetSdk 34
- APK firmado con la clave de release del proyecto.

## Cómo aportar desde la app

1. Pestaña **Cuenta** → **Iniciar sesión con GitHub** (Client ID ya precargado) o pega un PAT.
2. Reporta números en **Lista SPAM**.
3. Pulsa **"Aportar a la base pública"** → se crea un Issue con tu propuesta.

## Requisitos en el repo

- **Issues habilitados** (Settings → Features → Issues).
- Para Device Flow: OAuth App con **Enable Device Flow** activado.
