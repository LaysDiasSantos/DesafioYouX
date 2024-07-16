# DesafioYouX
Este projeto consiste em sistema para o cadastro de clientes e vendas de uma empresa, utilizando HTML, CSS, JavaScript e Thymeleaf para o frontend e Java com Spring Boot para o backend.

## Configuração
Para executar o projeto localmente, você deve ter um ambiente de desenvolvimento Java com Spring Boot, a versão do framework Spring Boot deve ser a 2.7.3 e o Java a partir da versão 11. O banco de dados PostgreSǪĮ na versão 14. 
Recomendo a utilização da Eclipse IDE.


Para executar o projeto, siga os seguintes passos:

1. Clone este repositório em sua máquina;
2. Abra a IDE e importe o projeto;
3. Crie o banco de dados chamado 'sistema' no seu postgree;
4. abra o arquivo application.properties e insira o seu username e password do postgree;
5. Execute o projeto e aguarde até que a aplicação esteja completamente iniciada.
6. Abra o seu navegador e acesse o seguinte endereço: 'http://localhost:8090/'.
### A tela de menu será exibida com as seguinte opções:

Lista de clientes:
- Na tela de listar clientes você encontrará uma barra de pesquisa que busca o cliente pelo seu cnpj, o cnpj é unico.
- A lista com todos os cliente cadastrados 
- Pode editar um cliente 
- Um cliente só pode ser excluido se nenhuma venda estiver associada a ele

  
Cadastrar cliente
- Clicando em cadastrar cliente você será redrecionado para pagina de cadastro de cliente. Nela você encontrará um formulário com todas as informações para cadastrar um cliente, todos os campos são obrigatorios.
- Para cadastrar um novo cliente você precisará de um CNPJ valido, recomendo gerar um em algum site gerador de CNPJ.
  
Lista de Vendas
- Na tela de listar clientes você também encontrará uma barra de pesquisa que busca uma venda pelo nome do cliente associado a essa venda 
- A lista com todos as vendas cadastradas
  
Cadastrar Venda
- Para adicionar uma venda você precisará informar quali cliente está realizando essa venda.

Baixar Relatorio de Venda
- Baixa um arquivo .csv com todos os clientes cadastrados 






