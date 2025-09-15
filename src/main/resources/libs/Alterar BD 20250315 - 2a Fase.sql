USE [GPROD-Tradifana]
GO

-- Criar Campos adicionais nas tabelas KG e KgStage

ALTER TABLE Kg ADD Tempo float NULL
ALTER TABLE Kg ADD Preheat float NULL
ALTER TABLE Kg ADD Cook float NULL
ALTER TABLE KgStage ADD Tempo float NULL
ALTER TABLE KgStage ADD Preheat float NULL
ALTER TABLE KgStage ADD Cook float NULL

  -- Apagar registos em duplicado nos Ingredientes

  DELETE FROM Ingredientes
WHERE [Massa] IN (
    SELECT [Massa]
    FROM Ingredientes
    GROUP BY [Massa], [Ingrediente], [Centro]
    HAVING COUNT(*) > 1
)
AND [Ingrediente] IN (
    SELECT [Ingrediente]
    FROM Ingredientes
    GROUP BY [Massa], [Ingrediente], [Centro]
    HAVING COUNT(*) > 1
)
AND [Centro] IN (
    SELECT [Centro]
    FROM Ingredientes
    GROUP BY [Massa], [Ingrediente], [Centro]
    HAVING COUNT(*) > 1
)
  -- Alterar campos NULL nos Ingredientes

  UPDATE [Ingredientes] SET [Centro]=0 WHERE [Centro] IS NULL
  UPDATE [Ingredientes] SET [Massa]=0 WHERE [Massa] IS NULL
  UPDATE [Ingredientes] SET [Ingrediente]=0 WHERE [Ingrediente] IS NULL
  ALTER TABLE [Ingredientes] ALTER COLUMN [Centro] nvarchar(30) NOT NULL
  ALTER TABLE [Ingredientes] ALTER COLUMN [Ingrediente] nvarchar(50) NOT NULL
  ALTER TABLE [Ingredientes] ALTER COLUMN [Massa] nvarchar(50) NOT NULL

  -- Criar Indice Ingredientes 

  ALTER TABLE [Ingredientes] ADD PRIMARY KEY([Massa],[Ingrediente],[Centro])

  -- Criar tabela FluxoCentroMassa

  CREATE TABLE [dbo].[FluxoMassaCentro](
	[Massa] nvarchar(50) NOT NULL,
	[Centro] nvarchar(30) NOT NULL,
	[Fluxo] INT NOT NULL,
	) ON [PRIMARY]

	ALTER TABLE [FluxoMassaCentro] ADD PRIMARY KEY([Centro],[Massa])

	-- Criar novo Campo "Fluxo"

	ALTER TABLE [MassasProd] ADD Fluxo INT NULL
	update [MassasProd] set Fluxo=1

GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'1904100', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'1904101', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880101', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880102', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880103', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880105', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880106', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880107', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880108', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880109', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880110', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880111', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880112', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880113', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880115', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880116', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880118', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880127', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880128', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880129', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880130', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880131', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880132', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880133', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880134', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880135', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880136', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880137', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880138', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880139', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880140', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880141', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880142', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880143', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880144', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880145', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880146', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880147', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880148', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880149', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880150', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880151', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880152', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880153', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880154', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880155', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880156', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880157', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880158', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880159', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880160', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880201', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880202', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880203', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880204', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880205', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880206', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880210', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880211', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880212', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880213', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880214', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880215', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880216', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880217', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880218', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880219', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880220', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880221', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880222', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880223', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880224', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880225', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880304', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880305', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880306', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880307', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880308', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880309', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880310', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880311', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880318', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880319', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880321', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880397', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880398', N'Amassadeira', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880399', N'Amassadeira', 2)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880127', N'Amassadeira2', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880135', N'Amassadeira2', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880120', N'Amassadeira4', 1)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880210', N'Depositadora', 3)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880397', N'Depositadora', 3)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880136', N'Extrusora', 2)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880210', N'Extrusora', 2)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880397', N'Extrusora', 2)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880398', N'Extrusora', 2)
GO
INSERT [dbo].[FluxoMassaCentro] ([Massa], [Centro], [Fluxo]) VALUES (N'880399', N'Extrusora', 1)
GO

