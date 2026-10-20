# -*- coding: utf-8 -*-
"""Genera las fichas de Google Play por idioma en playstore/listings/<idioma>/.

Estructura (estilo fastlane): title.txt, short_description.txt, full_description.txt,
release_notes.txt. Valida los limites de Play (30 / 80 / 4000 / 500 caracteres).
La publica tools/play_store_pack.mjs.
"""
import os, sys

AQUI = os.path.dirname(os.path.abspath(__file__))
REPO = "https://github.com/jhonsu01/OpenCallShield"

L = {}

L["en-US"] = dict(
title="OpenCallShield: SPAM Blocker",
short="Block SPAM calls with a shared community list. Private, free and open source.",
full=f"""OpenCallShield blocks SPAM calls automatically, with no ads and respecting your privacy. It is open source (MIT license): its code is public and anyone can review it.

HOW IT WORKS
It uses Android's official call screening service (CallScreeningService, Android 10 or later). When a call comes in, OpenCallShield decides on your own device whether to let it through, silence it or reject it, according to your rules.

DETECTION RULES
• Calls from your saved contacts always ring.
• Numbers reported as SPAM (by you or by the community list) are blocked.
• Block whole countries with one tap on their flag: no need to type prefixes.
• Optional: block unknown numbers that are not in your contacts.
You choose whether to reject the call or just silence it (it stays as a missed call).

COMMUNITY LIST
The app syncs a public list of SPAM numbers hosted on GitHub. If you want, you can contribute your own reports to help others: only numbers marked as SPAM are shared, never your contacts. Signing in is optional; the app works fully without an account.

PRIVACY FIRST
• No servers of its own: everything is processed on your phone.
• No ads and no trackers.
• It does not read your call log.
• Your contacts are only checked locally, so they are never blocked.

AVAILABLE IN 9 LANGUAGES
English, Español, Português, Français, Deutsch, Русский, 中文, 日本語 and 한국어. Change the language from the app itself (globe icon).

OPEN SOURCE
OpenCallShield is free and open source. View the project, suggest improvements or build your own version:
{REPO}

A transparent, decentralized alternative to protect yourself from phone SPAM.""",
notes="""• The app is now available in 9 languages: English, Spanish, Portuguese, French, German, Russian, Chinese, Japanese and Korean.
• New language selector (globe icon), also available in Android settings.
• Country names are shown in your language.
• Updated for Android 16 (API 36).
• Stability improvements.""",
)

L["es-419"] = dict(
title="OpenCallShield: Bloqueo SPAM",
short="Bloquea llamadas SPAM con una lista colaborativa. Privado, gratis y open source.",
full=f"""OpenCallShield bloquea las llamadas SPAM automáticamente, sin anuncios y respetando tu privacidad. Es open source (licencia MIT): su código es público y cualquiera puede revisarlo.

¿CÓMO FUNCIONA?
Usa el servicio oficial de filtrado de llamadas de Android (CallScreeningService, Android 10 o superior). Cuando entra una llamada, OpenCallShield decide en tu propio dispositivo si la deja pasar, la silencia o la rechaza, según tus reglas.

REGLAS DE DETECCIÓN
• Las llamadas de tus contactos guardados siempre suenan.
• Los números reportados como SPAM (por ti o por la lista colaborativa) se bloquean.
• Bloquea países enteros tocando su bandera: no necesitas escribir prefijos.
• Opcional: bloquear números desconocidos que no están en tus contactos.
Puedes elegir entre rechazar la llamada o solo silenciarla (queda como perdida).

LISTA COLABORATIVA
La app sincroniza una lista pública de números SPAM alojada en GitHub. Si quieres, puedes aportar tus propios reportes para ayudar a la comunidad: solo se comparten los números marcados como SPAM, nunca tus contactos. Iniciar sesión es opcional; la app funciona completa sin cuenta.

PRIVACIDAD PRIMERO
• No tiene servidores propios: todo se procesa en tu teléfono.
• Sin anuncios ni rastreadores.
• No lee tu registro de llamadas.
• Tus contactos se consultan solo localmente, para no bloquearlos nunca.

DISPONIBLE EN 9 IDIOMAS
English, Español, Português, Français, Deutsch, Русский, 中文, 日本語 y 한국어. Cambia el idioma desde la propia app (icono del globo).

CÓDIGO ABIERTO
OpenCallShield es gratis y open source. Mira el proyecto, propón mejoras o crea tu propia versión:
{REPO}

Una alternativa transparente y descentralizada para protegerte del SPAM telefónico.""",
notes="""• La app ya está disponible en 9 idiomas: inglés, español, portugués, francés, alemán, ruso, chino, japonés y coreano.
• Nuevo selector de idioma (icono del globo), también disponible en los ajustes de Android.
• Los nombres de los países se muestran en tu idioma.
• Actualizada para Android 16 (API 36).
• Mejoras de estabilidad.""",
)

