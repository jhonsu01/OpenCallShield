#!/usr/bin/env node
/**
 * play_store_pack.mjs — Publica en Google Play, en UNA sola edicion atomica:
 *   - el AAB en el canal indicado, con notas de version en todos los idiomas,
 *   - la ficha (titulo, descripciones) de cada idioma,
 *   - icono, grafico de funciones y capturas de cada idioma,
 *   - el idioma predeterminado de la ficha.
 *
 * Lee playstore/listings/<idioma>/*.txt y playstore/screenshots/<idioma>/*.png.
 * Sin dependencias: reutiliza la cuenta de servicio de AndroidEntorno.
 *
 * Uso:
 *   node tools/play_store_pack.mjs --validate            (todo menos el commit)
 *   node tools/play_store_pack.mjs --aab app/build/outputs/bundle/release/app-release.aab
 *   node tools/play_store_pack.mjs --listings-only       (fichas y graficos, sin AAB)
 */
import { readFileSync, readdirSync, existsSync } from "node:fs";
import { createSign } from "node:crypto";
import { fileURLToPath } from "node:url";
import { dirname, resolve, join } from "node:path";

const RAIZ = resolve(dirname(fileURLToPath(import.meta.url)), "..");
const CREDENCIAL =
  "D:/ProyectosConIA/AndroidEntorno/execution/.secrets/play-service-account.json";
const PAQUETE = "com.jhonsu01.opencallshield";
const API = "https://androidpublisher.googleapis.com/androidpublisher/v3";
const SUBIDA = "https://androidpublisher.googleapis.com/upload/androidpublisher/v3";
const IDIOMA_PREDETERMINADO = "en-US";

const args = { track: "production", status: "completed" };
for (let i = 2; i < process.argv.length; i++) {
  const a = process.argv[i];
  if (a === "--aab") args.aab = process.argv[++i];
  else if (a === "--track") args.track = process.argv[++i];
  else if (a === "--status") args.status = process.argv[++i];
  else if (a === "--validate") args.validate = true;
  else if (a === "--listings-only") args.listingsOnly = true;
  else { console.error(`Argumento desconocido: ${a}`); process.exit(2); }
}
if (!args.aab && !args.listingsOnly) {
  console.error("Indica --aab <ruta> o --listings-only."); process.exit(2);
}

// --- Autenticacion (JWT de cuenta de servicio) -------------------------------
const b64 = (d) => Buffer.from(d).toString("base64url");
async function token() {
  const c = JSON.parse(readFileSync(CREDENCIAL, "utf8"));
  const t = Math.floor(Date.now() / 1000);
  const h = b64(JSON.stringify({ alg: "RS256", typ: "JWT" }));
  const p = b64(JSON.stringify({
    iss: c.client_email, scope: "https://www.googleapis.com/auth/androidpublisher",
    aud: "https://oauth2.googleapis.com/token", iat: t, exp: t + 3600,
  }));
  const s = b64(createSign("RSA-SHA256").update(`${h}.${p}`).sign(c.private_key));
  const r = await fetch("https://oauth2.googleapis.com/token", {
    method: "POST",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    body: new URLSearchParams({
      grant_type: "urn:ietf:params:oauth:grant-type:jwt-bearer", assertion: `${h}.${p}.${s}`,
    }),
  });
  const d = await r.json();
  if (!r.ok) throw new Error(`Autenticacion fallida: ${d.error_description || d.error}`);
  return d.access_token;
}

let TOKEN;
async function api(url, opts = {}) {
  const r = await fetch(url, { ...opts, headers: { Authorization: `Bearer ${TOKEN}`, ...(opts.headers || {}) } });
  const txt = await r.text();
  const d = txt ? JSON.parse(txt) : {};
  if (!r.ok) throw new Error(`HTTP ${r.status} ${opts.method || "GET"} ${url.split("/edits/")[1] || url}\n${d?.error?.message || txt}`);
  return d;
}
const json = (method, body) => ({
  method, headers: { "Content-Type": "application/json" }, body: JSON.stringify(body),
});

