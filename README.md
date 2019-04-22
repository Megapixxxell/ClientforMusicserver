Клиент-серверное приложение для взаимодействия с тестовым сервером 
https://android.academy.e-legion.com/docs/#/
для отображения альбомов, песен и добавления комментариев, 
а также для возможности логина и регистрации на сервере.
При входе в приложение необходимо зарегистрироваться либо войти, 
используя логин и пароль, указанный при регистрации.
Далее отображается список альбомов с сервера.
При нажатии на альбом, осуществляется переход на список песен выбранного альбома.
Через пункт меню в виде иконки сообщения можно перейти на экран комментариев, 
с возможностью добавления комменатрия. 

Клиент-серверное взаимодействие осуществляется с помощью OkHttp3 и Retrofit2
Для отправки запросов и обработки результатов используется RxJava2
Также в пиложении используется библиотека Room для работы с базой данных, 
в которую записываются полученные с сервера ответы
для их корректного отображения, если интернет отключен.