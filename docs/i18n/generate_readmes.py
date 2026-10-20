# -*- coding: utf-8 -*-
"""Genera docs/i18n/README.<idioma>.md a partir de una plantilla comun.

El README principal (ingles) esta en la raiz. Al cambiar contenido, editar aqui y
volver a ejecutar: asi las 8 traducciones no se desincronizan.
"""
import os

AQUI = os.path.dirname(os.path.abspath(__file__))

NAV = [("en", "English", "../../README.md"), ("es", "Español", "README.es.md"),
       ("pt", "Português", "README.pt.md"), ("fr", "Français", "README.fr.md"),
       ("de", "Deutsch", "README.de.md"), ("ru", "Русский", "README.ru.md"),
       ("zh", "中文", "README.zh.md"), ("ja", "日本語", "README.ja.md"),
       ("ko", "한국어", "README.ko.md")]

TPL = """# <img src="../../playstore/icon_512.png" width="44" align="top" alt="OpenCallShield"/> OpenCallShield

{nav}

[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](../../LICENSE)

{intro}

## ⬇️ {h_download}

<a href="https://play.google.com/store/apps/details?id=com.jhonsu01.opencallshield"><img alt="Google Play" src="https://play.google.com/intl/en_us/badges/static/images/badges/{badge}_badge_web_generic.png" height="60"/></a>

- **Google Play:** https://play.google.com/store/apps/details?id=com.jhonsu01.opencallshield
- **APK:** {apk}

## 📱 {h_shots}

<p>
  <img src="../../playstore/screenshots/{store}/01_protection.png" width="19%"/>
  <img src="../../playstore/screenshots/{store}/02_countries.png" width="19%"/>
  <img src="../../playstore/screenshots/{store}/03_spam_list.png" width="19%"/>
  <img src="../../playstore/screenshots/{store}/04_history.png" width="19%"/>
  <img src="../../playstore/screenshots/{store}/05_language.png" width="19%"/>
</p>

## ✨ {h_features}

{features}

## 🏗️ Stack

Kotlin · Jetpack Compose (Material 3) · Room · WorkManager · AppCompat · MVVM · `CallScreeningService` · targetSdk 36 (Android 16)

## 📦 {h_build}

```bash
./gradlew assembleDebug
./gradlew assembleRelease
./gradlew bundleRelease
```

{build_note}

## 📲 {h_usage}

{usage}

## 🌐 {h_list}

{list_text}

```
https://raw.githubusercontent.com/jhonsu01/OpenCallShield/main/spam_numbers.json
```

## 🔑 {h_contrib}

{contrib}

## 🔓 {h_license}

MIT — [LICENSE](../../LICENSE) · [PRIVACY.md](../../PRIVACY.md)

## ❤️ {h_support}

{support}

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/V7V81LV7GX)
"""

T = {}

T["es"] = dict(store="es-419", badge="es",
intro="App Android open source (MIT) para **bloquear llamadas SPAM** mediante `CallScreeningService` (Android 10+), con base de datos local Room, motor de reglas de detección y sincronización con una lista colaborativa pública en GitHub.\n\nAlternativa privacidad-first y descentralizada a Truecaller.",
h_download="Descargar", apk="descarga el `.apk` desde [**Releases**](https://github.com/jhonsu01/OpenCallShield/releases).",
h_shots="Capturas", h_features="Funciones",
features="""- **Bloqueo automático** vía `CallScreeningService` (rechazar o silenciar).
- **Motor de reglas:** contacto conocido → permitir; número reportado → bloquear; prefijo en lista negra → bloquear; número fuera de contactos → opcional.
- **Bloqueo por país** tocando la bandera (sin escribir prefijos).
- **Base local** Room con los números reportados.
- **Sincronización** con un JSON público en GitHub (diaria con WorkManager + manual).
- **Reportes** e historial de llamadas filtradas (añade o quita números con un toque).
- **9 idiomas:** English, Español, Português, Français, Deutsch, Русский, 中文, 日本語, 한국어 — elige desde el icono 🌐 de la app o desde los ajustes de idioma por app de Android.""",
h_build="Compilar", build_note="Requiere JDK 17 y Android SDK (compileSdk 36). Los resultados quedan en `app/build/outputs/`.",
h_usage="Uso", usage="""1. Instala la app.
2. Concede el permiso de contactos (para no bloquear nunca a tus contactos).
3. Pulsa **"Activar como app de filtrado de llamadas"** y acepta el rol en Android.
4. Ajusta las reglas, elige países a bloquear y/o sincroniza la lista colaborativa.""",
h_list="Lista colaborativa", list_text="La app sincroniza desde [`spam_numbers.json`](../../spam_numbers.json) de este repositorio. La URL se puede cambiar en *Ajustes avanzados*, así cualquiera puede mantener su propia lista ([guía](../CREAR_BASE_COLABORATIVA.md)).",
h_contrib="Aportar desde la app (opcional)", contrib="Desde **⋮ → Colaborar** puedes iniciar sesión con GitHub (código de dispositivo OAuth o token personal con permiso `public_repo`) y, en **Lista SPAM**, pulsar **\"Aportar a la base pública\"**: la app abre un **Issue** con tus números en JSON, listo para integrar en `spam_numbers.json`. El token se guarda **cifrado** en el dispositivo. Iniciar sesión es opcional: la app funciona completa sin cuenta.",
h_license="Licencia", h_support="Apoyar el proyecto",
support="OpenCallShield es **gratuito y open source**. Si te resulta útil, puedes apoyar su desarrollo:")