L["pt-BR"] = dict(
title="OpenCallShield: Bloqueio SPAM",
short="Bloqueie chamadas de SPAM com uma lista colaborativa. Privado, grátis e aberto.",
full=f"""O OpenCallShield bloqueia chamadas de SPAM automaticamente, sem anúncios e respeitando sua privacidade. É open source (licença MIT): o código é público e qualquer pessoa pode revisá-lo.

COMO FUNCIONA
Usa o serviço oficial de triagem de chamadas do Android (CallScreeningService, Android 10 ou superior). Quando uma chamada chega, o OpenCallShield decide no seu próprio aparelho se ela passa, é silenciada ou rejeitada, de acordo com as suas regras.

REGRAS DE DETECÇÃO
• Chamadas dos seus contatos salvos sempre tocam.
• Números denunciados como SPAM (por você ou pela lista colaborativa) são bloqueados.
• Bloqueie países inteiros tocando na bandeira: não é preciso digitar prefixos.
• Opcional: bloquear números desconhecidos que não estão nos seus contatos.
Você escolhe entre rejeitar a chamada ou apenas silenciá-la (fica como perdida).

LISTA COLABORATIVA
O app sincroniza uma lista pública de números de SPAM hospedada no GitHub. Se quiser, você pode contribuir com suas denúncias para ajudar a comunidade: apenas números marcados como SPAM são compartilhados, nunca seus contatos. Entrar com uma conta é opcional; o app funciona completamente sem ela.

PRIVACIDADE EM PRIMEIRO LUGAR
• Sem servidores próprios: tudo é processado no seu celular.
• Sem anúncios e sem rastreadores.
• Não lê o seu registro de chamadas.
• Seus contatos são consultados apenas localmente, para nunca serem bloqueados.

DISPONÍVEL EM 9 IDIOMAS
English, Español, Português, Français, Deutsch, Русский, 中文, 日本語 e 한국어. Mude o idioma no próprio app (ícone do globo).

CÓDIGO ABERTO
O OpenCallShield é gratuito e open source. Veja o projeto, sugira melhorias ou crie sua própria versão:
{REPO}

Uma alternativa transparente e descentralizada para se proteger do SPAM telefônico.""",
notes="""• O app agora está disponível em 9 idiomas: inglês, espanhol, português, francês, alemão, russo, chinês, japonês e coreano.
• Novo seletor de idioma (ícone do globo), também disponível nas configurações do Android.
• Os nomes dos países aparecem no seu idioma.
• Atualizado para o Android 16 (API 36).
• Melhorias de estabilidade.""",
)

