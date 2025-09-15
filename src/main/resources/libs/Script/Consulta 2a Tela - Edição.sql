/****** Script for SelectTopNRows command from SSMS  ******/
SELECT
	   d.[ID]
      ,d.[Codigo]
	  ,a.Descricao
      ,i.[Quant] as Previsto     
      ,d.[Consumo]
      ,d.[Unidade]
      ,i.[FixVar]
	  ,d.[Lote]

      	 
  FROM [GPROD-Tradifana].[dbo].[IngredientesProd] as d
  left join Artigos as a on d.Codigo=a.Artigo
  left join Ingredientes as i on Ingrediente=d.codigo and Massa=880102

  Where IDMassasProd=3960
  order by ordem


  
