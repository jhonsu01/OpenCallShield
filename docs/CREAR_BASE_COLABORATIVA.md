# 🗂️ Cómo crear tu propia base colaborativa de números SPAM

OpenCallShield sincroniza una lista de números SPAM desde un archivo **JSON público**.
Por defecto usa la base oficial, pero **puedes crear la tuya** (para tu familia, tu empresa
o tu comunidad) y ponerla en la app desde **Ajustes avanzados → URL de la base colaborativa**.

Esta guía te muestra 3 formas, de la más fácil a la más técnica. **No necesitas saber programar.**

---

## 📋 Qué formato debe tener

Un archivo de texto llamado `spam_numbers.json` con esta estructura:

```json
{
  "version": "1.0",
  "updated_at": "2026-07-19",
  "numbers": [
    { "number": "+573001112233", "reports": 5, "tag": "spam" },
    { "number": "033912345678", "reports": 2, "tag": "scam" }
  ]
}
```

- **number**: el número (con o sin `+` y código de país).
- **reports**: cuántas veces se ha reportado (un número, ej. 1).
- **tag**: una etiqueta libre (`spam`, `scam`, `publicidad`, etc.).

---

## ✅ Opción 1 — Con Excel o Google Sheets (la más fácil)

1. Descarga la plantilla [`plantilla_numeros.csv`](plantilla_numeros.csv) y ábrela en
   **Excel** o **Google Sheets**.
2. Llena las columnas **number**, **reports**, **tag** (una fila por número).
3. Convierte el CSV a JSON con un conversor gratuito online (busca **"CSV to JSON"**),
   o pídele a una IA: *"convierte este CSV al formato JSON de OpenCallShield"*.
4. Guarda el resultado como `spam_numbers.json`.
5. Súbelo a GitHub (Opción 3) para obtener el enlace.

> El archivo final debe tener la estructura de arriba (con `version`, `updated_at` y `numbers`).

---

## ✅ Opción 2 — Editando el JSON directamente

1. Copia el ejemplo de formato de arriba.
2. Agrega tus números dentro de `"numbers": [ ... ]`, uno por línea, respetando las comas.
3. Guárdalo como `spam_numbers.json`.

---

## ✅ Opción 3 — Alojarlo en GitHub (para tener el enlace)

La app necesita una **URL pública** que apunte a tu JSON. GitHub es gratis y perfecto:

1. Crea una cuenta en https://github.com (si no tienes).
2. Crea un **repositorio público** (botón *New*), por ejemplo `mi-base-spam`.
3. Sube tu `spam_numbers.json` (botón *Add file → Upload files*).
4. Abre el archivo en GitHub y pulsa el botón **"Raw"**.
5. Copia la URL que aparece (será algo como
   `https://raw.githubusercontent.com/TU_USUARIO/mi-base-spam/main/spam_numbers.json`).
6. En la app: **Protección → Ajustes avanzados → URL de la base colaborativa** → pega esa URL.
7. Pulsa **Sincronizar ahora**. ¡Listo! La app usará tu base.

> **Alternativas a GitHub:** cualquier hosting que entregue el archivo como texto plano por
> una URL directa sirve (GitLab "raw", un Gist, tu propio servidor, etc.).

---

## 🔄 Mantenerla actualizada

Cada vez que agregues números, vuelve a subir el `spam_numbers.json` actualizado (cambia
también la fecha en `updated_at`). La app se sincroniza sola cada día, o cuando pulses
**Sincronizar ahora**.

---

## 💡 Consejo

Si compartes tu base con más gente, todos apuntan a la **misma URL** y quedan protegidos con
la misma lista. Así funciona la base oficial de OpenCallShield, a la que también puedes
**aportar** desde la propia app (botón *Aportar a la base pública*).