L["fr-FR"] = dict(
title="OpenCallShield : Anti-SPAM",
short="Bloquez les appels SPAM grâce à une liste collaborative. Privé, gratuit, libre.",
full=f"""OpenCallShield bloque automatiquement les appels SPAM, sans publicité et dans le respect de votre vie privée. C'est un logiciel libre (licence MIT) : son code est public et chacun peut le vérifier.

COMMENT ÇA MARCHE
Il utilise le service officiel de filtrage d'appels d'Android (CallScreeningService, Android 10 ou ultérieur). Quand un appel arrive, OpenCallShield décide sur votre propre appareil de le laisser passer, de le mettre en silence ou de le rejeter, selon vos règles.

RÈGLES DE DÉTECTION
• Les appels de vos contacts enregistrés sonnent toujours.
• Les numéros signalés comme SPAM (par vous ou par la liste collaborative) sont bloqués.
• Bloquez des pays entiers en touchant leur drapeau : inutile de saisir des préfixes.
• En option : bloquer les numéros inconnus absents de vos contacts.
Vous choisissez de rejeter l'appel ou simplement de le mettre en silence (il reste en appel manqué).

LISTE COLLABORATIVE
L'application synchronise une liste publique de numéros SPAM hébergée sur GitHub. Si vous le souhaitez, vous pouvez partager vos signalements pour aider la communauté : seuls les numéros marqués comme SPAM sont partagés, jamais vos contacts. La connexion est facultative ; l'application fonctionne entièrement sans compte.

LA CONFIDENTIALITÉ D'ABORD
• Aucun serveur : tout est traité sur votre téléphone.
• Ni publicité ni traqueurs.
• Aucun accès à votre journal d'appels.
• Vos contacts ne sont consultés que localement, pour ne jamais les bloquer.

DISPONIBLE EN 9 LANGUES
English, Español, Português, Français, Deutsch, Русский, 中文, 日本語 et 한국어. Changez de langue depuis l'application (icône du globe).

LOGICIEL LIBRE
OpenCallShield est gratuit et open source. Consultez le projet, proposez des améliorations ou créez votre propre version :
{REPO}

Une alternative transparente et décentralisée pour vous protéger du SPAM téléphonique.""",
notes="""• L'application est désormais disponible en 9 langues : anglais, espagnol, portugais, français, allemand, russe, chinois, japonais et coréen.
• Nouveau sélecteur de langue (icône du globe), aussi accessible dans les paramètres d'Android.
• Les noms des pays s'affichent dans votre langue.
• Mise à jour pour Android 16 (API 36).
• Améliorations de stabilité.""",
)

L["de-DE"] = dict(
title="OpenCallShield: SPAM-Blocker",
short="Blockiere SPAM-Anrufe mit einer Gemeinschaftsliste. Privat, gratis, Open Source.",
full=f"""OpenCallShield blockiert SPAM-Anrufe automatisch – ohne Werbung und mit Respekt für deine Privatsphäre. Die App ist Open Source (MIT-Lizenz): Der Code ist öffentlich und jeder kann ihn prüfen.

SO FUNKTIONIERT ES
Die App nutzt den offiziellen Anruffilter-Dienst von Android (CallScreeningService, ab Android 10). Bei einem eingehenden Anruf entscheidet OpenCallShield direkt auf deinem Gerät, ob er durchgelassen, stummgeschaltet oder abgelehnt wird – nach deinen Regeln.

ERKENNUNGSREGELN
• Anrufe von gespeicherten Kontakten klingeln immer.
• Als SPAM gemeldete Nummern (von dir oder aus der Gemeinschaftsliste) werden blockiert.
• Blockiere ganze Länder mit einem Tipp auf ihre Flagge – ohne Vorwahlen einzutippen.
• Optional: unbekannte Nummern blockieren, die nicht in deinen Kontakten sind.
Du entscheidest, ob der Anruf abgelehnt oder nur stummgeschaltet wird (er erscheint dann als verpasst).

GEMEINSCHAFTSLISTE
Die App synchronisiert eine öffentliche Liste von SPAM-Nummern auf GitHub. Wenn du möchtest, kannst du deine Meldungen beitragen und so anderen helfen: Geteilt werden nur als SPAM markierte Nummern, niemals deine Kontakte. Eine Anmeldung ist optional; die App funktioniert vollständig ohne Konto.

DATENSCHUTZ ZUERST
• Keine eigenen Server: Alles wird auf deinem Telefon verarbeitet.
• Keine Werbung, keine Tracker.
• Kein Zugriff auf deine Anrufliste.
• Deine Kontakte werden nur lokal abgefragt, damit sie nie blockiert werden.

IN 9 SPRACHEN VERFÜGBAR
English, Español, Português, Français, Deutsch, Русский, 中文, 日本語 und 한국어. Die Sprache änderst du direkt in der App (Globus-Symbol).

OPEN SOURCE
OpenCallShield ist kostenlos und Open Source. Sieh dir das Projekt an, schlage Verbesserungen vor oder erstelle deine eigene Version:
{REPO}

Eine transparente, dezentrale Alternative zum Schutz vor Telefon-SPAM.""",
notes="""• Die App ist jetzt in 9 Sprachen verfügbar: Englisch, Spanisch, Portugiesisch, Französisch, Deutsch, Russisch, Chinesisch, Japanisch und Koreanisch.
• Neue Sprachauswahl (Globus-Symbol), auch in den Android-Einstellungen.
• Ländernamen werden in deiner Sprache angezeigt.
• Aktualisiert für Android 16 (API 36).
• Stabilitätsverbesserungen.""",
)

