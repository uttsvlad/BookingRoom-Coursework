### 👨‍🎓 "Гостиничный бизнес" - разработка информационной системы. Курсовая работа.

Данная информационная система была разработана в качестве курсового проекта в рамках дисциплины **"Программные средства манипулирования данными"** в 1-ом семестре 3-го курса.</br>
Работа была защищена на оценку "".

---

 <p align="center">
   <b> ℹ️ Основная информация. </b>  
</p>

🖥️ Информационная система **используется** для бронирования комнат в отеле. <br/> 
🧑‍💻 Данная работа была **выполнена** [uttsvlad](https://github.com/uttsvlad), [kovalyov-z3](https://github.com/kovalyov-z3) и [coder-chekunkov](https://github.com/coder-chekunkov). <br/>
📫 Разработчики **всегда рады услышать** отзыв и мнение о данном проекте. <br/>
🛠️ В качестве шаблона разработки используется **микросервисная архитектура**. <br/>
🪃 Общение сервисов обеспечено по **REST**. <br/>
👀 Количество **посещений**: ![](https://visitor-badge.glitch.me/badge?page_id=coder-chekunkov.BookingRoom-Coursework)

<code><img src="https://avatars.githubusercontent.com/u/22798591?s=200&v=4" width="25" height="25"></code>
<code><img src="https://raw.githubusercontent.com/github/explore/caa262eeb858e81282d6f651d6eef1f8730b54ba/topics/intellij-idea/intellij-idea.png" width="25" height="25"></code>
<code><img src="https://raw.githubusercontent.com/github/explore/44926f43f6a0d183b5965bebd1e77069ab00c26a/topics/android-studio/android-studio.png" width="25" height="25"></code>
<code><img src="https://raw.githubusercontent.com/github/explore/80688e429a7d4ef2fca1e82350fe8e3517d3494d/topics/postgresql/postgresql.png" width="25" height="25"></code>
<code><img src="https://raw.githubusercontent.com/github/explore/80688e429a7d4ef2fca1e82350fe8e3517d3494d/topics/kotlin/kotlin.png" width="25" height="25"></code>
<code><img src="https://raw.githubusercontent.com/github/explore/8ab0be27a8c97992e4930e630e2d68ba8d819183/topics/spring/spring.png" width="25" height="25"></code>
<code><img src="https://raw.githubusercontent.com/github/explore/80688e429a7d4ef2fca1e82350fe8e3517d3494d/topics/android/android.png" width="25" height="25"></code>

---
 <p align="center">
   <b> 💾 База данных. </b>  
</p>

💡База данных используется для хранения всех сведений, необходимых информационной системе.

📂 Весь основной **функционал** реализован с помощью [PostgreSQL](https://github.com/postgres/postgres). <br/>
📝 **Скрипт** создания и заполнения базы данных приведен [здесь](https://github.com/coder-chekunkov/BookingRoom-Coursework/blob/main/postgreSQL/courseWork.sql). <br/>
🔐 В базе данных была реализована **триггер-функция** хэширования пароля. <br/>
👥 Все доступные **роли** созданы в базе данных. <br/> 
✒️ Дополнительно были реализованы **функции**, **процедуры** и **триггеры**. <br/>

📖 Более подробно о базе данных можно узнать в [технической документации](https://github.com/coder-chekunkov/BookingRoom-Coursework/blob/main/documents/%D0%A2%D0%B5%D1%85%D0%BD%D0%B8%D1%87%D0%B5%D1%81%D0%BA%D0%B0%D1%8F%20%D0%B4%D0%BE%D0%BA%D1%83%D0%BC%D0%B5%D0%BD%D1%82%D0%B0%D1%86%D0%B8%D1%8F.pdf).

 <p align="center">
   <b> 💽 Серверная часть. </b>  
</p>

💡Серверная часть информационной системы используется для связи с базой данных.

💻 Серверная часть была разработана на **языке программирования** "Kotlin". <br/>
⚒️ При разработки использовался **фреймворк** "Spring". <br/>
🪚 Задействованные **модули**: Spring Core, Spring Security, Spring Data и Spring Web. <br/>
📜 Для обработки входящих запросов были созданы **REST-контроллеры**: <br/>
 - AuthenticationController.  <br/>
 - AdministratorController. <br/>
 - ClientController. <br/>
 - SuperAdminController. <br/>

📖 Более подробно о серверной части можно узнать в [технической документации](https://github.com/coder-chekunkov/BookingRoom-Coursework/blob/main/documents/%D0%A2%D0%B5%D1%85%D0%BD%D0%B8%D1%87%D0%B5%D1%81%D0%BA%D0%B0%D1%8F%20%D0%B4%D0%BE%D0%BA%D1%83%D0%BC%D0%B5%D0%BD%D1%82%D0%B0%D1%86%D0%B8%D1%8F.pdf).

 <p align="center">
   <b> 📱 Мобильное приложение. </b>  
</p>

💡 В качестве графического интерфейса для информационной системы было разработано мобильное приложения для ОС Android. 

🖥️ Приложение было разработано на **языке программирования** "Kotlin". <br/>
🛠️ Основные **инструменты**: Android SDK, Android-Jetpack. <br/>
🔧 **Библиотеки**, использующиеся при разработки приложения: [Retrofit 2](https://github.com/square/retrofit), [GSON](https://github.com/google/gson), [Glide](https://github.com/bumptech/glide), [EmojiRain](https://github.com/Luolc/EmojiRain), [CarouselView](https://github.com/sayyam/carouselview).<br/>
📜 В приложении реализованы следующие **функции**: <br/>
 - Авторизация / регистрация пользователей.  <br/>
 - Отображение всей информации о пользователе. <br/>
 - Бронирование номера в гостинице клиентом. <br/>
 - Отображение данных о заявке клиента. <br/>
 - Возможность принять / отклонить заявку клиента администратором. <br/>
 - Возможность добавить сумму ущерба администратором.
 
 📖 Более подробно о графическом интерфейсе можно узнать в [технической документации](https://github.com/coder-chekunkov/BookingRoom-Coursework/blob/main/documents/%D0%A2%D0%B5%D1%85%D0%BD%D0%B8%D1%87%D0%B5%D1%81%D0%BA%D0%B0%D1%8F%20%D0%B4%D0%BE%D0%BA%D1%83%D0%BC%D0%B5%D0%BD%D1%82%D0%B0%D1%86%D0%B8%D1%8F.pdf).


 <p align="center">
   <b> 🖼️ Галерея мобильного приложения. </b>  
</p>

 🔗 Для просмотра всех изображений **перейдите** по [ссылке](https://github.com/coder-chekunkov/BookingRoom-Coursework/tree/main/documents/wiki_images).
 
 
<p align="center">
  <img alt="GIF" src="https://github.com/coder-chekunkov/BookingRoom-Coursework/blob/main/documents/wiki_images/wiki_image_1.jpg" width="230"/>
  <img alt="GIF" src="https://github.com/coder-chekunkov/BookingRoom-Coursework/blob/main/documents/wiki_images/wiki_image_2.jpg" width="230"/>
  <img alt="GIF" src="https://github.com/coder-chekunkov/BookingRoom-Coursework/blob/main/documents/wiki_images/wiki_image_20.jpg" width="230"/> </br>
  <img alt="GIF" src="https://github.com/coder-chekunkov/BookingRoom-Coursework/blob/main/documents/wiki_images/wiki_image_11.jpg" width="230"/>
  <img alt="GIF" src="https://github.com/coder-chekunkov/BookingRoom-Coursework/blob/main/documents/wiki_images/wiki_image_15.jpg" width="230"/>
  <img alt="GIF" src="https://github.com/coder-chekunkov/BookingRoom-Coursework/blob/main/documents/wiki_images/wiki_image_8.jpg" width="230"/>
</p>
 
---

🏆 Мы надеемся, что данная работа помогла Вам и достойна высокой оценки. <br/>
📧 При возникновении каких-либо вопросов и предложений - свяжитесь с разработчиками. <br/>
🤝 Спасибо, что заинтересовались проектом.