T["pt"] = dict(store="pt-BR", badge="pt-br",
intro="App Android open source (MIT) para **bloquear chamadas de SPAM** com `CallScreeningService` (Android 10+), com banco de dados local Room, motor de regras de detecção e sincronização com uma lista colaborativa pública no GitHub.\n\nUma alternativa descentralizada ao Truecaller, com a privacidade em primeiro lugar.",
h_download="Baixar", apk="baixe o `.apk` em [**Releases**](https://github.com/jhonsu01/OpenCallShield/releases).",
h_shots="Capturas de tela", h_features="Recursos",
features="""- **Bloqueio automático** via `CallScreeningService` (rejeitar ou silenciar).
- **Motor de regras:** contato conhecido → permitir; número denunciado → bloquear; prefixo na lista negra → bloquear; número fora dos contatos → opcional.
- **Bloqueio por país** tocando na bandeira (sem digitar prefixos).
- **Banco local** Room com os números denunciados.
- **Sincronização** com um JSON público no GitHub (diária via WorkManager + manual).
- **Denúncias** e histórico de chamadas filtradas (adicione ou remova números com um toque).
- **9 idiomas:** English, Español, Português, Français, Deutsch, Русский, 中文, 日本語, 한국어 — escolha pelo ícone 🌐 do app ou nas configurações de idioma por app do Android.""",
h_build="Compilar", build_note="Requer JDK 17 e Android SDK (compileSdk 36). Os resultados ficam em `app/build/outputs/`.",
h_usage="Uso", usage="""1. Instale o app.
2. Conceda a permissão de contatos (para nunca bloquear seus contatos).
3. Toque em **"Definir como app de triagem de chamadas"** e aceite a função no Android.
4. Ajuste as regras, escolha países para bloquear e/ou sincronize a lista colaborativa.""",
h_list="Lista colaborativa", list_text="O app sincroniza a partir de [`spam_numbers.json`](../../spam_numbers.json) deste repositório. A URL pode ser alterada em *Configurações avançadas*, então qualquer pessoa pode manter sua própria lista ([guia, em espanhol](../CREAR_BASE_COLABORATIVA.md)).",
h_contrib="Contribuir pelo app (opcional)", contrib="Em **⋮ → Colaborar** você pode entrar com o GitHub (código de dispositivo OAuth ou token pessoal com escopo `public_repo`) e, em **Lista de SPAM**, tocar em **\"Contribuir para a base pública\"**: o app abre uma **Issue** com seus números em JSON, pronta para ser integrada ao `spam_numbers.json`. O token fica **criptografado** no aparelho. Entrar é opcional: o app funciona completamente sem conta.",
h_license="Licença", h_support="Apoie o projeto",
support="O OpenCallShield é **gratuito e open source**. Se for útil para você, apoie o desenvolvimento:")

