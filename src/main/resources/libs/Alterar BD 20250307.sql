USE [GPROD-Tradifana]
GO

/****** Object:  Table [dbo].[Lotes]    Script Date: 07/03/2025 00:16:54 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[LotesUso](
	[Artigo] [nvarchar](48) NOT NULL,
	[Centro] [nvarchar](30) NOT NULL,
	[Lote] [nvarchar](20) NOT NULL

	) ON [PRIMARY]
GO

ALTER TABLE MassasProd ADD DataPrev date NULL
update MassasProd set DataPrev=Dia