L["ru-RU"] = dict(
title="OpenCallShield: блок спама",
short="Блокируйте спам-звонки с помощью общей базы. Приватно, бесплатно, открыто.",
full=f"""OpenCallShield автоматически блокирует спам-звонки — без рекламы и с уважением к вашей конфиденциальности. Это приложение с открытым исходным кодом (лицензия MIT): код публичен, и любой может его проверить.

КАК ЭТО РАБОТАЕТ
Приложение использует официальную службу фильтрации звонков Android (CallScreeningService, Android 10 и новее). Когда поступает звонок, OpenCallShield прямо на вашем устройстве решает, пропустить его, отключить звук или отклонить — по вашим правилам.

ПРАВИЛА ОБНАРУЖЕНИЯ
• Звонки от сохранённых контактов всегда проходят.
• Номера, отмеченные как спам (вами или в общей базе), блокируются.
• Блокируйте целые страны одним нажатием на флаг — вводить коды не нужно.
• По желанию: блокировать неизвестные номера, которых нет в контактах.
Вы выбираете: отклонить звонок или просто отключить звук (он останется пропущенным).

ОБЩАЯ БАЗА
Приложение синхронизирует публичный список спам-номеров на GitHub. При желании вы можете отправлять свои жалобы, чтобы помочь другим: передаются только номера, отмеченные как спам, и никогда — ваши контакты. Вход в аккаунт необязателен; приложение полностью работает без него.

КОНФИДЕНЦИАЛЬНОСТЬ ПРЕЖДЕ ВСЕГО
• Нет собственных серверов: всё обрабатывается на вашем телефоне.
• Без рекламы и трекеров.
• Не читает журнал звонков.
• Контакты проверяются только локально, чтобы никогда их не блокировать.

9 ЯЗЫКОВ
English, Español, Português, Français, Deutsch, Русский, 中文, 日本語 и 한국어. Язык меняется прямо в приложении (значок глобуса).

ОТКРЫТЫЙ КОД
OpenCallShield бесплатен и открыт. Посмотрите проект, предложите улучшения или создайте свою версию:
{REPO}

Прозрачная децентрализованная защита от телефонного спама.""",
notes="""• Приложение теперь доступно на 9 языках: английском, испанском, португальском, французском, немецком, русском, китайском, японском и корейском.
• Новый выбор языка (значок глобуса), также в настройках Android.
• Названия стран отображаются на вашем языке.
• Обновлено для Android 16 (API 36).
• Улучшена стабильность.""",
)

