  Запросы к API и ответы определены в формате JSON.

## Формат ответов

Если **HTTP status code** равен 200, ответ от API приходит в JSON формате.

Если при выполнении запроса произошла ошибка и **HTTP-status** не равен 200, ответ от API приходит в следующем формате:
```
error_description
```

В **error_description** указано описание ошибки.

## Авторизация

#### POST /login - авторизация

**request header**
```
Authorization: Basic YWxleEB0ci5jb206MTIz
```
**"errors"**
```
message: "Bad credentials"
```

#### POST /register - регистрация

**request header**
```js
{
	email: String,
	password: String,
	passwordConfirmation: String
}
```
**"errors"**
```
message: "Password do not match"
message: "User already exists"
```
**"response"**
```
User created
```

## Методы API


### Пользователи

#### GET /users - Получение информации о пользователях
**"response"**
```js
{
    content: [
        {
            id: Integer,
            username: String,
            email: String
            age: Integer,
            description: String
        },
        ...
    ],
    last: Boolean,
    totalElements: Integer,
    totalPages: Integer,
    size: Integer,
    number: Integer,
    sort: ,
    first: Boolean,
    numberOfElements: Integer 
}
```
last - является последней страницей

totalElements - количество элементов

size - кол-во элементов на странице

number - 

sort - тип сортировки Asc/Desc

first: является первой страницей

numberOfElements: количество элементов 

#### GET /user/get/{id} - Получение информации о пользователе, {id} - идентификатор пользователя
**"response"**
```js
{
    id: Integer,
    username: String,
    email: String,
    age: Integer,
    description: String
}
```
**"errors"**
```
message: "USER_IS_NOT_FOUND"
```

#### PUT /user - Редактирование информации о пользователе, {id} - идентификатор пользователя
**"request"**
```js
{
    username: String,
    email: String,
    age: Integer,
    description: String
}
```
**"response"**
```js
{
    id: Integer,
    username: String,
    email: String,
    age: Integer,
    description: String
}
```


### Записи

#### GET /posts - Получение информации о записях
**"response"**
```js
{
    content: [
        {
		    id: Integer,
		    title: String,
		    body: String,
		    createdAt: Date,
		    updatedAt: Date,
            creator: {
                // Autor data
            }
        },
        ...
    ],
    last: Boolean,
    totalElements: Integer,
    totalPages: Integer,
    size: Integer,
    number: Integer,
    sort: ,
    first: Boolean,
    numberOfElements: Integer 
}
```
last - является последней страницей

totalElements - количество элементов

size - кол-во элементов на странице

number - 

sort - тип сортировки Asc/Desc

first: является первой страницей

numberOfElements: количество элементов 


#### GET /post/getByCreator/{creatorName} - Получение информации о записях, по имени автора
**"response"**
```js
{
    content: [
        {
		    id: Integer,
		    title: String,
		    body: String,
		    createdAt: Date,
		    updatedAt: Date,
            creator: {
                // Autor data
            }
        },
        ...
    ],
    last: Boolean,
    totalElements: Integer,
    totalPages: Integer,
    size: Integer,
    number: Integer,
    sort: ,
    first: Boolean,
    numberOfElements: Integer 
}
```
last - является последней страницей

totalElements - количество элементов

size - кол-во элементов на странице

number - 

sort - тип сортировки Asc/Desc

first: является первой страницей

numberOfElements: количество элементов 


#### GET /post/get/{id} - Получение информации о записе, {id} - идентификатор записи
**"response"**
```js
{
    id: Integer,
    title: String,
    body: String,
    createdAt: Date,
    updatedAt: Date,
    creator: {
        // Autor data
    }
}
```
**"errors"**
```
message: "POST_IS_NOT_FOUND"
```

#### POST /post - Создание пользователя
**"request"**
```js
{
    title: String,
    body: String
}
```
**"response"**
```js
{
    id: Integer,
    title: String,
    body: String,
    createdAt: Date,
    updatedAt: Date,
 	creator: {
        // Autor data
    }
}
```

#### PUT /post/{id} - Редактирование информации о записи, {id} - идентификатор задачи
**"request"**
```js
{
    title: String,
    body: String
}
```
**"response"**
```js
{
    id: Integer,
    title: String,
    body: String,
    createdAt: Date,
    updatedAt: Date,
 	creator: {
        // Autor data
    }
}
```
**"errors"**
```
message: "POST_IS_NOT_FOUND"
```

#### DELETE /post/{id} - Удаление информации о записи, {id} - идентификатор задачи
**"response"**
```
"Post {deleted post id} is deleted"
```
**"errors"**
```
message: "POST_IS_NOT_FOUND"
```