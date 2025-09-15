/****** Script for SelectTopNRows command from SSMS  ******/
SELECT
      [Lote]
      
  FROM [GPROD-Tradifana].[dbo].[Lotes]
  
  where Activo=1 and artigo='1101011'
  ORDER by DataFabrico