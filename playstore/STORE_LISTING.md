# Ficha de Google Play — OpenCallShield

Material listo para copiar/pegar en Play Console. Idioma: Español (Latinoamérica).

---

## Nombre de la app (máx. 30)
```
OpenCallShield
```

## Descripción corta (máx. 80)
```
Bloquea llamadas SPAM con una base colaborativa. Privado y open source.
```

## Descripción completa (máx. 4000)
```
OpenCallShield bloquea las llamadas SPAM automáticamente, sin anuncios y respetando tu privacidad. Es una app open source (licencia MIT): su código es público y cualquiera puede revisarlo.

¿CÓMO FUNCIONA?
Usa el servicio oficial de filtrado de llamadas de Android (CallScreeningService, Android 10 o superior). Cuando entra una llamada, OpenCallShield decide en tu propio dispositivo si la deja pasar, la silencia o la rechaza, según tus reglas.

REGLAS DE DETECCIÓN
• Si el número es de un contacto guardado, la llamada siempre pasa.
• Si el número está reportado como SPAM (por ti o por la base colaborativa), se bloquea.
• Si el prefijo está en tu lista negra, se bloquea.
• Opcional: bloquear números desconocidos que no están en tus contactos.
Puedes elegir entre rechazar la llamada o solo silenciarla (queda como perdida).

BASE COLABORATIVA
La app sincroniza una base pública de números SPAM alojada en GitHub. Si quieres, puedes aportar tus propios reportes a esa base para ayudar a la comunidad: solo se comparten los números marcados como SPAM, nunca tus contactos.

PRIVACIDAD PRIMERO
• No tiene servidores propios: todo se procesa en tu teléfono.
• No muestra anuncios ni incluye rastreadores.
• No accede a tu registro de llamadas.
• Tus contactos se consultan solo localmente, para no bloquearlos.

CÓDIGO ABIERTO
OpenCallShield es gratis y open source. Puedes ver el proyecto, proponer mejoras o crear tu propia versión:
https://github.com/jhonsu01/OpenCallShield

Una alternativa transparente y descentralizada para protegerte del SPAM telefónico.
```

## Notas de la versión (v1.3.1) — es-419
```
• Sincroniza la base pública antes de aportar: solo se reportan números nuevos.
• Historial: añade o quita números de tu lista de SPAM con un toque.
• Nuevos enlaces de apoyo (Ko-fi) y al proyecto en GitHub.
• Mejoras de estabilidad.
```

---

## Datos de la ficha
- **Categoría de la app:** Herramientas
- **Etiquetas:** llamadas, spam, bloqueador, seguridad
- **Correo de contacto:** jhonsu777@gmail.com
- **Sitio web (opcional):** https://github.com/jhonsu01/OpenCallShield
- **Política de privacidad (URL):** https://github.com/jhonsu01/OpenCallShield/blob/main/PRIVACY.md

## Recursos gráficos (en esta carpeta)
- `icon_512.png` — Ícono de la app (512×512).
- `feature_1024x500.png` — Gráfico de funciones (1024×500).
- Capturas de pantalla: usa las del teléfono (mínimo 2; recomendado 4-8). Sirven las
  pantallas de Protección, Lista SPAM, Historial y Cuenta.

---

## Cuestionario "Seguridad de los datos" (Data safety)

**¿La app recopila o comparte datos de los usuarios?** Sí (mínimo, explicado abajo).

| Pregunta | Respuesta |
| --- | --- |
| ¿Se cifran los datos en tránsito? | Sí (HTTPS hacia GitHub) |
| ¿El usuario puede pedir que se eliminen sus datos? | Sí (cerrando sesión / desinstalando; los aportes públicos se pueden retirar abriendo una solicitud en GitHub) |

**Tipos de datos:**
- **Contactos:** se ACCEDEN pero NO se recopilan ni comparten (procesamiento solo en el
  dispositivo, para no bloquear contactos). Si Play obliga a declararlo: uso = "Funcionalidad
  de la app", no compartido, no recopilado en servidor.
- **Información de la app / actividad:** los números que el usuario marca como SPAM y
  decide aportar se publican en GitHub a iniciativa del usuario. Declarar como:
  recopilación = opcional, compartido = sí (repositorio público), propósito =
  "Funcionalidad de la app", iniciado por el usuario.
- **Identificadores / cuenta GitHub:** solo si el usuario inicia sesión; el token se
  guarda cifrado en el dispositivo y no se comparte con terceros.

> Resumen para marcar: NO se recopila ubicación, ni datos financieros, ni mensajes, ni
> registro de llamadas, ni datos de salud. Sin publicidad. Sin analítica de terceros.

---

## Clasificación de contenido (Content rating)
- Tipo de app: Utilidad / Comunicación.
- Sin violencia, sin contenido sexual, sin lenguaje ofensivo, sin sustancias, sin apuestas.
- ¿Comparte contenido generado por el usuario? Sí, indirecto: los aportes a GitHub.
- Resultado esperado: apta para todo público (PEGI 3 / ESRB Everyone).

---

## Acceso a la app (App access)
- Todas las funciones están disponibles sin credenciales especiales.
- El inicio de sesión con GitHub es OPCIONAL (solo para aportar) → no hace falta dar
  credenciales de prueba a Google.
