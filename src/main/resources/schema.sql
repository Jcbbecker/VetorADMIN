CREATE TABLE `Artigos` (
    `Artigo` VARCHAR(48) NOT NULL,
    `Descricao` VARCHAR(75) NULL,
    `UnidadeBase` VARCHAR(5) NOT NULL,
    `MovStock` CHAR(1) NULL,
    `Familia` VARCHAR(10) NULL,
    `ArmazemSugestao` VARCHAR(5) NULL,
    `TipoArtigo` VARCHAR(3) NOT NULL,
    `TipoComponente` SMALLINT NULL,
    `ArtigoAnulado` BOOLEAN NOT NULL,
    `Peso` DOUBLE NULL,
    `Marca` VARCHAR(10) NULL,
    `TratamentoLotes` BOOLEAN NOT NULL,
    `CDU_LOTE` VARCHAR(3) NULL,
    `PCMedio` DOUBLE NULL,
    `PCUltimo` DOUBLE NULL,
    `Volume` DOUBLE NULL,
    PRIMARY KEY (`Artigo`)
);

CREATE TABLE `Centros` (
    `Codigo` VARCHAR(30) NULL,
    `Nome` VARCHAR(30) NULL
);

CREATE TABLE `Config` (
    `ID` INT NULL,
    `Versao` VARCHAR(50) NULL,
    `DataFechada` DATE NULL
);

CREATE TABLE `Ingredientes` (
    `Massa` VARCHAR(50) NULL,
    `Ingrediente` VARCHAR(50) NULL,
    `Quant` DOUBLE NULL,
    `FixVar` VARCHAR(50) NULL,
    `Centro` VARCHAR(30) NULL
);

CREATE TABLE `IngredientesProd` (
    `ID` INT AUTO_INCREMENT NOT NULL,
    `IDMassasProd` INT NOT NULL,
    `Codigo` VARCHAR(30) NOT NULL,
    `Ordem` INT NOT NULL,
    `Lote` VARCHAR(30) NULL,
    `Consumo` DOUBLE NULL,
    `Unidade` VARCHAR(10) NULL,
    `DataRegisto` DATETIME NULL,
    `Utilizador` VARCHAR(30) NULL,
    `Terminal` VARCHAR(30) NULL,
    PRIMARY KEY (`ID`)
);

CREATE TABLE `Lotes` (
    `Artigo` VARCHAR(48) NOT NULL,
    `Lote` VARCHAR(20) NOT NULL,
    `DataFabrico` DATETIME NULL,
    `Validade` DATETIME NULL,
    `Activo` BOOLEAN NULL,
    PRIMARY KEY (`Artigo`, `Lote`)
);

CREATE TABLE `Marcas` (
    `Codigo` VARCHAR(50) NULL,
    `Nome` VARCHAR(50) NULL
);

CREATE TABLE `Massas` (
    `Acabado` VARCHAR(50) NOT NULL,
    `Massa` VARCHAR(50) NOT NULL
);

CREATE TABLE `MassasPrev` (
    `Dia` DATE NULL,
    `Codigo` VARCHAR(50) NULL,
    `Previsao` DOUBLE NULL,
    `DataRegisto` DATETIME NULL,
    `Utilizador` VARCHAR(30) NULL,
    `Terminal` VARCHAR(30) NULL
);

CREATE TABLE `MassasProd` (
    `ID` INT AUTO_INCREMENT NOT NULL,
    `Dia` DATE NULL,
    `Hora` TIME NULL,
    `Codigo` VARCHAR(30) NULL,
    `Ordem` INT NULL,
    `Lote` VARCHAR(30) NULL,
    `Producao` DOUBLE NULL,
    `DataRegisto` DATETIME NULL,
    `Utilizador` VARCHAR(30) NULL,
    `Terminal` VARCHAR(30) NULL,
    PRIMARY KEY (`ID`)
);

CREATE TABLE `Terminais` (
    `Codigo` VARCHAR(30) NULL,
    `Nome` VARCHAR(30) NULL
);

CREATE TABLE `Utilizadores` (
    `Codigo` VARCHAR(30) NULL,
    `Nome` VARCHAR(30) NULL,
    `Senha` VARCHAR(30) NULL,
    `Tipo` VARCHAR(30) NULL
);
