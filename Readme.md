# API de Rede Social

Essa API está em construção, a visão é criar um exemplo de API para rede social. 

Toda a API roda in-memory, ou seja, não temos ainda implementação de persitência de dados (que tal contribuir?).

 O principal foco dessa implementação é na intereção entre as partes e seus comportamentos.

Esse documento cobre:

- Define os recursos

- Define os modelos com seus respectivos objetos

- Alguns exemplos dos objetos usando JSON

- Lista as ações implementadas

- Lista as URLs e suas ações respectivas

Qualquer dúvida, pode mandar mensagem ou abre uma issue! Dicas também são muito bem-vindas! 

# Executando

Para executar basta rodar o spring boot na pasta do projeto.

No Linux, basta rodar o comando:

```shell
$ ./mvnw spring-boot:run
```

## Dependências:

- Spring Boot e Maven;

- Java 8;

O Spring/Maven cuida das depências do projeto.

# Design

## Recursos

Os principais recursos aceitos pela API são:

- Usuários;

- *Vídeos* / Fotos

- *Comentários*

Esses recursos, quando combinados, geram um tópico ( também chamado de post). A relação entre os recursos está melhor detalhado na figura abaixo:

![Image](/docs/images/2020-08-27-14-21-26-image.png)


## Relacionamentos

Os modelos para os recursos fica conforme o diagrama, onde:

- Um post contêm apenas 1 Foto e 1 usuário;
  
  - *O modelo da foto está incompleto, uma vez que o usuário pode para o mesmo post selecionar masis fotos*
  
  - *A lista de palavras chaves do modelo da foto ainda não está implementada*

- Um usuário pode conter N posts;
  
  - *A foto de usuário precisa ser redesenhada.*

- Uma foto pode conter N posts;

![Image](/docs/images/2020-08-27-14-45-25-image.png)

# Implementação

A API utiliza o padrão REST. 

Parar versionar a API, utilizei o prefixo `/v1`.

## Requisições

### Usuários

Todas as requisições de usuários tem o prefixo: `/v1/users`

| Método | URI                  | Ação                                                          |
| ------ | -------------------- | ------------------------------------------------------------- |
| GET    | /username/{username} | Responde com o userId (id) correspondente ao username enviado |
| GET    | /id/{userId}         | Responde com o usuário correspondente ao userId (id) enviado  |
| GET    | /id/{userId}/posts   | Responde com o(s) post(s) do userId (id) enviado              |
| GET    | /id/{userId}/photos  | Responde com a(s) foto(s) do userId (id) enviado              |
| POST   | /new                 | Cria um novo usuário. Recebe um userModel em JSON.            |
| POST   | /newFriend           | Criar uma conexão entre dois usários.                         |
| PATCH  | /update              | Atualiza um usuário existente. Recebe um userModel em JSON.   |
| DELETE | /delete              | Deleta o usuário passado como parâmetro(request-body).        |

## Posts

Todas as requisições de posts tem o prefixo: `/v1/posts`

| Método | URI  | Ação               |
| ------ | ---- | ------------------ |
| POST   | /new | Cria um novo Post. |

## Feed

Todas as requisições do feed tem o prefixo: `/v1/feed`

| Método | URI                | Ação                                                                                                  |
| ------ | ------------------ | ----------------------------------------------------------------------------------------------------- |
| GET    | /{username}/main   | Responde com a estrutura do feed principal do usuário(username) enviado.                              |
| GET    | /{username}/photos | Responde com a(s) foto(s) do feed principal com base no usuário(username) enviando.                   |
| GET    | /{username}/ads    | **Não implementado** :: Recupera a estrutura de propaganda(s) com base no usuário(username) enviando. |

## Fotos

Todas as requisições das fotos tem o prefixo: `/v1/photos`

(NENHUMA IMPLEMENTAÇÃO AINDA)

# Funcionamento

Acompanha junto ao projeto uma pequena interface, capaz de interagir com a API. 

O Controlador da página é o `HomeController.java`. A API conta com alguns usuáriso cadastrados, de forma que é possível interagir com a API.

Ao acessar a página de usuário, temos:

Endereço: `http://127.0.0.1:8080/user/neyjr`

![Image](/docs/images/2020-08-27-16-14-01-image.png)

