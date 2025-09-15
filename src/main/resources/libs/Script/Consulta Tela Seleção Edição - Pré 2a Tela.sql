/****** Script for SelectTopNRows command from SSMS  ******/
SELECT [ID]
      ,[DataPrev] as Dia
      ,[Hora]
      ,[Lote]
      ,[Producao]
      
  FROM [GPROD-Tradifana].[dbo].[MassasProd]
  where Codigo=880135 and DataPrev='2024-05-21'
  order by DataPrev , Hora