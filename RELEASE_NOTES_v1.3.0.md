# OpenCallShield v1.3.0

Aportes más limpios (sin duplicados), donaciones y enlace al proyecto.

## Novedades

- 🔄 **Sincronizar antes de aportar:** nuevo botón **"Sincronizar base pública"** en
  *Lista SPAM*, justo debajo de *Aportar*, con una nota que recomienda sincronizar primero.
- 🚫 **Sin reportes duplicados:** al sincronizar, los números del usuario que ya están en
  la base pública se marcan automáticamente, y **el aporte solo envía los números nuevos**.
  Esto reduce los Issues con números repetidos.
- ☕ **Donaciones (Ko-fi):** botón de apoyo en la pestaña *Protección* (debajo de
  "Sincronizar ahora") que abre la página de Ko-fi del proyecto.
- 🔗 **Enlace al proyecto:** pie **"OpenCallShield – ver el proyecto en GitHub"** al final
  de *Protección*, que abre el repositorio.

## Técnico

- `mergeRemote` marca como `github` los números locales que también están en la base
  pública (evita re-aportarlos). versionName `1.3.0` (versionCode 6).
