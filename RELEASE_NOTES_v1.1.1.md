# OpenCallShield v1.1.1

Corrección del inicio de sesión con GitHub ante redes inestables.

## Correcciones

- 🐛 **Device Flow resiliente:** un fallo de red transitorio durante la espera de
  autorización ya **no cancela** el inicio de sesión. La app sigue reintentando hasta
  que autorizas en `github.com/login/device` o el código expira. Antes, un parpadeo de
  red abortaba el proceso aunque la autorización en GitHub fuera correcta.
- 💬 **Mensajes de error más claros:** "Sin conexión o el DNS no resuelve github.com…"
  y "La conexión tardó demasiado…" en vez del error técnico crudo.

## Sin cambios funcionales

Mismo conjunto de funciones que v1.1.0 (bloqueo SPAM, base local, sync, login GitHub,
aporte vía Issue). versionCode 3.
