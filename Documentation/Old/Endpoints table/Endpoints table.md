# Рольова модель доступу (RBAC)

Роль	    Опис	                        Доступ

Public	    Неавторизований користувач	    Перегляд фільмів, реєстрація, логін
User	    Авторизований користувач	    Перегляд контенту, профіль, історія, AI чат
Admin	    Адміністратор системи	        Повний CRUD доступ до всіх ресурсів

# Основні функціональні модулі MVP
* Аутентифікація та користувачі
* Каталог фільмів
* AI-консультант (чат + рекомендації)
* Історія переглядів
* Адміністрування контенту

# Зведена таблиця ендпоінтів
## Аутентифікація та користувачі
Метод   	Endpoint	                Опис	                   Params / Body      	Доступ	    Успіх	Помилки

POST	    /api/v1/auth/register	    Реєстрація користувача	   email, password	    Public	    201     Created + userId	400 (invalid), 409 (exists)
POST	    /api/v1/auth/login	        Авторизація	               email, password	    Public	    200     OK + JWT	401 (invalid creds)
GET	        /api/v1/users/me	        Отримати свій профіль	   -	                User	    200     OK + user data	401
PATCH	    /api/v1/users/me	        Оновити профіль	           name, avatar	        User	    200     OK	400, 401
DELETE	    /api/v1/users/me	        Видалити акаунт	           -	                User	    204     No Content	401

## Каталог фільмів
Метод   	Endpoint	            Опис	            Params / Body	    Доступ	    Успіх	        Помилки

GET	        /api/v1/movies	        Список фільмів	    page, size, genre	Public	    200 OK + list	400
GET	        /api/v1/movies/{id}	    Деталі фільму	    id	                Public	    200 OK	        404
GET	        /api/v1/movies/search	Пошук фільмів	    query	            Public	    200 OK	        400

## AI-консультант
Метод	    Endpoint	                Опис	                        Params / Body	    Доступ	    Успіх	            Помилки

POST	    /api/v1/ai/messages	        Надіслати повідомлення AI	    message	            User	    200 OK + response	400, 401
GET	        /api/v1/ai/recommendations	Отримати рекомендації	        optional: genre	    User	    200 OK + list	    401

## Історія переглядів
Метод	    Endpoint	            Опис	                Params / Body	    Доступ      Успіх	            Помилки

GET	        /api/v1/history	        Історія переглядів	    -	                User	    200 OK	            401
POST	    /api/v1/history	        Додати перегляд	        movieId, progress	User	    201 Created	        400, 401
DELETE	    /api/v1/history/{id}	Видалити запис	        id	                User	    204 No Content	    401, 404

## Адміністрування фільмів
Метод	    Endpoint	            Опис	            Params / Body	            Доступ      Успіх	         Помилки

POST	    /api/v1/movies	        Додати фільм	    title, description, genre	Admin	    201 Created	     400
PUT	        /api/v1/movies/{id}	    Оновити фільм	    full object	                Admin	    200 OK	         404
DELETE	    /api/v1/movies/{id} 	Видалити фільм	    id	                        Admin	    204 No Content	 404
PATCH	    /api/v1/movies/{id} 	Часткове оновлення	fields	                    Admin	    200 OK	         400