T["fr"] = dict(store="fr-FR", badge="fr",
intro="Application Android libre (MIT) qui **bloque les appels SPAM** grâce à `CallScreeningService` (Android 10+), avec une base locale Room, un moteur de règles de détection et une synchronisation avec une liste collaborative publique sur GitHub.\n\nUne alternative décentralisée à Truecaller, respectueuse de la vie privée.",
h_download="Télécharger", apk="téléchargez le `.apk` depuis [**Releases**](https://github.com/jhonsu01/OpenCallShield/releases).",
h_shots="Captures d'écran", h_features="Fonctionnalités",
features="""- **Blocage automatique** via `CallScreeningService` (rejeter ou mettre en silence).
- **Moteur de règles :** contact connu → autoriser ; numéro signalé → bloquer ; préfixe sur liste noire → bloquer ; numéro hors contacts → facultatif.
- **Blocage par pays** en touchant le drapeau (aucun préfixe à saisir).
- **Base locale** Room des numéros signalés.
- **Synchronisation** avec un JSON public sur GitHub (quotidienne via WorkManager + manuelle).
- **Signalements** et historique des appels filtrés (ajoutez ou retirez des numéros d'un toucher).
- **9 langues :** English, Español, Português, Français, Deutsch, Русский, 中文, 日本語, 한국어 — à choisir via l'icône 🌐 de l'application ou dans les paramètres de langue par application d'Android.""",
h_build="Compiler", build_note="Nécessite le JDK 17 et le SDK Android (compileSdk 36). Les fichiers produits se trouvent dans `app/build/outputs/`.",
h_usage="Utilisation", usage="""1. Installez l'application.
2. Accordez l'autorisation d'accès aux contacts (pour ne jamais bloquer vos contacts).
3. Touchez **« Définir comme app de filtrage d'appels »** et acceptez le rôle dans Android.
4. Réglez les règles, choisissez les pays à bloquer et/ou synchronisez la liste collaborative.""",
h_list="Liste collaborative", list_text="L'application se synchronise depuis [`spam_numbers.json`](../../spam_numbers.json) de ce dépôt. L'URL peut être modifiée dans les *Paramètres avancés* : chacun peut donc tenir sa propre liste ([guide, en espagnol](../CREAR_BASE_COLABORATIVA.md)).",
h_contrib="Contribuer depuis l'application (facultatif)", contrib="Depuis **⋮ → Contribuer**, connectez-vous avec GitHub (code d'appareil OAuth ou jeton personnel avec la portée `public_repo`) puis, dans **Liste SPAM**, touchez **« Contribuer à la base publique »** : l'application ouvre une **Issue** avec vos numéros en JSON, prête à être intégrée à `spam_numbers.json`. Le jeton est stocké **chiffré** sur l'appareil. La connexion est facultative : l'application fonctionne entièrement sans compte.",
h_license="Licence", h_support="Soutenir le projet",
support="OpenCallShield est **gratuit et open source**. S'il vous est utile, vous pouvez soutenir son développement :")

T["de"] = dict(store="de-DE", badge="de",
intro="Open-Source-Android-App (MIT), die **SPAM-Anrufe blockiert** – über `CallScreeningService` (Android 10+), mit lokaler Room-Datenbank, Regel-Engine zur Erkennung und Synchronisierung mit einer öffentlichen Gemeinschaftsliste auf GitHub.\n\nEine dezentrale, datenschutzfreundliche Alternative zu Truecaller.",
h_download="Herunterladen", apk="lade die `.apk` unter [**Releases**](https://github.com/jhonsu01/OpenCallShield/releases) herunter.",
h_shots="Screenshots", h_features="Funktionen",
features="""- **Automatisches Blockieren** über `CallScreeningService` (ablehnen oder stummschalten).
- **Regel-Engine:** bekannter Kontakt → zulassen; gemeldete Nummer → blockieren; Vorwahl auf der Sperrliste → blockieren; Nummer nicht in den Kontakten → optional.
- **Blockieren nach Land** per Tipp auf die Flagge (keine Vorwahlen eintippen).
- **Lokale Room-Datenbank** mit gemeldeten Nummern.
- **Synchronisierung** mit einem öffentlichen JSON auf GitHub (täglich per WorkManager + manuell).
- **Meldungen** und Verlauf gefilterter Anrufe (Nummern mit einem Tipp hinzufügen oder entfernen).
- **9 Sprachen:** English, Español, Português, Français, Deutsch, Русский, 中文, 日本語, 한국어 – Auswahl über das 🌐-Symbol in der App oder in den App-Spracheinstellungen von Android.""",
h_build="Bauen", build_note="Benötigt JDK 17 und das Android SDK (compileSdk 36). Die Ergebnisse liegen in `app/build/outputs/`.",
h_usage="Verwendung", usage="""1. Installiere die App.
2. Erteile die Kontaktberechtigung (damit deine Kontakte nie blockiert werden).
3. Tippe auf **„Als Anruffilter-App festlegen“** und bestätige die Rolle in Android.
4. Passe die Regeln an, wähle Länder zum Blockieren und/oder synchronisiere die Gemeinschaftsliste.""",
h_list="Gemeinschaftsliste", list_text="Die App synchronisiert aus [`spam_numbers.json`](../../spam_numbers.json) in diesem Repository. Die URL lässt sich in den *Erweiterten Einstellungen* ändern, sodass jeder eine eigene Liste betreiben kann ([Anleitung, auf Spanisch](../CREAR_BASE_COLABORATIVA.md)).",
h_contrib="Aus der App beitragen (optional)", contrib="Unter **⋮ → Mitwirken** kannst du dich mit GitHub anmelden (OAuth-Gerätecode oder persönliches Token mit dem Bereich `public_repo`) und dann in der **SPAM-Liste** auf **„Zur öffentlichen Datenbank beitragen“** tippen: Die App öffnet ein **Issue** mit deinen Nummern als JSON, bereit zur Übernahme in `spam_numbers.json`. Das Token wird **verschlüsselt** auf dem Gerät gespeichert. Die Anmeldung ist optional: Die App funktioniert vollständig ohne Konto.",
h_license="Lizenz", h_support="Projekt unterstützen",
support="OpenCallShield ist **kostenlos und Open Source**. Wenn dir die App hilft, kannst du die Entwicklung unterstützen:")

