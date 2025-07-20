# Mercado Livre API Integration for Android

## Descrição

Este projeto integra a API do **Mercado Livre** com um aplicativo Android. Ele permite realizar buscas de produtos, obter detalhes completos sobre um produto específico e navegar entre as telas usando **Navigation Compose**. O projeto utiliza **Retrofit** para a comunicação com a API, **Gson** para a serialização dos dados, **Kotlin Coroutines** para operações assíncronas, **Jetpack Compose** para a criação da UI, **Firebase Analytics** para monitoramento de eventos e **Hilt** para injeção de dependências.

### Tecnologias Utilizadas

- **Jetpack Compose**: Framework moderno para construção de interfaces de usuário no Android de forma declarativa.
- **Retrofit**: Biblioteca para realizar requisições HTTP e consumir APIs RESTful.
- **Gson**: Biblioteca para conversão de objetos Java/Kotlin para JSON e vice-versa.
- **Kotlin Coroutines**: Para operações assíncronas, permitindo que a UI permaneça responsiva.
- **Firebase Analytics**: Usado para monitorar e registrar eventos, como buscas de produtos e visualizações de itens.
- **Timber**: Biblioteca de logging, usada para facilitar o processo de depuração.
- **Hilt**: Framework de injeção de dependência para facilitar a injeção de dependências no Android.

## Funcionalidades

- **Tela de Splash**: Exibe uma tela de introdução com o logotipo da aplicação enquanto o app carrega, proporcionando uma boa experiência de usuário durante o carregamento.
- **Busca de Produtos**: Permite realizar buscas de produtos com base em termos de pesquisa fornecidos.
- **Detalhes do Produto**: Mostra informações detalhadas sobre um produto específico quando o usuário seleciona um item.
- **Mock de Dados**: Durante o desenvolvimento, a busca de produtos pode ser simulada usando dados mockados, sem a necessidade de uma conexão com a internet.
- **Firebase Analytics**: O app registra eventos de busca e visualização de produtos, permitindo o acompanhamento do comportamento do usuário.

## Estrutura do Projeto

### 1. **MainActivity**
A `MainActivity` é o ponto de entrada do aplicativo, onde o tema é aplicado e o sistema de navegação é configurado com **Navigation Compose**. Esta Activity também instala a tela de **Splash** ao iniciar o aplicativo.

### 2. **Navegação**
O sistema de navegação é gerenciado por **Navigation Compose**, permitindo que o aplicativo tenha transições suaves entre as telas:
- **Tela de Pesquisa**: Tela onde o usuário pode digitar um termo de pesquisa.
- **Tela de Resultados**: Exibe os produtos encontrados com base no termo de pesquisa.
- **Tela de Detalhes do Produto**: Exibe informações completas sobre um produto específico.

### 3. **ViewModels**
O projeto utiliza **ViewModels** para separar a lógica de negócios da interface do usuário (UI). Cada ViewModel gerencia o estado da UI de maneira isolada e responde a mudanças nos dados, mantendo a UI reativa.

- **SearchViewModel**: Gerencia a lógica de busca de produtos, tratando os estados de carregamento, erro e resultados.
- **ProductDetailViewModel**: Gerencia os detalhes de um produto específico, mantendo o estado da UI enquanto os dados são carregados.

### 4. **Modelos de Dados**
Os modelos de dados (ou `Data Models`) são usados para representar as informações que são enviadas e recebidas da API do Mercado Livre. Exemplos incluem:
- **Product**: Representa os produtos retornados pela API.
- **ProductDetail**: Contém informações detalhadas sobre um produto.
- **SearchResponse**: Contém os dados da resposta da busca, incluindo a lista de produtos encontrados.

### 5. **State Management**
O gerenciamento de estado é realizado por classes **`ViewState`** e **`Resource`**, que ajudam a manter a UI consistente:
- **Resource**: Uma classe selada usada para representar os diferentes estados de uma operação assíncrona (`Loading`, `Success`, `Error`).
- **ViewState**: Armazena o estado atual da UI (se está carregando, se há um erro, se os resultados foram encontrados, etc.).

### 6. **Firebase Analytics**
O **Firebase Analytics** é usado para registrar eventos no aplicativo, como:
- **Log de Pesquisa**: Quando o usuário realiza uma busca.
- **Log de Visualização de Produto**: Quando o usuário visualiza os detalhes de um produto.

Esses eventos permitem acompanhar as ações dos usuários no aplicativo, oferecendo insights sobre seu comportamento.

### 7. **Carregamento de Arquivos (Assets)**
A classe **AssetUtils** é responsável por carregar arquivos JSON do diretório **assets** do Android. Isso é útil para simular a resposta da API durante o desenvolvimento.

### 8. **Tagueamento de Eventos (Analytics)**

O **Firebase Analytics** é utilizado para monitorar o comportamento do usuário, registrando eventos importantes, como buscas realizadas, visualizações de produtos e interações com o app.

### 9. **Acessibilidade**

Para garantir que o aplicativo seja acessível a todos os usuários, a acessibilidade é tratada através de **contentDescription** em componentes interativos, como botões, campos de texto e ícones. Isso permite que leitores de tela forneçam uma descrição clara dos elementos para pessoas com deficiência visual.

Além disso, os **ViewStates** utilizam mensagens claras para indicar os estados de carregamento, erro e sucesso, garantindo que a interface seja informativa e fácil de entender para todos os usuários.

## Como Executar

### Pré-requisitos

Certifique-se de que você tenha o Android Studio instalado e configurado corretamente. Este projeto utiliza o **Kotlin** e requer o **Hilt** para injeção de dependência.

1. Clone o repositório:
[git clone https://github.com/seu_usuario/mercadolivre-api-integration.git](https://github.com/LuanadeSouza/MercadoLivre.git)


## Estrutura de Pastas

- **ui**: Contém os componentes da interface de usuário (telas, views, etc).
  - **search**: Tela de busca de produtos.
  - **detail**: Tela de detalhes do produto.
  - **splash**: Tela inicial do aplicativo.

- **data**: Contém os modelos de dados e repositórios responsáveis pela interação com a API.

- **utils**: Contém utilitários, como o carregamento de arquivos JSON de **assets** e o gerenciador do **Firebase Analytics**.

- **domain**: Contém casos de uso e lógica de negócios.

- **navigation**: Contém a lógica de navegação entre as telas.
