create table endereco(
	id bigint not null auto_increment primary key,
	tipo_endereco varchar (15),
	cep varchar (8),
	rua varchar(80) not null,
	numero varchar (6) unique,
	complemento varchar (10) unique,
	bairro varchar (30),
	estado varchar (2),
	municipio varchar (50),
	cliente_id bigint not null,
	constraint fk_cliente
	foreign key (cliente_id) references cliente(id)
);