// --- Contenido local ----------------------------------------------------------
const DIR_FICHAS = join(RAIZ, "playstore", "listings");
const DIR_CAPTURAS = join(RAIZ, "playstore", "screenshots");
const leer = (lang, f) => readFileSync(join(DIR_FICHAS, lang, f), "utf8").trim();
const idiomas = readdirSync(DIR_FICHAS, { withFileTypes: true })
  .filter((e) => e.isDirectory() && existsSync(join(DIR_FICHAS, e.name, "title.txt")))
  .map((e) => e.name);

async function main() {
  TOKEN = await token();
  console.log(`Idiomas: ${idiomas.join(", ")}`);
  const edit = (await api(`${API}/applications/${PAQUETE}/edits`, { method: "POST" })).id;
  const E = `${API}/applications/${PAQUETE}/edits/${edit}`;
  const EU = `${SUBIDA}/applications/${PAQUETE}/edits/${edit}`;
  try {
    // 1. Fichas por idioma (antes del AAB: las notas solo valen en idiomas con ficha).
    for (const lang of idiomas) {
      await api(`${E}/listings/${lang}`, json("PUT", {
        language: lang,
        title: leer(lang, "title.txt"),
        shortDescription: leer(lang, "short_description.txt"),
        fullDescription: leer(lang, "full_description.txt"),
      }));
      // 2. Graficos: icono y grafico de funciones comunes; capturas por idioma.
      const subir = async (tipo, ruta) => api(`${EU}/listings/${lang}/${tipo}?uploadType=media`, {
        method: "POST", headers: { "Content-Type": "image/png" }, body: readFileSync(ruta),
      });
      for (const tipo of ["icon", "featureGraphic", "phoneScreenshots"]) {
        await api(`${E}/listings/${lang}/${tipo}`, { method: "DELETE" });
      }
      await subir("icon", join(RAIZ, "playstore", "icon_512.png"));
      await subir("featureGraphic", join(RAIZ, "playstore", "feature_1024x500.png"));
      const dir = join(DIR_CAPTURAS, lang);
      const capturas = readdirSync(dir).filter((f) => f.endsWith(".png")).sort();
      for (const f of capturas) await subir("phoneScreenshots", join(dir, f));
      console.log(`  ${lang}: ficha + icono + grafico + ${capturas.length} capturas`);
    }

    // 3. Idioma predeterminado de la ficha.
    const detalles = await api(`${E}/details`);
    if (detalles.defaultLanguage !== IDIOMA_PREDETERMINADO) {
      await api(`${E}/details`, json("PATCH", { defaultLanguage: IDIOMA_PREDETERMINADO }));
      console.log(`Idioma predeterminado: ${detalles.defaultLanguage} -> ${IDIOMA_PREDETERMINADO}`);
    }

    // 4. AAB + canal con notas en todos los idiomas.
    if (args.aab) {
      const bin = readFileSync(resolve(args.aab));
      console.log(`Subiendo ${args.aab} (${(bin.length / 1048576).toFixed(1)} MB)...`);
      const b = await api(`${EU}/bundles?uploadType=media`, {
        method: "POST", headers: { "Content-Type": "application/octet-stream" }, body: bin,
      });
      const notas = idiomas.map((lang) => ({ language: lang, text: leer(lang, "release_notes.txt") }));
      await api(`${E}/tracks/${args.track}`, json("PUT", {
        track: args.track,
        releases: [{ status: args.status, versionCodes: [String(b.versionCode)], releaseNotes: notas }],
      }));
      console.log(`versionCode ${b.versionCode} -> '${args.track}' (${args.status}), notas en ${notas.length} idiomas`);
    }

    await api(`${E}:validate`, { method: "POST" });
    console.log("Edicion validada por Google Play.");
    if (args.validate) {
      await api(E, { method: "DELETE" });
      console.log("--validate: edicion descartada, nada publicado.");
      return;
    }
    await api(`${E}:commit`, { method: "POST" });
    console.log("COMMIT OK: cambios enviados a Google Play.");
  } catch (e) {
    await api(E, { method: "DELETE" }).catch(() => {});
    throw e;
  }
}

main().catch((e) => { console.error(`\n[play] ${e.message}`); process.exit(1); });