L["zh-CN"] = dict(
title="OpenCallShield：骚扰电话拦截",
short="借助社区共享名单拦截骚扰电话。注重隐私、免费、开源。",
full=f"""OpenCallShield 自动拦截骚扰电话，无广告，尊重你的隐私。本应用开源（MIT 许可证）：代码公开，任何人都可以审查。

工作原理
使用 Android 官方来电过滤服务（CallScreeningService，Android 10 及以上）。有来电时，OpenCallShield 会在你自己的设备上按照你的规则决定放行、静音或拒接。

识别规则
• 已保存联系人的来电始终会响铃。
• 被举报为骚扰的号码（由你或社区共享名单举报）会被拦截。
• 点按国旗即可拦截整个国家/地区的来电，无需输入区号。
• 可选：拦截不在通讯录中的陌生号码。
你可以选择拒接来电，或仅将其静音（会显示为未接来电）。

社区共享名单
应用会同步托管在 GitHub 上的公共骚扰号码名单。你也可以贡献自己的举报来帮助他人：只会分享被标记为骚扰的号码，绝不会分享你的联系人。登录是可选的，不登录也能使用全部功能。

隐私优先
• 没有自己的服务器：所有处理都在你的手机上完成。
• 无广告，无追踪器。
• 不读取你的通话记录。
• 仅在本地查询联系人，确保不会拦截他们。

支持 9 种语言
English、Español、Português、Français、Deutsch、Русский、中文、日本語 和 한국어。可在应用内（地球图标）切换语言。

开源
OpenCallShield 免费且开源。欢迎查看项目、提出改进建议或创建你自己的版本：
{REPO}

一个透明、去中心化的骚扰电话防护方案。""",
notes="""• 应用现已支持 9 种语言：英语、西班牙语、葡萄牙语、法语、德语、俄语、中文、日语和韩语。
• 新增语言选择器（地球图标），也可在 Android 设置中更改。
• 国家/地区名称以你的语言显示。
• 已适配 Android 16（API 36）。
• 稳定性改进。""",
)

L["ja-JP"] = dict(
title="OpenCallShield 迷惑電話ブロック",
short="みんなで共有するリストで迷惑電話をブロック。プライバシー重視・無料・オープンソース。",
full=f"""OpenCallShield は迷惑電話を自動でブロックします。広告はなく、プライバシーを尊重します。オープンソース（MIT ライセンス）なので、コードは公開されており誰でも確認できます。

しくみ
Android 公式の着信フィルタサービス（CallScreeningService、Android 10 以降）を使います。着信があると、OpenCallShield があなたの端末上で、ルールに従って通す・消音する・拒否するを判断します。

検出ルール
• 保存済みの連絡先からの着信は必ず鳴ります。
• 迷惑電話として報告された番号（あなた自身または共有リスト）はブロックされます。
• 国旗をタップするだけで国・地域ごとブロック。番号を入力する必要はありません。
• 任意：連絡先にない不明な番号をブロック。
着信を拒否するか、消音だけにするか（不在着信として残ります）を選べます。

共有リスト
アプリは GitHub 上の公開迷惑電話リストを同期します。希望すれば自分の報告を提供してみんなを助けることもできます。共有されるのは迷惑電話としてマークした番号だけで、連絡先は決して共有されません。ログインは任意で、アカウントなしでもすべての機能を使えます。

プライバシー第一
• 独自のサーバーはありません。すべてスマートフォン上で処理します。
• 広告もトラッカーもありません。
• 通話履歴は読み取りません。
• 連絡先はブロックしないために端末内でのみ確認します。

9 言語に対応
English、Español、Português、Français、Deutsch、Русский、中文、日本語、한국어。アプリ内（地球アイコン）で言語を切り替えられます。

オープンソース
OpenCallShield は無料のオープンソースです。プロジェクトを見たり、改善を提案したり、独自版を作ったりできます：
{REPO}

迷惑電話から身を守る、透明で分散型の選択肢です。""",
notes="""• アプリが 9 言語に対応しました：英語、スペイン語、ポルトガル語、フランス語、ドイツ語、ロシア語、中国語、日本語、韓国語。
• 新しい言語選択（地球アイコン）。Android の設定からも変更できます。
• 国名があなたの言語で表示されます。
• Android 16（API 36）に対応。
• 安定性を改善しました。""",
)