T["ru"] = dict(store="ru-RU", badge="ru",
intro="Android-приложение с открытым кодом (MIT), которое **блокирует спам-звонки** через `CallScreeningService` (Android 10+). Локальная база Room, движок правил обнаружения и синхронизация с общим публичным списком на GitHub.\n\nДецентрализованная альтернатива Truecaller с приоритетом конфиденциальности.",
h_download="Скачать", apk="скачайте `.apk` в разделе [**Releases**](https://github.com/jhonsu01/OpenCallShield/releases).",
h_shots="Скриншоты", h_features="Возможности",
features="""- **Автоматическая блокировка** через `CallScreeningService` (отклонить или без звука).
- **Движок правил:** известный контакт → пропустить; номер в жалобах → блокировать; код в чёрном списке → блокировать; номер не из контактов → по желанию.
- **Блокировка по странам** нажатием на флаг (без ввода кодов).
- **Локальная база** Room с отмеченными номерами.
- **Синхронизация** с публичным JSON на GitHub (ежедневно через WorkManager + вручную).
- **Жалобы** и история отфильтрованных звонков (добавляйте и убирайте номера одним нажатием).
- **9 языков:** English, Español, Português, Français, Deutsch, Русский, 中文, 日本語, 한국어 — выбор через значок 🌐 в приложении или в настройках языка приложений Android.""",
h_build="Сборка", build_note="Нужны JDK 17 и Android SDK (compileSdk 36). Результаты — в `app/build/outputs/`.",
h_usage="Использование", usage="""1. Установите приложение.
2. Разрешите доступ к контактам (чтобы контакты никогда не блокировались).
3. Нажмите **«Назначить приложением для фильтрации звонков»** и подтвердите роль в Android.
4. Настройте правила, выберите страны для блокировки и/или синхронизируйте общий список.""",
h_list="Общий список", list_text="Приложение синхронизируется из [`spam_numbers.json`](../../spam_numbers.json) этого репозитория. URL можно изменить в *Дополнительных настройках*, поэтому любой может вести собственный список ([руководство на испанском](../CREAR_BASE_COLABORATIVA.md)).",
h_contrib="Вклад из приложения (необязательно)", contrib="В меню **⋮ → Участие** можно войти через GitHub (код устройства OAuth или личный токен с областью `public_repo`), а затем в **Списке СПАМа** нажать **«Пополнить общую базу»**: приложение создаст **Issue** с вашими номерами в JSON, готовыми для `spam_numbers.json`. Токен хранится на устройстве **в зашифрованном виде**. Вход необязателен: приложение полностью работает без аккаунта.",
h_license="Лицензия", h_support="Поддержать проект",
support="OpenCallShield **бесплатен и открыт**. Если приложение вам полезно, поддержите разработку:")

