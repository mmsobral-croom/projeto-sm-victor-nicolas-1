# Manual de Utilização — Comparador de Preços de Supermercados

## Estrutura de dados

- A ListaSequencial foi escolhida pois apresenta uma facilidade na inserção de dados no final dela e pela eficiência no acesso a elementos
- Foi identificado um ponto crítico na parte do construtor de ComparadorProdutos onde houve a oportunidade de usar ordenação para busca binária, mas não obtivemos bons resultados por se tratar de uma lista relativamente pequena onde o custo de ordenar e depois procurar não fez nenhuma diferença

## Descrição

Este programa realiza a busca de produtos nos supermercados Bistek, Giassi e Fort Atacadista, permitindo ao usuário montar uma cesta de compras virtual e identificar qual supermercado oferece o menor custo total para os itens selecionados.

---

## Cache de Buscas

Para otimizar o tempo de resposta e evitar requisições redundantes aos servidores dos supermercados, o sistema possui um mecanismo de cache local:
- **Armazenamento**: Os termos pesquisados e os dados básicos dos produtos encontrados (como `id`, `nome`, `marca` e `ean`) são salvos localmente em arquivos JSON no diretório raiz (ex: `cache_giassi.json`, `cache_bistek.json` e `cache_fort.json`).
- **Atualização em Tempo Real**: Quando um termo já existente no cache é pesquisado de novo, o programa lê as informações estáticas do cache e realiza uma consulta rápida por ID aos supermercados. Isso garante que os preços e a disponibilidade dos produtos estejam sempre atualizados para a comparação de valores, sem a necessidade de reprocessar uma busca por termo inteira.

---

## Requisitos para Execução

- Java 25 ou superior instalado
- Gradle disponível no projeto (via wrapper `gradlew`)
- Conexão com a internet (as buscas são realizadas nos sites dos supermercados em tempo real)

---

## Como Executar

No diretório raiz do projeto, execute o seguinte comando no terminal:

```
./gradlew run
```

No Windows, utilize:

```
gradlew run
```

---

## Fluxo de Uso

### 1. Busca de Produto

Ao iniciar, o programa solicita que o usuário digite o nome de um produto:

```
Digite um produto:
```

Informe o nome ou parte do nome do produto desejado e pressione Enter. O programa realizará a busca simultaneamente nos três supermercados.

**Observacao:** O campo nao pode ser deixado em branco.

---

### 2. Selecao do Produto

Apos a busca, o programa exibe a lista de produtos encontrados em comum nos tres supermercados, com os respectivos precos:

```
Produtos disponíveis:
1 - Leite Integral 1L | Bistek: R$ 5,99 | Giassi: R$ 6,20 | Fort: R$ 5,79
2 - Leite Desnatado 1L | Bistek: R$ 6,49 | Giassi: R$ 6,10 | Fort: R$ 6,30
```

Somente produtos identificados pelo mesmo codigo EAN (codigo de barras) nos tres supermercados sao exibidos, garantindo que a comparacao seja feita sobre o mesmo item.

Em seguida, o programa solicita:

```
Escolha uma opcao:
```

Digite o numero correspondente ao produto desejado e pressione Enter. O item sera adicionado a cesta.

**Observacao:** Caso nenhum produto seja encontrado nos tres supermercados simultaneamente, o programa informa "Nenhum produto encontrado." e prossegue para a proxima etapa.

---

### 3. Adicionar Mais Produtos

Apos cada selecao, o programa pergunta:

```
Deseja adicionar mais produtos? (s/n):
```

- Digite `s` para buscar e adicionar outro produto.
- Digite `n` para encerrar a montagem da cesta e visualizar o resultado final.

---

### 4. Resultado Final — Cesta e Ranking

Ao encerrar, o programa exibe todos os itens adicionados e o custo total calculado para cada supermercado, ordenados do mais barato ao mais caro:

```
=== Cesta ===
1 - Leite Integral 1L | Bistek: R$ 5,99 | Giassi: R$ 6,20 | Fort: R$ 5,79
2 - Arroz 5kg | Bistek: R$ 22,90 | Giassi: R$ 21,50 | Fort: R$ 23,10

=== Ranking por preço total da cesta ===
1° Giassi: R$ 27,70
2° Bistek: R$ 28,89
3° Fort Atacadista: R$ 28,89
```

O ranking indica em qual supermercado a cesta completa sairia mais barata.

---

## Observacoes Gerais

- O programa considera apenas produtos presentes nos tres supermercados ao mesmo tempo. Produtos exclusivos de um ou dois supermercados nao sao listados.
- Os precos exibidos correspondem ao momento da busca e podem variar conforme atualizacao nos sites dos supermercados.
- Nao ha limite de itens na cesta.
- Para encerrar o programa antecipadamente, pressione `Ctrl+C` no terminal e confirme com `S` quando solicitado.
