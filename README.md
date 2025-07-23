# 🛒 MyMercadoLivreApplication

Aplicativo Android criado como simulação de experiência de compra no Mercado Livre. Desenvolvido com foco em arquitetura limpa, acessibilidade, testes automatizados, logging e boas práticas de UI/UX com Jetpack Compose.

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
│   └── remote              # Interface Retrofit (não implementada neste mock)
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

- SearchViewModelTest.kt
- ProductDetailViewModelTest.kt

Cobertura: carregamento, falhas, reset, sem resultados.

---

## 📦 Mock de Dados

- Carregamento local via AssetUtils  
- Arquivos simulam a resposta da API do Mercado Livre  
- Ex: search-MLA-cafe.json, item-MLA123.json

---

## 📱 Acessibilidade

- Uso intensivo de contentDescription
- Compatível com leitores de tela

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

- Arquitetura escalável
- Firebase Analytics
- Composables reutilizáveis
- Testes de ViewModel
- Splash nativa com ícone animado
- Logging com Timber

---


### 👩‍💻 Desenvolvido por

*Luana de Souza Alves Silva*  
[LinkedIn](https://www.linkedin.com/in/luanadesouza-desenvolvedora-android/)