T["zh"] = dict(store="zh-CN", badge="zh-cn",
intro="开源（MIT）Android 应用，通过 `CallScreeningService`（Android 10+）**拦截骚扰电话**，配备本地 Room 数据库、识别规则引擎，并与托管在 GitHub 上的公共共享名单同步。\n\n注重隐私、去中心化的 Truecaller 替代方案。",
h_download="下载", apk="在 [**Releases**](https://github.com/jhonsu01/OpenCallShield/releases) 下载 `.apk`。",
h_shots="截图", h_features="功能",
features="""- 通过 `CallScreeningService` **自动拦截**（拒接或静音）。
- **规则引擎：**已知联系人 → 放行；被举报号码 → 拦截；黑名单区号 → 拦截；不在通讯录中的号码 → 可选。
- 点按国旗即可**按国家/地区拦截**（无需输入区号）。
- 保存举报号码的**本地 Room 数据库**。
- 与 GitHub 上的公共 JSON **同步**（WorkManager 每日同步 + 手动同步）。
- **举报**及已过滤来电记录（一键添加或移除号码）。
- **9 种语言：**English、Español、Português、Français、Deutsch、Русский、中文、日本語、한국어 —— 可在应用内的 🌐 图标或 Android 的应用语言设置中选择。""",
h_build="构建", build_note="需要 JDK 17 和 Android SDK（compileSdk 36）。产物位于 `app/build/outputs/`。",
h_usage="使用方法", usage="""1. 安装应用。
2. 授予通讯录权限（确保永远不会拦截你的联系人）。
3. 点按**“设为来电过滤应用”**，并在 Android 中接受该角色。
4. 调整规则、选择要拦截的国家/地区，和/或同步共享名单。""",
h_list="共享名单", list_text="应用从本仓库的 [`spam_numbers.json`](../../spam_numbers.json) 同步。可在*高级设置*中更改 URL，任何人都可以维护自己的名单（[指南，西班牙语](../CREAR_BASE_COLABORATIVA.md)）。",
h_contrib="从应用贡献（可选）", contrib="在 **⋮ → 参与贡献** 中使用 GitHub 登录（OAuth 设备代码，或具有 `public_repo` 权限的个人令牌），然后在**骚扰号码**中点按**“向公共数据库贡献”**：应用会创建一个包含你的号码（JSON 格式）的 **Issue**，可直接合并到 `spam_numbers.json`。令牌在设备上**加密**保存。登录是可选的：不登录也能使用全部功能。",
h_license="许可证", h_support="支持本项目",
support="OpenCallShield **免费且开源**。如果它对你有帮助，欢迎支持开发：")

T["ja"] = dict(store="ja-JP", badge="ja",
intro="`CallScreeningService`（Android 10 以降）で**迷惑電話をブロック**するオープンソース（MIT）の Android アプリです。ローカルの Room データベース、検出ルールエンジン、GitHub 上の公開共有リストとの同期を備えています。\n\nプライバシーを重視した、分散型の Truecaller 代替アプリです。",
h_download="ダウンロード", apk="[**Releases**](https://github.com/jhonsu01/OpenCallShield/releases) から `.apk` をダウンロードできます。",
h_shots="スクリーンショット", h_features="機能",
features="""- `CallScreeningService` による**自動ブロック**（拒否または消音）。
- **ルールエンジン：**既知の連絡先 → 許可、報告済みの番号 → ブロック、ブラックリストの国番号 → ブロック、連絡先にない番号 → 任意。
- 国旗をタップして**国・地域ごとにブロック**（番号の入力は不要）。
- 報告した番号を保存する**ローカル Room データベース**。
- GitHub 上の公開 JSON と**同期**（WorkManager で毎日 + 手動）。
- **報告**とフィルタした通話の履歴（ワンタップで番号を追加・削除）。
- **9 言語：**English、Español、Português、Français、Deutsch、Русский、中文、日本語、한국어 — アプリの 🌐 アイコン、または Android のアプリ別言語設定から選べます。""",
h_build="ビルド", build_note="JDK 17 と Android SDK（compileSdk 36）が必要です。成果物は `app/build/outputs/` にあります。",
h_usage="使い方", usage="""1. アプリをインストールします。
2. 連絡先へのアクセスを許可します（連絡先を決してブロックしないため）。
3. **「着信フィルタアプリに設定」**をタップし、Android でロールを許可します。
4. ルールを調整し、ブロックする国・地域を選び、共有リストを同期します。""",
h_list="共有リスト", list_text="アプリはこのリポジトリの [`spam_numbers.json`](../../spam_numbers.json) から同期します。URL は*詳細設定*で変更できるため、誰でも独自のリストを運用できます（[ガイド・スペイン語](../CREAR_BASE_COLABORATIVA.md)）。",
h_contrib="アプリから貢献（任意）", contrib="**⋮ → 貢献する** から GitHub でログインし（OAuth デバイスコード、または `public_repo` スコープの個人トークン）、**迷惑電話リスト**で**「公開データベースに提供」**をタップすると、番号を JSON にした **Issue** がこのリポジトリに作成され、そのまま `spam_numbers.json` に取り込めます。トークンは端末内に**暗号化**して保存されます。ログインは任意で、アカウントなしでもすべての機能を使えます。",
h_license="ライセンス", h_support="プロジェクトを応援",
support="OpenCallShield は**無料のオープンソース**です。役に立ったら、開発を応援してください：")