## Interagindo com a API

### Novo usuário

Para adicionar um usuário, basta realizar uma requisição POST ao endereço: `/v1/users/new`, passando os dados do modelo conforme abaixo:

Dados do usuário:

```json
{
  "id": 0,
  "name": "Gusatvo",
  "username": "gutodisse",
  "password": "gutodisse",
  "description": "Quem se descreve, se limita!",
  "photoPath" : ""
}
```

Resposta(s):

1. Caso o usuário seja cadastrado:

```json
OK
```

2. Caso o usuário já seja cadastrado:
   
   - *Precisa mudar o header, trocar o Status para error.*

```json
Usuário já registrado
```

3. Caso o modelo esteja incompleto:
   
   - *Precisa mudar o header, trocar o Status para error.*
   
   ```json
   Usuário é obrigatório!
   ```
   
   ```json
   Nome é obrigatório!
   ```
   
   ```json
   Id é obrigatório!
   ```
   
   ```json
   Senha é obrigatório!
   ```

### Editar usuário

Para alterar um usuário, basta realizar uma requisição PATCH ao endereço: `/v1/users/update`, passando os dados do modelo conforme abaixo:

Dados do usuário:

```json
{
  "id": 0,
  "name": "Gustavo",
  "username": "gutodisse",
  "password": "gutodisse",
  "description": "Quem se descreve, se limita!",
  "photoPath" : ""
}
```

Possui as mesmas resposta da criação de usuário.

Por fim, ao acessar agora `http://127.0.0.1:8080/user/gutodisse`:

![Image](/docs/images/2020-08-27-19-37-10-image.png)

### Adicionando foto

Para adicionar basta clicar em adicionar foto, ou acessar o endereço:

`http://127.0.0.1:8080/user/gutodisse/new` (para o usuário que acabamos de criar)

Ou podemos fazer a chamada da API pelo Curl:

```shell
$ curl http://127.0.0.1:8080/v1/posts/new -X POST --form username=gutodisse -F title=TBT -F description=sds -F file=@minhaFoto.jpg
```

O resultado:

![Image](/docs/images/2020-08-27-20-04-36-image.png)


## Fazendo amigos

A rede social conta com três contas padrões que podem ser deletadas, elas existem apenas para facilitar o desenvolvimento inicial. Sendo os perfis:

| userId | Nome      | Usuário       | Link                                     |
| ------ | --------- |:-------------:| ---------------------------------------- |
| 0      | MeninoNey | neyjr         | http://127.0.0.1:8080/user/neyjr         |
| 1      | Marta     | martavsilva10 | http://127.0.0.1:8080/user/martavsilva10 |
| 2      | Gustavo   | gutodisse     | http://127.0.0.1:8080/user/gutodisse     |

Para adicionar um amigo a lista de amigos, basta enviar uma requisição POST para o endereço `/v1/users/newFriend` seguindo a seguinte regra:

1. O "ID" será o do novo amigo;

2. Username e Senha do usuário que está adicionando a lista de amigos;
   
   - A rede é assimétrica, ou seja, caso A adicione B, A será amigo de B - contudo, B não será amigo de A.

### Por exemplo:

Para adicionar Marta (userID=1) a lista de amigos de Gustavo, basta enviar a requisição dessa forma:

```json
{
    "id":1,
    "username":"gutodisse",
    "password":"gutodisse",
    "name":"gustavo"
}
```

A resposta segue o padrão adotado até aqui, se tudo ocorrer normalmente, teremos um "ok".



### Meus amigos

O resultado de fazer amizades é que agora é possível acessar o que chamamos de linha do tempo, o algoritmo irá montar uma lista de postagens interessantes com base no que seus amigos publicam.

Para recuperar a lista de post por usuário, basta requisistar por GET ao `/v1/feed/{username}/main`  lista de post e ao `/v1/feed/{username}/photos` a lista de fotos. Ambos os casos requisitamos usando o nome do usuário. 

Acompanha o projeto um exemplo de código de como criar um Feed, dentro da `HomeController.java`.

O resultado  da linha do tempo do usuário Neymar, fica assim:

`http://127.0.0.1:8080/user/neyjr/feed`

![Image](/docs/images/2020-08-30-00-33-13-image.png)
