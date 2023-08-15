# practicum-android-diploma

# Предварительная настройка проекта

## Добавление секретных констант для API HeadHunter

Для проброса секретных констант, полученных после регистрации приложения для использования API HeadHunter, создайте в
корне проекта файл `develop.properties` и добавьте туда два свойства:

```properties
hhClientId=my_client_id
hhClientSecret=my_client_secret
```

Вместо `my_client_id` и `my_client_secret` вставьте полученные после регистрации значения `Client ID` и `Client Secret`.
После изменения значений синхронизируйте проект.

Файл `develop.properties` игнорируется при коммитах в Git, поэтому можно не бояться, что эти значения попадут в открытый
доступ. Значения, записанные в файл `develop.properties`, будут добавлены в приложение на стадии сборки и попадут в
специальный объект, который называется `BuildConfig`. Подробнее про этот объект можно почитать
в [документации](https://developer.android.com/build/gradle-tips#share-custom-fields-and-resource-values-with-your-app-code).
