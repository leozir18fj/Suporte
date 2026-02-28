create table produto(
	id bigint not null auto_increment primary key,
	descricao varchar(60),
	codigo_barras varchar(13) unique,
	preco decimal,
	quantidade_estoque double
);