L["ko-KR"] = dict(
title="OpenCallShield: 스팸 전화 차단",
short="커뮤니티 공유 목록으로 스팸 전화를 차단하세요. 개인정보 보호, 무료, 오픈 소스.",
full=f"""OpenCallShield는 광고 없이 개인정보를 존중하며 스팸 전화를 자동으로 차단합니다. 오픈 소스(MIT 라이선스)이므로 코드가 공개되어 있으며 누구나 검토할 수 있습니다.

작동 방식
Android 공식 통화 필터 서비스(CallScreeningService, Android 10 이상)를 사용합니다. 전화가 오면 OpenCallShield가 사용자의 규칙에 따라 기기에서 직접 연결, 무음 처리 또는 거절을 결정합니다.

감지 규칙
• 저장된 연락처의 전화는 항상 울립니다.
• 스팸으로 신고된 번호(직접 신고했거나 공유 목록에 있는 번호)는 차단됩니다.
• 국기를 탭하여 국가 전체를 차단할 수 있습니다. 국가 번호를 입력할 필요가 없습니다.
• 선택 사항: 연락처에 없는 모르는 번호 차단.
전화를 거절할지, 무음 처리만 할지(부재중 전화로 남음) 선택할 수 있습니다.

커뮤니티 공유 목록
앱은 GitHub에 있는 공개 스팸 번호 목록을 동기화합니다. 원하면 직접 신고한 번호를 기여하여 다른 사람을 도울 수 있습니다. 스팸으로 표시한 번호만 공유되며 연락처는 절대 공유되지 않습니다. 로그인은 선택 사항이며 계정 없이도 모든 기능을 사용할 수 있습니다.

개인정보 보호 우선
• 자체 서버가 없습니다. 모든 처리는 휴대전화에서 이루어집니다.
• 광고와 추적기가 없습니다.
• 통화 기록을 읽지 않습니다.
• 연락처는 차단하지 않기 위해 기기에서만 확인합니다.

9개 언어 지원
English, Español, Português, Français, Deutsch, Русский, 中文, 日本語, 한국어. 앱에서 바로 언어를 바꿀 수 있습니다(지구본 아이콘).

오픈 소스
OpenCallShield는 무료 오픈 소스입니다. 프로젝트를 보고, 개선을 제안하거나 나만의 버전을 만들어 보세요:
{REPO}

전화 스팸으로부터 나를 지키는 투명하고 분산된 대안입니다.""",
notes="""• 이제 9개 언어를 지원합니다: 영어, 스페인어, 포르투갈어, 프랑스어, 독일어, 러시아어, 중국어, 일본어, 한국어.
• 새로운 언어 선택(지구본 아이콘). Android 설정에서도 변경할 수 있습니다.
• 국가 이름이 사용자의 언어로 표시됩니다.
• Android 16(API 36)에 맞게 업데이트되었습니다.
• 안정성이 개선되었습니다.""",
)

LIMITES = {"title": 30, "short": 80, "full": 4000, "notes": 500}
ARCHIVO = {"title": "title.txt", "short": "short_description.txt",
           "full": "full_description.txt", "notes": "release_notes.txt"}

errores = []
for lang, d in L.items():
    for k, lim in LIMITES.items():
        if len(d[k]) > lim:
            errores.append(f"{lang}.{k}: {len(d[k])}/{lim}")
if errores:
    print("\n".join(errores)); sys.exit(1)

for lang, d in L.items():
    carpeta = os.path.join(AQUI, lang)
    os.makedirs(carpeta, exist_ok=True)
    for k, nombre in ARCHIVO.items():
        with open(os.path.join(carpeta, nombre), "w", encoding="utf-8", newline="\n") as f:
            f.write(d[k].strip() + "\n")
    print(f"OK {lang:6s} titulo {len(d['title'])}/30 · breve {len(d['short'])}/80 · "
          f"completa {len(d['full'])}/4000 · notas {len(d['notes'])}/500")
