# 🛒 MyMercadoLivreApplication

Aplicativo Android criado como simulação de experiência de compra no Mercado Livre. Desenvolvido com foco em arquitetura limpa, acessibilidade, testes automatizados, logging e boas práticas de UI/UX com Jetpack Compose.

---

## 🖼️ Screenshots do App

<p float="left">
  <img src="https://github.com/user-attachments/assets/35b0fae1-8aa3-49bd-9001-0beb5dd1b248" width="30%" />
  <img src="https://github.com/user-attachments/assets/b3a19a33-36ff-433f-b266-625f9b7b1aa7" width="30%" />
  <img src="https://github.com/user-attachments/assets/3d562b20-217b-46ab-84ac-cf2e6eed69d7" width="30%" />
</p>

<p float="left">
  <img src="https://github.com/user-attachments/assets/e0c42aef-b03a-464f-8083-55c3663a86e5" width="30%" />
  <img src="https://github.com/user-attachments/assets/45ac4361-0464-4f4c-920f-c85da99dedd7" width="30%" />
  <img src="https://github.com/user-attachments/assets/dc6360e7-6815-49a0-a79c-3c236c5af861" width="30%" />
</p>

---

## 🚀 Tecnologias Utilizadas

- *Linguagem:* Kotlin
- *UI:* Jetpack Compose (Material 3)
- *Arquitetura:* MVVM com Hilt (DI) e StateFlow
- *Rede:* Retrofit + OkHttp
- *Parsing:* Gson
- *Analytics:* Firebase Analytics
- *Mock:* MockK + Turbine
- *Testes:* JUnit 4 + Coroutine Test + MainDispatcherRule

---

## 🧠 Arquitetura

```plaintext
📦 mymercadolivreapplication
├── ui
│   ├── splash              # Splash screen com animação
│   ├── search              # Tela de busca e ViewModel
│   ├── result              # Tela de resultados + ProductItem
│   ├── detail              # Detalhes do produto + estado + erro
│   └── component           # Componentes reutilizáveis (ex: RatingStars)
├── data
│   ├── model               # Modelos: Product, ProductDetail, etc.
├── di                      # Módulos de injeção Hilt
├── utils                   # AssetUtils, FirebaseAnalyticsManager
├── navigation              # Navegação entre telas
└── MainActivity.kt         # Entry point com Theme e Navigation

---

## 📲 Funcionalidades

- 🔍 Busca simulada com arquivos .json
- 📦 Lista de produtos com imagem, preço, avaliações e frete
- 📄 Tela de detalhes com galeria, atributos e desconto
- ❌ Tela de erro com ações alternativas
- 🔁 Navegação declarativa (Jetpack Navigation)
- 📊 Firebase Analytics (screen view + eventos)

---

## 🧪 Testes Automatizados

Testes são fundamentais para garantir a estabilidade, previsibilidade e confiança em um projeto Android moderno — especialmente em times colaborativos ou projetos que evoluem com frequência.

Este projeto conta com testes de unidade para os principais ViewModels:

- `SearchViewModelTest.kt`
- `ProductDetailViewModelTest.kt`

Os testes cobrem cenários essenciais como:

- Carregamento inicial dos dados
- Tratamento de falhas (ex: exceções simuladas)
- Reset de estados
- Situações sem resultados

Com isso, é possível:

- Refatorar com segurança
- Documentar o comportamento esperado da UI
- Garantir que funcionalidades críticas continuem funcionando mesmo com mudanças futuras
- Simular dependências (como FirebaseAnalyticsManager) via MockK

> O uso de `MainDispatcherRule`, `Coroutine Test` e `Turbine` permite testes reativos e confiáveis para `StateFlow`.

---

## 📋 Cenários BDD

Para facilitar a documentação e validação de comportamento, incluímos cenários BDD (Behavior-Driven Development) com foco em usabilidade, navegação e estados de tela.

Você pode visualizar todos os cenários no arquivo:

📄 [`docs/BDD_MyMercadoLivreApplication.md`](./docs/BDD_MyMercadoLivreApplication.md)

Esse arquivo descreve, em português, os comportamentos esperados em cada tela do aplicativo, incluindo:

- Tela de Busca
- Tela de Resultados
- Tela de Detalhes
- Tela de Erro de Produto
- Tela de Splash
- Tela de Sem Resultados

Esses cenários são úteis para QA, automação de testes e validação funcional.

## 📦 Mock de Dados

- Carregamento local via AssetUtils  
- Arquivos simulam a resposta da API do Mercado Livre  
- Ex: search-MLA-cafe.json, item-MLA123.json

---

## 📱 Acessibilidade

A acessibilidade não é apenas uma boa prática — é um critério de qualidade reconhecido pelo Google.
Aplicativos com suporte adequado a leitores de tela, descrições semânticas e navegação acessível têm maior visibilidade na Play Store, podendo receber a tag oficial de “Acessível” e melhor posicionamento no ranking de busca.

Este app foi desenvolvido com foco em acessibilidade desde a base:

- Uso extensivo de `contentDescription` em imagens, botões e textos importantes
- Compatibilidade com leitores de tela (TalkBack)
- Semântica clara em componentes interativos (Compose)
- Experiência inclusiva para usuários com deficiência visual

---

## 🎨 Design e Estilo

- Tipografia: Montserrat
- Paleta: #FFE600, #2D2D2D, #009E0F
- Layout com LazyVerticalGrid, HorizontalPager, previews de UI

---

## 🔀 Rotas de Navegação

| Tela             | Rota                    |
|------------------|-------------------------|
| Splash           | splash                |
| Busca            | search                |
| Resultados       | results/{query}       |
| Detalhes Produto | detail/{productId}    |

---

### ▶️ Como Executar

- Abrir no *Android Studio*
- Rodar em *emulador* ou *dispositivo físico*
- *Requisitos:*
  - SDK 33+
  - Compose Compiler 1.5.0+
  - Kotlin 1.9+

---

### 🧠 Diferenciais Técnicos

- Arquitetura escalável (MVVM com DI)
- Firebase Analytics com tagueamento de eventos
- Acessibilidade com uso extensivo de `contentDescription`
- Composables reutilizáveis com previews
- Testes de ViewModel com fluxo completo (mock, falha, sucesso)
- Splash nativa com ícone animado (MaterialTheme)
- Logging com Timber

---


### 👩‍💻 Desenvolvido por

*Luana de Souza Alves Silva*  
[LinkedIn](https://www.linkedin.com/in/luanadesouza-desenvolvedora-android/)
