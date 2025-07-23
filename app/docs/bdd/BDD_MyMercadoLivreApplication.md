
# 📄 Cenários BDD - MyMercadoLivreApplication

Documentação de testes comportamentais (Behavior-Driven Development) para cada tela da aplicação.

---

## 🔍 SearchScreen

**Funcionalidade:** Tela de Busca de Produtos

### Cenário: Usuário acessa a tela de busca
- **Dado** que o usuário abriu o aplicativo
- **Quando** a tela de busca é exibida
- **Então** o título "Busca" deve estar visível com acessibilidade

### Cenário: Usuário digita um termo válido e clica em buscar
- **Dado** que o usuário digitou "notebook"
- **Quando** o botão "Buscar" for clicado
- **Então** o app deve navegar para a tela de resultados da pesquisa com o termo "notebook"
- **E** deve ser registrado um evento no Firebase Analytics

---

## 📋 SearchResultsScreen

**Funcionalidade:** Exibição de Resultados de Busca

### Cenário: Resultados encontrados com sucesso
- **Dado** que o usuário buscou por "celular"
- **Quando** os resultados forem carregados
- **Então** uma grade de produtos deve ser exibida com 2 colunas

### Cenário: Nenhum resultado encontrado
- **Dado** que a busca retornou zero produtos
- **Quando** a tela carregar
- **Então** a ilustração de "sem resultados" e a mensagem correspondente devem ser exibidas

### Cenário: Ocorreu um erro ao buscar
- **Dado** que houve uma falha na requisição
- **Quando** a tela carregar
- **Então** uma mensagem de erro deve ser exibida ao usuário

### Cenário: O usuário volta para a tela de busca
- **Dado** que o usuário está na tela de resultados
- **Quando** clica no botão de voltar
- **Então** ele deve retornar para a tela de busca
- **E** deve ser logado um evento de navegação no Firebase Analytics

---

## 📄 NoResultsScreen

**Funcionalidade:** Exibir mensagem de resultados vazios

### Cenário: Exibição padrão da tela
- **Dado** que a busca não retornou nenhum produto
- **Quando** a tela for exibida
- **Então** a ilustração e o texto "Nenhum resultado encontrado" devem aparecer
- **E** devem conter contentDescription para acessibilidade

---

## 📦 ProductDetailScreen / ProductDetailErrorScreen

**Funcionalidade:** Tela de Detalhes do Produto

### Cenário: Produto carregado com sucesso
- **Dado** que um ID de produto válido foi recebido
- **Quando** os detalhes forem carregados
- **Então** as informações do produto devem ser exibidas
- **E** o evento de visualização deve ser registrado no Firebase

### Cenário: Falha ao carregar os detalhes
- **Dado** que houve um erro ao buscar o produto
- **Quando** a tela for exibida
- **Então** uma ilustração de erro deve aparecer
- **E** o usuário deve ter opções de "Voltar para a busca" e "Ir para a Home"

### Cenário: Usuário interage com botões de erro
- **Dado** que a tela de erro está visível
- **Quando** o usuário clicar em "Voltar para a busca"
- **Então** deve navegar para a tela anterior e logar o evento de clique

---

## 🚀 SplashScreen

**Funcionalidade:** Tela de Splash Inicial

### Cenário: Exibição da splash ao abrir o app
- **Dado** que o app foi iniciado
- **Quando** a tela splash é carregada
- **Então** a logo do aplicativo deve ser exibida em tela cheia com cor de fundo amarela

### Cenário: Timeout da splash
- **Dado** que a splash está sendo exibida
- **Quando** se passam 2 segundos
- **Então** a função onTimeout() deve ser chamada para continuar a navegação
