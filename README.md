**Objetivo**

Criação de um projeto para listar os filmes em cartaz no cinema utilizando a API The Movie Database (TMDb).

**API**

Documentação da rota “filmes em cartaz” https://developers.themoviedb.org/3/movies/get-now-playing
Documentação da rota “poster do filme” https://developers.themoviedb.org/3/getting-started/images

**Requisitos:**

**Criação do projeto no repositório Git:**

- Tela com uma lista paginada contendo os filmes providos pela API onde cada item devem conter a imagem da capa do filme e o título do mesmo.
- Tela que exibe os detalhes de um filme ao se clicar em um filme na tela de lista de filmes.
- Tela de pesquisar filmes, atrelado com filmes populares sendo mostrados antes da pesquisa do usuário.
- Função de “favoritar” e “desfavoritar” um filme na tela de detalhes de um filme, filtrado através de um fragment "favoritos". Lógica feita localmente, não dependendo de novas requisições à API.
- Utilizado arquitetura MVVM.
- Adcionado alguns Lint no projeto
- Utilização de outras rotas da API (pesquisa de filmes e filmes populares)
