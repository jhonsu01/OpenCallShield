# OpenCallShield v1.2.0

Gestión del SPAM desde el historial y corrección del aporte a la base pública.

## Novedades

- ➕ **Historial accionable:** cada llamada filtrada tiene ahora un botón de **un toque**
  para **añadir** el número a la lista de SPAM (icono +) o **quitarlo** (icono ✓ si ya está).
  Así decides rápidamente qué números desconocidos pasan a tu lista negra.

## Correcciones

- 🐛 **Aporte sin error 403:** los Issues de aporte ya **no** intentan poner una etiqueta
  (`label`), operación que requería permiso de escritura sobre el repo y fallaba para
  usuarios externos. Ahora cualquier cuenta autenticada puede aportar a un repo público.
- 🔧 **Workflow de Release más robusto:** el paso de publicación usa `tag_name`, `name` y
  `token` explícitos para evitar fallos al adjuntar el APK al Release.

## Técnico

- versionName `1.2.0` (versionCode 4) · sin cambios de permisos.
