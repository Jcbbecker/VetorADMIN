/****** Script for SelectTopNRows command from SSMS  ******/
SELECT
      i.[Ingrediente]
	  ,a.Descricao
      ,i.[Quant] as Previsto
	  ,i.[Quant] * 10 as Consumido
	  ,a.[UnidadeBase]
      ,i.[FixVar]
	  ,l.[Lote]
      	 
  FROM [GPROD-Tradifana].[dbo].[Ingredientes] as i
  left join Artigos as a on Ingrediente=a.Artigo
  left join LotesUso as l on Ingrediente=l.Artigo

  Where Massa=880102 and i.Centro='Amassadeira'

  
