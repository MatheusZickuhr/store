## Como dar build no projeto
- Baixe o projeto: 

```
git clone https://github.com/MatheusZickuhr/store.git
```

- Crie o arquivo application-default.properties em src/main/resources/ para definir suas configurações
  locais do banco de dados. Exemplo de arquivo:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=postgres
```
- Por fim, para compilar e rodar o projeto:
```
cd store
// para rodar o projeto diretamente no terminal
// (requer o maven instalado no PATH do sistema operacional)
mvn spring-boot:run
// ou abra o projeto em sua IDE de preferência
```

## EndPoints disponíveis

- /orders/
- /products/
- /order_items/



### Como cadastrar um pedido

 Basta fazer um request do tipo POST no endpoint /orders/ contendo um objeto json representando o pedido. Exemplo:
```
{
    "price": 0,
    // valor entre 0 e 1
    "discountRate" : 0.1, 10%
    "finished": false
}
```

### Como atualizar um pedido

Basta fazer um request do tipo POST no endpoint /orders/ contendo um objeto json representando o pedido.
Como está sendo atualizado um pedido existente, deve ser enviado junto ao request o ID do pedido.
Lembrando que, quando o pedido for finalizado (finished = true), a propiedade price será calculada
automaticamente. Exemplo:
```
{
    "price": 0,
    "discountRate" : 0.2, 20%
    "finished": true
}
```

### Como excluir um pedido
Basta fazer um request do tipo DELETE no endpoint /orders/ contendo um objeto json representando o pedido.
Apenas o ID já é suficiente. Exemplo:

```
{
    "id": 1
}
```

### Como listar os pedidos

Basta fazer um request do tipo GET no endpoint /orders/. Também pode ser acessado com paginação usando /orders/0/
(página 0).

### Como cadastrar um produto

Basta fazer um request do tipo POST no endpoint /products/ contendo um objeto json representando o produto. Exemplo:

```
{
    "name": "product",
    "isService": false,
    "price": 4,
    "enabled": true
}
```

### Como atualizar um produto

Basta fazer um request do tipo POST no endpoint /products/ contendo um objeto json representando o produto.
Como está sendo atualizado um produto existente, deve ser enviado junto ao request o ID do produto. Exemplo:

```
{
    "id": 1,
    "name": "product",
    "isService": false,
    "price": 4,
    "enabled": true
}
```

### Como excluir um produto

Basta fazer um request do tipo DELETE no endpoint /products/ contendo um objeto json representando o produto.
Apenas o ID já é suficiente. Exemplo:

```
{
    "id": 1
}
```

### Como listar os produtos

Basta fazer um request do tipo GET no endpoint /products/. Também pode ser acessado com paginação usando /products/0/
(página 0).

### Como cadastrar um item do pedido

Basta fazer um request do tipo POST no endpoint /order_items/ contendo um objeto json representando o item do pedido.
Exemplo:

```
{
    "order": {"id" : 1},
    "product": {"id" : 1}
}
```

### Como atualizar um item do pedido

Basta fazer um request do tipo POST no endpoint /order_items/ contendo um objeto json representando o item do pedido.
como está sendo atualizado um item do pedido existente, deve ser enviado junto ao request o ID do item do pedido.
Exemplo:

```
{
    "id": 1,
    "order": {"id" : 1},
    "product": {"id" : 2}
}
```

### Como excluir um item do pedido
Basta fazer um request do tipo DELETE no endpoint /order_items/ contendo um objeto json representando o item do pedido.
Apenas o ID já é suficiente. Exemplo:
```
{
    "id": 1
}
```

### Como listar os itens do pedido

Basta fazer um request do tipo GET no endpoint /order_items/. Também pode ser acessado com paginação usando /order_items/0/
(página 0).