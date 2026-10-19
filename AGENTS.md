# Инструкции для агента (Cline)

Это шаблон Android-приложения. Сборка APK — только в GitHub Actions, эмулятор не нужен.

## Новое приложение из шаблона
1. `gh repo create <имя> --private --template Slavochkas/android-app-template --clone` и `cd <имя>`.
   Репозиторий ВСЕГДА приватный (`--private`).
2. Заменить `com.velvi.probazametki` на новое имя пакета везде:
   - `app/build.gradle.kts` (`namespace`, `applicationId`);
   - папка `app/src/main/java/com/velvi/newapp` → новый путь;
   - строка `package ...` в каждом `.kt` файле;
   - `app_name` в `app/src/main/res/values/strings.xml`, `rootProject.name` в `settings.gradle.kts`.
   Проверка: `git grep newapp` не должен ничего находить (кроме этого файла и README).
3. `git add -A && git commit -m "..." && git push`.
4. `gh run watch` — дождаться зелёной сборки. Если упала: `gh run view --log-failed`, исправить, снова push.
5. APK: `gh release download --pattern "*.apk"` или вкладка Releases (тег `build-N`, prerelease).

## Подпись (по желанию)
Без секретов APK подписан debug-ключом. Для своего ключа:
`base64 -w0 my.jks | gh secret set VELVI_KEYSTORE_BASE64`, затем `gh secret set` для
`VELVI_STORE_PASSWORD`, `VELVI_KEY_ALIAS`, `VELVI_KEY_PASSWORD`. После этого сделать push или `gh workflow run build.yml`.

## Запрещено
- Коммитить `*.jks`, `keystore/`, `keystore.properties`, `local.properties`, папки `build/`.
- Делать репозиторий публичным.
- Запускать Android-эмулятор.

- Готовый APK сам скачивай в две папки (PowerShell):
  New-Item -ItemType Directory C:\velvi\Cline -Force
  gh release download <тег> --pattern "*.apk" --dir C:\velvi\Cline --clobber
  New-Item -ItemType Directory "$env:USERPROFILE\OneDrive\VELVI\Cline" -Force
  Copy-Item C:\velvi\Cline\*.apk "$env:USERPROFILE\OneDrive\VELVI\Cline\" -Force
- Новые проекты (приложения) создавай только внутри C:\velvi\Cline\<имя-проекта>.
- Объясняй всё простыми словами, без сложных терминов. Если сборка на GitHub не получилась, сам посмотри причину, почини и отправь снова. Повторяй, пока не соберётся (но не больше 5 раз подряд). Потом сам скажи пользователю простыми словами: что было сломано, что ты починил и что всё готово.
