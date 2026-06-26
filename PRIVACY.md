# Política de Privacidad — OpenCallShield

**Última actualización:** 26 de junio de 2026

OpenCallShield ("la app") es una aplicación Android open source (licencia MIT) para
bloquear llamadas SPAM. Esta política explica qué datos usa la app y cómo los trata.
Resumen: **la app no tiene servidores propios, no vende datos y no incluye publicidad
ni rastreadores de terceros.**

## Responsable

- Desarrollador: jhonsu01
- Contacto: jhonsu777@gmail.com
- Código fuente: https://github.com/jhonsu01/OpenCallShield

## Qué datos usa la app y para qué

- **Número de la llamada entrante:** cuando recibes una llamada, el sistema Android
  entrega el número a la app a través de `CallScreeningService` para decidir si la
  bloquea. Este número se evalúa **en tu dispositivo** y no se envía a ningún servidor.
- **Contactos (permiso de Contactos):** la app consulta tu agenda **localmente** solo
  para **no bloquear** llamadas de personas que ya tienes guardadas. La app no copia,
  no almacena ni transmite tu lista de contactos.
- **Lista local de números SPAM e historial de llamadas filtradas:** se guardan **solo
  en tu dispositivo** (base de datos local). No se suben a ningún sitio de forma
  automática.

## Conexiones a Internet

- **Sincronización:** la app descarga una base pública de números SPAM desde un archivo
  JSON alojado en GitHub. Es una descarga de solo lectura; no envía datos tuyos.
- **Aportes voluntarios (opcional):** si **tú** decides aportar, e inicias sesión con
  GitHub, la app crea una *Issue* pública en el repositorio con los números SPAM que
  reportaste. **Esos números pasan a ser públicos.** No se incluyen tus contactos ni
  ningún dato personal: solo los números que marcaste como SPAM. Esta acción es
  siempre iniciada por ti.

## Inicio de sesión con GitHub (opcional)

- Solo es necesario si quieres aportar a la base pública.
- El token de acceso se obtiene mediante el flujo oficial de GitHub (Device Flow) o un
  token personal, y se guarda **cifrado** en tu dispositivo (`EncryptedSharedPreferences`).
- El token solo se usa para comunicarse con la API de GitHub. Puedes cerrar sesión en
  cualquier momento desde la app, lo que borra el token del dispositivo.

## Permisos que solicita la app

- **Contactos (READ_CONTACTS):** para no bloquear a tus contactos conocidos.
- **Internet / Estado de red:** para sincronizar la base pública y enviar aportes.
- **Rol de filtrado de llamadas (ROLE_CALL_SCREENING):** lo concedes manualmente para
  permitir que la app filtre llamadas. La app no accede a tu registro de llamadas.

## Con quién se comparten los datos

- Con **nadie**, salvo los números SPAM que tú elijas aportar, que se publican en el
  repositorio público de GitHub a petición tuya.
- No hay analítica de terceros, ni anuncios, ni venta de datos.

## Datos de menores

La app no está dirigida a menores de 13 años y no recopila datos de ellos.

## Cambios

Cualquier cambio en esta política se publicará en esta misma página del repositorio.

## Contacto

Para dudas sobre privacidad: jhonsu777@gmail.com
