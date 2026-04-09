create table cliente(
	id bigint not null auto_increment primary key,
	nome varchar(80) not null,
	cpf_cnpj varchar (20) unique,
	ie_rg varchar (10) unique,
	tipo_cliente varchar (18)
);