T["ko"] = dict(store="ko-KR", badge="ko",
intro="`CallScreeningService`(Android 10+)로 **스팸 전화를 차단**하는 오픈 소스(MIT) Android 앱입니다. 로컬 Room 데이터베이스, 감지 규칙 엔진, GitHub의 공개 공유 목록 동기화를 제공합니다.\n\n개인정보 보호를 우선하는 분산형 Truecaller 대안입니다.",
h_download="다운로드", apk="[**Releases**](https://github.com/jhonsu01/OpenCallShield/releases)에서 `.apk`를 다운로드하세요.",
h_shots="스크린샷", h_features="기능",
features="""- `CallScreeningService`를 통한 **자동 차단**(거절 또는 무음).
- **규칙 엔진:** 알려진 연락처 → 허용, 신고된 번호 → 차단, 차단 목록의 국가 번호 → 차단, 연락처에 없는 번호 → 선택 사항.
- 국기를 탭하여 **국가별 차단**(국가 번호 입력 불필요).
- 신고된 번호를 저장하는 **로컬 Room 데이터베이스**.
- GitHub의 공개 JSON과 **동기화**(WorkManager로 매일 + 수동).
- **신고** 및 걸러낸 전화 기록(탭 한 번으로 번호 추가·제거).
- **9개 언어:** English, Español, Português, Français, Deutsch, Русский, 中文, 日本語, 한국어 — 앱의 🌐 아이콘 또는 Android의 앱별 언어 설정에서 선택하세요.""",
h_build="빌드", build_note="JDK 17과 Android SDK(compileSdk 36)가 필요합니다. 결과물은 `app/build/outputs/`에 있습니다.",
h_usage="사용 방법", usage="""1. 앱을 설치합니다.
2. 연락처 권한을 허용합니다(연락처가 절대 차단되지 않도록).
3. **"통화 필터 앱으로 설정"**을 탭하고 Android에서 역할을 허용합니다.
4. 규칙을 조정하고, 차단할 국가를 선택하고, 공유 목록을 동기화합니다.""",
h_list="공유 목록", list_text="앱은 이 저장소의 [`spam_numbers.json`](../../spam_numbers.json)에서 동기화합니다. *고급 설정*에서 URL을 바꿀 수 있으므로 누구나 자신만의 목록을 운영할 수 있습니다([가이드, 스페인어](../CREAR_BASE_COLABORATIVA.md)).",
h_contrib="앱에서 기여하기(선택 사항)", contrib="**⋮ → 기여하기**에서 GitHub로 로그인(OAuth 기기 코드 또는 `public_repo` 범위의 개인 토큰)한 뒤 **스팸 목록**에서 **\"공개 데이터베이스에 기여\"**를 탭하면, 앱이 번호를 JSON으로 담은 **Issue**를 만들어 `spam_numbers.json`에 바로 반영할 수 있습니다. 토큰은 기기에 **암호화**되어 저장됩니다. 로그인은 선택 사항이며 계정 없이도 모든 기능을 사용할 수 있습니다.",
h_license="라이선스", h_support="프로젝트 후원",
support="OpenCallShield는 **무료 오픈 소스**입니다. 도움이 되셨다면 개발을 후원해 주세요:")

for lang, d in T.items():
    nav = " · ".join(f"**{name}**" if code == lang else f"[{name}]({path})"
                     for code, name, path in NAV)
    with open(os.path.join(AQUI, f"README.{lang}.md"), "w", encoding="utf-8", newline="\n") as f:
        f.write(TPL.format(nav=nav, **d))
    print("OK", lang)
