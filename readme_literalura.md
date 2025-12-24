# 📚 Literalura – Catálogo de Livros - Challenge One G9

Status do Projeto: ✔️ Concluído (versão console)

## 📚 Tópicos

🔹 [Descrição do projeto](#descrição-do-projeto)  
🔹 [Funcionalidades](#funcionalidades)  
🔹 [Layout da Aplicação](#layout-da-aplicação-)  
🔹 [Pré-requisitos](#pré-requisitos)  
🔹 [Como rodar a aplicação](#como-rodar-a-aplicação-️)  
🔹 [Casos de Uso](#casos-de-uso)  
🔹 [Linguagens e tecnologias utilizadas](#linguagens-e-tecnologias-utilizadas-)  
🔹 [Estrutura do projeto](#estrutura-do-projeto-)  
🔹 [Modelo de Dados](#modelo-de-dados-)  
🔹 [Melhorias futuras](#melhorias-futuras)  
🔹 [Desenvolvedores](#desenvolvedorescontribuintes)

---

## Descrição do projeto

O **Literalura** é uma aplicação de linha de comando desenvolvida em Java com Spring Boot que permite buscar, catalogar e consultar livros utilizando a API pública **Gutendex** (Project Gutenberg).

O foco do projeto é criar um catálogo pessoal de livros clássicos da literatura mundial, permitindo ao usuário:

- Buscar livros por título ou autor na API do Project Gutenberg
- Salvar automaticamente os livros encontrados em um banco de dados PostgreSQL
- Consultar o catálogo local por diferentes critérios (idioma, autor, downloads)
- Listar autores vivos em um determinado ano
- Visualizar o top 10 de livros mais baixados

Este projeto foi desenvolvido como parte do desafio **Literalura** do programa **ONE G9 (Oracle Next Education)**, com ênfase em:

- Consumo de APIs REST externas
- Persistência de dados com Spring Data JPA
- Relacionamentos ManyToMany entre entidades
- Manipulação de JSON
- Organização do código em camadas (layered architecture)
- Interface de console estilizada com códigos ANSI

A aplicação utiliza uma **interface colorida no terminal** para melhorar a experiência do usuário, tornando a navegação mais intuitiva e visualmente agradável.

---

## Funcionalidades

✔️ **Busca de livros por título** na API Gutendex e salvamento automático no banco  
✔️ **Busca de livros por autor** com persistência do primeiro resultado  
✔️ **Listagem completa** de todos os livros catalogados localmente  
✔️ **Filtro por idioma** para encontrar livros em português, inglês, espanhol, etc.  
✔️ **Listagem de todos os autores** salvos no catálogo  
✔️ **Consulta de autores vivos** em um ano específico (ex: autores vivos em 1900)  
✔️ **Top 10 livros mais baixados** do Project Gutenberg no seu catálogo  
✔️ **Relacionamento ManyToMany** entre livros e autores (um livro pode ter múltiplos autores)  
✔️ **Prevenção de duplicatas** através de constraint única no ID externo do Gutendex  
✔️ **Interface colorida (ANSI)** com ícones e formatação profissional  
✔️ **Menu interativo** organizado por categorias  
✔️ **Validação de entradas** do usuário com mensagens de erro claras  
✔️ **Persistência automática** com Hibernate (DDL auto-update)  

---

## Layout da Aplicação 💨

Esta é uma aplicação **de linha de comando (console)**, sem interface gráfica.

Ao executar o programa, o usuário verá um menu estilizado com cores e ícones:

```text
╔════════════════════════════════════════╗
║  LITERALURA - CATÁLOGO DE LIVROS  ║
╚════════════════════════════════════════╝

 📖 BUSCAR E ADICIONAR
   1 → Buscar livro por título
   2 → Buscar livro por autor

 📚 CONSULTAR LIVROS
   3 → Listar todos os livros salvos
   4 → Listar livros por idioma
   7 → Top 10 livros mais baixados

 ✍️  CONSULTAR AUTORES
   5 → Listar autores salvos
   6 → Listar autores vivos em determinado ano

 🚪 SAIR
   0 → Encerrar aplicação

➜ Escolha uma opção: 
```

### Screenshots

[<img src="./assets/menu-principal.png" width="500"><br><sub>Menu Principal</sub>](./assets/menu-principal.png)

<br>

[<img src="./assets/busca-titulo.png" width="500"><br><sub>Busca por Título</sub>](./assets/busca-titulo.png)

<br>

[<img src="./assets/lista-livros.png" width="500"><br><sub>Listagem de Livros</sub>](./assets/lista-livros.png)

<br>

[<img src="./assets/top10.png" width="500"><br><sub>Top 10 Mais Baixados</sub>](./assets/top10.png)

---

## Pré-requisitos

✅ **Java 17** ou superior  
✅ **PostgreSQL** instalado e rodando  
✅ **Maven** (ou usar o wrapper incluído no projeto)  
✅ **IntelliJ IDEA** ou outra IDE de sua preferência  

---

## Como rodar a aplicação ▶️

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/literalura-challenge-one-g9.git
cd literalura-challenge-one-g9
```

### 2. Configure o banco de dados

Crie um banco de dados PostgreSQL chamado `literalura`:

```sql
CREATE DATABASE literalura;
```

### 3. Configure as credenciais

Edite o arquivo `src/main/resources/application.yml` com suas credenciais do PostgreSQL:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/literalura
    username: seu_usuario
    password: sua_senha
```

### 4. Execute a aplicação

**Opção A - Via IDE:**
- Abra o projeto no IntelliJ IDEA
- Execute a classe `Application.java`

**Opção B - Via Maven:**
```bash
./mvnw spring-boot:run
```

**Opção C - Gerando JAR:**
```bash
./mvnw clean package
java -jar target/literalura-0.0.1-SNAPSHOT.jar
```

### 5. Use o menu

O Hibernate criará automaticamente as tabelas necessárias no primeiro run (modo `ddl-auto: update`).

---

## Casos de Uso

### 📖 Buscar e catalogar um livro

1. Selecionar opção **1** (Buscar livro por título)
2. Digite o título do livro (ex: "Pride and Prejudice")
3. O sistema busca na API Gutendex
4. O primeiro resultado é salvo automaticamente no banco
5. Detalhes do livro são exibidos

---

### 🌍 Consultar livros por idioma

1. Selecionar opção **4** (Listar livros por idioma)
2. Digite o código do idioma (ex: `pt`, `en`, `es`, `fr`)
3. Sistema exibe todos os livros salvos naquele idioma

**Códigos comuns:**
- `pt` → Português
- `en` → Inglês
- `es` → Espanhol
- `fr` → Francês
- `de` → Alemão

---

### 👤 Descobrir autores de uma época

1. Selecionar opção **6** (Listar autores vivos em determinado ano)
2. Digite um ano (ex: `1850`)
3. Sistema mostra autores que estavam vivos naquele ano (nasceram antes e morreram depois, ou ainda estão vivos)

---

### 🏆 Ver os clássicos mais populares

1. Selecionar opção **7** (Top 10 livros mais baixados)
2. Sistema exibe ranking com medalhas 🥇🥈🥉 para os 3 primeiros
3. Mostra título e número de downloads de cada livro

---

## Linguagens e tecnologias utilizadas 📚

- **Java 17**
- **Spring Boot 4.0.1**
  - Spring Data JPA
  - Spring Web
  - Spring Boot DevTools
- **PostgreSQL** (banco de dados relacional)
- **Hibernate** (ORM)
- **Gutendex API** (API pública do Project Gutenberg)
- **Maven** (gerenciamento de dependências)
- **Códigos ANSI** (estilização do console)

---

## Estrutura do Projeto 🧱

```
src/main/java/com/martinpereztovar/literalura/
├── Application.java                    # Classe principal
├── client/
│   └── GutendexClient.java            # Cliente HTTP para API Gutendex
├── domain/
│   ├── Author.java                    # Entidade JPA - Autor
│   └── Book.java                      # Entidade JPA - Livro
├── dto/
│   ├── GutendexAuthorDTO.java        # DTO para autor da API
│   ├── GutendexBookDTO.java          # DTO para livro da API
│   └── GutendexResponseDTO.java      # DTO para resposta da API
├── menu/
│   └── Menu.java                      # Interface de menu do console
├── repository/
│   ├── AuthorRepository.java         # Repository Spring Data JPA
│   └── BookRepository.java           # Repository Spring Data JPA
├── service/
│   ├── BookSearchService.java        # Serviço de busca na API
│   └── CatalogService.java           # Serviço de catálogo local
└── util/
    └── AnsiColors.java               # Utilitário para cores ANSI

src/main/resources/
└── application.yml                    # Configurações do Spring
```

### Responsabilidades das Camadas

**📡 Client:** Comunicação com API externa (Gutendex)  
**🗄️ Domain:** Entidades JPA mapeadas para o banco  
**📦 DTO:** Objetos de transferência de dados da API  
**🎨 Menu:** Interface de usuário no console  
**💾 Repository:** Acesso a dados via Spring Data JPA  
**⚙️ Service:** Lógica de negócio e orquestração  
**🛠️ Util:** Classes auxiliares (cores ANSI)

---

## Modelo de Dados 🗂️

### Entidade: Book (Livro)

```java
- id (Long) - PK, auto-incremento
- externalId (Integer) - ID do Gutendex, UNIQUE
- title (String) - Título do livro
- language (String) - Código do idioma (ex: pt, en)
- downloadCount (Integer) - Número de downloads
- authors (Set<Author>) - Relacionamento ManyToMany
```

### Entidade: Author (Autor)

```java
- id (Long) - PK, auto-incremento
- externalId (Integer) - ID do Gutendex, UNIQUE
- name (String) - Nome do autor
- birthYear (Integer) - Ano de nascimento
- deathYear (Integer) - Ano de falecimento (null se vivo)
- books (Set<Book>) - Relacionamento ManyToMany
```

### Relacionamento

```
Book ←→ book_authors ←→ Author
(ManyToMany com tabela intermediária)
```

**Características:**
- Um livro pode ter múltiplos autores
- Um autor pode ter escrito múltiplos livros
- Constraint única em `externalId` previne duplicatas
- Relacionamento bidirecional gerenciado pelo método `addAuthor()`

---

## Melhorias Futuras

- 🖥️ **Frontend web** com Spring MVC ou React  
- 🤖 **Integração com IA** para gerar biografias de autores  
- 📊 **Recomendações inteligentes** de livros do mesmo autor ou gênero  
- 🔍 **Busca avançada** com múltiplos filtros (período, gênero, idioma)  
- 📖 **Adicionar campo de sinopse** e gêneros literários  
- ⭐ **Sistema de favoritos** e avaliações pessoais  
- 📈 **Estatísticas** do catálogo (autores mais prolíficos, idiomas, etc.)  
- 🌐 **API REST própria** para expor o catálogo  
- 🧪 **Testes automatizados** (JUnit, Mockito)  
- 📱 **Versão mobile** ou Progressive Web App  

---

## Desenvolvedores/Contribuintes

| [<img src="./assets/foto-martin.jpg" width=115><br><sub>Martín Pérez Tovar</sub>](https://github.com/martinpereztovar) |
| :----------------------------------------------------------------------------------------------------------------------: |

---

## Licença

The MIT License (MIT)

Copyright ©️ 2025 – Literalura

---

## Agradecimentos

- **Oracle Next Education (ONE)** e **Alura** pelo desafio
- **Project Gutenberg** pela API gratuita de livros clássicos
- Comunidade Spring Boot pela excelente documentação
