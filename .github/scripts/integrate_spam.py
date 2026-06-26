#!/usr/bin/env python3
"""
Integra los numeros propuestos en un Issue de aporte dentro de spam_numbers.json.

Lee el cuerpo del Issue desde la variable de entorno ISSUE_BODY (asi se evita la
inyeccion de shell), extrae el primer bloque ```json ... ```, lo fusiona con la
base existente (sin duplicados, sumando reportes) y reescribe el archivo.
"""
import json
import os
import re
import sys
from datetime import datetime, timezone

DB_PATH = "spam_numbers.json"


def normalize(num: str) -> str:
    """Misma normalizacion que la app: conserva digitos y '+'."""
    return "".join(c for c in str(num) if c.isdigit() or c == "+")


def extract_json_block(body: str) -> str | None:
    # Bloque ```json ... ```
    m = re.search(r"```json\s*(.*?)```", body, re.DOTALL | re.IGNORECASE)
    if m:
        return m.group(1).strip()
    # Fallback: cualquier bloque ``` ... ``` que contenga un array
    m = re.search(r"```\s*(\[.*?\])\s*```", body, re.DOTALL)
    return m.group(1).strip() if m else None


def main() -> int:
    body = os.environ.get("ISSUE_BODY", "") or ""
    raw = extract_json_block(body)
    if not raw:
        print("No se encontro un bloque JSON en el Issue. Nada que integrar.")
        return 0

    try:
        incoming = json.loads(raw)
    except json.JSONDecodeError as exc:
        print(f"JSON invalido en el Issue: {exc}")
        return 1

    if not isinstance(incoming, list):
        print("El JSON del Issue no es una lista. Nada que integrar.")
        return 0

    # Cargar base existente (o crear estructura nueva).
    if os.path.exists(DB_PATH):
        with open(DB_PATH, encoding="utf-8") as fh:
            db = json.load(fh)
    else:
        db = {"version": "1.0", "updated_at": "", "numbers": []}

    numbers = db.get("numbers", [])
    index = {normalize(n.get("number", "")): n for n in numbers}

    added, updated = 0, 0
    for item in incoming:
        if not isinstance(item, dict):
            continue
        num = normalize(item.get("number", ""))
        if not num:
            continue
        reports = int(item.get("reports", 1) or 1)
        tag = str(item.get("tag", "spam")) or "spam"
        if num in index:
            existing = index[num]
            existing["reports"] = int(existing.get("reports", 1)) + reports
            updated += 1
        else:
            entry = {"number": num, "reports": reports, "tag": tag}
            numbers.append(entry)
            index[num] = entry
            added += 1

    db["numbers"] = numbers
    db["updated_at"] = datetime.now(timezone.utc).strftime("%Y-%m-%d")

    with open(DB_PATH, "w", encoding="utf-8") as fh:
        json.dump(db, fh, ensure_ascii=False, indent=2)
        fh.write("\n")

    print(f"Integrados: {added} nuevos, {updated} actualizados. Total: {len(numbers)}.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
